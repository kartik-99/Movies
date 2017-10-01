package com.example.kartik.movies;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

//import com.example.kartik.movies.database.GetFavourites;
import com.example.kartik.movies.database.MoviesContract;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    Spinner spinner;
    final String[] categories = new String[] { "Popular", "Top-Rated", "Favourites"};
    String jsonData, name, posterPath, id;
    ArrayList<Movie> movies = new ArrayList<>();
    GridAdapter adapter;
    Cursor cursor;
    TextView noFavourites;
    int selected = 0, position = 0;
    //TODO insert your api key here
    public static final String api_key = "";

    String urlPopular = "https://api.themoviedb.org/3/movie/popular?api_key="+api_key+"&language=en-US&page=1";
    String urlTopRated = "https://api.themoviedb.org/3/movie/top_rated?api_key="+api_key+"&language=en-US&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridView = (GridView)findViewById(R.id.gridView);
        spinner = (Spinner)findViewById(R.id.categorySpinner);
        noFavourites = (TextView) findViewById(R.id.no_favourites);
        noFavourites.setVisibility(View.GONE);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinner.setAdapter(arrayAdapter);

        downloadAndParseData(selected);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected = i;
                downloadAndParseData(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        position = gridView.getFirstVisiblePosition();
        outState.putInt("position", position);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int pos = savedInstanceState.getInt("position");
        gridView.setSelection(pos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(selected == 2){
            displayFavourites();
        }
    }

    private void downloadAndParseData(int i) {
        if(i==0){
            try {
                jsonData = new Downloader(MainActivity.this, urlPopular).execute().get();
                movies.clear();
                movies = new Parser(this, jsonData).execute().get();
                setRecyclerView();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else if(i==1){
            try {
                jsonData = new Downloader(MainActivity.this, urlTopRated).execute().get();
                movies.clear();
                movies = new Parser(this, jsonData).execute().get();
                setRecyclerView();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }else{
            displayFavourites();
        }
    }

    public void displayFavourites(){
        try {
            movies.clear();
            cursor = new GetFavourites(getContentResolver()).execute().get();
            if(cursor.getCount() == 0){
                noFavourites.setVisibility(View.VISIBLE);
            }else{
                noFavourites.setVisibility(View.GONE);
                cursor.moveToFirst();
                do{
                    name = cursor.getString(cursor.getColumnIndex(MoviesContract.FavouriteEntry.COLUMN_NAME));
                    posterPath = cursor.getString(cursor.getColumnIndex(MoviesContract.FavouriteEntry.COLUMN_POSTER_PATH));
                    id = cursor.getString(cursor.getColumnIndex(MoviesContract.FavouriteEntry.COLUMN_ID));

                    movies.add(new Movie(id, name, posterPath));
                }while(cursor.moveToNext());
                setRecyclerView();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void setRecyclerView() {
        adapter = new GridAdapter(this, movies);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = movies.get(i);
                Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                intent.putExtra("movie", (Parcelable) movie);
                startActivity(intent);
            }
        });
    }



    public class GetFavourites extends AsyncTask<Void, Void, Cursor> {

        ContentResolver contentResolver;
        int type;

        public GetFavourites(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);


        }

        @Override
        protected Cursor doInBackground(Void... params) {
            try {
                return contentResolver.query(MoviesContract.FavouriteEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}

