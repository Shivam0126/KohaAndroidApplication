package com.example.shivam.culibrary;

/**
 * Created by shivam on 5/1/18.
 */

public class books_issued {
    public String Title,author,issue_Date;

    public books_issued(String title, String author, String issue_Date) {
        Title = title;
        this.author = author;
        this.issue_Date = issue_Date;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIssue_Date(String issue_Date) {
        this.issue_Date = issue_Date;
    }

    public String getTitle() {

        return Title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIssue_Date() {
        return issue_Date;
    }

    public books_issued() {
    }
}
