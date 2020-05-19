package com.example.shivam.culibrary;

/**
 * Created by shivam on 5/1/18.
 */

public class waiting_due {
    public String title,author,due_Date;
    public waiting_due()
    {

    }

    public waiting_due(String title, String author, String due_Date) {
        this.title = title;
        this.author = author;
        this.due_Date = due_Date;

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

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDue_Date(String due_Date) {
        this.due_Date = due_Date;
    }

}
