package com.ntc.math.generators.lop4;

import com.ntc.math.Question;
import com.ntc.math.generators.QuestionGenerationStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FractionsLop4 implements QuestionGenerationStrategy {
    @Override
    public Question generate(int difficulty) {
        Random random = new Random();
        int num1, den1, num2, den2;
        String questionText, correctAnswer;

        // Mẫu số chung
        den1 = random.nextInt(5) + 5; // Mẫu số từ 5-9

        // Mức độ 1 & 2: Cộng/Trừ phân số cùng mẫu
        num1 = random.nextInt(den1 - 1) + 1;
        num2 = random.nextInt(den1 - num1) + 1; // Đảm bảo num1 + num2 <= den1

        if (difficulty < 3) { // Phép cộng
            int answerNum = num1 + num2;
            questionText = num1 + "/" + den1 + " + " + num2 + "/" + den1 + " = ?";
            correctAnswer = answerNum + "/" + den1;
        } else { // Phép trừ
            int sum = num1 + num2;
            questionText = sum + "/" + den1 + " - " + num1 + "/" + den1 + " = ?";
            correctAnswer = num2 + "/" + den1;
        }

        // Tạo các lựa chọn sai
        List<String> options = new ArrayList<>();
        options.add(correctAnswer);
        while (options.size() < 4) {
            int wrongNum = random.nextInt(den1) + 1;
            String wrongAnswer = wrongNum + "/" + den1;
            if (!options.contains(wrongAnswer)) {
                options.add(wrongAnswer);
            }
        }
        Collections.shuffle(options);
        return new Question(questionText, 0, options, correctAnswer);
    }
}