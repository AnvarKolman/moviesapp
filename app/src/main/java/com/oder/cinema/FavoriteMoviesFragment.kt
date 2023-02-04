package com.oder.cinema

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.oder.cinema.adapters.MoviesAdapter
import com.oder.cinema.adapters.decorations.GroupVerticalItemDecoration
import com.oder.cinema.adapters.decorations.HorizontalDividerItemDecoration
import com.oder.cinema.databinding.FragmentFavoriteMoviesBinding
import com.oder.cinema.model.Movie
import com.oder.cinema.viewmodels.FavoriteMoviesViewModel
import com.oder.cinema.viewmodels.FavoriteMoviesViewModelFactory
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
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(_binding.cinemaRecycler) {
            adapter = _moviesAdapter
            _moviesAdapter.onMoreBtnClick = { doc, view ->
                /*if (isPressed) {

                }*/
            }
            _moviesAdapter.onDetailBtnClick = { doc ->
                activity?.supportFragmentManager?.commit {
                    setReorderingAllowed(true)
                    val bundle = Bundle()
                    bundle.putString(Movie::class.java.name, Gson().toJson(doc))
                    replace<MovieDetailFragment>(
                        R.id.fragmentContainerView,
                        args = bundle
                    )
                    addToBackStack("movies")
                }
            }
            layoutManager = LinearLayoutManager(this@FavoriteMoviesFragment.context)
            addItemDecoration(HorizontalDividerItemDecoration(50))
            addItemDecoration(GroupVerticalItemDecoration(R.layout.movies_row_item, 20, 30))
        }
        bindMovies("")
    }

    private fun bindMovies(movieName: String) {
        //_binding.moviesIndicator.show()
        //_binding.infoTextView.visibility = View.GONE
        _moviesAdapter.setData(emptyList())
        val single = _viewModel.getAll()
        val disposable = single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                /*_binding.moviesIndicator.hide()
                if (it.size == 0) {
                    _binding.infoTextView.visibility = View.VISIBLE
                    _binding.infoTextView.text = "Фильмы не найдены"
                }*/
                _moviesAdapter.setData(it)
            }, {
                /*_binding.moviesIndicator.hide()
                _binding.infoTextView.visibility = View.VISIBLE
                _binding.infoTextView.text = "Ошибка"*/
                Log.e("error", it.message.toString())
            })
        //cs.add(disposable)
    }

}