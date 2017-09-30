package com.example.kartik.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kartik on 30/3/17.
 */

public class Movie implements Parcelable{
    String name, posterPath, urlToImage, id;

    public Movie(String id, String name, String posterPath) {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.urlToImage = "http://image.tmdb.org/t/p/w185/" + this.posterPath;

    }

    protected Movie(Parcel in) {
        name = in.readString();
        posterPath = in.readString();
        urlToImage = in.readString();
        id = in.readString();
    }

    public String getPosterPath() {
        return posterPath;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(posterPath);
        dest.writeString(urlToImage);
        dest.writeString(id);
    }
}
