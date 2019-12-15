package com.example.fampayassignment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LoginDao {

    @Insert
    void insert(LoginPojo loginPojo);

    @Query("SELECT * FROM logged_in_users")
    LiveData<List<LoginPojo>> getAllLoggedInUsers();

    @Delete
    void delete(LoginPojo note);

    @Query("DELETE FROM logged_in_users")
    void deleteAllNotes();
}


