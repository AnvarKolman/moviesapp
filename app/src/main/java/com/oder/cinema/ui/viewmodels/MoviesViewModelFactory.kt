package com.oder.cinema.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oder.cinema.data.MoviesRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MoviesViewModelFactory @AssistedInject constructor(private val moviesRepository: MoviesRepository) :
    ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesViewModel(moviesRepository) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(): MoviesViewModelFactory

    }

}