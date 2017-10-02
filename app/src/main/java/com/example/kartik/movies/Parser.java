package com.example.kartik.movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kartik.movies.database.MovieContentProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kartik on 30/3/17.
 */

public class Parser extends AsyncTask<Void, Void, ArrayList<Movie>> {

    Context c;
    String jsonData;
    String TAG = "NOT bla bla";
    GridAdapter adapter;

    ProgressDialog pd;
    ArrayList<Movie> movies= new ArrayList<>();

    public Parser(Context c, String jsonData) {
        this.c = c;
        this.jsonData = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        pd = new ProgressDialog(c);
        pd.setTitle("Please wait...");
        pd.setMessage("Loading list of news newsItems");
        pd.show();
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        return parse();
    }

    @Override
    protected void onPostExecute(final ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        pd.dismiss();
        if(movies==null)
            Toast.makeText(c, "Unable to parse", Toast.LENGTH_LONG).show();
    }

    /*@Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);
        Log.d(TAG, "onPosExecute: parser reached");
        pd.dismiss();

        if(isParsed){
            adapter = new GridAdapter(c, movies);
            gv.setAdapter(adapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Movie movie = movies.get(i);
                    Intent intent = new Intent(c, MovieActivity.class);
                    intent.putExtra("name", movie.getName());
                    intent.putExtra("id", movie.getId());
                    c.startActivity(intent);
                }
            });

        }else
            Toast.makeText(c, "Unable to parse", Toast.LENGTH_LONG).show();


    }*/


    private ArrayList<Movie> parse(){
        try{
            JSONObject object = new JSONObject(jsonData);
            JSONArray jsonArray = object.getJSONArray("results");
            JSONObject jsonObject;

            movies.clear();

            for(int i=0; i<jsonArray.length(); i++){ //jsonArray.length()

                jsonObject = jsonArray.getJSONObject(i);

                String title = jsonObject.getString("title");
                String posterPath = jsonObject.getString("poster_path");
                long lolol = jsonObject.getLong("id");
                String id = Long.toString(lolol);

                movies.add(new Movie(id, title, posterPath));
            }


            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

