package com.ntc.math;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 🏫 ClassSelectActivity
 * - Màn hình chọn khối lớp (1–12)
 * - Sau khi chọn, lưu lại lớp hiện tại qua AdaptiveManager
 * - Chuyển sang TopicSelectActivity
 */
public class ClassSelectActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ListView lvClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_select);

        tvTitle = findViewById(R.id.tvTitle);
        lvClasses = findViewById(R.id.lvClasses);

        tvTitle.setText("🎓 Chọn khối lớp của bạn");

        List<String> classes = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            classes.add("Lớp " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, classes
        );
        lvClasses.setAdapter(adapter);

        lvClasses.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            int selectedClass = position + 1;
            AdaptiveManager.setCurrentClass(this, selectedClass);

            Intent i = new Intent(this, TopicSelectActivity.class);
            startActivity(i);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
