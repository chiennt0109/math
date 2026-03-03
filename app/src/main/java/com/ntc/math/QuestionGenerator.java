package com.ntc.math;

import java.util.*;

/**
 * QuestionGenerator
 * --------------------
 * Sinh câu hỏi ngẫu nhiên theo chủ đề và độ khó.
 * Mỗi chủ đề có tập mẫu riêng biệt, tránh trùng lặp.
 */
public class QuestionGenerator {

    private static final Random rand = new Random();
    private static final Map<String, List<Question>> VERTICAL_TOPIC_BANK = createVerticalTopicBank();

    public static List<Question> generate(String topic, String difficulty) {
        return generate(topic, difficulty, 0);
    }

    public static List<Question> generate(String topic, String difficulty, int preferredLevel) {
        List<Question> list = new ArrayList<>();

        int count = switch (difficulty) {
            case "Dễ" -> 5;
            case "Trung bình" -> 7;
            default -> 10;
        };

        if (VERTICAL_TOPIC_BANK.containsKey(topic)) {
            List<Question> pool = VERTICAL_TOPIC_BANK.get(topic);
            List<Question> filtered = new ArrayList<>();
            if (preferredLevel >= 1) {
                for (Question q : pool) {
                    if (q.getLevel() == preferredLevel) filtered.add(q);
                }
            }
            if (filtered.isEmpty()) filtered = pool;
            for (int i = 0; i < count; i++) {
                list.add(filtered.get(rand.nextInt(filtered.size())));
            }
            return list;
        }

        for (int i = 0; i < count; i++) {
            Question q = createQuestion(topic, difficulty);
            if (q != null) list.add(q);
        }
        return list;
    }

    private static Question createQuestion(String topic, String diff) {
        String text = "";
        List<String> options = new ArrayList<>();
        String correct = "";

        int a = rand.nextInt(20) + 1;
        int b = rand.nextInt(20) + 1;

        switch (topic) {

            // -------- LỚP 1–2 --------
            case "Số và phép tính trong phạm vi 100" -> {
                text = "🧮 " + a + " + " + b + " = ?";
                correct = String.valueOf(a + b);
                options = generateOptions(correct, 3);
            }
            case "Phép cộng – trừ có nhớ" -> {
                text = "➕ " + (a + 10) + " - " + a + " = ?";
                correct = "10";
                options = generateOptions(correct, 3);
            }
            case "Đo độ dài, thời gian, tiền Việt Nam" -> {
                text = "💰 Nếu có " + a + " đồng, thêm " + b + " đồng, tổng là?";
                correct = (a + b) + " đồng";
                options = Arrays.asList((a + b) + " đồng", (a + b + 1) + " đồng", (a + b - 1) + " đồng", (a + b + 2) + " đồng");
            }

            // -------- LỚP 3–4 --------
            case "Phép nhân và chia" -> {
                text = "✖️ " + a + " × " + b + " = ?";
                correct = String.valueOf(a * b);
                options = generateOptions(correct, 3);
            }
            case "Phân số và so sánh phân số" -> {
                text = "🍰 So sánh " + a + "/" + b + " và " + a + "/" + (b + 1);
                correct = ">";
                options = Arrays.asList(">", "<", "=");
            }
            case "Chu vi và diện tích các hình cơ bản" -> {
                text = "📐 Hình vuông cạnh " + a + " cm, chu vi là?";
                correct = String.valueOf(4 * a);
                options = generateOptions(correct, 3);
            }

            // -------- LỚP 5–6 --------
            case "Số thập phân & phép tính với số thập phân" -> {
                double x = rand.nextInt(9) + rand.nextDouble();
                double y = rand.nextInt(9) + rand.nextDouble();
                text = "💯 " + String.format("%.1f", x) + " + " + String.format("%.1f", y) + " = ?";
                correct = String.format("%.1f", x + y);
                options = generateOptions(correct, 3);
            }
            case "Phân số & hỗn số" -> {
                text = "🍰 " + a + "/" + b + " + " + a + "/" + b + " = ?";
                correct = (2 * a) + "/" + b;
                options = Arrays.asList(correct, (a + b) + "/" + b, (a * b) + "/" + a, "Không xác định");
            }

            // -------- LỚP 7–9 --------
            case "Biểu thức & biến số" -> {
                text = "🔢 Cho x=" + a + ", tính giá trị của x+5 = ?";
                correct = String.valueOf(a + 5);
                options = generateOptions(correct, 3);
            }
            case "Phương trình & bất phương trình bậc nhất" -> {
                int c = rand.nextInt(10);
                text = "🧩 Giải phương trình: x + " + a + " = " + (a + c);
                correct = String.valueOf(c);
                options = generateOptions(correct, 3);
            }
            case "Định lý Pitago & tam giác vuông" -> {
                text = "📏 Tam giác vuông có 2 cạnh " + a + ", " + b + ". Cạnh huyền bằng?";
                correct = String.valueOf((int) Math.sqrt(a * a + b * b));
                options = generateOptions(correct, 3);
            }
            case "Hàm số bậc nhất & đồ thị" -> {
                int k = rand.nextInt(5) + 1;
                int x = rand.nextInt(10);
                text = "📉 Với hàm y=" + k + "x+" + a + ", khi x=" + x + " thì y=?";
                correct = String.valueOf(k * x + a);
                options = generateOptions(correct, 3);
            }
            case "Xác suất & tổ hợp cơ bản" -> {
                text = "🎲 Tung đồng xu, xác suất ra mặt sấp là?";
                correct = "1/2";
                options = Arrays.asList("1/2", "1/3", "1/4", "1");
            }

            // -------- LỚP 10-12 (ma trận dọc) --------
            case "Lớp 10 - Hàm số bậc hai - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                 "Lớp 10 - Hàm số bậc hai - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                 "Lớp 10 - Hàm số bậc hai - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                 "Lớp 10 - Hàm số bậc hai - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số",
                 "Lớp 11 - Hàm lượng giác & liên tục - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                 "Lớp 11 - Hàm lượng giác & liên tục - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                 "Lớp 11 - Hàm lượng giác & liên tục - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                 "Lớp 11 - Hàm lượng giác & liên tục - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số",
                 "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                 "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                 "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                 "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số" -> {
                List<Question> pool = VERTICAL_TOPIC_BANK.get(topic);
                if (pool != null && !pool.isEmpty()) {
                    return pool.get(rand.nextInt(pool.size()));
                }
            }

            default -> {
                text = "⚙️ Câu hỏi không xác định chủ đề.";
                correct = "Không biết";
                options = Arrays.asList("A", "B", "C", "D");
            }
        }

        return new Question(text, 0, options, correct);
    }

    private static Map<String, List<Question>> createVerticalTopicBank() {
        Map<String, List<Question>> map = new HashMap<>();

        List<String> topics = Arrays.asList(
                "Lớp 10 - Hàm số bậc hai - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                "Lớp 10 - Hàm số bậc hai - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                "Lớp 10 - Hàm số bậc hai - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                "Lớp 10 - Hàm số bậc hai - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số",
                "Lớp 11 - Hàm lượng giác & liên tục - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                "Lớp 11 - Hàm lượng giác & liên tục - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                "Lớp 11 - Hàm lượng giác & liên tục - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                "Lớp 11 - Hàm lượng giác & liên tục - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số",
                "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị",
                "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị",
                "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm",
                "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số"
        );

        for (String topic : topics) {
            map.put(topic, new ArrayList<>());
        }

        // Demo theo CTGDPT 2018 Toán THPT (50 câu)
        addV(map, topics.get(0), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 1, "Cho f(x)=x²-2x+3. Giá trị f(2) bằng?", "3", "1", "0", "5", "3");
        addV(map, topics.get(0), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 1, "Đồ thị y=ax² (a>0) có dạng nào?", "Parabol mở lên", "Đường thẳng", "Hypebol", "Elip", "Parabol mở lên");
        addV(map, topics.get(0), "Hàm số – đạo hàm – tích phân", 10, "TIẾN", 2, "Trong hàm bậc hai y=ax²+bx+c, trục đối xứng là?", "x=-b/(2a)", "x=b/(2a)", "y=-b/(2a)", "x=-c/(2a)", "x=-b/(2a)");
        addV(map, topics.get(0), "Hình học giải tích", 10, "TÍCH HỢP", 2, "Đỉnh của y=x²-4x+1 là?", "(2,-3)", "(-2,-3)", "(2,3)", "(4,1)", "(2,-3)");

        addV(map, topics.get(1), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 2, "Hàm y=x² đồng biến trên khoảng nào?", "(0,+∞)", "(-∞,0)", "R", "Không có", "(0,+∞)");
        addV(map, topics.get(1), "Hàm số – đạo hàm – tích phân", 10, "TIẾN", 2, "Tập xác định của y=1/(x-2) là?", "R\\{2}", "R", "x>2", "x<2", "R\\{2}");
        addV(map, topics.get(1), "Hàm số – đạo hàm – tích phân", 10, "TIẾN", 2, "Với y=-x²+2x+3, hàm số đạt GTLN tại x=?", "1", "-1", "2", "3", "1");
        addV(map, topics.get(1), "Hình học giải tích", 10, "TÍCH HỢP", 2, "Đồ thị y=(x-1)²+2 là đồ thị y=x² được?", "Tịnh tiến sang phải 1, lên 2", "Tịnh tiến trái 1, lên 2", "Co giãn theo Ox", "Đối xứng qua Oy", "Tịnh tiến sang phải 1, lên 2");

        addV(map, topics.get(2), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 3, "GTLN của y=-x²+4x-1 là?", "3", "1", "-1", "4", "3");
        addV(map, topics.get(2), "Hàm số – đạo hàm – tích phân", 10, "TIẾN", 3, "Phương trình x²-5x+6=0 có số nghiệm thực là?", "2", "1", "0", "Vô số", "2");
        addV(map, topics.get(2), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 3, "Δ của x²-4x+4 là?", "0", "4", "-4", "8", "0");
        addV(map, topics.get(2), "Hình học giải tích", 10, "TÍCH HỢP", 3, "Parabol y=x²-2x+2 có GTNN bằng?", "1", "0", "2", "-1", "1");

        addV(map, topics.get(3), "Hàm số – đạo hàm – tích phân", 10, "TIẾN", 4, "Hình chữ nhật có chu vi 20, diện tích lớn nhất khi?", "Là hình vuông cạnh 5", "Dài 8 rộng 2", "Dài 6 rộng 4", "Dài 9 rộng 1", "Là hình vuông cạnh 5");
        addV(map, topics.get(3), "Hàm số – đạo hàm – tích phân", 10, "TIẾN", 4, "Để phương trình x²-2(m+1)x+m²=0 có nghiệm kép thì m=?", "1", "0", "-1", "2", "1");
        addV(map, topics.get(3), "Tổ hợp – xác suất", 10, "TÍCH HỢP", 4, "Mô phỏng tối ưu hoá bằng dữ liệu ngẫu nhiên giúp mục tiêu nào?", "Kiểm chứng mô hình", "Bỏ điều kiện", "Thay chứng minh", "Giảm độ khó", "Kiểm chứng mô hình");
        addV(map, topics.get(3), "Hình học giải tích", 11, "TÍCH HỢP", 4, "Bài toán tối ưu khoảng cách trong Oxy thường đưa về?", "Xét cực trị hàm một biến", "Giải tổ hợp", "Đếm chỉnh hợp", "Hệ ba ẩn", "Xét cực trị hàm một biến");

        addV(map, topics.get(4), "Hàm số – đạo hàm – tích phân", 11, "LÙI", 1, "Giá trị sin(0) là?", "0", "1", "-1", "1/2", "0");
        addV(map, topics.get(4), "Hàm số – đạo hàm – tích phân", 11, "LÙI", 1, "Giá trị cos(0) là?", "1", "0", "-1", "1/2", "1");
        addV(map, topics.get(4), "Hàm số – đạo hàm – tích phân", 11, "TIẾN", 2, "Chu kì của hàm y=sin x là?", "2π", "π", "4π", "1", "2π");
        addV(map, topics.get(4), "Hình học giải tích", 11, "TÍCH HỢP", 2, "Điểm M(cos t, sin t) luôn thuộc?", "Đường tròn đơn vị", "Parabol", "Elip", "Hypebol", "Đường tròn đơn vị");

        addV(map, topics.get(5), "Hàm số – đạo hàm – tích phân", 11, "LÙI", 2, "Tập xác định của y=tan x là?", "R\\{π/2+kπ}", "R", "x>0", "x<0", "R\\{π/2+kπ}");
        addV(map, topics.get(5), "Hàm số – đạo hàm – tích phân", 11, "TIẾN", 2, "Hàm y=sin x đồng biến trên?", "(-π/2,π/2)", "(0,π)", "(π,2π)", "R", "(-π/2,π/2)");
        addV(map, topics.get(5), "Hàm số – đạo hàm – tích phân", 11, "TIẾN", 2, "Hàm liên tục trên đoạn [a,b] thì luôn?", "Có GTLN và GTNN", "Có đạo hàm", "Đơn điệu", "Có nghiệm duy nhất", "Có GTLN và GTNN");
        addV(map, topics.get(5), "Hình học giải tích", 11, "TÍCH HỢP", 3, "Để vẽ y=2sin x, từ y=sin x ta?", "Co giãn theo Oy hệ số 2", "Tịnh tiến phải 2", "Đối xứng qua Ox", "Đổi chu kì", "Co giãn theo Oy hệ số 2");

        addV(map, topics.get(6), "Hàm số – đạo hàm – tích phân", 11, "LÙI", 3, "GTLN của y=sin x là?", "1", "0", "-1", "2", "1");
        addV(map, topics.get(6), "Hàm số – đạo hàm – tích phân", 11, "TIẾN", 3, "Phương trình sin x = 1/2 có nghiệm tổng quát?", "x=π/6+k2π hoặc 5π/6+k2π", "x=kπ", "x=π/2+kπ", "x=2kπ", "x=π/6+k2π hoặc 5π/6+k2π");
        addV(map, topics.get(6), "Hàm số – đạo hàm – tích phân", 11, "TIẾN", 3, "Nếu f liên tục và f(a)f(b)<0 thì tồn tại c∈(a,b) sao cho?", "f(c)=0", "f'(c)=0", "f(c)=1", "f(c)>0", "f(c)=0");
        addV(map, topics.get(6), "Tổ hợp – xác suất", 11, "TÍCH HỢP", 3, "Biên độ dao động lượng giác được dùng trong xác suất để?", "Mô hình hóa hiện tượng tuần hoàn", "Tính chỉnh hợp", "Giải logarit", "Vẽ hypebol", "Mô hình hóa hiện tượng tuần hoàn");

        addV(map, topics.get(7), "Hàm số – đạo hàm – tích phân", 11, "TIẾN", 4, "Bài toán góc nâng hạ theo thời gian thường mô hình bằng?", "Hàm lượng giác", "Hàm hằng", "Đa thức bậc 1", "Hàm log", "Hàm lượng giác");
        addV(map, topics.get(7), "Hàm số – đạo hàm – tích phân", 11, "TIẾN", 4, "Để cực đại hóa A sin x + B cos x, ta dùng?", "Biến đổi về R sin(x+φ)", "Đạo hàm cấp hai ngay", "Lập bảng tổ hợp", "Bất đẳng thức AM-GM", "Biến đổi về R sin(x+φ)");
        addV(map, topics.get(7), "Hình học giải tích", 12, "TÍCH HỢP", 4, "Từ mô hình dao động, bước TIẾN lớp 12 phù hợp là?", "Dùng đạo hàm tìm thời điểm cực đại", "Giải hệ 3 ẩn", "Đếm nhị thức", "Đổi sang tích phân ngay", "Dùng đạo hàm tìm thời điểm cực đại");
        addV(map, topics.get(7), "Tổ hợp – xác suất", 12, "TÍCH HỢP", 4, "Bài toán tích hợp dữ liệu cảm biến theo chu kì phục vụ?", "Dự báo xác suất vượt ngưỡng", "Tính diện tích tam giác", "Giải hệ tuyến tính", "Tính thể tích", "Dự báo xác suất vượt ngưỡng");

        addV(map, topics.get(8), "Hàm số – đạo hàm – tích phân", 12, "LÙI", 1, "Giá trị ln(1)=?", "0", "1", "e", "không xác định", "0");
        addV(map, topics.get(8), "Hàm số – đạo hàm – tích phân", 12, "LÙI", 1, "Tập xác định của y=log₂x là?", "x>0", "x≥0", "R", "x<0", "x>0");
        addV(map, topics.get(8), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 2, "Đạo hàm của y=e^x là?", "e^x", "x e^x", "1/x", "ln x", "e^x");
        addV(map, topics.get(8), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 2, "Đạo hàm của y=ln x là?", "1/x", "x", "ln x", "e^x", "1/x");

        addV(map, topics.get(9), "Hàm số – đạo hàm – tích phân", 12, "LÙI", 2, "Hàm y=ln x đồng biến trên?", "(0,+∞)", "R", "(-∞,0)", "[0,+∞)", "(0,+∞)");
        addV(map, topics.get(9), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 2, "Hàm y=a^x (a>1) có tính chất?", "Đồng biến trên R", "Nghịch biến trên R", "Không liên tục", "Không xác định tại 0", "Đồng biến trên R");
        addV(map, topics.get(9), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 2, "Nghiệm của log₂x = 3 là?", "8", "6", "9", "3", "8");
        addV(map, topics.get(9), "Tổ hợp – xác suất", 12, "TÍCH HỢP", 3, "Tăng trưởng mũ thường dùng để mô tả?", "Dân số/lãi kép", "Chu vi tam giác", "Tịnh tiến vectơ", "Đường tròn", "Dân số/lãi kép");

        addV(map, topics.get(10), "Hàm số – đạo hàm – tích phân", 12, "LÙI", 3, "Nếu f'(x)>0 trên (a,b) thì f?", "Đồng biến trên (a,b)", "Nghịch biến", "Không đổi", "Không kết luận", "Đồng biến trên (a,b)");
        addV(map, topics.get(10), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 3, "Điểm cực trị của hàm số thường là nghiệm của?", "f'(x)=0 hoặc không xác định", "f(x)=0", "f''(x)=0", "f(x)=1", "f'(x)=0 hoặc không xác định");
        addV(map, topics.get(10), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 3, "Để tìm GTLN-GTNN trên [a,b], cần xét?", "Cực trị trong khoảng và hai đầu mút", "Chỉ đầu mút", "Chỉ điểm f'=0", "Chỉ điểm giữa", "Cực trị trong khoảng và hai đầu mút");
        addV(map, topics.get(10), "Hình học giải tích", 12, "TÍCH HỢP", 3, "Hệ số góc tiếp tuyến đồ thị tại x0 bằng?", "f'(x0)", "f(x0)", "1/f'(x0)", "-f'(x0)", "f'(x0)");

        addV(map, topics.get(11), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 4, "Tích phân ∫_0^1 2x dx bằng?", "1", "2", "0", "1/2", "1");
        addV(map, topics.get(11), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 4, "Nguyên hàm của 3x² là?", "x³ + C", "3x + C", "x² + C", "6x + C", "x³ + C");
        addV(map, topics.get(11), "Hàm số – đạo hàm – tích phân", 12, "TÍCH HỢP", 4, "Diện tích hình phẳng giới hạn bởi y=x và Ox trên [0,2] là?", "2", "1", "4", "0", "2");
        addV(map, topics.get(11), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 4, "Bài toán lãi kép tối ưu thời gian thường dùng hàm?", "Mũ - logarit", "Bậc nhất", "Hằng số", "Lượng giác", "Mũ - logarit");

        addV(map, topics.get(11), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 4, "Trong tối ưu chi phí, bước đúng đầu tiên là?", "Lập hàm mục tiêu theo biến", "Tính đạo hàm cấp hai ngay", "Đoán đáp án", "Bỏ điều kiện ràng buộc", "Lập hàm mục tiêu theo biến");
        addV(map, topics.get(10), "Tổ hợp – xác suất", 12, "TÍCH HỢP", 4, "Phân phối xác suất liên tục liên hệ với tích phân ở ý nào?", "Tính xác suất bằng diện tích dưới đường cong", "Tính bằng tổ hợp", "Tính bằng đạo hàm", "Không liên hệ", "Tính xác suất bằng diện tích dưới đường cong");

        return map;
    }

    private static void addV(Map<String, List<Question>> map,
                             String topic,
                             String axis,
                             int focusClass,
                             String type,
                             int level,
                             String prompt,
                             String a,
                             String b,
                             String c,
                             String d,
                             String correct) {
        String text = "Trục kiến thức: " + axis
                + "\nLớp trọng tâm: " + focusClass
                + "\nMức độ: " + level
                + "\n" + prompt;
        map.computeIfAbsent(topic, k -> new ArrayList<>())
                .add(new Question(text, 0, Arrays.asList(a, b, c, d), correct, type, level, topic + "|L" + level + "|" + axis));
    }

    private static List<String> generateOptions(String correct, int count) {
        Set<String> opts = new LinkedHashSet<>();
        opts.add(correct);
        while (opts.size() < count + 1) {
            int offset = rand.nextInt(5) - 2;
            try {
                int val = Integer.parseInt(correct) + offset;
                opts.add(String.valueOf(val));
            } catch (Exception e) {
                opts.add(correct + offset);
            }
        }
        List<String> list = new ArrayList<>(opts);
        Collections.shuffle(list);
        return list;
    }
}
