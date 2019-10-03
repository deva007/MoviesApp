package maheshwari.devashish.moviesapp.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import maheshwari.devashish.moviesapp.model.Movie;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Query("SELECT * FROM movie")
    List<Movie> getFavoriteMovies();

    @Query("SELECT * FROM movie where id = :movieId limit 1")
    Movie getMovieById(int movieId);

    @Query("DELETE FROM movie where id = :movieId")
    void deleteMovie(int movieId);
}
