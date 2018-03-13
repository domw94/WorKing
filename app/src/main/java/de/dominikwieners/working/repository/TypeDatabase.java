package de.dominikwieners.working.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.data.TypeDao;

/**
 * Created by dominikwieners on 13.03.18.
 */
@Database(entities = {Type.class}, version = 1, exportSchema = false)
public abstract class TypeDatabase extends RoomDatabase {
    public abstract TypeDao getTypeDao();
}
