package com.example.room_practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        //android system automatically destory viewmodel when context of activity done and recreate viewmodel
        noteViewModel = new ViewModelProvider(this, new ViewModelProvider
                .AndroidViewModelFactory(getApplication())).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //setNotes()를 통해 notes가 전달되면 setNotes의 notify 메소드에 의해 어뎁터가 업데이트됨(=ui업데이트됨)
                adapter.setNotes(notes);
            }
        });


    }
}