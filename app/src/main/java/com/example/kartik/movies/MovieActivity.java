package com.example.kartik.movies;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.kartik.movies.database.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.kartik.movies.MainActivity.api_key;


public class MovieActivity extends AppCompatActivity {

    String movieUrl, movieInfoJson, id, movieName;
    ProgressDialog pd;
    String TAG = "MovieActivity : ";
    Context context;

    TextView title, tagline, overview, date,duration, rating, noReviews;
    ImageView poster;
    ProgressBar progressBar;
    RecyclerView videosRecyclerView;
    ArrayList<Video> videos;
    JSONObject movieObject, videosObject, singleVideo;
    JSONArray vids;
    VideoAdapter videoAdapter;
    Button addToFavButton;
    boolean click;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar  = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_movie);





        title = (TextView)findViewById(R.id.movieTitle);
        tagline = (TextView)findViewById(R.id.movieTagline);
        overview = (TextView)findViewById(R.id.overviewTextView);
        date = (TextView)findViewById(R.id.releaseDateTextView);
        duration = (TextView)findViewById(R.id.durationTextview);
        rating = (TextView)findViewById(R.id.ratingTextView);
        poster = (ImageView)findViewById(R.id.moviePoster);
        progressBar = (ProgressBar)findViewById(R.id.movieProgressBar);
        noReviews = (TextView) findViewById(R.id.no_trailers_textview);
        videosRecyclerView = (RecyclerView) findViewById(R.id.videos_recyclerview);
        addToFavButton = (Button) findViewById(R.id.add_to_fav_button);



        videos = new ArrayList<>();


        context = MovieActivity.this;
        Intent intent = getIntent();
        movie = intent.getParcelableExtra("movie");
        movieName  = movie.getName();
        id = movie.getId();
        movieUrl = " https://api.themoviedb.org/3/movie/"+id+"?api_key="+api_key+"&append_to_response=videos&language=en-US";


        setClickStatus();



        addToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click){
                    addToFavButton.setText(R.string.added_to_favourites);
                    addToFavButton.setBackgroundColor(Color.GRAY);
                    onClickAddMovie();
                    click = false;
                }else{
                    addToFavButton.setText(R.string.add_to_favourites);
                    addToFavButton.setBackgroundColor(Color.BLUE);
                    onClickDeleteMovie();
                    click = true;
                }
            }
        });

        Log.d("onCreate: ", movieUrl);

        try {
            movieInfoJson = download();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            movieObject = new JSONObject(movieInfoJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            getVideos();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }




    private void setClickStatus() {
        Uri uri = Uri.parse(MoviesContract.FavouriteEntry.CONTENT_URI+"/"+movie.getId().toString());
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        if(c.getCount() == 0)
            click = false;
        else
            click = true;


        if(click){
            addToFavButton.setText(R.string.added_to_favourites);
            addToFavButton.setBackgroundColor(Color.GRAY);
            click = false;
        }else{
            addToFavButton.setText(R.string.add_to_favourites);
            addToFavButton.setBackgroundColor(Color.BLUE);
            click = true;
        }
    }

    private void onClickAddMovie() {
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.FavouriteEntry.COLUMN_ID, id);
        cv.put(MoviesContract.FavouriteEntry.COLUMN_NAME, movieName);
        cv.put(MoviesContract.FavouriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        Uri uri = getContentResolver().insert(MoviesContract.FavouriteEntry.CONTENT_URI, cv);
        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }

        // Finish activity (this returns back to MainActivity)
        //finish();
    }


    private void onClickDeleteMovie() {
        Uri uri = Uri.parse(MoviesContract.FavouriteEntry.CONTENT_URI+"/"+movie.getId().toString());
        // args has the data
        getContentResolver().delete(uri, MoviesContract.FavouriteEntry.COLUMN_ID+"=?", new String[]{movie.getId().toString()});
    }

    private void getVideos() throws JSONException {
        String id, key, name, site, type;
        videos.clear();
        try {
            videosObject = movieObject.getJSONObject("videos");
            vids = videosObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(vids == null || vids.length() == 0){
            noReviews.setVisibility(View.VISIBLE);
            videosRecyclerView.setVisibility(View.GONE);
        }else{
            noReviews.setVisibility(View.GONE);
            videosRecyclerView.setVisibility(View.VISIBLE);

            for(int i = 0; i<vids.length(); i++){
                try {
                    singleVideo = vids.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                id = singleVideo.getString("id");
                key = singleVideo.getString("key");
                name = singleVideo.getString("name");
                site = singleVideo.getString("site");
                type = singleVideo.getString("type");

                videos.add(new Video(id, key, name, site, type));

            }

            videoAdapter = new VideoAdapter(videos, this);
            videosRecyclerView.setAdapter(videoAdapter);
            videosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        }

    }

    public void getReviews(View view) {
        Intent intent = new Intent(context, ReviewsActivity.class);
        intent.putExtra("id", id);

        context.startActivity(intent);
    }

    public void onClickAddMovie(View view) {

    }


    class DownloadMovie extends AsyncTask<Void, Void, String>{

        Context context;
        String url;

        public DownloadMovie(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExecute: ");

            pd = new ProgressDialog(context);
            pd.setTitle("Please wait");
            pd.setMessage("Loading the movie");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: ");
            return download();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

            Log.d("onPostExecute: ", s);


        }

        private String download(){
            Log.d(TAG, "download: ");
            Object connection = Connector.connect(movieUrl);
            if(connection.toString().startsWith("Error"))
                return connection.toString();

            try{
                HttpURLConnection con = (HttpURLConnection)connection;
                if(con.getResponseCode()==con.HTTP_OK){
                    InputStream is = new BufferedInputStream(con.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    String line;
                    StringBuffer jsonData = new StringBuffer();

                    while((line = br.readLine())!=null)
                        jsonData.append(line+"\n");

                    br.close();
                    is.close();

                    Log.d("TAG", jsonData.toString());
                    return jsonData.toString();

                }else {
                    return "Error" + con.getResponseMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error : "+e.getMessage();
            }
        }


    }

    void setViews() throws JSONException {


        title.setText(movieObject.getString("original_title"));
        tagline.setText(movieObject.getString("tagline"));
        overview.setText(movieObject.getString("overview"));
        date.setText(extractDate(movieObject.getString("release_date")));
        duration.setText(movieObject.getString("runtime")+"min");
        rating.setText(movieObject.getDouble("vote_average")+"/10");

        String urlToPoser ="http://image.tmdb.org/t/p/w500/"+ movieObject.getString("poster_path");


        Glide.with(context)
                .load(urlToPoser)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.no_poster_available)
                .into(poster);




    }

    String download() throws ExecutionException, InterruptedException {
        String lol = new DownloadMovie(this, movieUrl).execute().get();
        return lol;
    }




    String extractDate(String date){
        String actualDate = date.substring(8, 10)+ordinal(date.substring(8, 10))+" "+extractMonth(date.substring(5,7))+", " + date.substring(0,4);
        return actualDate;

    }

    private String ordinal(String substring) {
        int a = Integer.parseInt(substring);
        int b = a%10;
        String c;
        switch (b){
            case 1 : c = "st"; break;
            case 2 : c = "nd"; break;
            case 3 : c = "rd"; break;
            default: c = "th"; break;
        }
        return c;
    }

    private String extractMonth(String substring) {
        int a = Integer.parseInt(substring);
        String month;
        switch (a){
            case 1  : month = "Jan"; break;
            case 2  : month = "Feb"; break;
            case 3  : month = "Mar"; break;
            case 4  : month = "April"; break;
            case 5  : month = "May"; break;
            case 6  : month = "June"; break;
            case 7  : month = "July"; break;
            case 8  : month = "Aug"; break;
            case 9  : month = "Sept"; break;
            case 10 : month = "Oct"; break;
            case 11 : month = "Nov"; break;
            case 12 : month = "Dec"; break;
            default:month = "error";
        }
        return month;
    }
}
