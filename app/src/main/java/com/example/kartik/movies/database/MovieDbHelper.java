package com.example.kartik.movies.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kartik on 29-09-2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasksDb.db";

    private static final int VERSION = 4;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + MoviesContract.FavouriteEntry.TABLE_NAME +"( " +
                MoviesContract.FavouriteEntry._ID + ", INTEGER PRIMARY KEY, " +
                MoviesContract.FavouriteEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MoviesContract.FavouriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesContract.FavouriteEntry.COLUMN_ID + " INTEGER NOT NULL);";

        Log.d("create command: ", CREATE_TABLE);

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavouriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
