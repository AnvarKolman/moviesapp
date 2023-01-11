package com.oder.cinema.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MoviesDao {
    @Query("SELECT * FROM docs_table")
    fun getAll(): List<Docs>

    @Query("SELECT * FROM docs_table WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Docs>

    @Insert
    fun insertAll(vararg users: Docs)

    @Delete
    fun delete(user: Docs)
}