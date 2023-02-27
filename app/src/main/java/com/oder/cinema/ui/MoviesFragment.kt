package com.oder.cinema.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import com.oder.cinema.R
import com.oder.cinema.ui.adapters.MoviesAdapter
import com.oder.cinema.ui.adapters.decorations.GroupVerticalItemDecoration
import com.oder.cinema.ui.adapters.decorations.HorizontalDividerItemDecoration
import com.oder.cinema.ui.appComponent
import com.oder.cinema.databinding.MoviesFragmentBinding
import com.oder.cinema.model.Movie
import com.oder.cinema.ui.viewmodels.MoviesViewModel
import com.oder.cinema.ui.viewmodels.MoviesViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class MoviesFragment : Fragment(R.layout.movies_fragment) {


    private lateinit var _moviesAdapter: MoviesAdapter
    private lateinit var _binding: MoviesFragmentBinding
    private lateinit var requestManager: RequestManager

    private val _viewModel: MoviesViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: MoviesViewModelFactory.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoviesFragmentBinding.inflate(inflater, container, false)
        requestManager = Glide.with(requireContext())
        _moviesAdapter = MoviesAdapter(requestManager)

        bindRecycler()

        _binding.searchBtn.setOnClickListener {
            goToSearchFragment(_binding.searchEditText.text.toString())
        }
        _binding.searchEditText.setOnEditorActionListener { view, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    goToSearchFragment(view.text.toString())
                    true
                }
                else -> false
            }
        }
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.movies.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                _binding.infoTextView.visibility = View.VISIBLE
            } else {
                _binding.infoTextView.visibility = View.GONE
                _moviesAdapter.setData(it)
            }
        }
        _viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                _binding.moviesIndicator.hide()
            } else {
                _binding.moviesIndicator.show()
            }
        }
    }

    private fun goToSearchFragment(searchText: String) {
        if (searchText.isNotEmpty()) {
            findNavController().navigate(
                R.id.action_moviesFragment_to_searchFragment,
                bundleOf("search" to searchText)
            )
        }
    }

    private fun bindRecycler() {
        with(_binding.cinemaRecycler) {
            adapter = _moviesAdapter
            _moviesAdapter.onMoreBtnClick = { movie, view ->
                val popupMenu = PopupMenu(context, view)
                popupMenu.inflate(R.menu.more_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_save -> {
                            _viewModel.saveDoc(movie)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.menu_share -> {
                            Toast.makeText(context, "В разработке", Toast.LENGTH_SHORT).show()
                            return@setOnMenuItemClickListener true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }
                popupMenu.show()
            }
            _moviesAdapter.onDetailBtnClick = { movie ->
                findNavController().navigate(
                    R.id.action_moviesFragment_to_detail,
                    bundleOf(Movie::class.java.name to Gson().toJson(movie))
                )
            }
            layoutManager = LinearLayoutManager(this@MoviesFragment.context)
            addItemDecoration(HorizontalDividerItemDecoration(50))
            addItemDecoration(GroupVerticalItemDecoration(R.layout.movies_row_item, 6, 14))
        }
    }

}
