package com.oder.cinema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.oder.cinema.databinding.FragmentMovieDetailBinding
import com.oder.cinema.model.Docs
import com.squareup.picasso.Picasso

class MovieDetail : Fragment(R.layout.fragment_movie_detail) {

    private lateinit var _binding: FragmentMovieDetailBinding
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments?.getString(Docs::class.java.name)
        gson.fromJson(arg, Docs::class.java)?.let {
            Picasso.get().load(it.poster?.url).into(_binding.movieImageView)
            _binding.movieNameTextView.text = it.name
            it.alternativeName?.let { alternativeName ->
                _binding.alternativeNameTextView.text = alternativeName
            }
            _binding.movieDescriptionTextView.text = it.description
        }

    }

}