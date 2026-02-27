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
        if (score >= 85) {
            AdaptiveManager.increaseDifficulty(this, topic);
            return "🌟 Bạn làm rất tốt! Hệ thống sẽ tăng độ khó trong buổi tới.";
        } else if (score >= 60) {
            return "👍 Kết quả khá tốt! Hãy giữ vững phong độ ở độ khó '" + diff + "'.";
        } else {
            AdaptiveManager.decreaseDifficulty(this, topic);
            return "💪 Hãy luyện thêm nhé. Hệ thống sẽ giảm độ khó để bạn ôn tập dễ hơn.";
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
            Intent i = new Intent(this, ClassSelectActivity.class);
            startActivity(i);
            finish();
        });
    }
}
