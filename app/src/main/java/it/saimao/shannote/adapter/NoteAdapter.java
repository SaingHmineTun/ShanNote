package it.saimao.shannote.adapter;

import static it.saimao.shannote.utils.Utils.COLORS;
import static it.saimao.shannote.utils.Utils.STROKE_COLORS;
import static it.saimao.shannote.utils.Utils.getColorFromTheme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import it.saimao.shannote.R;
import it.saimao.shannote.model.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private Context context;
    private List<Note> notes;
    private NoteClickListener noteClickListener;


    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault());


    public NoteAdapter(Context context, List<Note> notes, NoteClickListener noteClickListener) {
        this.context = context;
        this.notes = notes;
        this.noteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.tvNoteTitle.setText(note.getTitle());
        holder.tvNoteTitle.setSelected(true);
        holder.tvNote.setText(note.getNote());
        holder.tvNoteDate.setText(simpleDateFormat.format(note.getUpdated()));
        holder.tvNoteDate.setSelected(true);
        if (note.isPinned()) holder.ivNotePin.setVisibility(View.VISIBLE);
        else holder.ivNotePin.setVisibility(View.GONE);

        /*
        To Add Random Color for Different Note
        int color = getRandomColor();
        holder.noteContainer.setCardBackgroundColor(color);
         */

        holder.cvNoteContainer.setCardBackgroundColor(getColorFromTheme(context, COLORS.get(note.getColor())));
        holder.cvNoteContainer.setStrokeColor(getColorFromTheme(context, STROKE_COLORS.get(note.getColor())));
//        holder.cvNoteContainer.setOnClickListener(v -> noteClickListener.onNoteClicked(note));
        holder.cvNoteContainer.setOnClickListener(v -> noteClickListener.onNoteClicked(note));
        holder.cvNoteContainer.setOnLongClickListener(v -> {
            noteClickListener.onNoteLongClicked(note, holder.cvNoteContainer);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}

class NoteViewHolder extends RecyclerView.ViewHolder {

    MaterialCardView cvNoteContainer;
    TextView tvNoteTitle, tvNote, tvNoteDate;
    ImageView ivNotePin;

    public NoteViewHolder(View itemView) {
        super(itemView);
        cvNoteContainer = itemView.findViewById(R.id.note_item_container);
        tvNoteTitle = itemView.findViewById(R.id.note_title);
        tvNote = itemView.findViewById(R.id.note_content);
        tvNoteDate = itemView.findViewById(R.id.note_date);
        ivNotePin = itemView.findViewById(R.id.note_pin);
    }
}

