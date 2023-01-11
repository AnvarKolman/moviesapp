package com.oder.cinema.data

import com.oder.cinema.MovieModel
import com.oder.cinema.data.storage.LocalDataSource
import com.oder.cinema.model.Docs
import com.oder.cinema.model.Result
import io.reactivex.rxjava3.core.Single

interface MoviesRepository {
    suspend fun getMovies(): List<MovieModel>
    fun findByName(movieName: String): Single<Result>
    fun findById(id: String): Single<Result>
    fun saveDoc(docs: Docs)
}

class MoviesRepositoryImpl(
    private val moviesService: MoviesService
) : MoviesRepository {

    private val token = "06MD2ET-PGHMH4W-M5K93FD-2PQBZ0F"

    override fun saveDoc(docs: Docs) {
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