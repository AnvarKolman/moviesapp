package com.oder.cinema.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

}
