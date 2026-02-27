// path: app/src/main/java/com/ntc/math/SplashActivity.java
package com.ntc.math;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Giữ splash trong 2 giây rồi chuyển sang ClassSelectActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, ClassSelectActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
