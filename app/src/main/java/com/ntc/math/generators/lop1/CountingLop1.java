package com.ntc.math.generators.lop1;

import com.ntc.math.Question;
import com.ntc.math.R;
import com.ntc.math.generators.QuestionGenerationStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CountingLop1 implements QuestionGenerationStrategy {

    @Override
    public Question generate(int difficulty) {
        Random random = new Random();
        // difficulty: 1 (rất dễ) → 5 (khó)
        if (difficulty <= 2) {
            // đếm hình ảnh / đếm số lượng
            int cnt = random.nextInt(5) + 3; // 3..7
            List<String> options = new ArrayList<>();
            int ans = cnt;
            options.add(String.valueOf(ans));
            options.add(String.valueOf(ans + 1));
            options.add(String.valueOf(Math.max(1, ans - 1)));
            options.add(String.valueOf(ans + 2));
            Collections.shuffle(options);
            String q = "Đếm số hình: có bao nhiêu hình?";
            return new Question(q, /*imageResId*/0, options, String.valueOf(ans));
        } else if (difficulty <= 4) {
            // so sánh số nhỏ
            int a = random.nextInt(20), b = random.nextInt(20);
            String correct = (a > b) ? String.valueOf(a) : String.valueOf(b);
            List<String> options = Arrays.asList(String.valueOf(a), String.valueOf(b),
                    String.valueOf(Math.max(0, Math.min(a,b)-1)), String.valueOf(Math.max(a,b)+1));
            Collections.shuffle(options);
            String q = "Số nào lớn hơn?";
            return new Question(q, 0, new ArrayList<>(options), correct);
        } else {
            // pattern số, điền tiếp theo
            int start = random.nextInt(5)+1; // 1..5
            int diff = random.nextInt(3)+1;  // +1..+3
            int a = start, b = start+diff, c = start+2*diff;
            int ans = start+3*diff;
            List<String> options = Arrays.asList(
                    String.valueOf(ans),
                    String.valueOf(ans-1),
                    String.valueOf(ans+1),
                    String.valueOf(ans+2)
            );
            Collections.shuffle(options);
            String q = "Điền số tiếp theo của dãy: " + a + ", " + b + ", " + c + ", ?";
            return new Question(q, 0, new ArrayList<>(options), String.valueOf(ans));
        }
    }

}