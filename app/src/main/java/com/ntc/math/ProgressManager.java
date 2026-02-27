package com.ntc.math;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ProgressManager {
    private static final String PREFS = "progress_prefs";
    private static final String KEY_HISTORY = "history";

    public static class Session {
        public String topic;
        public int classLevel;
        public int correct;
        public int total;
        public long timestamp;

        public JSONObject toJson() throws JSONException {
            JSONObject o = new JSONObject();
            o.put("topic", topic);
            o.put("classLevel", classLevel);
            o.put("correct", correct);
            o.put("total", total);
            o.put("timestamp", timestamp);
            return o;
        }

        public static Session fromJson(JSONObject o) throws JSONException {
            Session s = new Session();
            s.topic = o.getString("topic");
            s.classLevel = o.getInt("classLevel");
            s.correct = o.getInt("correct");
            s.total = o.getInt("total");
            s.timestamp = o.getLong("timestamp");
            return s;
        }
    }

    /** 📝 Ghi lại 1 phiên học */
    public static void addSession(Context ctx, Session session) {
        try {
            SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            JSONArray arr = new JSONArray(sp.getString(KEY_HISTORY, "[]"));
            arr.put(session.toJson());
            sp.edit().putString(KEY_HISTORY, arr.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 📖 Đọc toàn bộ lịch sử */
    public static List<Session> getAll(Context ctx) {
        List<Session> list = new ArrayList<>();
        try {
            SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            JSONArray arr = new JSONArray(sp.getString(KEY_HISTORY, "[]"));
            for (int i = 0; i < arr.length(); i++) {
                list.add(Session.fromJson(arr.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 📊 Tính tỉ lệ trung bình đúng của tất cả các buổi học */
    public static double getAverageScore(Context ctx) {
        List<Session> list = getAll(ctx);
        int correct = 0, total = 0;
        for (Session s : list) {
            correct += s.correct;
            total += s.total;
        }
        return total == 0 ? 0 : (double) correct / total;
    }
}
