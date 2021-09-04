package org.techtown.dingdong.login_register;

import com.google.gson.annotations.SerializedName;

public class AuthNickRequset {
    @SerializedName("nickname")
    String nick;

    public AuthNickRequset(String nick){
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
