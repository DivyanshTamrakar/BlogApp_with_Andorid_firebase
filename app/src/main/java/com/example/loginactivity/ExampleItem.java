package com.example.loginactivity;
import java.util.Date;

public class ExampleItem {

    public String user_id, image_url, description, title;

     public Date timestamp;



    public  ExampleItem(){}

    public ExampleItem(String user_id, String image_url, String description, String title,Date timestamp) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.description = description;
        this.title = title;
        this.timestamp = timestamp;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}