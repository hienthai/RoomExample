package com.hienthai.roomexample.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hienthai.roomexample.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM USER")
    List<User> getAllUser();

    @Query("SELECT * FROM USER WHERE name= :name")
    List<User> checkUser(String name);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM USER")
    void deleteAllUser();

    @Query("SELECT * FROM USER WHERE name LIKE '%' || :name || '%'")
    List<User> searchUser(String name);

}
