package com.example.fampayassignment;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "logged_in_users")
public class LoginPojo {

    @NonNull
    @PrimaryKey
    private String email;

    public String getEncryptedPwd() {
        return encryptedPwd;
    }

    public void setEncryptedPwd(String encryptedPwd) {
        this.encryptedPwd = encryptedPwd;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public LoginPojo(@NonNull String email, String encryptedPwd) {
        this.email = email;
        this.encryptedPwd = encryptedPwd;
    }

    private String encryptedPwd;

}
