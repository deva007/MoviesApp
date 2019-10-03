package maheshwari.devashish.moviesapp.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Objects;

import maheshwari.devashish.moviesapp.model.Movie;
import maheshwari.devashish.moviesapp.response.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesViewModel extends AndroidViewModel {

    private final static String POPULAR_MOVIES = "Popular Movies";
    private final static String TOP_RATED_MOVIES = "Top Rated Movies";
    private final static String FAVORITE_MOVIES = "Favorite Movies";

    public MutableLiveData<String> selectedMovies = new MutableLiveData<>();

    public MutableLiveData<List<Movie>> popularMoviesLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Movie>> topRatedMoviesLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Movie>> favoriteMoviesLiveData = new MutableLiveData<>();

    public MoviesRepository moviesRepository;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        moviesRepository = new MoviesRepository(application);
    }

    public void getTopRatedMovies(int page) {
        moviesRepository.getTopRatedMovies(page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {
                    List<Movie> tempMovies = topRatedMoviesLiveData.getValue();
                    if (tempMovies != null) {
                        tempMovies.addAll(response.body().getResults());
                        topRatedMoviesLiveData.postValue(tempMovies);
                    } else {
                        topRatedMoviesLiveData.postValue(response.body().getResults());
                    }
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    public void getPopularMovies(int page) {
        moviesRepository.getPopularMovies(page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {
                    List<Movie> tempMovies = popularMoviesLiveData.getValue();
                    if (tempMovies != null) {
                        tempMovies.addAll(response.body().getResults());
                        popularMoviesLiveData.postValue(tempMovies);
                    } else {
                        popularMoviesLiveData.postValue(response.body().getResults());
                    }
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    public void switchPopularMovies() {
        selectedMovies.postValue(POPULAR_MOVIES);
    }

    public void switchTopRatedMovies() {
        selectedMovies.postValue(TOP_RATED_MOVIES);
    }

    public void switchFavoriteMovies() {
        selectedMovies.postValue(FAVORITE_MOVIES);
    }

    public boolean isPopularMoviesSelected() {
        return Objects.equals(selectedMovies.getValue(), POPULAR_MOVIES);
    }

    public boolean isTopRatedMoviesSelected() {
        return Objects.equals(selectedMovies.getValue(), TOP_RATED_MOVIES);
    }

    public boolean isFavoriteMoviesSelected() {
        return Objects.equals(selectedMovies.getValue(), FAVORITE_MOVIES);
    }

    public void getMovies(int page) {
        if(isPopularMoviesSelected()){
            getPopularMovies(page);
        } else if (isTopRatedMoviesSelected()) {
            getTopRatedMovies(page);
        }
    }

    public void getFavoriteMovies() {
        favoriteMoviesLiveData.postValue(moviesRepository.getFavoriteMovies());
    }

    public void inserts(Movie movie) {
        moviesRepository.insert(movie);
    }

    public void deletes(int movieId) {
        moviesRepository.delete(movieId);
    }

    public Movie getMovieId(int id) {
        moviesRepository.getMovieById(id);
        return null;
    }
}


