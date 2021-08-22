package org.techtown.dingdong.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.sql.Timestamp;

public class Share {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("imageUrl")
    @Expose
    private String images;
    @SerializedName("bio")
    @Expose
    private String maintext;
    @SerializedName("createdDate")
    @Expose
    private String date;
    //private String hashtag;
    @SerializedName("cost")
    @Expose
    private String price;
    @SerializedName("local")
    @Expose
    private String place;
    @SerializedName("people")
    @Expose
    private String  personnelcapacity;
    //private int personnelactual;


    public Share(String title, String images, String maintext, String date, String price, String place, String personnelcapacity) {
        this.title = title;
        this.images = images;
        this.maintext = maintext;
        this.date = date;
        this.price = price;
        this.place = place;
        this.personnelcapacity = personnelcapacity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getMaintext() {
        return maintext;
    }

    public void setMaintext(String maintext) {
        this.maintext = maintext;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPersonnelcapacity() {
        return personnelcapacity;
    }

    public void setPersonnel_capacity(String personnelcapacity) {
        this.personnelcapacity = personnelcapacity;
    }

}
