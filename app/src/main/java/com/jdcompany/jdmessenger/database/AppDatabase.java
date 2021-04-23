package com.jdcompany.jdmessenger.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jdcompany.jdmessenger.data.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;

    public abstract UserDao userDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "messenger_database").build();
        }
        return database;
    }
}

