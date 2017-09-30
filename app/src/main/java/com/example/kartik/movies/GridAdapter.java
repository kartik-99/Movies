package com.example.kartik.movies;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {


    private Context context;
    ArrayList<Movie> movies;
    String TAG = "gridadapter : ";

    public GridAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        //this.titles = titles;
        this.movies = movies;
    }


    static class ViewHolder{
        TextView textView;
        ImageView imageView;
        ProgressBar progressBar;
    }



    @Override
    public int getCount() {
        if(movies == null){
            Log.d(TAG, "getCount: No items in movies");
            return 0;
        }

        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        if(movies == null)
            return null;
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View singleGrid = convertView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder holder;
        final Movie movie = movies.get(position);

        if (singleGrid == null) {


            singleGrid = inflater.inflate(R.layout.grid_single, null);

            holder = new ViewHolder();

            holder.textView = (TextView) singleGrid.findViewById(R.id.grid_title_text);
            holder.imageView = (ImageView)singleGrid.findViewById(R.id.grid_poster);
            holder.progressBar = (ProgressBar)singleGrid.findViewById(R.id.progressbar);

            singleGrid.setTag(holder);
        } else {
            holder = (ViewHolder) singleGrid.getTag();
        }


        holder.textView.setText(movie.getName());

        Glide.with(context)
                .load(movie.getUrlToImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.no_poster_available)
                .into(holder.imageView);


        return singleGrid;
    }
}
  