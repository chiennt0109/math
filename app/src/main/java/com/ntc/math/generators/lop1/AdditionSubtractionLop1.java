package com.ntc.math.generators.lop1;

import com.ntc.math.Question;
import com.ntc.math.generators.QuestionGenerationStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AdditionSubtractionLop1 implements QuestionGenerationStrategy {
    @Override
    public Question generate(int difficulty) {
        Random random = new Random();
        int num1, num2, answer;
        String questionText;

        if (difficulty == 1) { // Mức độ 1: Phép cộng đơn giản
            num1 = random.nextInt(10);
            num2 = random.nextInt(10 - num1);
            answer = num1 + num2;
            questionText = num1 + " + " + num2 + " = ?";
        } else if (difficulty == 2) { // Mức độ 2: Phép trừ đơn giản
            num1 = random.nextInt(10) + 1;
            num2 = random.nextInt(num1);
            answer = num1 - num2;
            questionText = num1 + " - " + num2 + " = ?";
        } else { // Mức độ 3: Tìm số hạng còn thiếu
            answer = random.nextInt(5) + 1;
            num1 = random.nextInt(9 - answer) + 1;
            int sum = num1 + answer;
            questionText = num1 + " + ? = " + sum;
        }

        List<String> options = new ArrayList<>();
        options.add(String.valueOf(answer));

        while (options.size() < 4) {
            int wrongAnswer = random.nextInt(11);
            if (wrongAnswer != answer && !options.contains(String.valueOf(wrongAnswer))) {
                options.add(String.valueOf(wrongAnswer));
            }
        }
        Collections.shuffle(options);
        return new Question(questionText, 0, options, String.valueOf(answer));
    }
}