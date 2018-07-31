package com.sharvesh.flick.flicker.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharvesh.flick.flicker.R;
import com.sharvesh.flick.flicker.model.Movies;
import com.sharvesh.flick.flicker.model.Trailers;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context mContext;
    private List<Trailers> trailerList;

    public TrailerAdapter(Context mContext, List<Trailers> trailerList){
        this.mContext = mContext;
        this.trailerList = trailerList;
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_cards, viewGroup, false);
        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.TrailerViewHolder viewHolder, int i){
        viewHolder.title.setText(trailerList.get(i).getName());
    }

    @Override
    public int getItemCount(){

        return trailerList.size();

    }

   public class TrailerViewHolder extends RecyclerView.ViewHolder{
       public TextView title;
       ImageView thumbnail;

       TrailerViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.trailer_title);
            thumbnail = view.findViewById(R.id.backdrop_image);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        String videoId = trailerList.get(pos).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+videoId));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("VIDEO_ID", videoId);
                        mContext.startActivity(intent);
                    }
                }
            });

        }
    }
}
