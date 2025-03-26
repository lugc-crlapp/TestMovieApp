package food.olegfatass.newingandimprovingbros

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    @GET("/")
    fun searchMovies(
        @Query("apikey") apiKey: String = "9dcd9a3d",
        @Query("s") query: String
    ): Call<MovieResponse>
}

data class MovieResponse(val Search: List<Movie>)
