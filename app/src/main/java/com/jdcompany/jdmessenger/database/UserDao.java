package com.jdcompany.jdmessenger.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.jdcompany.jdmessenger.data.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

}
