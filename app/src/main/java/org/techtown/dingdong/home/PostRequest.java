package org.techtown.dingdong.home;

import com.google.gson.annotations.SerializedName;

public class PostRequest {

    @SerializedName("title")
    private String title;

    @SerializedName("people")
    private Integer people;

    @SerializedName("cost")
    private Integer cost;

    @SerializedName("bio")
    private String bio;

    @SerializedName("local")
    private String local;

    @SerializedName("categoryId")
    private Integer categoryId;

    @SerializedName("postTag")
    private String hashtag = null;

    public PostRequest(String title, Integer people, Integer cost, String bio, String local, Integer categoryId, String hashtag) {
        this.title = title;
        this.people = people;
        this.cost = cost;
        this.bio = bio;
        this.local = local;
        this.categoryId = categoryId;
        this.hashtag = hashtag;
    }
}
