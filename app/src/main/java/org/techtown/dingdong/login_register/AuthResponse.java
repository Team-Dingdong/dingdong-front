package org.techtown.dingdong.login_register;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class AuthResponse {
    @SerializedName("status")
    public int status;
    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("requestTime")
        public Timestamp time;

        public Data(Timestamp time) {
            this.time = time;
        }

        public Timestamp getTime() {
            return time;
        }

        public void setTime(Timestamp time) {
            this.time = time;
        }
    }
}
