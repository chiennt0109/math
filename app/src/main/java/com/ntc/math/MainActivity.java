package com.ntc.math;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private RecyclerView topicsRecyclerView;
    private TopicAdapter topicAdapter;
    private int userClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.welcomeTextView);
        topicsRecyclerView = findViewById(R.id.topicsRecyclerView);

        // Đọc dữ liệu người dùng
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userName = prefs.getString("UserName", "Bạn");
        userClass = prefs.getInt("UserClass", 1);

        // Cập nhật lời chào
        welcomeTextView.setText("Chào " + userName + "!");

        // Lấy danh sách chủ đề dựa trên lớp và hiển thị
        loadTopicsForCurrentClass();
    }

    // --- CÁC HÀM XỬ LÝ MENU MỚI ---

    // Hàm này được gọi để tạo menu tùy chọn trên thanh công cụ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Hàm này được gọi khi người dùng nhấn vào một mục trong menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_change_class) {
            showChangeClassDialog();
            return true;
        } else if (id == R.id.action_reset) {
            showResetDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // --- CÁC HÀM CŨ VẪN GIỮ NGUYÊN ---

    private void loadTopicsForCurrentClass() {
        Map<Integer, List<String>> allTopics = TopicDataSource.getTopics();
        List<String> currentTopics = allTopics.getOrDefault(userClass, Collections.emptyList());

        topicsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        topicAdapter = new TopicAdapter(currentTopics, userClass);
        topicsRecyclerView.setAdapter(topicAdapter);
    }

    private void showChangeClassDialog() {
        String[] classes = {"Lớp 1", "Lớp 2", "Lớp 3", "Lớp 4", "Lớp 5"};
        new AlertDialog.Builder(this)
                .setTitle("Chọn lớp mới")
                .setItems(classes, (dialog, which) -> {
                    int newClass = which + 1;
                    SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    prefs.edit().putInt("UserClass", newClass).apply();
                    // Tải lại Activity để cập nhật tên và danh sách chủ đề
                    recreate();
                })
                .show();
    }

    private void showResetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Dữ liệu")
                .setMessage("Bạn có chắc chắn muốn xóa toàn bộ quá trình học tập và bắt đầu lại không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Xóa dữ liệu người dùng và dữ liệu quá trình học
                    getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().clear().apply();
                    getSharedPreferences("PerformancePrefs", MODE_PRIVATE).edit().clear().apply();

                    // Quay về màn hình thiết lập ban đầu
                    Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Không", null)
                .show();
    }
}