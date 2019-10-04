package maheshwari.devashish.moviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import maheshwari.devashish.moviesapp.EndlessRecyclerViewScrollListener;
import maheshwari.devashish.moviesapp.R;
import maheshwari.devashish.moviesapp.model.Movie;
import maheshwari.devashish.moviesapp.mvvm.MoviesViewModel;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesAppAdapter adapter;
    public static MoviesViewModel moviesViewModel;
    private GridLayoutManager gridLayoutManager;
    private boolean mTwoPane;

    private final static String POPULAR_MOVIES = "PopularMovies";
    private final static String TOP_RATED_MOVIES = "Top Rated Movies";
    private final static String FAVORITE_MOVIES = "Favorite Movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Stetho.initializeWithDefaults(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTwoPane = findViewById(R.id.movie_detail_fragment) != null;
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        recyclerView = findViewById(R.id.item_list);
        adapter = new MoviesAppAdapter(this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(100);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                moviesViewModel.getMovies(page);
            }
        });

        moviesViewModel.popularMoviesLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                setPopularMovies(movies);
            }
        });

        moviesViewModel.topRatedMoviesLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                setTopRatedMovies(movies);
            }
        });

        moviesViewModel.favoriteMoviesLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                setFavoriteMovies(movies);
            }
        });

        moviesViewModel.selectedMovies.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String string) {
                switch (string) {
                    case POPULAR_MOVIES:
                        setPopularMovies(null);
                        break;
                    case TOP_RATED_MOVIES:
                        setTopRatedMovies(null);
                        break;
                    case FAVORITE_MOVIES:
                        setFavoriteMovies(null);
                        break;
                }
            }
        });

        init();
    }

    public class MoviesAppAdapter extends RecyclerView.Adapter<MoviesAppAdapter.MyViewHolder> {

        Context mContext;
        List<Movie> movieList;

        private static final String baseImageUrl = "https://image.tmdb.org/t/p/w342";

        public MoviesAppAdapter(Context context) {
            mContext = context;
            movieList = new ArrayList<>();
        }

        public void updateDataSet(List<Movie> newMovies) {
            if (newMovies != null) {
                movieList = newMovies;
                notifyDataSetChanged();
            }
        }

        @Override
        public MoviesAppAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_movie, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(final MoviesAppAdapter.MyViewHolder viewHolder, int i) {
            viewHolder.title.setText(movieList.get(i).getOriginalTitle());
            Log.d("IMAGE_URL", baseImageUrl+movieList.get(i).getPosterPath());
            Picasso.with(mContext).load(baseImageUrl+movieList.get(i).getPosterPath()).into(viewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView title;
            ImageView imageView;

            MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.movie_name_id);
                imageView = view.findViewById(R.id.image_id);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
               //todo
                        Bundle arguments = new Bundle();
                        arguments.putString("original_title", movieList.get(position).getOriginalTitle());
                        arguments.putString("release_date", movieList.get(position).getReleaseDate());
                        arguments.putString("synopsis_movie", movieList.get(position).getOverview());
                        arguments.putDouble("average_rating", movieList.get(position).getVoteAverage());
                        arguments.putString("backdrop_path", movieList.get(position).getBackdropPath());
                        arguments.putInt("movie_id", movieList.get(position).getId());
                        arguments.putParcelable("MOVIE", movieList.get(position));
                        if (mTwoPane) {
                            MovieDetailFragment fragment = new MovieDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.movie_detail_fragment, fragment)
                                    .commit();
                        } else {
                            Intent intent = new Intent(mContext, MovieDetailActivity.class);
                            intent.putExtra("ARGUMENTS", arguments);
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_id: {
                moviesViewModel.switchPopularMovies();
                return true;
            }
            case R.id.top_rated_id: {
                moviesViewModel.switchTopRatedMovies();
                return true;
            }
            case R.id.favorite_id: {
                moviesViewModel.switchFavoriteMovies();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void init() {
        setToolbarTitle(POPULAR_MOVIES);
        moviesViewModel.switchPopularMovies();
        moviesViewModel.getPopularMovies(1);
        moviesViewModel.getTopRatedMovies(1);
        moviesViewModel.getFavoriteMovies();
    }

    private void setPopularMovies(List<Movie> movies) {
        if (moviesViewModel.isPopularMoviesSelected()) {
            setToolbarTitle(POPULAR_MOVIES);

            if (movies != null) {
                adapter.updateDataSet(movies);
            } else {
                adapter.updateDataSet(moviesViewModel.popularMoviesLiveData.getValue());
            }
        }
    }

    private void setTopRatedMovies(List<Movie> movies) {
        if (moviesViewModel.isTopRatedMoviesSelected()) {
            setToolbarTitle(TOP_RATED_MOVIES);

            if (movies != null) {
                adapter.updateDataSet(movies);
            } else {
                adapter.updateDataSet(moviesViewModel.topRatedMoviesLiveData.getValue());
            }
        }
    }

    private void setFavoriteMovies(List<Movie> movies) {
        if (moviesViewModel.isFavoriteMoviesSelected()) {
            setToolbarTitle(FAVORITE_MOVIES);

            if (movies != null) {
                adapter.updateDataSet(movies);
            } else {
                adapter.updateDataSet(moviesViewModel.favoriteMoviesLiveData.getValue());
            }
        }
    }

}
