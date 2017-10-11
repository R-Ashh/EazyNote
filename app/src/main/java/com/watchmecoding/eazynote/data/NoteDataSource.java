package com.watchmecoding.eazynote.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;

public class NoteDataSource {

    private SharedPreferences notePrefs;
    private static final String DB_KEY = "NoteDataSource";

    public NoteDataSource(Context context) {
        notePrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public ArrayList<NoteItem> findAll() {
        ArrayList<NoteItem> list = new ArrayList<>();
        String data = notePrefs.getString(DB_KEY, null);
        if (data != null) {
            NoteItem[] notes = new Gson().fromJson(data, NoteItem[].class);
            Collections.addAll(list, notes);
        }
        return list;
    }

    public boolean update(NoteItem note) {
        ArrayList<NoteItem> list = findAll();
        int index = getIntex(note, list);
        if (index != -1) {
            list.set(index, note);
        }
        update(list);
        return true;
    }

    public boolean remove(NoteItem note) {
        ArrayList<NoteItem> list = findAll();
        int index = getIntex(note, list);
        if (index != -1) {
            list.remove(index);
        }
        update(list);
        return true;
    }

    private int getIntex(NoteItem note, ArrayList<NoteItem> list) {
        int index = -1;
        for (NoteItem item : list) {
            if (item.getKey().equals(note.getKey()))
                index = list.indexOf(item);
        }
        return index;
    }

    private void update(ArrayList<NoteItem> list) {
        NoteItem[] data = list.toArray(new NoteItem[list.size()]);
        String json = new Gson().toJson(data);
        notePrefs.edit().putString(DB_KEY, json).apply();
    }

    public boolean add(NoteItem note) {
        ArrayList<NoteItem> list = findAll();
        list.add(note);
        update(list);
        return true;
    }

}
