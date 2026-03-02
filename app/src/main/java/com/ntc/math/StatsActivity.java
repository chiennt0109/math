package com.ntc.math;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        TextView tvCurrentStatus = findViewById(R.id.tvCurrentStatus);
        TextView tvCounts = findViewById(R.id.tvCounts);

        int currentClass = AdaptiveManager.getCurrentClass(this);
        int suggestedClass = AdaptiveManager.getSuggestedClass(this);
        int totalCorrect = AdaptiveManager.getTotalCorrect(this);
        int totalWrong = AdaptiveManager.getTotalWrong(this);
        float accuracy = AdaptiveManager.getOverallAccuracy(this);

        tvCurrentStatus.setText("📌 Tình trạng hiện tại\n"
                + "- Lớp đã chọn ban đầu: " + currentClass + "\n"
                + "- Gợi ý cấp học hiện tại: Lớp " + suggestedClass + "\n"
                + "- Độ chính xác tổng: " + String.format(Locale.US, "%.1f%%", accuracy));

        tvCounts.setText("✅ Đúng: " + totalCorrect + "\n❌ Sai: " + totalWrong + "\n🧮 Tổng câu đã làm: " + (totalCorrect + totalWrong));
    }
}
