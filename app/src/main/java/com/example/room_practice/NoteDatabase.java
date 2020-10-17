package com.example.room_practice;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    //because we build database, Room automatically generate all neccessary code for notedao abstract class
    public abstract NoteDao noteDao();

    private static final int NUMBER_OF_THREADS = 5;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

//    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//
//            // If you want to keep data through app restarts,
//            // comment out the following block
//            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
//                WordDao dao = INSTANCE.wordDao();
//                dao.deleteAll();
//
//                Word word = new Word("Hello");
//                dao.insert(word);
//                word = new Word("World");
//                dao.insert(word);
//            });
//        }
//    };

    //onCreate -> when database first created (this mean i have to reinstall to see onCreate generate)
    //onOpen -> generate when everytime app is openned(= when app opens database gets called)
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            NoteDao dao = instance.noteDao();
            Note note1 = new Note("movie go!"
                    , "pick movide and enjoy time with family!", 1);
            Note note2 = new Note("movie tonight!!"
                    , "pick movide and enjoy time with friends!", 2);


            databaseWriteExecutor.execute(() -> {
                dao.deleteAll();
                dao.insert(note1);
                dao.insert(note2);
            });

        }
    };
}
