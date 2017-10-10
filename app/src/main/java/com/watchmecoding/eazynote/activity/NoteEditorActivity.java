package com.watchmecoding.eazynote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.watchmecoding.eazynote.R;
import com.watchmecoding.eazynote.data.NoteItem;


public class NoteEditorActivity extends AppCompatActivity {

    private NoteItem note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);


        Intent intent = this.getIntent();
        note = new NoteItem();
        note.setKey(intent.getStringExtra("key"));
        note.setText(intent.getStringExtra("text"));

        EditText et = (EditText) findViewById(R.id.noteText);
        et.setText(note.getText());
        et.setSelection(note.getText().length());
    }

    private void saveAndFinish() {
        EditText et = (EditText) findViewById(R.id.noteText);
        String noteText = et.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("key", note.getKey());
        intent.putExtra("text", noteText);
        setResult(RESULT_OK, intent);
        finish();
    }
}