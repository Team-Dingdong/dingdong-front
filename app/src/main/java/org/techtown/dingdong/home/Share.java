package org.techtown.dingdong.home;

public class Share {
    private String title;
    private String[]images;
    private String maintext;
    private String date;
    private String hashtag;
    private String price;
    private String place;
    private int personnel_capacity, personnel_actual;

    public Share(String title, String[] images, String maintext, String date, String hashtag, String price, String place, int personnel_capacity, int personnel_actual) {
        this.title = title;
        this.images = images;
        this.maintext = maintext;
        this.date = date;
        this.hashtag = hashtag;
        this.price = price;
        this.place = place;
        this.personnel_capacity = personnel_capacity;
        this.personnel_actual = personnel_actual;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
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

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
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

    public int getPersonnel_capacity() {
        return personnel_capacity;
    }

    public void setPersonnel_capacity(int personnel_capacity) {
        this.personnel_capacity = personnel_capacity;
    }

    public int getPersonnel_actual() {
        return personnel_actual;
    }

    public void setPersonnel_actual(int personnel_actual) {
        this.personnel_actual = personnel_actual;
    }
}
