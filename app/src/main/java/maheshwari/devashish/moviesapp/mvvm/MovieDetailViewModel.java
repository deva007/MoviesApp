package maheshwari.devashish.moviesapp.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import maheshwari.devashish.moviesapp.model.Movie;
import maheshwari.devashish.moviesapp.model.Review;
import maheshwari.devashish.moviesapp.model.Trailer;
import maheshwari.devashish.moviesapp.response.ReviewsResponse;
import maheshwari.devashish.moviesapp.response.TrailerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailViewModel extends AndroidViewModel {

    public MutableLiveData<List<Trailer>> trailerLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Review>> reviewLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Movie>> floatingLiveData = new MutableLiveData<>();
    public MovieDetailRepository movieDetailRepository;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDetailRepository = new MovieDetailRepository(application);
    }

    public void getTrailers(int trailerId) {
        movieDetailRepository.getTrailer(trailerId).enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.body() != null) {
                    List<Trailer> tempMovies = trailerLiveData.getValue();
                    if (tempMovies != null) {
                        tempMovies.addAll(response.body().getResults());
                        trailerLiveData.postValue(tempMovies);
                    } else {
                        trailerLiveData.postValue(response.body().getResults());
                    }
                }

            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {

            }
        });
    }

    public void getReviews(int reviewId) {
        movieDetailRepository.getReview(reviewId).enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                if (response.body() != null) {
                    List<Review> tempMovies = reviewLiveData.getValue();
                    if (tempMovies != null) {
                        tempMovies.addAll(response.body().getResults());
                        reviewLiveData.postValue(tempMovies);
                    } else {
                        reviewLiveData.postValue(response.body().getResults());
                    }
                }

            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {

            }
        });
    }

}
