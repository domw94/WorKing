package de.dominikwieners.working.data.room;

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

    @Query("SELECT * FROM work WHERE year = :selectedYear AND month = :selectedMonth")
    List<Work> loadDataByMonth(int selectedYear, int selectedMonth);

    @Query("SELECT work_type FROM work WHERE year = :selectedYear AND month = :selectedMonth GROUP BY work_type")
    List<String> loadWorkingTypes(int selectedYear, int selectedMonth);

    @Query("SELECT year FROM work GROUP BY year")
    List<Integer> loadYears();

    @Insert
    void insertAll(Work... works);

    @Delete
    void delete(Work work);

    @Query("DELETE FROM work WHERE year = :selectedYear AND month = :selectedMonth ")
    void deleteByMonth(int selectedYear, int selectedMonth);

    @Query("DELETE FROM work WHERE year = :selectedYear")
    void deleteByYear(int selectedYear);

}
