package it.saimao.shannote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.saimao.shannote.activity.AddNoteActivity;
import it.saimao.shannote.adapter.NoteAdapter;
import it.saimao.shannote.adapter.NoteClickListener;
import it.saimao.shannote.database.NoteDao;
import it.saimao.shannote.database.NoteDatabase;
import it.saimao.shannote.databinding.ActivityMainBinding;
import it.saimao.shannote.model.Note;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final NoteClickListener noteClickListener = new NoteClickListener() {
        @Override
        public void onNoteClicked(Note note) {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            intent.putExtra("old_note", note);
            startActivityForResult(intent, UPDATE_NOTE_REQUEST_CODE);
        }

        @Override
        public void onNoteLongClicked(Note note, CardView cardView) {

        }
    };
    private List<Note> allNotes, filteredNotes;
    private NoteDao noteDao;
    private final int ADD_NOTE_REQUEST_CODE = 101;
    private final int UPDATE_NOTE_REQUEST_CODE = 102;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initListeners();
    }

    private void initListeners() {
        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST_CODE);
        });
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filteredNotes.clear();
                if (s.toString().isEmpty()) {
                    filteredNotes.addAll(allNotes);
                } else {
                    filteredNotes.addAll(allNotes.stream().filter(note -> note.getTitle().toLowerCase().contains(s) || note.getNote().toLowerCase().contains(s)).collect(Collectors.toList()));
                }
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateRecycler() {
        if (filteredNotes.isEmpty()) {
            binding.llEmpty.setVisibility(View.VISIBLE);
            binding.rvNotes.setVisibility(View.GONE);
        } else {
            binding.llEmpty.setVisibility(View.GONE);
            binding.rvNotes.setVisibility(View.VISIBLE);
            noteAdapter = new NoteAdapter(this, filteredNotes, noteClickListener);
            binding.rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            binding.rvNotes.setAdapter(noteAdapter);
        }
    }

    private void init() {
        NoteDatabase database = NoteDatabase.getInstance(this);
        noteDao = database.getNoteDao();
        allNotes = noteDao.getAllNotes();
        filteredNotes = new ArrayList<>(allNotes);
        updateRecycler();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == ADD_NOTE_REQUEST_CODE) {
                Note newNote = (Note) data.getSerializableExtra("note");
                noteDao.addNote(newNote);
            } else if (requestCode == UPDATE_NOTE_REQUEST_CODE) {
                Note updatedNote = (Note) data.getSerializableExtra("note");
                noteDao.updateNote(updatedNote);
            }

            allNotes = noteDao.getAllNotes();
            filteredNotes.clear();
            filteredNotes.addAll(allNotes);
            if (noteAdapter != null) noteAdapter.notifyDataSetChanged();
            else updateRecycler();
        }
    }

}