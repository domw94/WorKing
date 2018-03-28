package de.dominikwieners.working.data.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by dominikwieners on 18.03.18.
 */
@Entity
public class Work implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "work_type")
    private String workType;

    @ColumnInfo(name = "day_of_week")
    private String dayOfWeek;

    @ColumnInfo(name = "day")
    private int day;

    @ColumnInfo(name = "month")
    private int month;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "start_hour")
    private int startHour;

    @ColumnInfo(name = "start_min")
    private int startMin;

    @ColumnInfo(name = "end_hour")
    private int endHour;

    @ColumnInfo(name = "end_Min")
    private int endMin;

    @ColumnInfo(name = "todays_hours")
    private int todaysMin;

    public Work(String workType, String dayOfWeek, int day, int month, int year, int startHour, int startMin, int endHour, int endMin, int todaysMin) {
        this.workType = workType;
        this.dayOfWeek = dayOfWeek;
        this.day = day;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.todaysMin = todaysMin;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMin() {
        return endMin;
    }

    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }

    public int getTodaysMin() {
        return todaysMin;
    }

    public void setTodaysMin(int todaysMin) {
        this.todaysMin = todaysMin;
    }
}
