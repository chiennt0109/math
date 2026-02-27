package com.ntc.math.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attempts")
public class Attempt {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long timestamp;
    public int userClass;
    public String topic;
    public int difficulty;
    public boolean correct;
    public long timeMs;

    // ✅ Constructor trống — bắt buộc để Room có thể khởi tạo object
    public Attempt() {
    }

    // ✅ Constructor đầy đủ với tên tham số TRÙNG tên field
    public Attempt(long timestamp, int userClass, String topic, int difficulty, boolean correct, long timeMs) {
        this.timestamp = timestamp;
        this.userClass = userClass;
        this.topic = topic;
        this.difficulty = difficulty;
        this.correct = correct;
        this.timeMs = timeMs;
    }
}
