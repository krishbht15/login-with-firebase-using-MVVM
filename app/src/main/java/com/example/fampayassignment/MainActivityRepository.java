package com.example.fampayassignment;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MainActivityRepository {

    private LoginDao loginDao;
    private LiveData<List<LoginPojo>> allLoginUsers;
    public MainActivityRepository(Application application) {
        LoggedInDatabase database = LoggedInDatabase.getInstance(application);
        loginDao = database.loginDao();
        allLoginUsers = loginDao.getAllLoggedInUsers();
    }
    public void insert(LoginPojo loginPojo) {
        new MainActivityRepository.InsertNoteAsyncTask(loginDao).execute(loginPojo);
    }
    public void delete(LoginPojo loginPojo) {
        new MainActivityRepository.DeleteNoteAsyncTask(loginDao).execute(loginPojo);
    }

    public void deleteAllNotes() {
        new MainActivityRepository.DeleteAllNotesAsyncTask(loginDao).execute();
    }

    public LiveData<List<LoginPojo>> getAllNotes() {
        return allLoginUsers;
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
    private static class DeleteNoteAsyncTask extends AsyncTask<LoginPojo, Void, Void> {
        private LoginDao loginDao;

        private DeleteNoteAsyncTask(LoginDao loginDao) {
            this.loginDao = loginDao;
        }

        @Override
        protected Void doInBackground(LoginPojo... loginPojos) {
            loginDao.delete(loginPojos[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private LoginDao loginDao;

        private DeleteAllNotesAsyncTask(LoginDao loginDao) {
            this.loginDao = loginDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            loginDao.deleteAllNotes();
            return null;
        }
    }
}

