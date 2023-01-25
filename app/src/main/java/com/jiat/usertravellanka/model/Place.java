package com.jiat.usertravellanka.model;

public class Place {


    private String pname;
    private String Details;
    private double latitude;
    private double longitude;
    private String image;
    private String placeLocation;
    private String tell;
    private String openClose;
    private String website;


    public Place(){

    }

    public Place(String pname, String details, double latitude, double longitude, String image, String placeLocation, String tell, String openClose, String website) {
        this.pname = pname;
        Details = details;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.placeLocation = placeLocation;
        this.tell = tell;
        this.openClose = openClose;
        this.website = website;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(String placeLocation) {
        this.placeLocation = placeLocation;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public String getOpenClose() {
        return openClose;
    }

    public void setOpenClose(String openClose) {
        this.openClose = openClose;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
