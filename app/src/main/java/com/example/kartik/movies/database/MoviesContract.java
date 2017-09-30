package com.example.kartik.movies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kartik on 29-09-2017.
 */

public class MoviesContract {



    public static final String AUTHORITY = "com.example.kartik.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";






    public static final class FavouriteEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME = "moviename";
        public static final String COLUMN_ID = "movieid";
        public static final String COLUMN_POSTER_PATH = "posterpath";
    }
}
