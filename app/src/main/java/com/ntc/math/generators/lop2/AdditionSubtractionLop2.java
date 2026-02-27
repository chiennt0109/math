package com.ntc.math.generators.lop2;

import com.ntc.math.Question;
import com.ntc.math.generators.QuestionGenerationStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AdditionSubtractionLop2 implements QuestionGenerationStrategy {
    @Override
    public Question generate(int difficulty) {
        Random random = new Random();
        int num1, num2, answer;
        String questionText;

        // Mức độ 1 & 2: Cộng/Trừ có nhớ 2 chữ số
        if (difficulty < 3) {
            // Đảm bảo phép tính có nhớ
            num1 = random.nextInt(90) + 10; // 10-99
            num2 = random.nextInt(90) + 10; // 10-99

            // 50% là phép cộng, 50% là phép trừ
            if (random.nextBoolean()) { // Phép cộng
                answer = num1 + num2;
                questionText = num1 + " + " + num2 + " = ?";
            } else { // Phép trừ (đảo để không bị âm)
                if (num1 < num2) {
                    int temp = num1;
                    num1 = num2;
                    num2 = temp;
                }
                answer = num1 - num2;
                questionText = num1 + " - " + num2 + " = ?";
            }
        }
        // Mức độ 3: Tìm X
        else {
            num1 = random.nextInt(50) + 1;
            num2 = random.nextInt(50) + 1;
            answer = num2;
            questionText = "X + " + num1 + " = " + (num1 + num2);
        }

        // Tạo các lựa chọn
        List<String> options = new ArrayList<>();
        options.add(String.valueOf(answer));
        while (options.size() < 4) {
            int wrongAnswer = answer + random.nextInt(21) - 10; // Lệch tối đa 10
            if (wrongAnswer >= 0 && !options.contains(String.valueOf(wrongAnswer))) {
                options.add(String.valueOf(wrongAnswer));
            }
        }

        Collections.shuffle(options);
        return new Question(questionText, 0, options, String.valueOf(answer));
    }
}