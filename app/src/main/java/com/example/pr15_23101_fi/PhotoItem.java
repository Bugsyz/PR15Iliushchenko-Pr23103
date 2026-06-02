package com.example.pr15_23101_fi;

public class PhotoItem {
    private String imagePath;
    private String timestamp;

    public PhotoItem(String imagePath, String timestamp) {
        this.imagePath = imagePath;
        this.timestamp = timestamp;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTimestamp() {
        return timestamp;
    }
}