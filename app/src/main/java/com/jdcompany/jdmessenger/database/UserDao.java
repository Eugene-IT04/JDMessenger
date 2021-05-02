package com.jdcompany.jdmessenger.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.jdcompany.jdmessenger.data.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    Flowable<List<User>> getAll();

    @Query("SELECT * FROM users WHERE id = :userId")
    Maybe<User> getUserById(long userId);

    @Insert
    Completable insert(User user);

    @Delete
    Completable delete(User user);

}
