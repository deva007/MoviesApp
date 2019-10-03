package maheshwari.devashish.moviesapp.mvvm;

import android.app.Application;

import maheshwari.devashish.moviesapp.response.ReviewsResponse;
import maheshwari.devashish.moviesapp.response.TrailerResponse;
import maheshwari.devashish.moviesapp.retrofit.ClientRetrofit;
import maheshwari.devashish.moviesapp.retrofit.ServiceRetrofit;
import maheshwari.devashish.moviesapp.room.MovieDao;
import maheshwari.devashish.moviesapp.room.MovieDatabase;
import retrofit2.Call;

import static maheshwari.devashish.moviesapp.retrofit.ServiceRetrofit.API_KEY;

public class MovieDetailRepository {
    MovieDao movieDao;

    private ServiceRetrofit apiService = ClientRetrofit.getRetrofit().create(ServiceRetrofit.class);

    public MovieDetailRepository(Application application) {
        movieDao = MovieDatabase.getInstance(application).MovieDao();

    }

    public Call<TrailerResponse> getTrailer(int movieId) {
       return apiService.getTrailer(movieId, API_KEY);
    }

    public Call<ReviewsResponse> getReview(int reviewId) {
       return apiService.getReviews(reviewId, API_KEY);
    }

}
