package com.ntc.math;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    private EditText nameEditText;
    private Spinner classSpinner;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // KIỂM TRA TRƯỚC KHI HIỂN THỊ GIAO DIỆN
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isOnboardingCompleted = prefs.getBoolean("OnboardingCompleted", false);

        if (isOnboardingCompleted) {
            // Nếu đã hoàn thành, vào thẳng màn hình chính
            Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đóng màn hình này lại
            return; // Dừng hàm onCreate tại đây
        }

        // Nếu chưa hoàn thành, hiển thị giao diện như bình thường
        setContentView(R.layout.activity_onboarding);

        nameEditText = findViewById(R.id.nameEditText);
        classSpinner = findViewById(R.id.classSpinner);
        confirmButton = findViewById(R.id.confirmButton);

        String[] classes = new String[]{"Lớp 1", "Lớp 2", "Lớp 3", "Lớp 4", "Lớp 5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }

    private void saveUserData() {
        String name = nameEditText.getText().toString().trim();
        int selectedClass = classSpinner.getSelectedItemPosition() + 1;

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("UserName", name);
        editor.putInt("UserClass", selectedClass);
        editor.putBoolean("OnboardingCompleted", true);
        editor.apply();

        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}