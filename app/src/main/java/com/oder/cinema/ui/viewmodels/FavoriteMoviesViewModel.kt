package com.oder.cinema.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oder.cinema.data.MoviesRepository
import com.oder.cinema.model.Movie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Flow.Subscriber

class FavoriteMoviesViewModel(
    private val moviesRepository: MoviesRepository //TODO localRepo
) : ViewModel() {

    private val moviesList = MutableLiveData<List<Movie>>()
    val savedMovies: LiveData<List<Movie>>
        get() = moviesList
    private val cs = CompositeDisposable()

    fun fetchSavedMovies() {
        val result = moviesRepository.getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                moviesList.postValue(it)
            }, {
                //TODO
            })
        cs.add(result)
    }

    override fun onCleared() {
        super.onCleared()
        cs.clear()
    }

}