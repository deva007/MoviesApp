package maheshwari.devashish.moviesapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import maheshwari.devashish.moviesapp.R;
import maheshwari.devashish.moviesapp.adapter.ReviewsAdapter;
import maheshwari.devashish.moviesapp.adapter.TrailerAdapter;
import maheshwari.devashish.moviesapp.model.Review;
import maheshwari.devashish.moviesapp.model.Trailer;
import maheshwari.devashish.moviesapp.mvvm.MovieDetailViewModel;


public class MovieDetailFragment extends Fragment {

   private TextView movieTitle;
    private TextView releaseDate;
    private RatingBar voteAverage;
    private TextView plotSynopsis;
    public RecyclerView recyclerViewTrailer;
    public TrailerAdapter trailerAdapter;
    public MovieDetailViewModel movieDetailViewModel;

    public ReviewsAdapter reviewsAdapter;
    public RecyclerView recyclerViewReview;
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String SYNOPSIS_MOVIE = "synopsis_movie";
    public static final String RELEASE_DATE = "release_date";

    public static final String MOVIE_ID = "movie_id";
    public static final String AVERAGE_RATING = "average_rating";
    public static final String BACKIMAGE_PATH = "backdrop_path";
  //todo
    private int flag  = 404;

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        movieTitle =  rootView.findViewById(R.id.title_movie_fragment_id);
        releaseDate = rootView.findViewById(R.id.date_release_fragment_id);
        plotSynopsis = rootView.findViewById(R.id.synopsis_text_fragment_id);
        voteAverage = rootView.findViewById(R.id.average_rating_fragment_id);

        if(getArguments() != null) {
            String movieName  = getArguments().getString(ORIGINAL_TITLE);
            String releaseDateMovie  = getArguments().getString(RELEASE_DATE);
            String synopsis  = getArguments().getString(SYNOPSIS_MOVIE);
            int movieID = getArguments().getInt(MOVIE_ID);
            flag = movieID;
       //todo
            double rating = getArguments().getDouble(AVERAGE_RATING)/2;

            movieTitle.setText(movieName);
            releaseDate.setText(releaseDateMovie);
            voteAverage.setRating((float) rating);
            plotSynopsis.setText(synopsis);
        } else {
        Toast.makeText(getContext(), "No API data", Toast.LENGTH_SHORT).show();
        }

        recyclerViewTrailer = rootView.findViewById(R.id.trailers_recycle_fragment_id);
        trailerAdapter = new TrailerAdapter(getContext());
        recyclerViewTrailer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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

        movieDetailViewModel.getTrailers(flag);

        recyclerViewReview = rootView.findViewById(R.id.reviews_recycle_fragment_id);
        reviewsAdapter = new ReviewsAdapter(getContext());
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewReview.setAdapter(reviewsAdapter);
        movieDetailViewModel.reviewLiveData.observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                if (reviews != null && !reviews.isEmpty()) {
                    reviewsAdapter.updateReviewDataset(reviews);
                }
            }
        });
        movieDetailViewModel.getReviews(flag);

    }
}
