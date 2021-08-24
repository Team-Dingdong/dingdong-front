package org.techtown.dingdong.login_register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("data")
    @Expose
    public Data data;

    @SerializedName("code")
    @Expose
    public String result;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public class Data{
        @SerializedName("accessToken")
        @Expose
        public String accessToken;

        @SerializedName("refreshToken")
        @Expose
        public String refreshToken;

        @SerializedName("grantType")
        @Expose
        private String tokentype;

        public String getTokentype() {
            // OAuth requires uppercase Authorization HTTP header value for token type
            if(!Character.isUpperCase(tokentype.charAt(0))) {
                tokentype = Character.toString(tokentype.charAt(0)).toUpperCase() + tokentype.substring(1);
            }

            return tokentype;
        }

        public void setToken_type(String tokentype) {
            this.tokentype = tokentype;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getExpireIn() {
            return expireIn;
        }

        public void setExpireIn(String expireIn) {
            this.expireIn = expireIn;
        }

        @SerializedName("accessTokenExpiresIn")
        @Expose
        public String expireIn;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }


}
