package org.techtown.dingdong.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.sql.Timestamp;

public class Share {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("imageUrl1")
    @Expose
    private String image1 = null;
    @SerializedName("imageUrl2")
    @Expose
    private String image2 = null;
    @SerializedName("imageUrl3")
    @Expose
    private String image3 = null;
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
    private String  id;
    private String[] images;

    public Share(String title, String image1, String image2, String image3, String maintext, String date, String price, String place, String personnelcapacity, String id) {
        this.title = title;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.maintext = maintext;
        this.date = date;
        this.price = price;
        this.place = place;
        this.personnelcapacity = personnelcapacity;
        this.id = id;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages() {
        String[] myimage = new String[3];
        myimage[0] = image1;
        myimage[1] = image2;
        myimage[2] = image3;
        this.images = myimage;
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

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
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

    public void setPersonnelcapacity(String personnelcapacity) {
        this.personnelcapacity = personnelcapacity;
    }

}
