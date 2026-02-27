package com.ntc.math.generators.lop2;

import com.ntc.math.Question;
import com.ntc.math.generators.QuestionGenerationStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultiplicationDivisionLop2 implements QuestionGenerationStrategy {
    @Override
    public Question generate(int difficulty) {
        Random random = new Random();
        int[] factors = {2, 3, 4, 5};

        int num1, num2, answer;
        String questionText;

        num1 = factors[random.nextInt(factors.length)];

        if (difficulty == 1) { // Mức độ 1: Phép nhân
            num2 = random.nextInt(9) + 1; // Nhân với số từ 1-9
            answer = num1 * num2;
            questionText = num1 + " x " + num2 + " = ?";
        } else { // Mức độ 2 & 3: Phép chia
            num2 = random.nextInt(9) + 1;
            int product = num1 * num2;
            answer = num2; // Đáp án là số còn lại
            questionText = product + " : " + num1 + " = ?";
        }

        List<String> options = new ArrayList<>();
        options.add(String.valueOf(answer));

        while (options.size() < 4) {
            int wrongAnswerOffset = random.nextInt(5) - 2; // -2, -1, 0, 1, 2
            int wrongAnswer = answer + wrongAnswerOffset;
            if (wrongAnswer > 0 && wrongAnswer != answer && !options.contains(String.valueOf(wrongAnswer))) {
                options.add(String.valueOf(wrongAnswer));
            }
        }
        Collections.shuffle(options);
        return new Question(questionText, 0, options, String.valueOf(answer));
    }
}