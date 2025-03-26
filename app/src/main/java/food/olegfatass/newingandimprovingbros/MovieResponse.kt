package food.olegfatass.newingandimprovingbros

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("Title") val Title: String,
    @SerializedName("Year") val Year: String,
    @SerializedName("Poster") val Poster: String,
    @SerializedName("Plot") val Plot: String = "",
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Rated") val Rated: String = "N/A",
    @SerializedName("Runtime") val Runtime: String = "N/A",
    @SerializedName("Genre") val Genre: String = "N/A",
    @SerializedName("Director") val Director: String = "N/A",
    @SerializedName("Actors") val Actors: String = "N/A",
    @SerializedName("Production") val Production: String = "N/A",
    @SerializedName("imdbRating") val imdbRating: String = "N/A"
)