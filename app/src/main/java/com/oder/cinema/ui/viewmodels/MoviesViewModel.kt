package com.oder.cinema.ui.viewmodels

import android.util.Log
import android.view.View
import android.widget.Toast
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

    val isLoading = MutableLiveData<Boolean>()
    private val movieList = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieList

    private val dispose = CompositeDisposable()

    init {
        findTopMovies()
    }

    fun saveDoc(movie: Movie) {
        val disposable = moviesRepository.saveDoc(movie).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i("Movie saved", "Movie saved")
            }, {

            })
        dispose.add(disposable)
    }

    private fun findTopMovies() {
        isLoading.value = false
        val result = moviesRepository.findTopMovies().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.movies }
            .subscribe({
                isLoading.value = true
                movieList.postValue(it)
            }, {
                isLoading.value = true
            })
        dispose.add(result)
    }

    override fun onCleared() {
        super.onCleared()
        dispose.clear()
    }

}