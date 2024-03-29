package it.saimao.shannote.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.NotActiveException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.saimao.shannote.R;
import it.saimao.shannote.databinding.ActivityAddNoteBinding;
import it.saimao.shannote.model.Note;

public class AddNoteActivity extends AppCompatActivity {


    private ActivityAddNoteBinding binding;
    private Note note;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListeners();
        checkForUpdate();
    }


    private void checkForUpdate() {
        Serializable ser = getIntent().getSerializableExtra("old_note");
        if (ser != null) {
            note = (Note) ser;
            binding.etTitle.setText(note.getTitle());
            binding.etNote.setText(note.getNote());
            binding.tvAppTitle.setText(getResources().getString(R.string.update_note));
        }
    }

    private void initListeners() {
        binding.ivBack.setOnClickListener(v -> finish());
        binding.ivSave.setOnClickListener(v -> {
            saveNote();
        });
    }

    private void saveNote() {
        String title = binding.etTitle.getText().toString();
        String content = binding.etNote.getText().toString();
        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "Unable to save", Toast.LENGTH_SHORT).show();
            return;
        }
        if (note == null) {
            // Adding new note
            note = new Note();
            note.setDate(simpleDateFormat.format(new Date()));
        }
        note.setTitle(title);
        note.setNote(content);

        Intent intent = new Intent();
        intent.putExtra("note", note);
        setResult(RESULT_OK, intent);
        finish();
    }

}