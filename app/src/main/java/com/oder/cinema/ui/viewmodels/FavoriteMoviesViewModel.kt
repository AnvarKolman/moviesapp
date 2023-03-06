package com.oder.cinema.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oder.cinema.data.MoviesRepository
import com.oder.cinema.model.Movie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FavoriteMoviesViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val moviesList = MutableLiveData<List<Movie>>()
    val savedMovies: LiveData<List<Movie>>
        get() = moviesList
    private val cs = CompositeDisposable()

    init {
        fetchSavedMovies()
    }


    fun removeMovie(movie: Movie) {
        movie.id?.let {
            moviesRepository.deleteById(it).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { fetchSavedMovies() },
                    { error -> Log.e(TAG, "Unable to remove movie", error) })
        }

    }

    fun fetchSavedMovies() {
        val result = moviesRepository.getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { moviesList.value = it },
                { error -> Log.e(TAG, "Unable to get movies", error) })
        cs.add(result)
    }

    override fun onCleared() {
        super.onCleared()
        cs.clear()
    }

    companion object {
        private val TAG = FavoriteMoviesViewModel::class.java.simpleName
    }

}