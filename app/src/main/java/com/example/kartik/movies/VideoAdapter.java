package com.example.kartik.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kartik on 29-09-2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private ArrayList<Video> videos;
    Context context;
    private LayoutInflater inflater;

    public VideoAdapter(ArrayList<Video> videos, Context context) {
        this.videos = videos;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  inflater.inflate(R.layout.card_video, parent, false);
        return new VideoAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video video = videos.get(position);
        holder.videoName.setText(video.getName());
        holder.videoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url  = "http://www.youtube.com/watch?v="+ video.getKey();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        CardView videoCard;
        TextView videoName;
        public VideoViewHolder(View itemView) {
            super(itemView);
            videoCard = (CardView) itemView.findViewById(R.id.video_card);
            videoName = (TextView) itemView.findViewById(R.id.video_name);

        }
    }
}
