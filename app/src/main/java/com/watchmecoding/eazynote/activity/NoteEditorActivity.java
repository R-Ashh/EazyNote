package com.watchmecoding.eazynote.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.watchmecoding.eazynote.R;
import com.watchmecoding.eazynote.data.NoteDataSource;
import com.watchmecoding.eazynote.data.NoteItem;


public class NoteEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private NoteItem note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        Button savebtn = (Button) findViewById(R.id.save_button);
        savebtn.setOnClickListener(this);
        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        String json = getIntent().getAction();
        if (json != null) {
            note = new Gson().fromJson(json, NoteItem.class);
            EditText et = (EditText) findViewById(R.id.noteText);
            et.setText(note.getText());
            et.setSelection(note.getText().length());
        }
    }

    private void deleteButtonClicked(final NoteItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteEditorActivity.this);
        builder.setTitle("Are you sure you want to delete this note?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NoteDataSource source = new NoteDataSource(NoteEditorActivity.this);
                source.remove(item);
                saveAndFinish();
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


    private void saveAndFinish() {
        EditText et = (EditText) findViewById(R.id.noteText);
        String noteText = et.getText().toString();
        NoteDataSource dataSource = new NoteDataSource(this);

        if (noteText.length() == 0) {
            setResult(RESULT_OK);
            finish();
            return;
        }

        if (note == null) {
//            add from fab
            NoteItem item = NoteItem.getNew();
            item.setText(noteText);
            dataSource.add(item);

        } else {
//            on note click
            note.setText(noteText);
            dataSource.update(note);
        }

        setResult(RESULT_OK);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                saveAndFinish();
                break;
            case R.id.delete_button:
                deleteButtonClicked(note);
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndFinish();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        saveAndFinish();
    }
}