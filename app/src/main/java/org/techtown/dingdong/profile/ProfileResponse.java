package org.techtown.dingdong.profile;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("code")
    public String code;

    @SerializedName("data")
    public Data data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @SerializedName("nickname")
        String nickname;

        @SerializedName("profile_bio")
        String profile_bio;

        @SerializedName("profileImageUrl")
        String profileImageUrl;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getProfile_bio() {
            return profile_bio;
        }

        public void setProfile_bio(String profile_bio) {
            this.profile_bio = profile_bio;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }
    }
}
