package com.jdcompany.jdmessenger.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.data.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM messages WHERE fromId + toId = :key ORDER BY time")
    Flowable<List<Message>> getAllForKey(long key);

    @Insert
    Completable insert(Message message);

    @Insert
    Completable insert(List<Message> messages);

    @Delete
    Completable delete(Message message);
}
