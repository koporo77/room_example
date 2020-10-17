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

    //we gonna directly pass (DIFF_CALLBACK) in here NoteAdapter class
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

    //this interface is for getting into setOnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

//in setNotes(), notifyDataSetChanged() this method tell adapter to change whole data because current data is invalid
// so it is quite unnecessary. so adapter gonna use ListAdapter which uses diffUtil that comparing data on the list
// and find one that is changed to update. / this way is more efficient