package com.example.kartik.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.kartik.movies.MainActivity.api_key;


public class ReviewsActivity extends AppCompatActivity {

    String id, reviewsUrl, reviewsInfoJson;
    TextView textView;
    ProgressDialog pd;
    RecyclerView rv;
    ArrayList<Review> reviews = new ArrayList<>();
    ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        reviewsUrl = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key="+api_key;
        setTitle("Reviews");

        textView = (TextView) findViewById(R.id.reviews_textbox);
        rv = (RecyclerView) findViewById(R.id.reviews_recycler_view);

        try {
            reviewsInfoJson = download();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            reviews = new ReviewParser(reviewsInfoJson, this, rv).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if(reviews == null){
            textView.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            reviewsAdapter = new ReviewsAdapter(reviews, this);
            rv.setAdapter(reviewsAdapter);
            rv.setLayoutManager(new LinearLayoutManager(this));
        }


    }

    private String download() throws ExecutionException, InterruptedException {
        String lol = new ReviewsActivity.DownloadReviews().execute().get();
        return lol;
    }

    class DownloadReviews extends AsyncTask<Void, Void, String>{


        public DownloadReviews() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ReviewsActivity.this);
            pd.setTitle("Please wait");
            pd.setMessage("Loading reviews");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return download();
        }

        private String download() {
            Object connection = Connector.connect(reviewsUrl);
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            //textView.setText(s.toString());
        }
    }
}
