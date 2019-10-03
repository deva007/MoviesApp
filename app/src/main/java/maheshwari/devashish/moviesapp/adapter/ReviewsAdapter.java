package maheshwari.devashish.moviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import maheshwari.devashish.moviesapp.model.Review;
import maheshwari.devashish.moviesapp.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyReviewsViewHolder> {

    private Context myContext;

    private List<Review> reviews = new ArrayList<>();

    public ReviewsAdapter(Context myContext) {
        this.myContext = myContext;
    }

    public void updateReviewDataset(List<Review> newReviews) {
        reviews = newReviews;
        notifyDataSetChanged();
    }
    @Override
    public ReviewsAdapter.MyReviewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyReviewsViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reviews_cardview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final ReviewsAdapter.MyReviewsViewHolder viewHolder, int i) {
        viewHolder.reviewText.setText(reviews.get(i).getContent());

    }
    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class MyReviewsViewHolder extends  RecyclerView.ViewHolder {

        public TextView reviewText;
        public MyReviewsViewHolder(View view) {
            super(view);
            reviewText = view.findViewById(R.id.text_review_id);
        }
    }
}
