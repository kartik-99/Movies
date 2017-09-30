package com.example.kartik.movies;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kartik on 28-09-2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private ArrayList<Review> reviews;
    Context context;
    private LayoutInflater inflater;

    public ReviewsAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ReviewsAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  inflater.inflate(R.layout.card_review, parent, false);
        return new ReviewsAdapter.ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewHolder holder, int position) {

        final Review review = reviews.get(position);

        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        CardView reviewCard;
        TextView author, content;

        public ReviewHolder(View itemView) {
            super(itemView);

            reviewCard = (CardView) itemView.findViewById(R.id.review_card);
            author = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
