package com.oder.cinema.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.oder.cinema.data.room.MovieEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MovieEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "alternativeName") val alternativeName: String? = null,
    @ColumnInfo(name = "enName") val enName: String? = null,
    @ColumnInfo(name = "year") val year: Int? = null,
    @ColumnInfo(name = "movieLength") val movieLength: Int? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "imageUrl") val imageUrl: String? = null,
    @ColumnInfo(name = "imdbRating") val imdbRating: Double? = null,
    @ColumnInfo(name = "kinopoiskRating") val kinopoiskRating: Double? = null,
) {
    companion object {
        const val TABLE_NAME = "movie_table"
    }
}
