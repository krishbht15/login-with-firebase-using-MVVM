package com.example.fampayassignment;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RegisterRepository {

    private LoginDao loginDao;
    private LiveData<List<LoginPojo>> allLoginUsers;
    public RegisterRepository(Application application) {
        LoggedInDatabase database = LoggedInDatabase.getInstance(application);
        loginDao = database.loginDao();
        allLoginUsers = loginDao.getAllLoggedInUsers();
    }
    public void insert(LoginPojo loginPojo) {
        new RegisterRepository.InsertNoteAsyncTask(loginDao).execute(loginPojo);
    }

    private static class InsertNoteAsyncTask extends AsyncTask<LoginPojo, Void, Void> {
        private LoginDao loginDao;

        private InsertNoteAsyncTask(LoginDao loginDao) {
            this.loginDao = loginDao;
        }

        @Override
        protected Void doInBackground(LoginPojo... notes) {
            loginDao.insert(notes[0]);
            return null;
        }
    }


}
