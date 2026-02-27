package com.ntc.math.generators;

import com.ntc.math.Question;

// Đây là "bản hợp đồng" mà mọi class sinh câu hỏi phải tuân theo.
// Bất kỳ class nào thực thi interface này đều BẮT BUỘC phải có hàm generate().
public interface QuestionGenerationStrategy {
    Question generate(int difficulty);
}