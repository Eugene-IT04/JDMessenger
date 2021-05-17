package com.jdcompany.jdmessenger.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jdcompany.jdmessenger.data.objects.Message;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM messages WHERE fromId + toId = :key ORDER BY time DESC")
    Flowable<List<Message>> getAllForKey(long key);

    @Query("SELECT m.* FROM messages m LEFT JOIN messages b ON m.fromId + m.toId = b.fromId + b.toId AND m.time < b.time WHERE b.time IS NULL")
    Flowable<List<Message>> getLastMessagesForAllChats();

    @Query("DELETE FROM messages WHERE toId + fromId = :key")
    Completable deleteAllMessagesByKey(long key);

    @Query("DELETE FROM messages WHERE id = :id")
    Completable deleteMessageById(long id);

    @Query("UPDATE messages SET body = :newBody WHERE id = :messageId")
    Completable updateMessageWithId(long messageId, String newBody);

    @Insert
    Completable insert(Message message);

    @Insert
    Completable insert(List<Message> messages);

    @Delete
    Completable delete(Message message);
}
