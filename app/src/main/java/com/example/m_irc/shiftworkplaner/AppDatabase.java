package com.example.m_irc.shiftworkplaner;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Shift.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShiftDao shiftDao();
}
