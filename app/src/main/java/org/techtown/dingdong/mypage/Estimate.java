package org.techtown.dingdong.mypage;

import retrofit2.http.Url;

public class Estimate {
    String imgpath;
    String nickname;

    public Estimate(String imgpath, String nickname){
        this.imgpath = imgpath;
        this.nickname = nickname;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
