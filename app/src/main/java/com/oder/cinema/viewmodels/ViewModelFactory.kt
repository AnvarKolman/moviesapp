package com.oder.cinema.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oder.cinema.data.MoviesRepository
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val repository: MoviesRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesViewModel(repository) as T
    }
}