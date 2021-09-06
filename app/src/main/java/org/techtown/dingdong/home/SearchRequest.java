package org.techtown.dingdong.home;

import com.google.gson.annotations.SerializedName;

public class SearchRequest {

    @SerializedName("keyword")
    public String searched;

    public SearchRequest(String searched) {
        this.searched = searched;
    }
}
