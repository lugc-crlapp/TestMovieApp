package food.olegfatass.newingandimprovingbros

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Define API interface
interface MovieApiService {
    @GET("?type=movie")
    suspend fun searchMovies(@Query("s") query: String, @Query("apikey") apiKey: String = "9dcd9a3d"): MovieResponse

    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full",
        @Query("apikey") apiKey: String = "9dcd9a3d"
    ): Movie
}

// Singleton Retrofit Client
object RetrofitClient {
    private const val BASE_URL = "https://www.omdbapi.com/"

    val instance: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}