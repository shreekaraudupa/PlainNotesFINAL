package com.example.sarvajna.plainnotes2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    private String  action;   //to remember whether i m inserting or updating
    private EditText editor;
    private String noteFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editor= (EditText) findViewById(R.id.editText);
        Intent intent=getIntent();
        Uri uri=intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if(uri==null)   //means i am inserting a new node
        {
            action=Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
        }else{
            action = Intent.ACTION_EDIT;
            noteFilter=DBOpenHelper.NOTE_ID+ "=" +uri.getLastPathSegment();

            Cursor cursor=getContentResolver().query(uri,DBOpenHelper.ALL_COLUMNS,noteFilter,null,null);
            cursor.moveToFirst();
            oldText=cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();
        }
    }
    private void finishedEditing(){
        String newText=editor.getText().toString().trim();

        switch(action){
            case Intent.ACTION_INSERT:
                if(newText.length()==0)
                {
                    setResult(RESULT_CANCELED);    //predifened
                }else
                {
                    insetNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if(newText.length()==0)
                {
                    deleteNote();
                }else if(oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                }else{
                    updateNote(newText);
                }
        }
        finish();
    }


    private void updateNote(String NoteText) {
        ContentValues values=new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,NoteText);
        getContentResolver().update(NotesProvider.CONTENT_URI,values,noteFilter,null);
        Toast.makeText(this, R.string.note_updated,Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
    }
    private void insetNote(String NoteText) {
        ContentValues values=new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,NoteText);
         getContentResolver().insert(NotesProvider.CONTENT_URI,values); //inserted a row in database table
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishedEditing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals(Intent.ACTION_EDIT)) {

            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch(id){
            case R.id.home:
            finishedEditing();
                break;
            case R.id.action_delete:
                deleteNote();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteNote() {
    getContentResolver().delete(NotesProvider.CONTENT_URI,noteFilter,null);
        Toast.makeText(this, R.string.note_deleted,Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();

    }
}
