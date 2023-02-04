package com.oder.cinema.viewmodels

import androidx.lifecycle.ViewModel
import com.oder.cinema.data.MoviesRepository
import com.oder.cinema.model.Movie
import com.oder.cinema.model.Result
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class MoviesViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    fun fetchMoviesByName(movieName: String) = moviesRepository.findByName(movieName)

    fun saveDoc(movie: Movie): Completable = moviesRepository.saveDoc(movie)

    fun findByName(movieName: String): Single<Result> = moviesRepository.findByName(movieName)

    fun findTopMovies(): Single<Result> = moviesRepository.findTopMovies()

}