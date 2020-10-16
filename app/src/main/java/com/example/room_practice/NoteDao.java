package com.example.room_practice;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//실제로 데이터를 넣는 작업은 dao에서 일어나고 note는 그냥 테이블 디자인 형태일뿐인거 같다
//예로 위에서 넘긴 데이터가 다오를 통해 노트에 들어갔다가 데이터 베이스에 들어가는게 아니라
//노트는 그냥 형태에 대한 정의를 하고 다오를 통해 실제 데이터베이스에 들어간다.
@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

    //getAllNotes is for getting data from database so that we can display data on UI
    //purpose is displaying data on UI
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();




}
