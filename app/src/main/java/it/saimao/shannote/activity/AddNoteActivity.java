package it.saimao.shannote.activity;

import static it.saimao.shannote.utils.Utils.COLORS;
import static it.saimao.shannote.utils.Utils.getColorFromTheme;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.saimao.shannote.R;
import it.saimao.shannote.database.NoteDao;
import it.saimao.shannote.database.NoteDatabase;
import it.saimao.shannote.databinding.ActivityAddNoteBinding;
import it.saimao.shannote.model.Note;
import it.saimao.shannote.viewmodel.AddNoteViewModel;

public class AddNoteActivity extends AppCompatActivity {


    private ActivityAddNoteBinding binding;
    private Note note;
    private CardView selectedColor;
    private NoteDao noteDao;
    private AddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewModel();
        checkForUpdate();
        initListeners();
        initBackgroundColor();
        initDatabase();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);

    }

    private void initDatabase() {
        if (note.getCreated() != null)
            noteDao = NoteDatabase.getInstance(this).getNoteDao();
    }

    private void initBackgroundColor() {
        setColor(note.getColor());
    }


    private void setColor(int colorIndex) {

        note.setColor(colorIndex);
        int color = getColorFromTheme(this, COLORS.get(colorIndex));
        binding.getRoot().setBackgroundColor(color);
        binding.tbAddNote.setBackgroundColor(color);
        getWindow().setStatusBarColor(color);
        switch (colorIndex) {
            case 0 -> selectedColor = binding.cv0;
            case 1 -> selectedColor = binding.cv1;
            case 2 -> selectedColor = binding.cv2;
            case 3 -> selectedColor = binding.cv3;
            case 4 -> selectedColor = binding.cv4;
        }
        selectedColor.getChildAt(0).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (viewModel.hasUnsavedChanges()) {
            showUnsavedChangesDialog();
        } else {
            super.onBackPressed();
        }
    }

    //    @Override
//    protected void onDestroy() {
//        Log.d("Kham", "Add Note Activity On Destroy");
//        if (note != null) {
//            // Update the note
//            if (!note.getTitle().equals(binding.etTitle.getText().toString()) || note.getNote().equals(binding.etNote.getText().toString())) {
//
//                Log.d("Kham", "Update Note : Save unsaved changes");
//                showUnsavedChangesDialog();
//            } else {
//                super.onDestroy();
//            }
//        } else {
//            // Add New Note
//            if (!binding.etTitle.getText().toString().isEmpty() || !binding.etNote.getText().toString().isEmpty()) {
//                Log.d("Kham", "Add Note : Save unsaved changes");
//                showUnsavedChangesDialog();
//            } else {
//                super.onDestroy();
//            }
//        }
//    }

    private void showUnsavedChangesDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Do you want to save them before exiting?")
                .setPositiveButton("Save", (dialog, which) -> {
                    saveNote();
                })
                .setNegativeButton("Discard", (dialog, which) -> {
                    finish();
                })
                .show();
    }

    private void checkForUpdate() {
        Serializable ser = getIntent().getSerializableExtra("old_note");
        if (ser != null) {
            note = (Note) ser;
            binding.etTitle.setText(note.getTitle());
            binding.etNote.setText(note.getNote());
            binding.ibPin.setImageResource(note.isPinned() ? R.drawable.ic_pin : R.drawable.ic_unpin);
            binding.tvLastUpdated.setText(MessageFormat.format("{0} : {1}", getString(R.string.last_updated), new SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault()).format(note.getUpdated())));
        } else {
            note = new Note();
            binding.tvLastUpdated.setText(R.string.newly_created);
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (note.getCreated() != null) {
                // Update the note
                viewModel.setHasUnsavedChanges((!(note.getTitle().equals(binding.etTitle.getText().toString())) || !(note.getNote().equals(binding.etNote.getText().toString()))));
            } else {
                // Add New Note
                viewModel.setHasUnsavedChanges(!binding.etTitle.getText().toString().isEmpty() || !binding.etNote.getText().toString().isEmpty());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initListeners() {
        binding.ivBack.setOnClickListener(v -> onBackPressed());
        binding.ivSave.setOnClickListener(v -> saveNote());
        binding.ibPin.setOnClickListener(v -> {
            note.setPinned(!note.isPinned());
            if (noteDao != null) noteDao.updateNote(note);
            binding.ibPin.setImageResource(note.isPinned() ? R.drawable.ic_pin : R.drawable.ic_unpin);
        });
        binding.ibColor.setOnClickListener(v -> {
            if (binding.lyTheme.getVisibility() == View.GONE) {
                binding.lyTheme.setVisibility(View.VISIBLE);
            } else {
                binding.lyTheme.setVisibility(View.GONE);
            }
        });
        binding.etTitle.addTextChangedListener(textWatcher);
        binding.etNote.addTextChangedListener(textWatcher);
    }

    private void saveNote() {
        String title = binding.etTitle.getText().toString();
        String content = binding.etNote.getText().toString();
        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "Unable to save", Toast.LENGTH_SHORT).show();
            return;
        }
        if (note.getTitle().isEmpty() && note.getNote().isEmpty()) {
            // Adding new note
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


    public void changeColor(View view) {
        selectedColor.getChildAt(0).setVisibility(View.GONE);
        if (view.getId() == R.id.cv0) {
            setColor(0);
        } else if (view.getId() == R.id.cv1) {

            setColor(1);
        } else if (view.getId() == R.id.cv2) {

            setColor(2);
        } else if (view.getId() == R.id.cv3) {

            setColor(3);
        } else if (view.getId() == R.id.cv4) {
            setColor(4);
        }
        if (noteDao != null) {
            noteDao.updateNote(note);
        }
    }

}