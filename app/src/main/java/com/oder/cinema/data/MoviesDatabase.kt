package com.oder.cinema.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oder.cinema.data.storage.Docs
import com.oder.cinema.data.storage.MoviesDao

@Database(entities = [Docs::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {


}
