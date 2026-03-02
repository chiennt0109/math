package com.ntc.math;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class QuestionActivity extends AppCompatActivity {

    private TextView tvQuestion, tvProgress, tvTopic, tvDifficulty;
    private RadioGroup radioGroup;
    private Button btnSubmit, btnNext;
    private ProgressBar progressBar;

    private List<Question> questions;
    private int current = 0;
    private int correctCount = 0;

    private String topic;
    private String difficulty;
    private int grade;
    private int currentVerticalLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvProgress = findViewById(R.id.tvProgress);
        tvTopic = findViewById(R.id.tvTopic);
        tvDifficulty = findViewById(R.id.tvDifficulty);
        radioGroup = findViewById(R.id.radioGroup);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnNext = findViewById(R.id.btnNext);
        progressBar = findViewById(R.id.progressBar);

        topic = getIntent().getStringExtra("topic");
        grade = getIntent().getIntExtra("grade", 1);
        difficulty = AdaptiveManager.getStartingDifficulty(this, topic);
        currentVerticalLevel = AdaptiveManager.getCurrentVerticalLevel(this, topic);

        tvTopic.setText("📘 Chủ đề: " + topic);
        tvDifficulty.setText("⚙️ Độ khó: " + difficulty + " | Cấp: " + currentVerticalLevel);

        questions = QuestionGenerator.generate(topic, difficulty, currentVerticalLevel);
        loadQuestion();

        btnSubmit.setOnClickListener(v -> checkAnswer());
        btnNext.setOnClickListener(v -> nextQuestion());
    }

    private void loadQuestion() {
        if (current >= questions.size()) {
            showResult();
            return;
        }

        Question q = questions.get(current);
        tvQuestion.setText(q.getText());
        tvProgress.setText("Câu " + (current + 1) + "/" + questions.size());

        radioGroup.removeAllViews();
        for (String opt : q.getOptions()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opt);
            rb.setTextSize(18);
            radioGroup.addView(rb);
        }

        progressBar.setProgress((int) ((current * 100.0f) / questions.size()));
        btnSubmit.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);
    }

    private void checkAnswer() {
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == -1) {
            Toast.makeText(this, "Hãy chọn đáp án!", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selected = findViewById(id);
        String ans = selected.getText().toString();
        Question q = questions.get(current);
        boolean isCorrect = ans.equals(q.getCorrectAnswer());

        AdaptiveManager.recordAnswer(this, topic, isCorrect);

        int questionLevel = q.getLevel() > 0 ? q.getLevel() : currentVerticalLevel;
        int updatedLevel = AdaptiveManager.recordVerticalAttempt(
                this,
                topic,
                q.getSkillKey().isEmpty() ? (topic + "|L" + questionLevel) : q.getSkillKey(),
                questionLevel,
                isCorrect
        );

        if (updatedLevel != currentVerticalLevel) {
            currentVerticalLevel = updatedLevel;
            tvDifficulty.setText("⚙️ Độ khó: " + difficulty + " | Cấp: " + currentVerticalLevel);
            questions = QuestionGenerator.generate(topic, difficulty, currentVerticalLevel);
            current = Math.min(current, questions.size() - 1);
            Toast.makeText(this, "🔁 Hệ thống tự điều chỉnh về cấp " + currentVerticalLevel, Toast.LENGTH_SHORT).show();
        }

        if (isCorrect) {
            correctCount++;
            Toast.makeText(this, "✅ Chính xác!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "❌ Sai. Đáp án đúng là: " + q.getCorrectAnswer(), Toast.LENGTH_LONG).show();
        }

        btnSubmit.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
    }

    private void nextQuestion() {
        current++;
        loadQuestion();
    }

    private void showResult() {
        float percent = (correctCount * 100f) / questions.size();
        AdaptiveManager.onSessionEnd(this, topic, percent);

        String msg = String.format(Locale.US, "🎯 Kết quả: %.1f%% (%d/%d)", percent, correctCount, questions.size());
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, ProgressActivity.class);
        i.putExtra("topic", topic);
        i.putExtra("score", percent);
        startActivity(i);
        finish();
    }
}
