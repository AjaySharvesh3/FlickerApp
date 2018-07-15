package com.sharvesh.flick.flicker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharvesh.flick.flicker.R;
import com.sharvesh.flick.flicker.model.Reviews;


import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Reviews> reviewsList;

    public ReviewAdapter(Context context, List<Reviews> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_cards, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.reviewer_name.setText(reviewsList.get(position).getAuthor());
        holder.description_review.setText(reviewsList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewer_name, description_review;

        ReviewViewHolder(View itemView) {
            super(itemView);

            reviewer_name = itemView.findViewById(R.id.reviewers_name);
            description_review = itemView.findViewById(R.id.description_reviews);
        }
    }

}
