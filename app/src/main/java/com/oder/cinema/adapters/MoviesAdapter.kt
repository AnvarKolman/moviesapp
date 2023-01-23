package com.oder.cinema.adapters

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.oder.cinema.R
import com.oder.cinema.model.Docs
import com.squareup.picasso.Picasso
import java.util.*

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private val moviesList: MutableList<Docs> = LinkedList()
    private val picasso: Picasso = Picasso.get()
    var onMoreBtnClick: ((Docs, View) -> Unit)? = null
    var onDetailBtnClick: ((Docs) -> Unit)? = null

    fun setData(newDocs: List<Docs>) {
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
        private val movieLayout: LinearLayout
        private val moreBtn: Button
        private val detailBtn: Button
        private val autoTransition = AutoTransition()

        init {
            movieName = view.findViewById(R.id.movieName)
            imageView = view.findViewById(R.id.image_view)
            year = view.findViewById(R.id.year)
            imdbRating = view.findViewById(R.id.rating_imdb)
            kinopoiskRationg = view.findViewById(R.id.rating_kinopoisk)
            description = view.findViewById(R.id.description)
            movieLayout = view.findViewById(R.id.movie_layout)
            movieLayout.setOnClickListener {
                val v = if (description.visibility == View.GONE) View.VISIBLE else View.GONE
                TransitionManager.beginDelayedTransition(movieLayout, autoTransition)
                v.also { description.visibility = it }
            }
            moreBtn = view.findViewById(R.id.favorite_btn)
            moreBtn.setOnClickListener {
                onMoreBtnClick?.invoke(moviesList[layoutPosition], it)
            }
            detailBtn = view.findViewById(R.id.detail_btn)
            detailBtn.setOnClickListener {
                onDetailBtnClick?.invoke(moviesList[layoutPosition])
            }
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
        movie.poster?.url?.let {
            picasso.load(it).into(holder.imageView);
        }
    }

    override fun getItemCount(): Int = moviesList.count()

}