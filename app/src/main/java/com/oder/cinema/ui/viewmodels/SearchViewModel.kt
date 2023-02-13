package com.oder.cinema.ui.viewmodels

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

    val isLoading = MutableLiveData<Boolean>()
    private val movieList = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieList

    private val dispose = CompositeDisposable()

    fun saveDoc(movie: Movie): Completable = moviesRepository.saveDoc(movie)

    fun findMovieByName(movieName: String) {
        isLoading.value = false
        val result = moviesRepository.findByName(movieName).subscribeOn(Schedulers.io())
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