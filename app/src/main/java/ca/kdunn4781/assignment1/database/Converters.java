package ca.kdunn4781.assignment1.database;

import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * This class contains converters for the Room database
 */
public class Converters {
    @TypeConverter
    public static LocalDateTime toDate(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }

    @TypeConverter
    public static String fromDate(LocalDateTime value) {
        return value == null ? null : value.toString();
    }

    @TypeConverter
    public static Duration toDuration(long value) {
        return value < 0 ? Duration.ZERO : Duration.ofMillis(value);
    }

    @TypeConverter
    public static long fromDuration(Duration value) {
        return value == null ? 0 : value.toMillis();
    }
}
