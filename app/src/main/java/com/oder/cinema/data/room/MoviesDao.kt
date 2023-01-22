package com.oder.cinema.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.oder.cinema.data.room.MovieEntity
import com.oder.cinema.data.room.MovieEntity.Companion.TABLE_NAME
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface MoviesDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Single<List<MovieEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): Single<List<MovieEntity>>

    @Insert
    fun insertAll(vararg users: MovieEntity) : Completable

    @Delete
    fun delete(user: MovieEntity) : Completable
}