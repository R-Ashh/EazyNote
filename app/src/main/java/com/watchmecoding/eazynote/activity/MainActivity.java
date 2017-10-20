package com.watchmecoding.eazynote.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.watchmecoding.eazynote.R;
import com.watchmecoding.eazynote.adapters.RecyclerAdapter;
import com.watchmecoding.eazynote.base.PermissionUtility;
import com.watchmecoding.eazynote.data.NoteDataSource;
import com.watchmecoding.eazynote.data.NoteItem;
import com.watchmecoding.eazynote.util.NetworkHelper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int EDITOR_ACTIVITY_REQUEST = 1001;
    private PermissionUtility util;
    private NoteDataSource datasource;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<NoteItem> notesList;
    CharSequence options[] = new CharSequence[]{"Open", "Delete", "Detail", "Share"};
    private boolean networkOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final FloatingActionButton fbutton = (FloatingActionButton) findViewById(R.id.create_note);
        fbutton.setOnClickListener(this);

        requestPermission();
        initNotes();
        fetch();
        noteEdit();
        checkInternet();
    }

    private void checkInternet() {
        if (networkOk = NetworkHelper.hasNetworkAccess(this)) {
            Toast.makeText(this, "Internet is Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You're offline", Toast.LENGTH_SHORT).show();
        }
    }

    public void holdForOption(final NoteItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What Do You Wanna Do?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openNote(item);
                        break;
                    case 1:
                        removeNote(item);
                        break;
                }
            }
        });
        builder.show();
    }

    private void removeNote(final NoteItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Are you sure you want to delete this note?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                NoteDataSource dataSource = new NoteDataSource(MainActivity.this);
                datasource.remove(item);
                refreshDisplay();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void noteEdit() {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                NoteItem note = notesList.get(position);
//                openNote(note);
//            }
//        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                NoteItem note = notesList.get(position);
//                holdForOption(note);
//                return true;
//            }
//        });
    }

    private void fetch() {

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(notesList, this);
        mRecyclerView.setAdapter(mAdapter);
//        listView = (ListView) findViewById(R.id.list);
//        adapter =
//                new ArrayAdapter<>(this, R.layout.list_item_layout, notesList);
//        listView.setAdapter(adapter);
    }

    private void refreshDisplay() {
        if (mAdapter != null && notesList != null) {
            notesList.clear();
            notesList.addAll(datasource.findAll());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initNotes() {
        datasource = new NoteDataSource(this);
        notesList = datasource.findAll();
    }


    private void requestPermission() {
        util = new PermissionUtility();

        util.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, this, new PermissionUtility.CallBack() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onNotGranted() {
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
        switch (v.getId()) {
            case R.id.create_note:
                addNote();
                break;
            default:
                break;
        }
    }

    private void addNote() {
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
    }

    public void openNote(NoteItem item) {
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.setAction(new Gson().toJson(item));
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            refreshDisplay();
        }
    }

}
