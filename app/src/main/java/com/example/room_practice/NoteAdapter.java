package com.example.room_practice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//<>안이 사용할 viewHolder 정의
//original extends is "RecyclerView.Adapter<NoteAdapter.NoteHolder>"
//notes.get(position) == getItem(position) / getItem is method in ListAdapter
public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {

    ///???
    private OnItemClickListener listener;

//    private List<Note> notes = new ArrayList<>(); // we gonna pass List<Note> to super class so we don't need to
    // hold this method directly here

    //we gonna directly pass (DIFF_CALLBACK) in here NoteAdapter class/ this method is for implementing all necessary code for
    //generating ListAdapter which comparing list and update
    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        //checking if item is same, it is different than contents
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }
        //checking if contents of item is same, if it return false listAdapter knows it should be changed so
        // it will take all necessary job for us
        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();

        }
    };

    //inflate layout and provide it to NoteHolder so that it can reference views
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        //???
        Note currentNote = getItem(position);
        //???
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }
    // This is part of ListAdapter so we don't need to write this anymore

//    //declaring how many item we gonna display on recyclerview
//    @Override
//    public int getItemCount() {
//        return notes.size();
//    }
//
//    public void setNotes(List<Note> notes) {
//        this.notes = notes;
//        //tell recyclerview to configure item
//        notifyDataSetChanged();
//    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    ///???
    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            //this code enable to click item
            //if we click ui, it return position
            //listener가 어떻게 null이 되는지??
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //when swiping animation, it became no position
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }

    }

    // 103번줄에서 화면 터치될때 호출이됨
    //리스너가 대체 어떤건지????
    //this interface is for getting into setOnItemClickListener
    //when it is called, it hold note info in onItemClick
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }
    //this code is used to editing
    //외부에서 리스너 인터페이스를 메소드(익명클래스 객체)를 만들면(=몸통이 생김)
    //그걸 어뎁터에 전달해 어뎁터의 listener 변수를 업데이트함
    //그리고 onClick 이될때 listener 변수가 null 이 아니라면 listener 인터페이스의
    //구현 부분인 onItemClick이 실행됨 이때 실행되는 리스너 변수는 몸통이 있으므로(메인 클래스에서 정의한 리스너 객체=익명클래스 객체)
    //메인에서 구현된 리스너 객체(= 익명클래스 객체)가 실행됨
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
//커스텀 이벤트 리스너
//1. EventListener 를 인터페이스로 정의, 2. 이 인터페이스의 함수 설정 3.외부에서 사용가능하도록 setOnitemclickListener() 구현
//in setNotes(), notifyDataSetChanged() this method tell adapter to change whole data because current data is invalid
// so it is quite unnecessary. so adapter gonna use ListAdapter which uses diffUtil that comparing data on the list
// and find one that is changed to update. / this way is more efficient