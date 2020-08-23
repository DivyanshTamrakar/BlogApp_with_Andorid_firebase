package com.example.loginactivity;

public class my_post {

        public String user_id;
        public String title;
        public String description;
        public String image_url;


        public my_post(){

        }

    public my_post(String user_id, String title, String description, String image_url) {
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.image_url = image_url;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}






