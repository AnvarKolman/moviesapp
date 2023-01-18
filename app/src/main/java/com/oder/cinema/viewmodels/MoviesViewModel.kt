package com.oder.cinema.viewmodels

import androidx.lifecycle.ViewModel
import com.oder.cinema.data.MoviesRepository
import com.oder.cinema.model.Docs
import com.oder.cinema.model.Result
import io.reactivex.rxjava3.core.Single

class MoviesViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    fun fetchMoviesByName(movieName: String) {
        moviesRepository.findByName(movieName)
    }

    fun saveDoc(docs: Docs) {
        moviesRepository.saveDoc(docs)
    }

    fun findByName(movieName: String): Single<Result> {
        return moviesRepository.findByName(movieName)
    }

}