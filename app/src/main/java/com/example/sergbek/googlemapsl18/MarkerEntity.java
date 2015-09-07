package com.example.sergbek.googlemapsl18;


public class MarkerEntity {

    private String id;
    private String latitude;
    private String longitude;
    private String title;
    private String photo;

    public MarkerEntity() {
    }

    public MarkerEntity(String latitude, String longitude, String title, String photo) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.photo = photo;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
