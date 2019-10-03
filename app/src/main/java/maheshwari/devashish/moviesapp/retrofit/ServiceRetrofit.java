package maheshwari.devashish.moviesapp.retrofit;

import maheshwari.devashish.moviesapp.response.MovieResponse;
import maheshwari.devashish.moviesapp.response.ReviewsResponse;
import maheshwari.devashish.moviesapp.response.TrailerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceRetrofit {

    String API_KEY = "03d6ddd8636eee3ef8f82bc006bb27f6";
    String LANGUAGE = "en-US";

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int pageNumber);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int pageNumber);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getTrailer(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("movie_id") int reviewId, @Query("api_key") String apiKey);
}
