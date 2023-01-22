package com.oder.cinema

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.oder.cinema.adapters.MoviesAdapter


class FavoriteMoviesFragment : Fragment() {

    private val _moviesAdapter = MoviesAdapter()

    /*@Inject
    lateinit var  database: MoviesDatabase*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.i("Movie", database.movieDao().getAll().toString())

    }

}