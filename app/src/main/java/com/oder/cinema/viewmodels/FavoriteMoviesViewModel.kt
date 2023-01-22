package com.oder.cinema.viewmodels

import androidx.lifecycle.ViewModel
import com.oder.cinema.data.MoviesRepository
import com.oder.cinema.model.Docs
import io.reactivex.rxjava3.core.Single

class FavoriteMoviesViewModel(
    private val moviesRepository: MoviesRepository //TODO localRepo
) : ViewModel() {

    fun getAll(): Single<List<Docs>> = moviesRepository.getAll()

}