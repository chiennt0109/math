package com.ntc.math;

import android.content.Context;
import android.content.SharedPreferences;

import com.ntc.math.data.AppDb;
import com.ntc.math.data.Attempt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PerformanceAI {
    private static final String PREFS = "PerformancePrefs";
    private static final double ALPHA = 0.15;
    private static final int MIN_FOR_PROMOTION = 80;
    private static final int MIN_FOR_DEMOTION = 50;
    private static final long PROMO_COOLDOWN_MS = 2L * 24 * 60 * 60 * 1000;
    private static final long DEMO_COOLDOWN_MS = 1L * 24 * 60 * 60 * 1000;
    private static final double PROMOTE_MASTERY = 0.85;
    private static final double DEMOTE_MASTERY = 0.55;

    private static final ExecutorService IO = Executors.newSingleThreadExecutor();

    private static String mk(String k){ return k.replace(' ','_').toLowerCase(); }

    public static void recordAttempt(Context ctx, int userClass, String topic, int difficulty, boolean correct, long timeMs){
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String base = "lop"+userClass+"_"+mk(topic);

        String mkMastery = "mastery_"+base;
        double oldM = Double.longBitsToDouble(p.getLong(mkMastery, Double.doubleToLongBits(0.6)));
        double newM = (1.0-ALPHA)*oldM + ALPHA*(correct?1.0:0.0);
        if (newM < 0) newM = 0; if (newM > 1) newM = 1;

        String mkDiff = "diff_"+base;
        int oldD = p.getInt(mkDiff, 2);
        int newD = oldD;
        if (correct && newM > 0.75 && timeMs < 12000) newD = Math.min(5, oldD+1);
        if (!correct && newM < 0.60) newD = Math.max(1, oldD-1);

        String mkTotal = "total_"+base, mkCorrect="correct_"+base;
        int total = p.getInt(mkTotal, 0) + 1;
        int cor = p.getInt(mkCorrect, 0) + (correct?1:0);

        p.edit()
            .putLong(mkMastery, Double.doubleToLongBits(newM))
            .putInt(mkDiff, newD)
            .putInt(mkTotal, total)
            .putInt(mkCorrect, cor)
            .apply();

        IO.submit(() -> AppDb.get(ctx).attemptDao()
           .insert(new Attempt(System.currentTimeMillis(), userClass, topic, difficulty, correct, timeMs)));
    }

    public static double getMastery(Context ctx, int userClass, String topic){
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return Double.longBitsToDouble(p.getLong("mastery_lop"+userClass+"_"+mk(topic),
                Double.doubleToLongBits(0.6)));
    }

    public static int getDifficulty(Context ctx, int userClass, String topic){
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return p.getInt("diff_lop"+userClass+"_"+mk(topic), 2);
    }

    public static void setDifficulty(Context ctx, int userClass, String topic, int d){
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putInt("diff_lop"+userClass+"_"+mk(topic), Math.max(1, Math.min(5,d))).apply();
    }

    public static boolean shouldPromote(Context ctx, int userClass, String[] coreTopics){
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        long now = System.currentTimeMillis();
        long last = p.getLong("last_promo_ts", 0L);
        if (now - last < PROMO_COOLDOWN_MS) return false;

        double sum = 0; int n = 0; int totalAttempts=0;
        for (String t: coreTopics){
            String base = "lop"+userClass+"_"+mk(t);
            sum += getMastery(ctx, userClass, t);
            n++;
            totalAttempts += p.getInt("total_"+base, 0);
        }
        double avg = (n==0?0:sum/n);
        if (avg >= PROMOTE_MASTERY && totalAttempts >= MIN_FOR_PROMOTION){
            p.edit().putLong("last_promo_ts", now).apply();
            return true;
        }
        return false;
    }

    public static boolean shouldDemote(Context ctx, int userClass, String[] coreTopics){
        if (userClass <= 1) return false;
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        long now = System.currentTimeMillis();
        long last = p.getLong("last_demo_ts", 0L);
        if (now - last < DEMO_COOLDOWN_MS) return false;

        double sum = 0; int n = 0; int totalAttempts=0;
        for (String t: coreTopics){
            String base = "lop"+userClass+"_"+mk(t);
            sum += getMastery(ctx, userClass, t);
            n++;
            totalAttempts += p.getInt("total_"+base, 0);
        }
        double avg = (n==0?1:sum/n);
        if (avg <= DEMOTE_MASTERY && totalAttempts >= MIN_FOR_DEMOTION){
            p.edit().putLong("last_demo_ts", now).apply();
            return true;
        }
        return false;
    }
}
