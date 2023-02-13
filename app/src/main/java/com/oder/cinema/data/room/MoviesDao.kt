package com.oder.cinema.data.room

import androidx.room.*
import com.oder.cinema.data.room.MovieEntity.Companion.TABLE_NAME
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface MoviesDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Single<List<MovieEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): Single<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: MovieEntity): Completable

    @Delete
    fun delete(user: MovieEntity): Completable
    
    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    fun deleteById(id: Int): Completable

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll(): Completable
}