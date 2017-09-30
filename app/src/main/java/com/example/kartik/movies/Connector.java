package com.example.kartik.movies;

/**
 * Created by kartik on 30/3/17.
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User on 28-03-2017.
 */

public class Connector {
    public static Object connect(String JSONUrl){
        try {
            URL url = new URL(JSONUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setRequestMethod("GET");
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            con.setDoInput(true);

            return con;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error"+e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error"+e.getMessage();
        }
    }
}

