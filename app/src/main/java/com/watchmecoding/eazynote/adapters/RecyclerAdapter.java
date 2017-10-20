package com.watchmecoding.eazynote.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.watchmecoding.eazynote.R;
import com.watchmecoding.eazynote.activity.MainActivity;
import com.watchmecoding.eazynote.data.NoteItem;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    ArrayList<NoteItem> notesList;

    private MainActivity context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public RecyclerAdapter(ArrayList<NoteItem> notesList, MainActivity mainActivity) {
        this.notesList = notesList;
        this.context = mainActivity;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        final NoteItem note = notesList.get(position);
        holder.mTextView.setText(note.getText());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.openNote(note);
            }
        });
        holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.holdForOption(note);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
