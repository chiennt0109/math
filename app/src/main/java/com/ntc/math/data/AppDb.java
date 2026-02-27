package com.ntc.math.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { Attempt.class }, version = 1)
public abstract class AppDb extends RoomDatabase {
    public abstract AttemptDao attemptDao();

    private static volatile AppDb INSTANCE;
    public static AppDb get(Context ctx){
        if (INSTANCE == null) {
            synchronized (AppDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(),
                            AppDb.class, "math.db").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
