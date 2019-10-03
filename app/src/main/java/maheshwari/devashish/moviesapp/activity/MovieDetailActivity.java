package maheshwari.devashish.moviesapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import maheshwari.devashish.moviesapp.R;
import maheshwari.devashish.moviesapp.adapter.ReviewsAdapter;
import maheshwari.devashish.moviesapp.adapter.TrailerAdapter;
import maheshwari.devashish.moviesapp.model.Movie;
import maheshwari.devashish.moviesapp.model.Review;
import maheshwari.devashish.moviesapp.model.Trailer;
import maheshwari.devashish.moviesapp.mvvm.MovieDetailViewModel;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MainActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {

    TextView movieTitle;
    TextView releaseDate;
    RatingBar voteAverage;
    TextView plotSynopsis;
    ImageView moviePoster;
    public RecyclerView recyclerViewTrailer;
    public TrailerAdapter trailerAdapter;
    public ReviewsAdapter reviewsAdapter;
    public MovieDetailViewModel movieDetailViewModel;
    public RecyclerView recyclerViewReview;
    private static final String baseImageUrl = "https://image.tmdb.org/t/p/w342";

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        moviePoster = findViewById(R.id.poster_movie);
        movieTitle = findViewById(R.id.title_movie_id);
        releaseDate = findViewById(R.id.date_release_id);
        plotSynopsis = findViewById(R.id.synopsis_text_id);
        voteAverage = findViewById(R.id.average_rating_id);

        if (getIntent().hasExtra("ARGUMENTS")) {
            Bundle bundle = getIntent().getBundleExtra("ARGUMENTS");
            movie = bundle.getParcelable("MOVIE");

            Picasso.with(this).load(baseImageUrl + movie.getBackdropPath()).placeholder(R.drawable.loading).into(moviePoster);
            movieTitle.setText(movie.getTitle());
            releaseDate.setText(movie.getReleaseDate());
            voteAverage.setRating((float) movie.getVoteAverage());
            plotSynopsis.setText(movie.getOverview());
        } else {
            //todo
            Toast.makeText(this, "No API data", Toast.LENGTH_SHORT).show();
        }

        recyclerViewTrailer = findViewById(R.id.trailers_recycle_id);
        trailerAdapter = new TrailerAdapter(this);
        recyclerViewTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTrailer.setAdapter(trailerAdapter);
        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.trailerLiveData.observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                if (trailers != null && !trailers.isEmpty()) {
                    trailerAdapter.updateTrailerDataset(trailers);
                }
            }
        });

        movieDetailViewModel.getTrailers(movie.getId());

        recyclerViewReview = findViewById(R.id.reviews_recycle_id);
        reviewsAdapter = new ReviewsAdapter(this);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewReview.setAdapter(reviewsAdapter);
        movieDetailViewModel.reviewLiveData.observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                if (reviews != null && !reviews.isEmpty()) {
                    reviewsAdapter.updateReviewDataset(reviews);
                }
            }
        });
        movieDetailViewModel.getReviews(movie.getId());

        final FloatingActionButton fab = findViewById(R.id.fab);
        if (savedStatus(movie.getId())) {
            fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savedStatus(movie.getId())) {
                    MainActivity.moviesViewModel.deletes(movie.getId());
                    fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));
                } else {
                    MainActivity.moviesViewModel.inserts(movie);
                    fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp));

                }
            }
        });

    }

    @Override
    //todo
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key", 1);
    }

    //todo
    Boolean savedStatus(int id) {
        Movie movie = MainActivity.moviesViewModel.getMovieId(id);
        if (movie == null) {
            return false;
        } else {
            return true;
        }
    }
}

