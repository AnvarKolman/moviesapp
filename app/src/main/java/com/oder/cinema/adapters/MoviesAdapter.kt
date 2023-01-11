package com.oder.cinema.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.oder.cinema.R
import com.oder.cinema.model.Docs
import com.squareup.picasso.Picasso
import java.util.*

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private val moviesList: MutableList<Docs> = LinkedList()
    private val picasso: Picasso = Picasso.get()
    var onFavoriteBtnClick: ((Docs, Boolean) -> Unit)? = null
    var onDetailBtnClick: ((Docs) -> Unit)? = null

    fun setData(newDocs: List<Docs>) {
        moviesList.clear()
        moviesList.addAll(newDocs)

        notifyDataSetChanged()
    }

    inner class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView: ImageView
        private val favoriteBtn: ToggleButton
        private val detailBtn: Button

        init {
            textView = view.findViewById(R.id.movieTextView)
            imageView = view.findViewById(R.id.image_view)
            favoriteBtn = view.findViewById(R.id.favorite_btn)
            favoriteBtn.setOnClickListener {
                onFavoriteBtnClick?.invoke(moviesList[layoutPosition], it.isPressed)
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
        holder.textView.text = movie.name
        movie.poster?.url?.let {
            picasso.load(it).into(holder.imageView);
        }
    }

    override fun getItemCount(): Int = moviesList.count()

}