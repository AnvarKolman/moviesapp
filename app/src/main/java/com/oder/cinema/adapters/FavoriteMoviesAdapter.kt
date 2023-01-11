package com.oder.cinema.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.oder.cinema.R
import com.oder.cinema.model.Docs
import com.squareup.picasso.Picasso
import java.util.*

class FavoriteMoviesAdapter : RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMoviesViewHolder>() {

    private val moviesList: MutableList<Docs> = LinkedList()
    private val picasso: Picasso = Picasso.get()
    var onItemClick: ((Docs, Boolean) -> Unit)? = null

    fun setData(newDocs: List<Docs>) {
        moviesList.clear()
        moviesList.addAll(newDocs)

        notifyDataSetChanged()
    }

    inner class FavoriteMoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*val textView: TextView
        val imageView: ImageView
        val favoriteBtn: ToggleButton*/

        init {
            /*textView = view.findViewById(R.id.movieTextView)
            imageView = view.findViewById(R.id.image_view)
            favoriteBtn = view.findViewById(R.id.favorite_btn)
            favoriteBtn.setOnClickListener {
                onItemClick?.invoke(moviesList[layoutPosition], it.isPressed)
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movies_row_item, parent, false)
        return FavoriteMoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteMoviesViewHolder, position: Int) {
        val movie = moviesList[position]
        //holder.textView.text = movie.name
        /*movie.poster?.url?.let {
            picasso.load(it).into(holder.imageView);
        }*/
    }

    override fun getItemCount(): Int = moviesList.count()

}