package com.ntc.math;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.*;

/**
 * AdaptiveManager
 * -------------------
 * Quản lý độ khó thích ứng và lưu lớp học hiện tại của học sinh.
 * Lưu tiến độ bằng EMA (Exponential Moving Average).
 * Học sinh càng trả lời đúng nhiều → hệ thống tự tăng độ khó.
 */
public class AdaptiveManager {

    private static final String PREF = "adaptive_math_v1_5";
    private static final double EMA_ALPHA = 0.5;
    private static final String KEY_CLASS = "current_class";
    private static final String KEY_CLASS_LOCKED = "class_locked";
    private static final String KEY_SUGGESTED_CLASS = "suggested_class";
    private static final String KEY_TOTAL_CORRECT = "total_correct";
    private static final String KEY_TOTAL_WRONG = "total_wrong";
    private static final String KEY_PRACTICE_TEST_COUNT = "practice_test_count";
    private static final String KEY_LAST_PRACTICE_SCORE = "last_practice_score";
    private static final String KEY_WEAK_AXIS_PREFIX = "weak_axis_";

    public static final String PRACTICE_TOPIC = "Luyện đề tổng hợp theo khối";

    private static final String KEY_VERTICAL_LEVEL = "vertical_level_";
    private static final String KEY_LEVEL_ATTEMPT_PREFIX = "vertical_attempt_";
    private static final String KEY_LEVEL_CORRECT_PREFIX = "vertical_correct_";
    private static final String KEY_SKILL_WRONG_STREAK_PREFIX = "vertical_wrong_streak_";

    // ==========================================================
    // 🏫 Quản lý lớp học hiện tại (ClassSelectActivity dùng)
    // ==========================================================
    public static void setCurrentClass(Context ctx, int grade) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_CLASS, grade).putBoolean(KEY_CLASS_LOCKED, true).apply();
    }

    public static int getCurrentClass(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_CLASS, 1);
    }

    public static boolean isClassLocked(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_CLASS_LOCKED, false);
    }

    public static int getSuggestedClass(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_SUGGESTED_CLASS, getCurrentClass(ctx));
    }

    public static void updateSuggestedClass(Context ctx, int suggestedClass) {
        int safe = Math.max(1, Math.min(12, suggestedClass));
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_SUGGESTED_CLASS, safe).apply();
    }


    public static int getCurrentVerticalLevel(Context ctx, String topic) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        int defaultLevel = extractLevelFromTopic(topic);
        int lv = sp.getInt(KEY_VERTICAL_LEVEL + topic, defaultLevel);
        return Math.max(1, Math.min(4, lv));
    }

    public static void setCurrentVerticalLevel(Context ctx, String topic, int level) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        int safe = Math.max(1, Math.min(4, level));
        sp.edit().putInt(KEY_VERTICAL_LEVEL + topic, safe).apply();
    }

    public static int recordVerticalAttempt(Context ctx,
                                            String topic,
                                            String skillKey,
                                            int level,
                                            boolean isCorrect) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);

        String levelAttemptKey = KEY_LEVEL_ATTEMPT_PREFIX + topic + "_L" + level;
        String levelCorrectKey = KEY_LEVEL_CORRECT_PREFIX + topic + "_L" + level;
        int attempts = sp.getInt(levelAttemptKey, 0) + 1;
        int correct = sp.getInt(levelCorrectKey, 0) + (isCorrect ? 1 : 0);

        String wrongStreakKey = KEY_SKILL_WRONG_STREAK_PREFIX + topic + "_" + skillKey;
        int wrongStreak = isCorrect ? 0 : sp.getInt(wrongStreakKey, 0) + 1;

        float accuracy = attempts == 0 ? 0f : (correct * 1f / attempts);
        int currentLevel = getCurrentVerticalLevel(ctx, topic);
        int newLevel = currentLevel;

        if (level == currentLevel && accuracy < 0.4f && wrongStreak >= 3) {
            newLevel = Math.max(1, currentLevel - 1);
        } else if (level == currentLevel && attempts >= 5 && accuracy >= 0.8f && wrongStreak == 0) {
            newLevel = Math.min(4, currentLevel + 1);
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(levelAttemptKey, attempts);
        editor.putInt(levelCorrectKey, correct);
        editor.putInt(wrongStreakKey, wrongStreak);
        editor.putInt(KEY_VERTICAL_LEVEL + topic, newLevel);
        editor.apply();
        return newLevel;
    }

    private static int extractLevelFromTopic(String topic) {
        if (topic == null) return 1;
        if (topic.contains("Cấp 4")) return 4;
        if (topic.contains("Cấp 3")) return 3;
        if (topic.contains("Cấp 2")) return 2;
        return 1;
    }

    // ==========================================================
    // 📊 Lưu & cập nhật điểm EMA cho từng chủ đề
    // ==========================================================
    public static void recordAnswer(Context ctx, String topic, boolean isCorrect) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        float oldEma = sp.getFloat(topic, 0.5f);
        float newEma = (float) (EMA_ALPHA * (isCorrect ? 1.0 : 0.0) + (1 - EMA_ALPHA) * oldEma);

        int totalCorrect = sp.getInt(KEY_TOTAL_CORRECT, 0) + (isCorrect ? 1 : 0);
        int totalWrong = sp.getInt(KEY_TOTAL_WRONG, 0) + (isCorrect ? 0 : 1);

        sp.edit()
                .putFloat(topic, newEma)
                .putInt(KEY_TOTAL_CORRECT, totalCorrect)
                .putInt(KEY_TOTAL_WRONG, totalWrong)
                .apply();
    }

    public static void onSessionEnd(Context ctx, String topic, float score) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        float oldEma = sp.getFloat(topic, 0.5f);
        float newEma = (float) (EMA_ALPHA * (score / 100.0) + (1 - EMA_ALPHA) * oldEma);
        sp.edit().putFloat(topic, newEma).apply();
    }

    // ==========================================================
    // ⚙️ Tự động xác định độ khó ban đầu theo EMA
    // ==========================================================
    public static String getStartingDifficulty(Context ctx, String topic) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        float ema = sp.getFloat(topic, 0.5f);
        if (ema < 0.4) return "Dễ";
        if (ema < 0.7) return "Trung bình";
        return "Khó";
    }

    // ==========================================================
    // 🧩 Danh sách chủ đề theo chương trình lớp 1–9 Việt Nam
    // ==========================================================
    public static List<String> getTopicsForGrade(int grade) {
        switch (grade) {
            case 1:
                return Arrays.asList(
                        "Số và phép tính trong phạm vi 100",
                        "Phép cộng – trừ có nhớ",
                        "Đo độ dài, thời gian, tiền Việt Nam"
                );
            case 2:
                return Arrays.asList(
                        "Số và phép tính đến 1000",
                        "Đo lường: cm, m, kg, lít",
                        "Toán có lời văn cơ bản"
                );
            case 3:
                return Arrays.asList(
                        "Phép nhân và chia",
                        "Bảng cửu chương & chia đều",
                        "Hình học cơ bản: tam giác, chữ nhật"
                );
            case 4:
                return Arrays.asList(
                        "Phân số và so sánh phân số",
                        "Chu vi và diện tích các hình cơ bản",
                        "Toán có lời văn nâng cao"
                );
            case 5:
                return Arrays.asList(
                        "Số thập phân & phép tính với số thập phân",
                        "Tỉ số, phần trăm, tỉ lệ",
                        "Thể tích & diện tích hình hộp chữ nhật"
                );
            case 6:
                return Arrays.asList(
                        "Số học cơ bản: số nguyên, lũy thừa",
                        "Phân số & hỗn số",
                        "Tỉ lệ & phần trăm nâng cao"
                );
            case 7:
                return Arrays.asList(
                        "Biểu thức & biến số",
                        "Hình học phẳng: tam giác, đường tròn",
                        "Thống kê & trung bình cộng"
                );
            case 8:
                return Arrays.asList(
                        "Phương trình & bất phương trình bậc nhất",
                        "Lũy thừa & căn bậc hai",
                        "Định lý Pitago & tam giác vuông"
                );
            case 9:
                return Arrays.asList(
                        "Hàm số bậc nhất & đồ thị",
                        "Hệ hai phương trình bậc nhất hai ẩn",
                        "Xác suất & tổ hợp cơ bản"
                );
            case 10:
                return Arrays.asList(
                        PRACTICE_TOPIC,
                        "Lớp 10 - Hàm số bậc hai - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                        "Lớp 10 - Hàm số bậc hai - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                        "Lớp 10 - Hàm số bậc hai - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                        "Lớp 10 - Hàm số bậc hai - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số"
                );
            case 11:
                return Arrays.asList(
                        PRACTICE_TOPIC,
                        "Lớp 11 - Hàm lượng giác & liên tục - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                        "Lớp 11 - Hàm lượng giác & liên tục - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                        "Lớp 11 - Hàm lượng giác & liên tục - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                        "Lớp 11 - Hàm lượng giác & liên tục - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số"
                );
            case 12:
                return Arrays.asList(
                        PRACTICE_TOPIC,
                        "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                        "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                        "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                        "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số"
                );
            default:
                return Arrays.asList("Số và phép tính cơ bản");
        }
    }

    // ==========================================================
    // ⬆️⬇️ Điều chỉnh độ khó (adaptive)
    // ==========================================================
    public static void increaseDifficulty(Context ctx, String topic) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        float ema = sp.getFloat(topic, 0.5f);
        ema = Math.min(1.0f, ema + 0.1f);
        sp.edit().putFloat(topic, ema).apply();
    }

    public static void decreaseDifficulty(Context ctx, String topic) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        float ema = sp.getFloat(topic, 0.5f);
        ema = Math.max(0.0f, ema - 0.1f);
        sp.edit().putFloat(topic, ema).apply();
    }

    public static int getTotalCorrect(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_TOTAL_CORRECT, 0);
    }

    public static int getTotalWrong(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_TOTAL_WRONG, 0);
    }

    public static float getOverallAccuracy(Context ctx) {
        int c = getTotalCorrect(ctx);
        int w = getTotalWrong(ctx);
        int total = c + w;
        if (total == 0) return 0f;
        return (c * 100f) / total;
    }

    public static void recordPracticeTestResult(Context ctx, float score, String weakAxis) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        int count = sp.getInt(KEY_PRACTICE_TEST_COUNT, 0) + 1;
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_PRACTICE_TEST_COUNT, count);
        editor.putFloat(KEY_LAST_PRACTICE_SCORE, score);
        if (weakAxis != null && !weakAxis.isEmpty() && !"Không có".equals(weakAxis)) {
            String key = KEY_WEAK_AXIS_PREFIX + weakAxis;
            editor.putInt(key, sp.getInt(key, 0) + 1);
        }
        editor.apply();
    }

    public static int getPracticeTestCount(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_PRACTICE_TEST_COUNT, 0);
    }

    public static float getLastPracticeScore(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getFloat(KEY_LAST_PRACTICE_SCORE, 0f);
    }

    public static String getWeakestAxis(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String[] axes = {"Hàm số – đạo hàm – tích phân", "Tổ hợp – xác suất", "Hình học giải tích"};
        String result = "Không có";
        int max = 0;
        for (String axis : axes) {
            int c = sp.getInt(KEY_WEAK_AXIS_PREFIX + axis, 0);
            if (c > max) {
                max = c;
                result = axis;
            }
        }
        return result;
    }

    // ==========================================================
    // 🔍 Debug hỗ trợ: xem tất cả EMA
    // ==========================================================
    public static Map<String, Float> getAllEma(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        Map<String, Float> result = new HashMap<>();
        for (Map.Entry<String, ?> e : sp.getAll().entrySet()) {
            if (e.getValue() instanceof Float) {
                result.put(e.getKey(), (Float) e.getValue());
            }
        }
        return result;
    }
}
