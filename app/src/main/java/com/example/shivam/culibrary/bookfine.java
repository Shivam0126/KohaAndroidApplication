package com.example.shivam.culibrary;

/**
 * Created by shivam on 5/1/18.
 */

public class bookfine {
    public String date,description,fineamount;
    public bookfine()
    {

    }

    public bookfine(String date, String description, String fineamount) {
        this.date = date;
        this.description = description;
        this.fineamount = fineamount;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getFineamount() {
        return fineamount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFineamount(String fineamount) {
        this.fineamount = fineamount;
    }
}
