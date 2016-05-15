package com.example.lcarrasco.hello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListNotesActivity extends AppCompatActivity {

    public static final int EDIT_NOTE= 999;
    private List<Note> notes = new ArrayList<>();
    private ListView lv_notes;
    private int editingNoteID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        lv_notes =  (ListView)findViewById(R.id.lV_Notes);

        lv_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Intent editNoteIntent = new Intent(view.getContext(), EditNoteActivity.class);
                editNoteIntent.putExtra(EditNoteActivity._NOTE, notes.get(position));
                editingNoteID = position;
                startActivityForResult(editNoteIntent, EDIT_NOTE);
            }
        });

        registerForContextMenu(lv_notes);

        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.list_notes, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        deleteElementAndRefreshList(info.position);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent editNoteIntent = new Intent(ListNotesActivity.this, EditNoteActivity.class);
        startActivityForResult(editNoteIntent, EDIT_NOTE);

        return true;
    }

    private void populateList() {
        List<String> values = new ArrayList<>();
        for (Note note : notes){
            values.add(note.getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        lv_notes.setAdapter(adapter);
    }

    private void deleteElementAndRefreshList(int position){
        notes.remove(position);
        populateList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case RESULT_CANCELED:
                return;
            case RESULT_OK:
                Serializable extra = data.getSerializableExtra(EditNoteActivity._NOTE);

                if (extra != null) {
                    Note newNote = (Note) extra;

                    if (editingNoteID > -1) {
                        notes.set(editingNoteID, newNote);
                        editingNoteID = -1;
                    } else {
                        notes.add(newNote);
                    }
                    populateList();
                }
                break;
            case EditNoteActivity.RESULT_DELETE:
                deleteElementAndRefreshList(editingNoteID);
                editingNoteID = -1;
                break;
        }
    }
}
