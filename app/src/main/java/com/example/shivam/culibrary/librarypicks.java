package com.example.shivam.culibrary;

/**
 * Created by shivam on 5/1/18.
 */

public class librarypicks {
    public String title,author,image;
    public librarypicks()
    {

    }

    public librarypicks(String title, String author, String image) {
        this.title = title;
        this.author = author;
        this.image = image;

    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
