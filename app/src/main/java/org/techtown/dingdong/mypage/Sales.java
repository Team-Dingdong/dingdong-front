package org.techtown.dingdong.mypage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sales {
    @Expose
    @SerializedName("createdDate")
    String date;
    @Expose
    @SerializedName("title")
    String title;
    @Expose
    @SerializedName("imageUrl1")
    String img1;
    @Expose
    @SerializedName("people")
    String people;
    @Expose
    @SerializedName("id")
    String id;

    public Sales(String date, String title, String img1, String people ){
        this.date = date;
        this.img1 = img1;
        this.title = title;
        this.people = people;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }


}
