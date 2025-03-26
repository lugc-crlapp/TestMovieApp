// MovieAdapter.kt
package food.olegfatass.newingandimprovingbros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieAdapter(
    private val movies: List<Movie>,
    private val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.movie_title)
        val year: TextView = view.findViewById(R.id.movie_year)
        val poster: ImageView = view.findViewById(R.id.movie_poster)
        val rating: TextView = view.findViewById(R.id.movie_rating)
        val studio: TextView = view.findViewById(R.id.movie_studio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.Title
        holder.year.text = movie.Year

        // Set default values while loading
        holder.rating.text = "Rating: Loading..."
        holder.studio.text = "Studio: Loading..."

        // Check if poster URL is valid
        if (movie.Poster.isNullOrEmpty() || movie.Poster == "N/A") {
            // Load placeholder image
            holder.poster.setImageResource(R.drawable.placeholder_image)
        } else {
            // Load actual poster
            Picasso.get()
                .load(movie.Poster)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(holder.poster)
        }

        // Fetch additional details for each movie
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fullMovie = RetrofitClient.instance.getMovieDetails(movie.imdbID)

                withContext(Dispatchers.Main) {
                    // Update movie with full details
                    if (holder.adapterPosition == position) { // Check if view is still showing this movie
                        // Update rating
                        if (!fullMovie.imdbRating.isNullOrEmpty() && fullMovie.imdbRating != "N/A") {
                            holder.rating.text = "Rating: ${fullMovie.imdbRating}"
                        } else {
                            holder.rating.text = "Rating: N/A"
                        }

                        // Update studio info
                        if (!fullMovie.Production.isNullOrEmpty() && fullMovie.Production != "N/A") {
                            holder.studio.text = "Studio: ${fullMovie.Production}"
                        } else {
                            holder.studio.text = "Studio: N/A"
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (holder.adapterPosition == position) {
                        holder.rating.text = "Rating: N/A"
                        holder.studio.text = "Studio: N/A"
                    }
                }
            }
        }

        holder.itemView.setOnClickListener { onClick(movie) }
    }

    override fun getItemCount(): Int = movies.size
}