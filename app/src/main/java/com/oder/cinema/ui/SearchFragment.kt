package com.oder.cinema.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import com.oder.cinema.R
import com.oder.cinema.databinding.FragmentSearchBinding
import com.oder.cinema.model.Movie
import com.oder.cinema.ui.adapters.MoviesAdapter
import com.oder.cinema.ui.adapters.decorations.GroupVerticalItemDecoration
import com.oder.cinema.ui.adapters.decorations.HorizontalDividerItemDecoration
import com.oder.cinema.ui.viewmodels.SearchViewModel
import com.oder.cinema.ui.viewmodels.SearchViewModelFactory
import javax.inject.Inject

class SearchFragment : Fragment() {

    private lateinit var _moviesAdapter: MoviesAdapter
    private lateinit var _binding: FragmentSearchBinding

    private val _viewModel: SearchViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: SearchViewModelFactory.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("search")?.let {
            _binding.searchEditText.setText(it)
            _viewModel.findMovieByName(it)
        }

        _moviesAdapter = MoviesAdapter()
        with(_binding.cinemaRecycler) {
            adapter = _moviesAdapter
            _moviesAdapter.onMoreBtnClick = { doc, view ->
                val popupMenu = PopupMenu(context, view)
                popupMenu.inflate(R.menu.more_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_save -> {
                            _viewModel.saveDoc(doc)
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
            _moviesAdapter.onDetailBtnClick = { doc ->
                findNavController().navigate(
                    R.id.action_searchFragment_to_detail,
                    bundleOf(Movie::class.java.name to Gson().toJson(doc))
                )
            }
            layoutManager = LinearLayoutManager(this@SearchFragment.context)
            addItemDecoration(HorizontalDividerItemDecoration(50))
            addItemDecoration(GroupVerticalItemDecoration(R.layout.movies_row_item, 10, 20))
        }
        _binding.searchBtn.setOnClickListener {
            _viewModel.findMovieByName(_binding.searchEditText.text.toString())
        }

        _binding.searchEditText.setOnEditorActionListener { view, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    _viewModel.findMovieByName(view.text.toString())
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
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
}