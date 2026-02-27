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

    public static List<Question> generate(String topic, String difficulty) {
        List<Question> list = new ArrayList<>();

        int count = switch (difficulty) {
            case "Dễ" -> 5;
            case "Trung bình" -> 7;
            default -> 10;
        };

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

            // -------- LỚP 10 --------
            case "Lớp 10 - Hàm số bậc hai - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị" -> {
                int x = rand.nextInt(7) - 3;
                text = "📘 Với f(x)=x²+2x+1, tính f(" + x + ")";
                correct = String.valueOf(x * x + 2 * x + 1);
                options = generateOptions(correct, 3);
            }
            case "Lớp 10 - Hàm số bậc hai - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị" -> {
                text = "📈 Hàm y=x² có tính chất nào đúng?";
                correct = "Nghịch biến trên (-∞,0), đồng biến trên (0,+∞)";
                options = Arrays.asList(
                        "Nghịch biến trên (-∞,0), đồng biến trên (0,+∞)",
                        "Đồng biến trên R",
                        "Nghịch biến trên R",
                        "Không xác định trên R"
                );
            }
            case "Lớp 10 - Hàm số bậc hai - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm" -> {
                text = "🎯 Giá trị nhỏ nhất của y=(x-2)²+3 là?";
                correct = "3";
                options = Arrays.asList("3", "2", "-3", "0");
            }
            case "Lớp 10 - Hàm số bậc hai - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số" -> {
                text = "🧠 Với m để phương trình x²-2x+m=0 có nghiệm kép, m bằng?";
                correct = "1";
                options = Arrays.asList("1", "-1", "2", "0");
            }

            // -------- LỚP 11 --------
            case "Lớp 11 - Hàm lượng giác & liên tục - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị" -> {
                text = "📘 Giá trị sin(0) bằng?";
                correct = "0";
                options = Arrays.asList("0", "1", "-1", "1/2");
            }
            case "Lớp 11 - Hàm lượng giác & liên tục - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị" -> {
                text = "📈 Tập xác định của y=tan(x) là?";
                correct = "R \\ {π/2 + kπ, k∈Z}";
                options = Arrays.asList(
                        "R \\ {π/2 + kπ, k∈Z}",
                        "R",
                        "x ≥ 0",
                        "(-π/2, π/2)"
                );
            }
            case "Lớp 11 - Hàm lượng giác & liên tục - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm" -> {
                text = "🎯 GTLN của y=sin(x) là?";
                correct = "1";
                options = Arrays.asList("1", "0", "-1", "2");
            }
            case "Lớp 11 - Hàm lượng giác & liên tục - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số" -> {
                text = "🧠 Nghiệm của sin(x)=0 trong [0,2π] là?";
                correct = "0 và π và 2π";
                options = Arrays.asList("0 và π và 2π", "π/2 và 3π/2", "chỉ π", "không có nghiệm");
            }

            // -------- LỚP 12 --------
            case "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 1: Khái niệm, thay số, nhận dạng đồ thị" -> {
                text = "📘 Giá trị ln(1) bằng?";
                correct = "0";
                options = Arrays.asList("0", "1", "e", "không xác định");
            }
            case "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 2: Đồng biến/nghịch biến, tập xác định, vẽ đồ thị" -> {
                text = "📈 Tập xác định của y=ln(x) là?";
                correct = "x > 0";
                options = Arrays.asList("x > 0", "x ≥ 0", "R", "x < 0");
            }
            case "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 3: Cực trị, GTLN-GTNN, biện luận nghiệm" -> {
                text = "🎯 Đạo hàm của y=x³ là?";
                correct = "3x²";
                options = Arrays.asList("3x²", "x²", "3x", "x³");
            }
            case "Lớp 12 - Ứng dụng đạo hàm & mũ-logarit - Cấp 4: Tối ưu hoá thực tế, bài toán tích hợp, tham số" -> {
                text = "🧠 Hàm f(x)=x+1/x (x>0) đạt GTNN tại x=?";
                correct = "1";
                options = Arrays.asList("1", "0", "2", "-1");
            }
            default -> {
                text = "⚙️ Câu hỏi không xác định chủ đề.";
                correct = "Không biết";
                options = Arrays.asList("A", "B", "C", "D");
            }
        }

        return new Question(text, 0, options, correct);
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
