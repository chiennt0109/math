package com.ntc.math;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

/**
 * ProgressActivity
 * --------------------
 * Hiển thị kết quả buổi học:
 * - Điểm số (%)
 * - Đánh giá độ tiến bộ
 * - Gợi ý độ khó / chủ đề tiếp theo
 */
public class ProgressActivity extends AppCompatActivity {

    private TextView tvSummary, tvSuggestion;
    private Button btnRetry, btnHome;
    private ProgressBar progressCircle;

    private String topic;
    private float score;
    private int currentClass;
    private String weakAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        tvSummary = findViewById(R.id.tvSummary);
        tvSuggestion = findViewById(R.id.tvSuggestion);
        btnRetry = findViewById(R.id.btnRetry);
        btnHome = findViewById(R.id.btnHome);
        progressCircle = findViewById(R.id.progressCircle);

        topic = getIntent().getStringExtra("topic");
        score = getIntent().getFloatExtra("score", 0f);
        currentClass = AdaptiveManager.getCurrentClass(this);
        weakAxis = getIntent().getStringExtra("weak_axis");

        showResult();
        setupButtons();
    }

    private void showResult() {
        String summary = String.format(Locale.US, "🎯 Điểm của bạn: %.1f%%", score);
        tvSummary.setText(summary);

        progressCircle.setProgress((int) score);

        // Lấy độ khó hiện tại để gợi ý bước tiếp theo
        String currentDiff = AdaptiveManager.getStartingDifficulty(this, topic);
        String nextSuggestion = getSuggestionText(score, currentDiff);

        tvSuggestion.setText(nextSuggestion);
    }

    private String getSuggestionText(float score, String diff) {
        if (AdaptiveManager.PRACTICE_TOPIC.equals(topic)) {
            AdaptiveManager.recordPracticeTestResult(this, score, weakAxis);
            String axis = (weakAxis == null || weakAxis.isEmpty()) ? "Không có" : weakAxis;
            if (score < 60 && !"Không có".equals(axis)) {
                return "📝 Kết quả luyện đề: cần ôn thêm trục '" + axis + "'. Hệ thống đã lưu để gợi ý lần sau.";
            }
            return "✅ Kết quả luyện đề đã được lưu. Tiếp tục luyện đều 3 trục để ổn định phong độ.";
        }

        int suggestedClass = currentClass;
        if (score >= 85) {
            AdaptiveManager.increaseDifficulty(this, topic);
            suggestedClass = Math.min(12, currentClass + 1);
        } else if (score < 40) {
            AdaptiveManager.decreaseDifficulty(this, topic);
            suggestedClass = Math.max(1, currentClass - 1);
        }

        AdaptiveManager.updateSuggestedClass(this, suggestedClass);

        if (score >= 85) {
            return "🌟 Bạn làm rất tốt! Gợi ý hiện tại: thử sức nội dung gần mức Lớp " + suggestedClass + ".";
        } else if (score >= 60) {
            return "👍 Kết quả khá tốt! Hãy giữ vững phong độ ở độ khó '" + diff + "'. Gợi ý cấp học: Lớp " + suggestedClass + ".";
        } else {
            return "💪 Hãy luyện thêm nhé. Hệ thống sẽ lùi mức để củng cố nền tảng. Gợi ý cấp học: Lớp " + suggestedClass + ".";
        }
    }

    private void setupButtons() {
        btnRetry.setOnClickListener(v -> {
            Intent i = new Intent(this, QuestionActivity.class);
            i.putExtra("topic", topic);
            i.putExtra("grade", 1);
            startActivity(i);
            finish();
        });

        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(this, TopicSelectActivity.class);
            startActivity(i);
            finish();
        });
    }
}
