package com.example.m_irc.shiftworkplaner;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;

import java.util.List;

@Entity(tableName = "shift")
public class Shift {

    @PrimaryKey
    public int uid;

    //@ColumnInfo(name = "name")
    public String name;

    //@ColumnInfo(name = "color")
    public int color;
}

