package com.oder.cinema.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import com.oder.cinema.R
import com.oder.cinema.ui.adapters.MoviesAdapter
import com.oder.cinema.ui.adapters.decorations.GroupVerticalItemDecoration
import com.oder.cinema.ui.adapters.decorations.HorizontalDividerItemDecoration
import com.oder.cinema.databinding.FragmentFavoriteMoviesBinding
import com.oder.cinema.model.Movie
import com.oder.cinema.ui.viewmodels.FavoriteMoviesViewModel
import com.oder.cinema.ui.viewmodels.FavoriteMoviesViewModelFactory
import javax.inject.Inject


class FavoriteMoviesFragment : Fragment(R.layout.fragment_favorite_movies) {

    private lateinit var _moviesAdapter: MoviesAdapter
    private lateinit var _binding: FragmentFavoriteMoviesBinding
    private lateinit var requestManager: RequestManager

    private val _viewModel: FavoriteMoviesViewModel by viewModels { factory.create() }

    @Inject
    lateinit var factory: FavoriteMoviesViewModelFactory.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        requestManager = Glide.with(requireContext())
        _moviesAdapter = MoviesAdapter(requestManager, true)
        with(_binding.cinemaRecycler) {
            adapter = _moviesAdapter
            _moviesAdapter.onMoreBtnClick = { movie, view ->
                val popupMenu = PopupMenu(context, view)
                popupMenu.inflate(R.menu.favorite_more_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.remove_item -> {
                            _viewModel.removeMovie(movie)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.share_item -> {
                            Toast.makeText(context, "В разработке", Toast.LENGTH_SHORT).show()
                            return@setOnMenuItemClickListener true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }
                popupMenu.show()
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
        _viewModel.savedMovies.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                _binding.emptyText.visibility = View.VISIBLE
            } else {
                _moviesAdapter.setData(it)
                _binding.emptyText.visibility = View.GONE
            }
        }
    }

}