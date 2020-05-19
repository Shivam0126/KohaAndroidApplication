package com.example.shivam.culibrary;

/**
 * Created by shivam on 4/1/18.
 */

public class location {
    public String campus,locate,status;
    public location()
    {}

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCampus() {

        return campus;
    }

    public String getLocate() {
        return locate;
    }

    public String getStatus() {
        return status;
    }

    public location(String campus, String locate, String status) {

        this.campus = campus;
        this.locate = locate;
        this.status = status;
    }
}
