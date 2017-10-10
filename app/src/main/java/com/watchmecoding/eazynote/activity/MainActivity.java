package com.watchmecoding.eazynote.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.watchmecoding.eazynote.R;
import com.watchmecoding.eazynote.base.PermissionUtility;
import com.watchmecoding.eazynote.data.NoteDataSource;
import com.watchmecoding.eazynote.data.NoteItem;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PermissionUtility util;
    private NoteDataSource datasource;
    ArrayList<NoteItem> notesList;
    private ListView listView;
    private ArrayAdapter<NoteItem> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fbutton = (FloatingActionButton) findViewById(R.id.create_note);
        fbutton.setOnClickListener(this);
        requestPermission();
        initNotes();
        fetch();
        noteEdit();
    }
    private void noteEdit() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteItem note = notesList.get(position);
                Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
                intent.putExtra("key", note.getKey());
                intent.putExtra("text", note.getText());
                startActivity(intent);
            }
        });

}

    private void fetch() {
        listView = (ListView) findViewById(R.id.list);
        adapter =
                new ArrayAdapter<NoteItem>(this, R.layout.list_item_layout, notesList);
        listView.setAdapter(adapter);
    }

    private void refresh() {
        if (adapter != null && notesList != null)
            adapter.notifyDataSetChanged();
    }

    private void initNotes() {
        datasource = new NoteDataSource(this);
        notesList = datasource.findAll();
        if (notesList.size() > 0) {
            Toast.makeText(this, "you have " + notesList.size() + " notes", Toast.LENGTH_SHORT).show();
            NoteItem item = notesList.get(0);
            item.setText("Kasra is faheshe");
            datasource.update(item);
        } else {
            Toast.makeText(this, "There is no note fuckface", Toast.LENGTH_SHORT).show();
            datasource.add(NoteItem.getNew());
        }
        Log.d("CURRENT", "initNotes() returned: " + new Gson().toJson(notesList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void requestPermission() {
        util = new PermissionUtility();

        util.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, this, new PermissionUtility.CallBack() {
            @Override
            public void onGranted() {
                if (Build.VERSION.SDK_INT >= 23) {
                    Toast.makeText(MainActivity.this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNotGranted() {
                if (Build.VERSION.SDK_INT >= 23) {
                    Toast.makeText(MainActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        util.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        NoteItem note = NoteItem.getNew();
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.putExtra("key", note.getKey());
        intent.putExtra("text", note.getText());
        startActivityForResult(intent, 1001);
    }

}
