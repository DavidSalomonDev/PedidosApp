// sv/edu/ues/pedidosapp/data/local/db/Converters.java
package sv.edu.ues.pedidosapp.data.local.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class Converters {

    private static Gson gson = new Gson();

    // Convertir Date a Long (timestamp)
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    // Convertir Long (timestamp) a Date
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    // Convertir List<String> a String JSON
    @TypeConverter
    public static String fromStringList(List<String> value) {
        if (value == null) {
            return null;
        }
        return gson.toJson(value);
    }

    // Convertir String JSON a List<String>
    @TypeConverter
    public static List<String> fromStringJson(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(value, listType);
    }

    // Convertir List<Integer> a String JSON
    @TypeConverter
    public static String fromIntegerList(List<Integer> value) {
        if (value == null) {
            return null;
        }
        return gson.toJson(value);
    }

    // Convertir String JSON a List<Integer>
    @TypeConverter
    public static List<Integer> fromIntegerJson(String value) {
        if (value == null) {
            return null;
        }
        Type listType = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.fromJson(value, listType);
    }

    // Convertir Boolean a Integer (para compatibilidad SQLite)
    @TypeConverter
    public static Integer fromBoolean(Boolean value) {
        return value == null ? null : (value ? 1 : 0);
    }

    // Convertir Integer a Boolean
    @TypeConverter
    public static Boolean toBoolean(Integer value) {
        return value == null ? null : value != 0;
    }
}