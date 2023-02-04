package com.oder.cinema.data

import com.oder.cinema.MovieModel
import com.oder.cinema.Token
import com.oder.cinema.data.room.MovieEntity
import com.oder.cinema.data.room.MoviesDatabase
import com.oder.cinema.model.Movie
import com.oder.cinema.model.Poster
import com.oder.cinema.model.Result
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface MoviesRepository {
    suspend fun getMovies(): List<MovieModel>
    fun findByName(movieName: String): Single<Result>
    fun findById(id: String): Single<Result>
    fun findTopMovies(): Single<Result>
    fun saveDoc(movie: Movie): Completable
    fun getAll(): Single<List<Movie>>
}

class MoviesRepositoryImpl(
    private val moviesService: MoviesService,
    private val moviesDatabase: MoviesDatabase,
) : MoviesRepository {

    private val token = Token().token()

    override fun saveDoc(movie: Movie): Completable = moviesDatabase.movieDao().insertAll(
        MovieEntity(
            id = movie.id ?: 10,
            name = wrapNull(movie.name),
            alternativeName = wrapNull(movie.alternativeName),
            enName = wrapNull(movie.enName),
            year = movie.year ?: 1,
            movieLength = movie.movieLength ?: 10,
            description = wrapNull(movie.description),
            imageUrl = movie.poster?.url.toString()
        )
    )

    override fun getAll(): Single<List<Movie>> = moviesDatabase.movieDao().getAll().map {
        it.map { entity ->
            Movie(
                name = entity.name,
                description = entity.description,
                id = entity.id,
                poster = Poster(
                    url = entity.imageUrl
                )
            )
        }
    }


    private fun wrapNull(value: String?): String {
        return value ?: ""
    }

    override suspend fun getMovies(): List<MovieModel> {
        val response = moviesService.movie(token)
        //if (response.isSuccessful && response.body() != null) {
        //Log.i("NAMES ", response.body()?.docs.toString())
        return response.body()?.movies?.map { MovieModel(it.name, it.poster?.url, null) }?.toList()
            ?: emptyList()
        //}
    }

    override fun findByName(movieName: String): Single<Result> {
        return moviesService.findByName(token = token, name = movieName)
    }

    override fun findById(id: String): Single<Result> {
        return moviesService.findById(token = token, id = id)
    }

    override fun findTopMovies(): Single<Result> {
        return moviesService.findTopMovies(token =token)
    }

}