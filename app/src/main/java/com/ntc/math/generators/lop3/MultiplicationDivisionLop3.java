package com.ntc.math.generators.lop3;

import com.ntc.math.Question;
import com.ntc.math.generators.QuestionGenerationStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultiplicationDivisionLop3 implements QuestionGenerationStrategy {
    @Override
    public Question generate(int difficulty) {
        Random random = new Random();
        int num1, num2, answer;
        String questionText;

        // Mức độ 1: Nhân số có 2 chữ số với số có 1 chữ số (không nhớ)
        if (difficulty == 1) {
            num2 = random.nextInt(3) + 2; // Nhân với 2, 3, 4
            int tens = random.nextInt(4) + 1; // 10, 20, 30, 40
            int ones = random.nextInt(4) + 1; // 1, 2, 3
            if (ones >= num2) ones = 1; // Đảm bảo không có nhớ
            num1 = tens * 10 + ones;
            answer = num1 * num2;
            questionText = num1 + " x " + num2 + " = ?";
        }
        // Mức độ 2: Nhân số có 2 chữ số với số có 1 chữ số (có nhớ)
        else if (difficulty == 2) {
            num2 = random.nextInt(4) + 6; // Nhân với 6, 7, 8, 9
            num1 = random.nextInt(89) + 10; // 10-98
            answer = num1 * num2;
            questionText = num1 + " x " + num2 + " = ?";
        }
        // Mức độ 3: Chia hết cho số có 1 chữ số
        else {
            num2 = random.nextInt(8) + 2; // Số chia từ 2-9
            answer = random.nextInt(89) + 10; // Kết quả từ 10-98
            num1 = answer * num2;
            questionText = num1 + " : " + num2 + " = ?";
        }

        // Tạo các lựa chọn
        List<String> options = new ArrayList<>();
        options.add(String.valueOf(answer));
        while (options.size() < 4) {
            int wrongAnswer = answer + random.nextInt(21) - 10; // Lệch 10
            if (wrongAnswer > 0 && !options.contains(String.valueOf(wrongAnswer))) {
                options.add(String.valueOf(wrongAnswer));
            }
        }

        Collections.shuffle(options);
        return new Question(questionText, 0, options, String.valueOf(answer));
    }
}