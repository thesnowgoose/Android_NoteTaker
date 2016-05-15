package com.example.lcarrasco.hello;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity {

    private boolean isInEditMode = true;
    private boolean isInAddMode = true;
    public static final String _NOTE = "Note";
    public static final int RESULT_DELETE = -500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // final -> faster to compile
        final Button b_save = (Button)findViewById(R.id.b_save);
        final EditText eT_Title = (EditText)findViewById(R.id.eT_Title);
        final EditText et_Notes = (EditText)findViewById(R.id.eT_notes);
        final TextView tV_Date = (TextView)findViewById(R.id.tV_Date);

        //To Open Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Serializable extra = getIntent().getSerializableExtra(_NOTE);

        if(extra != null){
            Note note = (Note)extra;
            eT_Title.setText(note.getTitle());
            et_Notes.setText(note.getNote());

            java.text.DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date = df.format(note.getDate());

            tV_Date.setText(date);

            isInEditMode = false;
            b_save.setText("Edit");
            et_Notes.setEnabled(false);
            eT_Title.setEnabled(false);

            isInAddMode = false;
        }

        if (b_save != null) {
            b_save.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v){

                    if(isInEditMode){
                        Intent returnIntent = new Intent();
                        //If there is text to save on note
                        if (et_Notes.getText() != null && !et_Notes.getText().toString().equals("")) {
                            //if there is no title
                            if ((eT_Title.getText() == null || eT_Title.getText().toString().equals(""))){
                                eT_Title.setText("<<No_Title>>");
                            }
                            //save the note
                            Note note = new Note(eT_Title.getText().toString(),
                                    et_Notes.getText().toString(),
                                    Calendar.getInstance().getTime());
                            returnIntent.putExtra(_NOTE, note);
                            setResult(RESULT_OK, returnIntent);

                        } else {
                            //close the editor without saving
                            setResult(RESULT_CANCELED, returnIntent);
                        }
                        finish();

                    } else {
                        isInEditMode = true;
                        b_save.setText("Save");
                        et_Notes.setEnabled(true);
                        eT_Title.setEnabled(true);
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.cancelNoteEdit:
                setResult(RESULT_CANCELED, new Intent());
                finish();
                break;
            case R.id.DeleteNoteEdit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_delete);
                builder.setTitle("Confirm Delete");
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int wich){

                    }

            });
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int wich){
                        Intent returnIntent = new Intent();
                        setResult(RESULT_DELETE, returnIntent);
                        finish();
                    }

            });
                builder.create().show();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_note, menu); //edit_note
        if (isInAddMode){
            menu.removeItem(R.id.DeleteNoteEdit);
        }
        return true;
    }
}
