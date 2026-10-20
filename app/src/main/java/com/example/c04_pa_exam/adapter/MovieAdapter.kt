package com.example.pa_mock.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.c04_pa_exam.data.model.Movie
import com.example.c04_pa_exam.databinding.ItemCardBinding

/** Shows each Movie as a card in the Show All list. */
class MovieAdapter(
    private var movies: List<Movie>,
    //click on the detail page
    private val onItemClick: (Movie) -> Unit
) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.tvTitle.text = movie.title
        holder.binding.tvRating.text = movie.rating
        holder.binding.tvDirector.text = movie.director
        holder.binding.tvReleaseDate.text = movie.release_date
        // Tell the fragment which Movie was tapped
        holder.binding.root.setOnClickListener { onItemClick(movie) }
    }
//    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
//        holder.binding.tvMovie.text = Movies[position].Movie
//    }

    /** Replaces the displayed list (used after filtering or sorting). */
    fun updateList(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun getItemCount() = movies.size
}