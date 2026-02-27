package com.ntc.math;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * 🧩 TopicSelectActivity
 * - Hiển thị danh sách các chủ đề phù hợp với khối lớp hiện tại
 * - Mỗi chủ đề có độ khó khởi đầu thích ứng
 * - Chuyển sang QuestionActivity khi người học chọn chủ đề
 */
public class TopicSelectActivity extends AppCompatActivity {

    private TextView tvHeader, tvDifficultyInfo;
    private ListView lvTopics;
    private int currentClass;
    private List<String> topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_select);

        tvHeader = findViewById(R.id.tvHeader);
        tvDifficultyInfo = findViewById(R.id.tvDifficultyInfo);
        lvTopics = findViewById(R.id.lvTopics);

        // Lấy khối lớp hiện tại
        currentClass = AdaptiveManager.getCurrentClass(this);
        tvHeader.setText("📚 Chủ đề khối lớp " + currentClass);

        // Lấy danh sách chủ đề theo lớp
        topics = AdaptiveManager.getTopicsForGrade(currentClass);

        if (topics == null || topics.isEmpty()) {
            tvHeader.setText("❌ Chưa có chủ đề cho lớp " + currentClass);
            return;
        }

        // Hiển thị danh sách chủ đề
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, topics
        );
        lvTopics.setAdapter(adapter);

        lvTopics.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            String topic = topics.get(position);
            String diff = AdaptiveManager.getStartingDifficulty(this, topic);
            tvDifficultyInfo.setText("🔹 Độ khó hiện tại: " + diff);

            // Chuyển sang màn câu hỏi
            Intent i = new Intent(this, QuestionActivity.class);
            i.putExtra("topic", topic);
            startActivity(i);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
