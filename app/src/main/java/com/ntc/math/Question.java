package com.ntc.math;

import java.util.List;

/**
 * Question
 * ------------------
 * Lớp lưu trữ 1 câu hỏi trắc nghiệm.
 */
public class Question {

    private final String text;
    private final int topicId;
    private final List<String> options;
    private final String correctAnswer;

    // Metadata cho adaptive theo ma trận dọc
    private final String learningType;
    private final int level;
    private final String skillKey;

    public Question(String text, int topicId, List<String> options, String correctAnswer) {
        this(text, topicId, options, correctAnswer, "", 0, "");
    }

    public Question(String text,
                    int topicId,
                    List<String> options,
                    String correctAnswer,
                    String learningType,
                    int level,
                    String skillKey) {
        this.text = text;
        this.topicId = topicId;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.learningType = learningType;
        this.level = level;
        this.skillKey = skillKey;
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

    public String getLearningType() {
        return learningType;
    }

    public int getLevel() {
        return level;
    }

    public String getSkillKey() {
        return skillKey;
    }
}
