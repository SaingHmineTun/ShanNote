package it.saimao.shannote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import it.saimao.shannote.activity.AboutUsActivity;
import it.saimao.shannote.activity.AddNoteActivity;
import it.saimao.shannote.adapter.NoteAdapter;
import it.saimao.shannote.adapter.NoteClickListener;
import it.saimao.shannote.database.NoteDao;
import it.saimao.shannote.database.NoteDatabase;
import it.saimao.shannote.databinding.ActivityMainBinding;
import it.saimao.shannote.model.Note;
import it.saimao.shannote.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean isSearchEnabled;
    private final NoteClickListener noteClickListener = new NoteClickListener() {
        @Override
        public void onNoteClicked(Note note) {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            intent.putExtra("old_note", note);
            startActivityForResult(intent, UPDATE_NOTE_REQUEST_CODE);
        }

        @Override
        public void onNoteLongClicked(Note note, CardView cardView) {
            showPopup(note, cardView);
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
        binding.ibSearch.setOnClickListener(v -> {
            if (isSearchEnabled) {
                // Disable Search Function
                binding.ibSearch.setImageResource(R.drawable.ic_search);
                binding.lySearch.setVisibility(View.GONE);

            } else {
                // Enable Search Function
                binding.ibSearch.setImageResource(R.drawable.ic_clear);
                binding.lySearch.setVisibility(View.VISIBLE);
            }
            isSearchEnabled = !isSearchEnabled;
        });
        binding.ibAboutUs.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutUsActivity.class));
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
        isSearchEnabled = false;
        NoteDatabase database = NoteDatabase.getInstance(this);
        noteDao = database.getNoteDao();
        noteDao.getAllNotes().observeForever(notes -> {
            allNotes = notes;
            filteredNotes = new ArrayList<>(allNotes);
            updateRecycler();
        });
    }


    private void showPopup(Note note, CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView, Gravity.CENTER, 0, R.style.AppTheme);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.mPin) {
                note.setPinned(!note.isPinned());
                noteDao.updateNote(note);
                Toast.makeText(this, note.isPinned() ? "Pinned Success!" : "Unpin Success!", Toast.LENGTH_SHORT).show();
            } else {
                noteDao.deleteNote(note);
                Toast.makeText(this, "Delete Note Success!", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == ADD_NOTE_REQUEST_CODE) {
                Note newNote = (Note) data.getSerializableExtra("note");
                noteDao.addNote(newNote);
                Toast.makeText(this, "Add Note Success!", Toast.LENGTH_SHORT).show();
            } else if (requestCode == UPDATE_NOTE_REQUEST_CODE) {
                Note updatedNote = (Note) data.getSerializableExtra("note");
                noteDao.updateNote(updatedNote);
                Toast.makeText(this, "Update Note Success!", Toast.LENGTH_SHORT).show();
            } else updateRecycler();
        }
    }

}