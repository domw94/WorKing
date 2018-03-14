package de.dominikwieners.working.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.data.TypeDao;

/**
 * Created by dominikwieners on 13.03.18.
 */
@Database(entities = {Type.class}, version = 1, exportSchema = false)
public abstract class TypeDatabase extends RoomDatabase {

    private static final String DB_Name = "type.db";
    private static volatile TypeDatabase instance;

    public static synchronized TypeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static TypeDatabase create(final Context context) {
        return Room.databaseBuilder(context, TypeDatabase.class, DB_Name).allowMainThreadQueries().build();
    }
    public abstract TypeDao getTypeDao();
}
