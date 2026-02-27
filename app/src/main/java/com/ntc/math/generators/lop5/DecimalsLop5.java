package com.ntc.math.generators.lop5;

import com.ntc.math.Question;
import com.ntc.math.generators.QuestionGenerationStrategy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DecimalsLop5 implements QuestionGenerationStrategy {
    @Override
    public Question generate(int difficulty) {
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("#.#");

        double num1 = (random.nextInt(90) + 10) / 10.0; // Số thập phân 1 chữ số, ví dụ 1.2, 9.9
        double num2 = (random.nextInt(50) + 1) / 10.0;  // Số thập phân nhỏ hơn, ví dụ 0.1, 5.0

        String questionText;
        double answer;

        if (difficulty < 3) { // Cộng, trừ
            if (random.nextBoolean()) { // Phép cộng
                answer = num1 + num2;
                questionText = df.format(num1) + " + " + df.format(num2) + " = ?";
            } else { // Phép trừ
                if (num1 < num2) { double temp = num1; num1 = num2; num2 = temp; }
                answer = num1 - num2;
                questionText = df.format(num1) + " - " + df.format(num2) + " = ?";
            }
        } else { // Phép nhân đơn giản
            num1 = (random.nextInt(40) + 1) / 10.0; // 0.1 -> 4.0
            num2 = random.nextInt(4) + 2; // Nhân với số nguyên 2,3,4,5
            answer = num1 * num2;
            questionText = df.format(num1) + " x " + (int)num2 + " = ?";
        }

        String correctAnswer = df.format(answer);
        List<String> options = new ArrayList<>();
        options.add(correctAnswer);
        while (options.size() < 4) {
            double wrongAnswer = answer + (random.nextInt(21) - 10) / 10.0; // Lệch +- 1.0
            String wrongAnswerStr = df.format(wrongAnswer);
            if (wrongAnswer > 0 && !options.contains(wrongAnswerStr)) {
                options.add(wrongAnswerStr);
            }
        }
        Collections.shuffle(options);
        return new Question(questionText, 0, options, correctAnswer);
    }
}