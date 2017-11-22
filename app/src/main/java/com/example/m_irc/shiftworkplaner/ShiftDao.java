package com.example.m_irc.shiftworkplaner;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ShiftDao {
    @Query("SELECT * FROM shift")
    List<Shift> getAll();

    @Query("SELECT * FROM shift WHERE uid IN (:shiftIds)")
    List<Shift> loadAllByIds(int[] shiftIds);

    @Query("SELECT * FROM shift WHERE name = :name LIMIT 1")
    Shift findByName(String name);

    @Insert
    void insertAll(Shift... shifts);

    @Update
    void updateShifts(Shift... shifts);

    @Delete
    void delete(Shift user);
}
