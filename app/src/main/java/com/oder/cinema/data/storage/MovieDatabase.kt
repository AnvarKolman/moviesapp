package com.oder.cinema.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Docs::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao
}