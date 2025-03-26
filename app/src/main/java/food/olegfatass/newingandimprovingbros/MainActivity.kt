// MainActivity.kt
package food.olegfatass.newingandimprovingbros

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var searchBox: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter
    private var movieList = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBox = findViewById(R.id.search_box)
        searchButton = findViewById(R.id.search_button)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(movieList) { movie ->
            val intent = Intent(this, MovieDetailsActivity::class.java).apply {
                putExtra("title", movie.Title)
                putExtra("year", movie.Year)
                putExtra("poster", movie.Poster)
                putExtra("imdbID", movie.imdbID)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Handle Enter key press
        searchBox.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                performSearch()
                true
            } else {
                false
            }
        }

        // Set click listener for search button
        searchButton.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        val query = searchBox.text.toString().trim()
        if (query.length >= 3) {
            fetchMovies(query)
        } else {
            Toast.makeText(this, "Please enter at least 3 characters", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMovies(query: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.searchMovies(query)
                movieList.clear()
                movieList.addAll(response.Search)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error fetching movies: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error fetching movies: ${e.message}")
            }
        }
    }
}