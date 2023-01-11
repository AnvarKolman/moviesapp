package com.oder.cinema.viewmodels

import androidx.lifecycle.ViewModel
import com.oder.cinema.data.MoviesRepository
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    //val moviesLiveData: LiveData<List<Docs>>

    fun fetchMoviesByName(movieName: String) {
        moviesRepository.findByName(movieName)
    }

    /*class Factory @AssistedInject constructor(
        private val moviesRepository: MoviesRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == MoviesViewModel::class)
            return MoviesViewModel(moviesRepository) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(): MoviesViewModel.Factory
        }
    }*/

}