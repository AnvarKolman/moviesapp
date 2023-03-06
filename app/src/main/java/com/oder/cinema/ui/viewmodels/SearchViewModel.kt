package com.oder.cinema.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oder.cinema.data.MoviesRepository
import com.oder.cinema.model.Movie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val movieList = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieList

    private val dispose = CompositeDisposable()

    fun saveDoc(movie: Movie) {
        val disposable = moviesRepository.saveDoc(movie).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e(TAG, "Movie saved")
            }, { error ->
                Log.e(TAG, "Unable to save movie", error)
            })
        dispose.add(disposable)
    }

    fun findMovieByName(movieName: String) {
        _isLoading.value = false
        val disposable = moviesRepository.findByName(movieName).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.movies }
            .subscribe({
                _isLoading.value = true
                movieList.postValue(it)
            }, { error ->
                _isLoading.value = true
                Log.e(TAG, "Unable to find movie by name", error)
            })
        dispose.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        dispose.clear()
    }

    companion object {
        private val TAG = SearchViewModel::class.java.simpleName
    }

}