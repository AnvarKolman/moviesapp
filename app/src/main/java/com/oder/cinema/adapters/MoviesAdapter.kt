package com.oder.cinema.adapters

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.oder.cinema.R
import com.oder.cinema.model.Movie
import com.squareup.picasso.Picasso
import java.util.*

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private val moviesList: MutableList<Movie> = LinkedList()
    private val picasso: Picasso = Picasso.get()
    var onMoreBtnClick: ((Movie, View) -> Unit)? = null
    var onDetailBtnClick: ((Movie) -> Unit)? = null

    fun setData(newDocs: List<Movie>) {
        moviesList.clear()
        moviesList.addAll(newDocs)

        notifyDataSetChanged()
    }

    inner class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieName: TextView
        val imageView: ImageView
        val year: TextView
        val imdbRating: TextView
        val kinopoiskRationg: TextView
        val description: TextView
        /*private val movieLayout: LinearLayout
        private val moreBtn: Button*/
        private val autoTransition = AutoTransition()

        init {
            movieName = view.findViewById(R.id.movieName)
            imageView = view.findViewById(R.id.image_view)
            year = view.findViewById(R.id.year)
            imdbRating = view.findViewById(R.id.rating_imdb)
            kinopoiskRationg = view.findViewById(R.id.rating_kinopoisk)
            description = view.findViewById(R.id.description)
            view.findViewById<LinearLayout>(R.id.movie_layout).setOnClickListener {
                /*val v = if (description.visibility == View.GONE) View.VISIBLE else View.GONE
                TransitionManager.beginDelayedTransition(movieLayout, autoTransition)
                v.also { description.visibility = it }*/
                onDetailBtnClick?.invoke(moviesList[layoutPosition])
            }
            view.findViewById<Button>(R.id.favorite_btn)?.setOnClickListener {
                onMoreBtnClick?.invoke(moviesList[layoutPosition], it)
            }
            /*view.findViewById<Button>(R.id.movie_layout).setOnClickListener {
                /*it.findNavController()
                    .navigate(
                        R.id.action_moviesFragment_to_movieDetailFragment,
                        bundleOf(Movie::class.java.name to Gson().toJson(moviesList[layoutPosition]))
                    )*/
                onDetailBtnClick?.invoke(moviesList[layoutPosition])
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movies_row_item, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.movieName.text = movie.name
        holder.year.text = movie.year.toString()
        holder.imdbRating.text = movie.rating?.imdb.toString()
        holder.kinopoiskRationg.text = movie.rating?.kp.toString()
        holder.description.text = movie.shortDescription
        if (movie.poster?.previewUrl != null) {
            picasso.load(movie.poster?.previewUrl).into(holder.imageView);
        } else {
            picasso.load(movie.poster?.url).into(holder.imageView);
        }
    }

    override fun getItemCount(): Int = moviesList.count()

}