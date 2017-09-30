package com.example.kartik.movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by kartik on 30/3/17.
 */

public class Downloader extends AsyncTask<Void, Void, String> {



    Context c;
    String jsonUrl;

    String TAG = "Bla bla";
    ProgressDialog pd;

    public Downloader(Context c, String jsonUrl) {
        this.c = c;
        this.jsonUrl = jsonUrl;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("Please wait...");
        pd.setMessage("Loading list of news sources");
        pd.show();
    }


    @Override
    protected String doInBackground(Void... params) {
        return download();
    }

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);

        pd.dismiss();
        if(jsonData.startsWith("Error")){
            String error = jsonData;
            Toast.makeText(c, error, Toast.LENGTH_SHORT).show();
        }
    }

    private String download(){
        Object connection = Connector.connect(jsonUrl);
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

                Log.d(TAG, jsonData.toString());
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
