package food.olegfatass.newingandimprovingbros

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import food.olegfatass.newingandimprovingbros.databinding.ActivityMovieDetailsBinding
import kotlinx.coroutines.launch

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up back button click handler
        binding.backButton.setOnClickListener {
            finish() // This closes the current activity and returns to previous screen
        }

        val title = intent.getStringExtra("title") ?: ""
        val year = intent.getStringExtra("year") ?: ""
        val imdbId = intent.getStringExtra("imdbID") ?: ""
        val poster = intent.getStringExtra("poster")

        // Display initial data
        binding.titleing.text = title
        binding.yearing.text = year
        binding.ploting.text = "Loading plot..."
        binding.genreValue.text = ""
        binding.directorValue.text = ""
        binding.actorsValue.text = ""
        binding.ratingValue.text = ""
        binding.runtimeValue.text = ""
        binding.studioValue.text = ""

        // Load poster
        if (!poster.isNullOrEmpty() && poster != "N/A") {
            try {
                Picasso.get()
                    .load(poster)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(binding.postering)
            } catch (e: Exception) {
                Log.e("MovieDetails", "Failed to load image: ${e.message}")
                binding.postering.setImageResource(R.drawable.placeholder_image)
            }
        } else {
            // Load placeholder for missing posters
            binding.postering.setImageResource(R.drawable.placeholder_image)
        }

        // Fetch complete movie details
        fetchMovieDetails(imdbId)
    }

    private fun fetchMovieDetails(imdbId: String) {
        if (imdbId.isEmpty()) {
            binding.ploting.text = "Unable to load plot information"
            return
        }

        lifecycleScope.launch {
            try {
                val movie = RetrofitClient.instance.getMovieDetails(imdbId)

                // Update UI with movie details
                binding.titleing.text = movie.Title
                binding.yearing.text = movie.Year
                binding.ploting.text = movie.Plot.takeIf { it.isNotEmpty() } ?: "Plot not available"

                // Additional details
                binding.genreValue.text = movie.Genre.takeIf { it != "N/A" } ?: "Not available"
                binding.directorValue.text = movie.Director.takeIf { it != "N/A" } ?: "Not available"
                binding.actorsValue.text = movie.Actors.takeIf { it != "N/A" } ?: "Not available"
                binding.ratingValue.text = movie.imdbRating.takeIf { it != "N/A" } ?: "Not rated"
                binding.runtimeValue.text = movie.Runtime.takeIf { it != "N/A" } ?: "Unknown"
                binding.studioValue.text = movie.Production.takeIf { it != "N/A" } ?: "Unknown"

                // Make all detail sections visible
                binding.detailsContainer.visibility = View.VISIBLE

            } catch (e: Exception) {
                Log.e("MovieDetails", "Failed to fetch movie details: ${e.message}")
                binding.ploting.text = "Error loading plot: ${e.message}"
            }
        }
    }
}