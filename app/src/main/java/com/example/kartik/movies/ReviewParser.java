package com.example.kartik.movies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kartik on 28-09-2017.
 */

public class ReviewParser extends AsyncTask<Void, Void, ArrayList<Review>> {

    String reviewJsonData, author, content, id, url;
    ArrayList<Review> reviews = new ArrayList<>();
    ReviewsAdapter reviewsAdapter;
    Context context;
    RecyclerView rv;

    public ReviewParser(String reviewJsonData, Context context, RecyclerView rv) {
        this.reviewJsonData = reviewJsonData;
        this.context = context;
        this.rv = rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... params) {
        try {
            return parse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        super.onPostExecute(reviews);
    }

    private ArrayList<Review> parse() throws JSONException {
        reviews.clear();
        JSONObject obj = new JSONObject(reviewJsonData);
        int results = Integer.parseInt(obj.getString("total_results"));

        if(results == 0)
            return null;

        JSONArray jsonArray = obj.getJSONArray("results");

        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            author = jsonObject.getString("author");
            content = jsonObject.getString("content");
            id = jsonObject.getString("id");
            url = jsonObject.getString("url");

            reviews.add(new Review(author, content, id, url));
        }

        return reviews;
    }
}
