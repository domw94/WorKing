package de.dominikwieners.working.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.data.TypeDao;
import de.dominikwieners.working.data.Work;
import de.dominikwieners.working.data.WorkDao;

/**
 * Created by dominikwieners on 13.03.18.
 */
@Database(entities = {Type.class, Work.class}, version = 1, exportSchema = false)
public abstract class WorkingDatabase extends RoomDatabase {

    private static final String DB_Name = "work.db";
    private static volatile WorkingDatabase instance;

    public static synchronized WorkingDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static WorkingDatabase create(final Context context) {
        return Room.databaseBuilder(context, WorkingDatabase.class, DB_Name).allowMainThreadQueries().build();
    }

    public abstract TypeDao getTypeDao();

    public abstract WorkDao getWorkDao();
}
