package com.ntc.math.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AttemptDao {
    @Insert long insert(Attempt a);

    @Query("SELECT * FROM attempts WHERE timestamp >= :since ORDER BY timestamp DESC")
    List<Attempt> recentSince(long since);

    @Query("SELECT COUNT(*) FROM attempts WHERE userClass=:uc AND topic=:topic")
    int countByTopic(int uc, String topic);

    @Query("SELECT SUM(CASE WHEN correct THEN 1 ELSE 0 END) FROM attempts WHERE userClass=:uc AND topic=:topic")
    int correctByTopic(int uc, String topic);
}
