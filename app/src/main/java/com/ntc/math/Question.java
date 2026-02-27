package com.ntc.math;

import java.util.List;

/**
 * Question
 * ------------------
 * Lớp lưu trữ 1 câu hỏi trắc nghiệm:
 * - text: nội dung câu hỏi (có emoji)
 * - topicId: chỉ số hoặc mã chủ đề
 * - options: danh sách đáp án
 * - correctAnswer: đáp án đúng
 */
public class Question {

    private final String text;
    private final int topicId;
    private final List<String> options;
    private final String correctAnswer;

    public Question(String text, int topicId, List<String> options, String correctAnswer) {
        this.text = text;
        this.topicId = topicId;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getText() {
        return text;
    }

    public int getTopicId() {
        return topicId;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
