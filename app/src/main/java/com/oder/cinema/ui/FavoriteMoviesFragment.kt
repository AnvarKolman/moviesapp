package com.oder.cinema.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.oder.cinema.R
import com.oder.cinema.adapters.MoviesAdapter
import com.oder.cinema.adapters.decorations.GroupVerticalItemDecoration
import com.oder.cinema.adapters.decorations.HorizontalDividerItemDecoration
import com.oder.cinema.ui.appComponent
import com.oder.cinema.databinding.FragmentFavoriteMoviesBinding
import com.oder.cinema.model.Movie
import com.oder.cinema.ui.viewmodels.FavoriteMoviesViewModel
import com.oder.cinema.ui.viewmodels.FavoriteMoviesViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class FavoriteMoviesFragment : Fragment(R.layout.fragment_favorite_movies) {

    private val _moviesAdapter = MoviesAdapter()
    private lateinit var _binding: FragmentFavoriteMoviesBinding

    private val _viewModel: FavoriteMoviesViewModel by viewModels { factory.create() }

    @Inject
    lateinit var factory: FavoriteMoviesViewModelFactory.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        with(_binding.cinemaRecycler) {
            adapter = _moviesAdapter
            _moviesAdapter.onMoreBtnClick = { doc, view ->
                /*if (isPressed) {

                }*/
            }
            _moviesAdapter.onDetailBtnClick = { doc ->
                findNavController().navigate(
                    R.id.action_favoriteMoviesFragment_to_detail,
                    bundleOf(Movie::class.java.name to Gson().toJson(doc))
                )
            }
            layoutManager = LinearLayoutManager(this@FavoriteMoviesFragment.context)
            addItemDecoration(HorizontalDividerItemDecoration(50))
            addItemDecoration(GroupVerticalItemDecoration(R.layout.movies_row_item, 20, 30))
        }
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.fetchSavedMovies()
        _viewModel.savedMovies.observe(viewLifecycleOwner, Observer {
            _moviesAdapter.setData(it)
        })
    }

}