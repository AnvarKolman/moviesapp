package com.oder.cinema.data.room

import com.oder.cinema.model.Movie

//TODO
class MovieWrapper {

    fun wrapMovieModelToEntry(movie: Movie) : MovieEntity {
        return MovieEntity(id = movie.id ?: 10)
    }

}