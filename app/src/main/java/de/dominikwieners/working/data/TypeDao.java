package de.dominikwieners.working.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by dominikwieners on 13.03.18.
 */
@Dao
public interface TypeDao {
    @Query("SELECT * FROM Type")
    List<Type> getAll();

    @Insert
    void insertAll(Type... types);

    @Delete
    void delete(Type type);
}
