package org.techtown.dingdong.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private String personnelcapacity;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("gatheredPeople")
    @Expose
    private String gatheredPeople;
    @SerializedName("nickname")
    @Expose
    private String username = null;
    @SerializedName("profile_bio")
    @Expose
    private String usertext;
    @SerializedName("good")
    @Expose
    private String usergood = null;
    @SerializedName("bad")
    @Expose
    private String userbad = null;
    @SerializedName("profileImageUrl")
    @Expose
    private String profileImg;
    @SerializedName("tagList")
    @Expose
    private List<String> hashtag;
    @SerializedName("category")
    @Expose
    private String category;

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

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public List<String> getHashtag() {
        return hashtag;
    }

    public void setHashtag(List<String> hashtag) {
        this.hashtag = hashtag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGatheredPeople() {
        return gatheredPeople;
    }

    public void setGatheredPeople(String gatheredPeople) {
        this.gatheredPeople = gatheredPeople;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertext() {
        return usertext;
    }

    public void setUsertext(String usertext) {
        this.usertext = usertext;
    }

    public String getUsergood() {
        return usergood;
    }

    public void setUsergood(String usergood) {
        this.usergood = usergood;
    }

    public String getUserbad() {
        return userbad;
    }

    public void setUserbad(String userbad) {
        this.userbad = userbad;
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
