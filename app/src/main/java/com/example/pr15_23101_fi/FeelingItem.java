package com.example.pr15_23101_fi;

import com.google.gson.annotations.SerializedName;

public class FeelingItem {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("position")
    private int position;

    private int iconResId = 0;

    public FeelingItem(int id, String title, String image, int position) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.position = position;
    }

    public FeelingItem(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public int getPosition() { return position; }
    public int getIconResId() { return iconResId; }
    public void setIconResId(int iconResId) { this.iconResId = iconResId; }
}