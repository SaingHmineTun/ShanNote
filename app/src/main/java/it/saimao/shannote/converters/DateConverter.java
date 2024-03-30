package it.saimao.shannote.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public Date fromTimestamp(long timestamp) {
        return new Date(timestamp);
    }

    @TypeConverter
    public long toTimestamp(Date date) {
        return date.getTime();
    }
}
