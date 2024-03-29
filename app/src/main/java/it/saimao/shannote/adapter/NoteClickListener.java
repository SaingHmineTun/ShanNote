package it.saimao.shannote.adapter;

import androidx.cardview.widget.CardView;

import it.saimao.shannote.model.Note;

public interface NoteClickListener {
    void onNoteClicked(Note note);

    void onNoteLongClicked(Note note, CardView cardView);
}