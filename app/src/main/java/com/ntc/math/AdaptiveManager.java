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

    // ==========================================================
    // 🏫 Quản lý lớp học hiện tại (ClassSelectActivity dùng)
    // ==========================================================
    public static void setCurrentClass(Context ctx, int grade) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_CLASS, grade).apply();
    }

    public static int getCurrentClass(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_CLASS, 1);
    }

    // ==========================================================
    // 📊 Lưu & cập nhật điểm EMA cho từng chủ đề
    // ==========================================================
    public static void recordAnswer(Context ctx, String topic, boolean isCorrect) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        float oldEma = sp.getFloat(topic, 0.5f);
        float newEma = (float) (EMA_ALPHA * (isCorrect ? 1.0 : 0.0) + (1 - EMA_ALPHA) * oldEma);
        sp.edit().putFloat(topic, newEma).apply();
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
                        "Lớp 10 - Hàm số bậc hai - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                        "Lớp 10 - Hàm số bậc hai - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                        "Lớp 10 - Hàm số bậc hai - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                        "Lớp 10 - Hàm số bậc hai - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số"
                );
            case 11:
                return Arrays.asList(
                        "Lớp 11 - Hàm lượng giác & liên tục - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                        "Lớp 11 - Hàm lượng giác & liên tục - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                        "Lớp 11 - Hàm lượng giác & liên tục - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                        "Lớp 11 - Hàm lượng giác & liên tục - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số"
                );
            case 12:
                return Arrays.asList(
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
