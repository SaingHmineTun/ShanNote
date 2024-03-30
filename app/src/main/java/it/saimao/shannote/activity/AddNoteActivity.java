package it.saimao.shannote.activity;

import static it.saimao.shannote.utils.Utils.COLORS;
import static it.saimao.shannote.utils.Utils.getColorFromTheme;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.saimao.shannote.R;
import it.saimao.shannote.databinding.ActivityAddNoteBinding;
import it.saimao.shannote.model.Note;

public class AddNoteActivity extends AppCompatActivity {


    private ActivityAddNoteBinding binding;
    private Note note;
    private CardView selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListeners();
        checkForUpdate();
        initBackgroundColor();
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
        }
        selectedColor.getChildAt(0).setVisibility(View.VISIBLE);
    }

    private void checkForUpdate() {
        Serializable ser = getIntent().getSerializableExtra("old_note");
        if (ser != null) {
            note = (Note) ser;
            binding.etTitle.setText(note.getTitle());
            binding.etNote.setText(note.getNote());
            binding.ibPin.setImageResource(note.isPinned() ? R.drawable.ic_pin : R.drawable.ic_unpin);
            binding.tvLastUpdated.setText(getString(R.string.last_updated) + " : " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault()).format(note.getUpdated()));
        } else {
            note = new Note();
            binding.tvLastUpdated.setText(R.string.newly_created);
        }
    }

    private void initListeners() {
        binding.ivBack.setOnClickListener(v -> finish());
        binding.ivSave.setOnClickListener(v -> saveNote());
        binding.ibPin.setOnClickListener(v -> {
            note.setPinned(!note.isPinned());
            binding.ibPin.setImageResource(note.isPinned() ? R.drawable.ic_pin : R.drawable.ic_unpin);
        });
        binding.ibColor.setOnClickListener(v -> {
            if (binding.lyTheme.getVisibility() == View.GONE) {
                binding.lyTheme.setVisibility(View.VISIBLE);
            } else {
                binding.lyTheme.setVisibility(View.GONE);
            }
        });
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


    @Override
    protected void onStop() {
        super.onStop();

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
        }
    }
}