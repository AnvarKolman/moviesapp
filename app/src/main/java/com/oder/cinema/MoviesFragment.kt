package com.oder.cinema

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.oder.cinema.adapters.MoviesAdapter
import com.oder.cinema.adapters.decorations.GroupVerticalItemDecoration
import com.oder.cinema.adapters.decorations.HorizontalDividerItemDecoration
import com.oder.cinema.databinding.MoviesFragmentBinding
import com.oder.cinema.model.Docs
import com.oder.cinema.viewmodels.MoviesViewModel
import com.oder.cinema.viewmodels.MoviesViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class MoviesFragment : Fragment(R.layout.movies_fragment) {


    private val _moviesAdapter = MoviesAdapter()
    private lateinit var _binding: MoviesFragmentBinding


    private val cs: CompositeDisposable = CompositeDisposable()

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
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { // Todo допилить
            if (!isOnline(it)) {
                Toast.makeText(it, "Отсутвует подключение к сети интернет", Toast.LENGTH_LONG)
                    .show()
            }
        }
        with(_binding.cinemaRecycler) {
            adapter = _moviesAdapter
            _moviesAdapter.onMoreBtnClick = { doc, view ->
                val popupMenu = PopupMenu(context, view)
                popupMenu.inflate(R.menu.more_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_save -> {
                            _viewModel.saveDoc(doc).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {  }
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
                activity?.supportFragmentManager?.commit {
                    setReorderingAllowed(true)
                    val bundle = Bundle()
                    bundle.putString(Docs::class.java.name, Gson().toJson(doc))
                    replace<MovieDetailFragment>(
                        R.id.fragmentContainerView,
                        args = bundle
                    )
                    addToBackStack("movies")
                }
            }
            layoutManager = LinearLayoutManager(this@MoviesFragment.context)
            addItemDecoration(HorizontalDividerItemDecoration(50))
            addItemDecoration(GroupVerticalItemDecoration(R.layout.movies_row_item, 10, 20))
        }
        _binding.searchBtn.setOnClickListener {
            val searchValue = _binding.searchEditText.text.toString()
            if (searchValue.isNotEmpty()) {
                bindMovies(searchValue)
            }
        }
        /*_binding.moviesSearchSpinner.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.search_items_array,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        }*/
    }

    private fun bindMovies(movieName: String) {
        _binding.moviesIndicator.show()
        _binding.infoTextView.visibility = View.GONE
        _moviesAdapter.setData(emptyList())
        val single = _viewModel.findByName(movieName)
        val disposable = single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.docs }
            .subscribe({
                _binding.moviesIndicator.hide()
                if (it.size == 0) {
                    _binding.infoTextView.visibility = View.VISIBLE
                    _binding.infoTextView.text = "Фильмы не найдены"
                }
                _moviesAdapter.setData(it)
            }, {
                _binding.moviesIndicator.hide()
                _binding.infoTextView.visibility = View.VISIBLE
                _binding.infoTextView.text = "Ошибка"
                Log.e("error", it.message.toString())
            })
        cs.add(disposable)
    }

    override fun onDestroy() {
        if (!cs.isDisposed) {
            cs.dispose()
        }
        super.onDestroy()
    }
}
