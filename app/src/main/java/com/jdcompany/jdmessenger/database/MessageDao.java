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
import io.reactivex.Maybe;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM messages WHERE fromId + toId = :key ORDER BY time DESC")
    Flowable<List<Message>> getAllForKey(long key);

    @Query("SELECT m.* FROM messages m LEFT JOIN messages b ON m.fromId + m.toId = b.fromId + b.toId AND m.time < b.time WHERE b.time IS NULL")
    Flowable<List<Message>> getLastMessagesForAllChats();

    @Insert
    Completable insert(Message message);

    @Insert
    Completable insert(List<Message> messages);

    @Delete
    Completable delete(Message message);
}
