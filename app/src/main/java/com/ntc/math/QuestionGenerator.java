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

        // 50 câu theo ma trận dọc (không bị giới hạn khối hiện tại)
        addV(map, topics.get(0), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 1, "Khái niệm hàm số nào từ THCS cần nhắc lại trước khi học f(x)=ax²+bx+c?", "Mỗi x cho tối đa một y", "Mỗi y cho một x", "f(x) luôn dương", "Đồ thị luôn là đường thẳng", "Mỗi x cho tối đa một y");
        addV(map, topics.get(0), "Hàm số – đạo hàm – tích phân", 11, "TIẾN", 2, "Từ bảng giá trị hàm bậc hai lớp 10, bước TIẾN phù hợp cho lớp 11 là gì?", "Đọc tính tuần hoàn đồ thị", "Học tích phân từng phần", "Đổi biến logarit", "Bỏ qua đồ thị", "Đọc tính tuần hoàn đồ thị");
        addV(map, topics.get(0), "Hình học giải tích", 10, "TÍCH HỢP", 2, "Khi nhận dạng parabol, kiến thức hình học giải tích nào được tích hợp trực tiếp?", "Tọa độ đỉnh và trục đối xứng", "Quy tắc cộng xác suất", "Công thức nhị thức", "Định nghĩa đạo hàm", "Tọa độ đỉnh và trục đối xứng");
        addV(map, topics.get(0), "Tổ hợp – xác suất", 10, "TIẾN", 2, "Tình huống sư phạm: để học sinh thấy liên môn, giáo viên nên làm gì?", "Dùng bảng tần số giá trị hàm để dự đoán xác suất", "Chỉ cho chép công thức", "Bỏ phần ví dụ", "Không cho thảo luận", "Dùng bảng tần số giá trị hàm để dự đoán xác suất");

        addV(map, topics.get(1), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 3, "Xét đồng biến/nghịch biến ở lớp 10 chuẩn bị tốt nhất cho năng lực nào lớp 12?", "Lập bảng biến thiên bằng đạo hàm", "Tính chỉnh hợp", "Chứng minh vuông góc", "Giải logarit", "Lập bảng biến thiên bằng đạo hàm");
        addV(map, topics.get(1), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 2, "Trước khi xét đơn điệu, học sinh cần LÙI về thao tác nào?", "Tính giá trị hàm tại vài điểm", "Tính tích phân", "Giải xác suất có điều kiện", "Dùng tọa độ cực", "Tính giá trị hàm tại vài điểm");
        addV(map, topics.get(1), "Hình học giải tích", 11, "TÍCH HỢP", 3, "Vẽ đồ thị cơ bản giúp TIẾN sang nội dung nào của lượng giác lớp 11?", "So sánh dạng đồ thị tuần hoàn", "Khai triển Newton", "Chỉnh hợp lặp", "Nguyên lý bù trừ", "So sánh dạng đồ thị tuần hoàn");
        addV(map, topics.get(1), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 3, "Bài toán cụ thể: để dự đoán cực trị nhanh, học sinh nên ưu tiên gì ở cấp 2?", "Quan sát chiều biến thiên và điểm nghi ngờ", "Nhớ công thức tổ hợp", "Đổi sang hình không gian", "Bấm máy không kiểm tra", "Quan sát chiều biến thiên và điểm nghi ngờ");

        addV(map, topics.get(2), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 3, "GTLN-GTNN của parabol cần củng cố công cụ nào?", "Hoàn thành bình phương", "Hoán vị", "Định lý cos", "Biến đổi logarit", "Hoàn thành bình phương");
        addV(map, topics.get(2), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 4, "Biện luận nghiệm phương trình bậc hai là nền cho chuyên đề nào lớp 12?", "Tương giao đồ thị khi khảo sát hàm", "Xác suất nhị thức", "Elip chính tắc", "Số phức", "Tương giao đồ thị khi khảo sát hàm");
        addV(map, topics.get(2), "Hình học giải tích", 10, "TÍCH HỢP", 3, "Cực trị hàm bậc hai gắn hình học giải tích như thế nào?", "Tung độ đỉnh là GTLN/GTNN", "Bán kính đường tròn ngẫu nhiên", "Cạnh tam giác đều", "Tâm ngoại tiếp", "Tung độ đỉnh là GTLN/GTNN");
        addV(map, topics.get(2), "Tổ hợp – xác suất", 11, "TIẾN", 3, "Tình huống sư phạm: học sinh vượt chuẩn lớp 10, nên TIẾN bằng hoạt động nào?", "Mô phỏng nhiều bộ hệ số rồi phân loại số nghiệm", "Giảm bài về chỉ tính nhanh", "Bỏ phần giải thích", "Không phản hồi sai lầm", "Mô phỏng nhiều bộ hệ số rồi phân loại số nghiệm");

        addV(map, topics.get(3), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 5, "Bài toán tối ưu lớp 10 nên TIẾN trực tiếp đến năng lực nào lớp 12?", "Lập mô hình tối ưu bằng đạo hàm", "Đếm chỉnh hợp", "Vẽ biểu đồ cột", "Giải phương trình lượng giác", "Lập mô hình tối ưu bằng đạo hàm");
        addV(map, topics.get(3), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 4, "Khi tối ưu thất bại, bước LÙI hợp lý là gì?", "Quay lại lập hàm mục tiêu đúng biến", "Đổi sang chương xác suất", "Bỏ điều kiện", "Chỉ đoán kết quả", "Quay lại lập hàm mục tiêu đúng biến");
        addV(map, topics.get(3), "Hình học giải tích", 12, "TÍCH HỢP", 5, "Tối ưu hóa có tham số có thể tích hợp dọc với nội dung nào?", "Khoảng cách điểm-đường trong Oxy", "Quy tắc cộng xác suất", "Bảng sin cos", "Lãi kép", "Khoảng cách điểm-đường trong Oxy");
        addV(map, topics.get(3), "Tổ hợp – xác suất", 11, "TÍCH HỢP", 4, "Bài toán tích hợp: chọn phương án đúng.", "Mô phỏng ngẫu nhiên để kiểm chứng nghiệm tối ưu", "Chỉ giữ 1 trường hợp", "Bỏ đơn vị đo", "Không kiểm tra điều kiện", "Mô phỏng ngẫu nhiên để kiểm chứng nghiệm tối ưu");
        addV(map, topics.get(3), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 5, "Hàm chứa tham số ở cấp 4 nên đánh giá theo tiêu chí nào?", "Lập luận điều kiện tham số theo mục tiêu bài toán", "Thuộc lòng kết quả", "Chọn đáp án dài nhất", "Chỉ xét 1 điểm", "Lập luận điều kiện tham số theo mục tiêu bài toán");

        addV(map, topics.get(4), "Hàm số – đạo hàm – tích phân", 11, "LÙI", 1, "Trước khi học lượng giác, học sinh cần LÙI về đơn vị đo nào?", "Radian và độ", "Mét và cm", "kg và g", "Lít và ml", "Radian và độ");
        addV(map, topics.get(4), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 2, "Nhận dạng đồ thị sin/cos dựa trên kiến thức nền nào từ lớp 10?", "Đọc biến thiên cơ bản của đồ thị", "Đếm hoán vị", "Giải hệ 3 ẩn", "Tính tích phân", "Đọc biến thiên cơ bản của đồ thị");
        addV(map, topics.get(4), "Hình học giải tích", 11, "TÍCH HỢP", 2, "Đường tròn lượng giác tích hợp trực tiếp với trục nào?", "Tọa độ điểm trên đường tròn đơn vị", "Công thức nhị thức", "Biến cố đối", "Dãy số", "Tọa độ điểm trên đường tròn đơn vị");
        addV(map, topics.get(4), "Tổ hợp – xác suất", 12, "TIẾN", 2, "Dạy nhận dạng đồ thị lượng giác có thể TIẾN cho xác suất bằng cách nào?", "Mô hình hóa dao động tuần hoàn thành dữ liệu", "Bỏ dữ liệu thực tế", "Chỉ đưa đáp án", "Không thảo luận", "Mô hình hóa dao động tuần hoàn thành dữ liệu");

        addV(map, topics.get(5), "Hàm số – đạo hàm – tích phân", 11, "LÙI", 2, "Tập xác định của tan(x) cần củng cố ý nào?", "Điểm làm mẫu số bằng 0", "Đỉnh parabol", "Công sai cấp số", "Biến cố độc lập", "Điểm làm mẫu số bằng 0");
        addV(map, topics.get(5), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 3, "Xét đơn điệu hàm lượng giác chuẩn bị cho bài nào lớp 12?", "Dùng đạo hàm đánh giá biến thiên", "Tính C(n,k)", "Phương trình elip", "Nguyên lý Dirichlet", "Dùng đạo hàm đánh giá biến thiên");
        addV(map, topics.get(5), "Hình học giải tích", 11, "TÍCH HỢP", 3, "Vẽ đồ thị cơ bản lượng giác kết nối hình học giải tích qua nội dung nào?", "Tịnh tiến và co giãn đồ thị", "Đếm hoán vị lặp", "Bảng phân phối", "Giải logarit", "Tịnh tiến và co giãn đồ thị");
        addV(map, topics.get(5), "Tổ hợp – xác suất", 10, "LÙI", 2, "Nếu học sinh yếu biến đổi đại số, bước LÙI phù hợp?", "Rút gọn biểu thức trước khi xét tập xác định", "Nhảy sang đạo hàm", "Làm đề nâng cao", "Bỏ phần giải", "Rút gọn biểu thức trước khi xét tập xác định");

        addV(map, topics.get(6), "Hàm số – đạo hàm – tích phân", 11, "LÙI", 3, "GTLN-GTNN của sin/cos cần nhắc lại miền giá trị nào?", "[-1,1]", "[0,+∞)", "R", "(0,1)", "[-1,1]");
        addV(map, topics.get(6), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 4, "Tìm cực trị lượng giác là tiền đề của kỹ thuật nào lớp 12?", "Xử lý phương trình đạo hàm bằng lượng giác", "Đếm biến cố", "Vẽ hypebol", "Tính xác suất có điều kiện", "Xử lý phương trình đạo hàm bằng lượng giác");
        addV(map, topics.get(6), "Hình học giải tích", 12, "TÍCH HỢP", 4, "Biện luận nghiệm lượng giác có thể tích hợp với hình học giải tích bằng cách nào?", "Diễn giải nghiệm bằng giao điểm đồ thị", "Đổi sang nhị thức", "Bỏ biểu diễn hình", "Chỉ thay số", "Diễn giải nghiệm bằng giao điểm đồ thị");
        addV(map, topics.get(6), "Tổ hợp – xác suất", 11, "TÍCH HỢP", 3, "Câu lý thuyết: tích hợp dọc hiệu quả khi nào?", "Kết quả lượng giác được dùng làm dữ liệu xác suất", "Tách rời hoàn toàn chủ đề", "Không kiểm tra nền", "Chỉ học thuộc", "Kết quả lượng giác được dùng làm dữ liệu xác suất");

        addV(map, topics.get(7), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 5, "Tối ưu hóa thực tế với lượng giác nên TIẾN lên đâu?", "Bài toán cực trị có ràng buộc bằng đạo hàm", "Tính giai thừa", "Vẽ đồ thị cột", "Giải hệ tuyến tính", "Bài toán cực trị có ràng buộc bằng đạo hàm");
        addV(map, topics.get(7), "Hàm số – đạo hàm – tích phân", 11, "LÙI", 4, "Khi học sinh vướng mô hình, bước LÙI tốt nhất?", "Quay lại xác định biến và miền giá trị", "Chuyển chương mới", "Chỉ đưa đáp số", "Bỏ điều kiện", "Quay lại xác định biến và miền giá trị");
        addV(map, topics.get(7), "Hình học giải tích", 12, "TÍCH HỢP", 5, "Bài toán tích hợp dọc: chọn hướng đúng.", "Kết nối góc lượng giác với tiếp tuyến đồ thị", "Đếm chập k", "Tính xác suất toàn phần", "Không cần hình", "Kết nối góc lượng giác với tiếp tuyến đồ thị");
        addV(map, topics.get(7), "Tổ hợp – xác suất", 11, "TIẾN", 4, "Tình huống sư phạm: học sinh vượt chuẩn nên làm gì?", "Giao dự án mô phỏng dao động + xác suất", "Giảm xuống bài chép", "Không cho phản biện", "Chỉ trắc nghiệm nhớ", "Giao dự án mô phỏng dao động + xác suất");

        addV(map, topics.get(8), "Hàm số – đạo hàm – tích phân", 12, "LÙI", 1, "Khái niệm ln(x) cần quay lại kiến thức nền nào?", "Điều kiện x>0", "x bất kỳ", "x<0", "x=0", "Điều kiện x>0");
        addV(map, topics.get(8), "Hàm số – đạo hàm – tích phân", 10, "LÙI", 2, "Trước khi học e^x, nên củng cố năng lực nào từ lớp 10?", "Thay số tính giá trị hàm", "Tính diện tích hình tròn", "Đếm chỉnh hợp", "Chứng minh vuông góc", "Thay số tính giá trị hàm");
        addV(map, topics.get(8), "Hình học giải tích", 12, "TÍCH HỢP", 2, "Nhận dạng đồ thị mũ/logarit tích hợp hình học qua gì?", "Quan sát tiệm cận và giao trục", "Tổ hợp lặp", "Biến cố độc lập", "Định nghĩa xác suất", "Quan sát tiệm cận và giao trục");
        addV(map, topics.get(8), "Tổ hợp – xác suất", 11, "TIẾN", 2, "Có thể TIẾN theo chiều dọc bằng hoạt động nào?", "So sánh tăng trưởng mũ với mô hình xác suất", "Chỉ làm bài đóng", "Bỏ giải thích", "Không phản hồi", "So sánh tăng trưởng mũ với mô hình xác suất");

        addV(map, topics.get(9), "Hàm số – đạo hàm – tích phân", 12, "LÙI", 2, "Tập xác định ln(g(x)) yêu cầu LÙI kiểm tra gì?", "g(x) > 0", "g(x)=0", "g(x)<0", "Không cần điều kiện", "g(x) > 0");
        addV(map, topics.get(9), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 3, "Xét đơn điệu hàm mũ/logarit chuẩn bị cho dạng nào?", "Bất phương trình mũ-logarit", "Tổ hợp chập", "Định lý sin", "Đường tròn", "Bất phương trình mũ-logarit");
        addV(map, topics.get(9), "Hình học giải tích", 10, "LÙI", 2, "Nếu học sinh nhầm đồ thị, cần LÙI về kỹ năng nào?", "Vẽ hệ trục và lấy điểm chuẩn", "Tính đạo hàm bậc hai", "Giải xác suất", "Biện luận tham số", "Vẽ hệ trục và lấy điểm chuẩn");
        addV(map, topics.get(9), "Tổ hợp – xác suất", 12, "TÍCH HỢP", 3, "Bài toán cụ thể: tích hợp dọc giữa mũ-logarit và xác suất là gì?", "Mô hình phân rã phóng xạ với xác suất sống sót", "Đếm tam giác", "Giải hệ tuyến tính", "Tính chu vi", "Mô hình phân rã phóng xạ với xác suất sống sót");

        addV(map, topics.get(10), "Hàm số – đạo hàm – tích phân", 12, "LÙI", 3, "Trước khi tìm cực trị bằng đạo hàm, cần ôn gì?", "Quy tắc đạo hàm cơ bản", "Công thức tổ hợp", "Định lý cos", "Tỉ lệ thức", "Quy tắc đạo hàm cơ bản");
        addV(map, topics.get(10), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 4, "Biện luận nghiệm f'(x)=0 hỗ trợ năng lực nào?", "Đọc bản chất điểm cực trị", "Đếm biến cố", "Vẽ hypebol", "Tính xác suất", "Đọc bản chất điểm cực trị");
        addV(map, topics.get(10), "Hình học giải tích", 11, "TÍCH HỢP", 4, "Cực trị hàm số kết nối hình học giải tích qua đâu?", "Tiếp tuyến song song trục Ox", "Đường tròn ngoại tiếp", "Tổ hợp lặp", "Số trung bình", "Tiếp tuyến song song trục Ox");
        addV(map, topics.get(10), "Tổ hợp – xác suất", 12, "TIẾN", 4, "Tình huống sư phạm: học sinh vượt chuẩn lớp 12 nên TIẾN ra sao?", "Làm dự án tối ưu có nhiễu xác suất", "Giảm về bài thay số", "Chỉ học thuộc", "Không yêu cầu giải thích", "Làm dự án tối ưu có nhiễu xác suất");

        addV(map, topics.get(11), "Hàm số – đạo hàm – tích phân", 12, "TIẾN", 5, "Bài toán tối ưu hóa thực tế ở cấp 4 cần sản phẩm nào?", "Mô hình hàm + lập luận nghiệm tối ưu", "Chỉ nêu đáp số", "Liệt kê công thức", "Bỏ dữ kiện", "Mô hình hàm + lập luận nghiệm tối ưu");
        addV(map, topics.get(11), "Hàm số – đạo hàm – tích phân", 11, "LÙI", 4, "Khi sai hàng loạt ở tối ưu, nên LÙI về đâu?", "Phân tích đơn vị và điều kiện thực tế", "Chuyển sang chương khác", "Bỏ bài toán", "Chấm theo cảm tính", "Phân tích đơn vị và điều kiện thực tế");
        addV(map, topics.get(11), "Hình học giải tích", 12, "TÍCH HỢP", 5, "Hàm có tham số tích hợp dọc tốt nhất với chủ đề nào?", "Vị trí tương đối đường thẳng - đường cong", "Tổ hợp chập k", "Xác suất toàn phần", "Cấp số nhân", "Vị trí tương đối đường thẳng - đường cong");
        addV(map, topics.get(11), "Tổ hợp – xác suất", 12, "TÍCH HỢP", 5, "Câu tư duy tổ chức chương trình: dấu hiệu cá nhân hóa tốt là gì?", "Hệ thống tự lùi/tiến theo năng lực ở cùng trục", "Cố định một lộ trình", "Chỉ kiểm tra cuối kỳ", "Bỏ theo dõi tiến trình", "Hệ thống tự lùi/tiến theo năng lực ở cùng trục");
        addV(map, topics.get(11), "Hàm số – đạo hàm – tích phân", 10, "TIẾN", 4, "Câu lý thuyết: ma trận dọc hiệu quả nhất khi nào?", "Mỗi cấp năng lực có điểm nối lên/xuống rõ ràng", "Chia cứng theo lớp", "Không có tiêu chí", "Chỉ dạy theo đề thi", "Mỗi cấp năng lực có điểm nối lên/xuống rõ ràng");

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
                + "\nLoại: " + type
                + "\nMức độ: " + level
                + "\n" + prompt;
        map.computeIfAbsent(topic, k -> new ArrayList<>())
                .add(new Question(text, 0, Arrays.asList(a, b, c, d), correct));
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
