package com.oder.cinema.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.oder.cinema.data.room.MovieEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "alternativeName") val alternativeName: String,
    @ColumnInfo(name = "enName") val enName: String,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "movieLength") val movieLength: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
) {
    companion object {
        const val TABLE_NAME = "movie_table"
    }
}
