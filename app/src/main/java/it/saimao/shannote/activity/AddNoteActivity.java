package it.saimao.shannote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.Date;

import it.saimao.shannote.R;
import it.saimao.shannote.databinding.ActivityAddNoteBinding;
import it.saimao.shannote.model.Note;

public class AddNoteActivity extends AppCompatActivity {


    private ActivityAddNoteBinding binding;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setColor();
        initListeners();
        checkForUpdate();
    }

    private void setColor() {
        binding.getRoot().setBackgroundColor(getColorFromTheme(com.google.android.material.R.attr.colorTertiaryContainer));
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
        binding.ivSave.setOnClickListener(v -> saveNote());
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
            note.setCreated(new Date());
        }
        note.setUpdated(new Date());
        note.setTitle(title);
        note.setNote(content);

        Intent intent = new Intent();
        intent.putExtra("note", note);
        setResult(RESULT_OK, intent);
        finish();
    }

    private int getColorFromTheme(int attributeId) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attributeId, typedValue, true);
        return ContextCompat.getColor(this, typedValue.resourceId);
    }

}