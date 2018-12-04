package com.example.mahdi.dia_diary;

import java.util.Date;

public class Upload {
    private String mName;
    private String mImageUrl;
    public Date time;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl,Date time) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        this.time = time;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}