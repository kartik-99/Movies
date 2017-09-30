package com.example.kartik.movies;

/**
 * Created by Kartik on 28-09-2017.
 */

public class Review {
    String author, content, id, url;

    public Review(String author, String body, String id, String url) {
        this.author = author;
        this.content = body;
        this.id = id;
        this.url = url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {

        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
