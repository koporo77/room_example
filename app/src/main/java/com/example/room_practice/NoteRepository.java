package com.example.room_practice;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    //below part is for exposing clean api to viewModel
    public void insert(final Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            noteDao.insert(note);
        });
    }

    public void update(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            noteDao.update(note);
        });

    }

    public void delete(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            noteDao.delete(note);
        });

    }

    public void deleteAll() {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            noteDao.deleteAll();
        });

    }

    //room will take care of this livedata method to run in background thread but others not
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
