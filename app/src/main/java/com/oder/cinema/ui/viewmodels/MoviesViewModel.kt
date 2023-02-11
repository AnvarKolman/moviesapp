package com.oder.cinema.ui.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.oder.cinema.data.MoviesRepository
import com.oder.cinema.model.Movie
import com.oder.cinema.model.Result
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MoviesViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val movieList = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieList

    private val dispose = CompositeDisposable()

    fun saveDoc(movie: Movie): Completable = moviesRepository.saveDoc(movie)

    fun findTopMovies() {
        val result = moviesRepository.findTopMovies().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.movies }
            .subscribe({
                movieList.postValue(it)
            }, {
            })
    }

    override fun onCleared() {
        super.onCleared()
        dispose.clear()
    }

}