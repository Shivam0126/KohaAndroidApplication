package com.example.shivam.culibrary;

/**
 * Created by shivam on 5/1/18.
 */

public class books_Due {
    public String title,author,due_Date,image;
    public books_Due()
    {

    }

    public books_Due(String title, String author, String due_Date,String image) {
        this.title = title;
        this.author = author;
        this.due_Date = due_Date;
        this.image= image;

    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDue_Date() {
        return due_Date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {

        return image;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDue_Date(String due_Date) {
        this.due_Date = due_Date;
    }


}
