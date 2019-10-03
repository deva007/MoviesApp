package maheshwari.devashish.moviesapp.mvvm;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

import maheshwari.devashish.moviesapp.model.Movie;
import maheshwari.devashish.moviesapp.response.MovieResponse;
import maheshwari.devashish.moviesapp.retrofit.ClientRetrofit;
import maheshwari.devashish.moviesapp.retrofit.ServiceRetrofit;
import maheshwari.devashish.moviesapp.room.MovieDao;
import maheshwari.devashish.moviesapp.room.MovieDatabase;
import retrofit2.Call;

import static maheshwari.devashish.moviesapp.retrofit.ServiceRetrofit.API_KEY;
import static maheshwari.devashish.moviesapp.retrofit.ServiceRetrofit.LANGUAGE;

public class MoviesRepository {

    private MovieDao movieDao;
    private ServiceRetrofit apiService = ClientRetrofit.getRetrofit().create(ServiceRetrofit.class);

    public MoviesRepository(Application application) {
        movieDao = MovieDatabase.getInstance(application).MovieDao();
    }

    public List<Movie> getFavoriteMovies() {
        try {
            return new getFavoriteMovies(movieDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Call<MovieResponse> getPopularMovies(int page) {
        return apiService.getPopularMovies(API_KEY, LANGUAGE, page);
    }

    public Call<MovieResponse> getTopRatedMovies(int page) {
        return apiService.getTopRatedMovies(API_KEY, LANGUAGE, page);
    }

    public void insert(Movie movie) {
        new InsertAsyncTask(movieDao).execute(movie);
    }

    public void delete(int movieId) {
        new deleteAsyncTask(movieDao).execute(movieId);
    }

    public Movie getMovieById(int movieId) {
        Movie movie = null;
        try {
            movie = new getMovieByIdAsyncTask(movieDao).execute(movieId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return movie;
    }

    private static class getMovieByIdAsyncTask extends AsyncTask<Integer, Void, Movie> {
        private MovieDao mAsyncTaskDao;

        getMovieByIdAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override

        protected Movie doInBackground(Integer... integers) {
            Movie movie = mAsyncTaskDao.getMovieById(integers[0]);
            return movie;
        }

    }

    private static class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {

        private MovieDao mAsyncTaskDao;

        deleteAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.deleteMovie(params[0]);
            return null;
        }

    }

    private static class InsertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        InsertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.insertMovie(params[0]);
            return null;
        }

    }

    private static class getFavoriteMovies extends AsyncTask<Void, Void, List<Movie>> {
        private MovieDao myAsyncTaskDao;

        getFavoriteMovies(MovieDao dao) {
            myAsyncTaskDao = dao;
        }

        @Override
        protected List<Movie> doInBackground(final Void... params) {
            List<Movie> movies = myAsyncTaskDao.getFavoriteMovies();
            Log.d("SIZE", String.valueOf(movies.size()));
            return movies;
        }
    }

}
