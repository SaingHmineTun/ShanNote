package it.saimao.shannote.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import it.saimao.shannote.converters.DateConverter;
import it.saimao.shannote.model.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();

    private static NoteDatabase noteDatabase;
    private static final String DATABASE_NAME = "note_database";

    public synchronized static NoteDatabase getInstance(Context context) {
        if (noteDatabase == null) {
            noteDatabase = Room.databaseBuilder(context, NoteDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return noteDatabase;
    }

}
