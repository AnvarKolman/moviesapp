package com.oder.cinema.data.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "docs_table")
data class Docs(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "alternativeName") val alternativeName:String,
    @ColumnInfo(name = "enName") val enName:String,
    @ColumnInfo(name = "year") val year:Int,
    @ColumnInfo(name = "movieLength") val movieLength:Int,
    @ColumnInfo(name = "description") val description:String
)
