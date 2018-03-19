package de.dominikwieners.working.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by dominikwieners on 18.03.18.
 */

@Dao
public interface WorkDao {

    @Query("SELECT * FROM work")
    List<Work> getAll();

    @Query("SELECT * FROM work WHERE month = :selectedMonth")
    List<Work> loadDataByMonth(int selectedMonth);

    @Insert
    void insertAll(Work... works);

    @Delete
    void delete(Work work);

    @Query("DELETE FROM type where id = :uid")
    void deleteById(int uid);
}
