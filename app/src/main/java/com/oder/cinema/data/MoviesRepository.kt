package com.oder.cinema.data

import com.oder.cinema.MovieModel
import com.oder.cinema.Token
import com.oder.cinema.data.room.MovieEntity
import com.oder.cinema.data.room.MoviesDatabase
import com.oder.cinema.model.Docs
import com.oder.cinema.model.Result
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface MoviesRepository {
    suspend fun getMovies(): List<MovieModel>
    fun findByName(movieName: String): Single<Result>
    fun findById(id: String): Single<Result>
    fun saveDoc(docs: Docs): Completable
}

class MoviesRepositoryImpl(
    private val moviesService: MoviesService,
    private val moviesDatabase: MoviesDatabase,
) : MoviesRepository {

    private val token = Token().token()

    override fun saveDoc(docs: Docs): Completable =
        moviesDatabase.movieDao().insertAll(
            MovieEntity(
                id = docs.id ?: 10,
                name = wrapNull(docs.name),
                alternativeName = wrapNull(docs.alternativeName),
                enName = wrapNull(docs.enName),
                year = docs.year ?: 1,
                movieLength = docs.movieLength ?: 10,
                description = wrapNull(docs.description)
            )
        )


    private fun wrapNull(value: String?): String {
        return value ?: ""
    }

    override suspend fun getMovies(): List<MovieModel> {
        val response = moviesService.movie(token)
        //if (response.isSuccessful && response.body() != null) {
        //Log.i("NAMES ", response.body()?.docs.toString())
        return response.body()?.docs?.map { MovieModel(it.name, it.poster?.url, null) }?.toList()
            ?: emptyList()
        //}
    }

    override fun findByName(movieName: String): Single<Result> {
        return moviesService.findByName(token = token, name = movieName)
    }

    override fun findById(id: String): Single<Result> {
        return moviesService.findById(token = token, id = id)
    }

}