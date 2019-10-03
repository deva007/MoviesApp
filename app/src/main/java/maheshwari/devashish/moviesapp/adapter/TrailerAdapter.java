package maheshwari.devashish.moviesapp.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import maheshwari.devashish.moviesapp.model.Trailer;
import maheshwari.devashish.moviesapp.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyTrailerViewHolder> {

    private Context myContext;
    private List<Trailer> trailers = new ArrayList<>();

    public TrailerAdapter(Context myContext) {
        this.myContext = myContext;
    }

    public void updateTrailerDataset(List<Trailer> newTrailers) {
        trailers = newTrailers;
        notifyDataSetChanged();
    }
    @Override
    public TrailerAdapter.MyTrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyTrailerViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_cardview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.MyTrailerViewHolder viewHolder, int i) {
        Picasso.with(myContext).load(trailers.get(i).getKey()).placeholder(R.drawable.ic_android_black_24dp).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public static void watchYoutubeVideo(Context myContext, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(id));
        try {
            myContext.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            myContext.startActivity(webIntent);
        }
    }

    public class MyTrailerViewHolder extends  RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyTrailerViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.trailer_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        String key = trailers.get(position).getKeyId();
                        watchYoutubeVideo(myContext ,key);
                    }
                }
            });

        }
    }
}
