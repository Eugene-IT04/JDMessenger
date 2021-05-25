package com.jdcompany.jdmessenger.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.android.play.core.install.model.UpdateAvailability;
import com.jdcompany.jdmessenger.data.objects.User;

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

    @Update
    Completable update(User user);

}
