package org.techtown.dingdong.network;

import org.techtown.dingdong.login_register.AuthRequest;
import org.techtown.dingdong.login_register.AuthResponse;
import org.techtown.dingdong.login_register.LoginRequest;
import org.techtown.dingdong.login_register.Loginitem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Apiinterface {

    //@GET("/post")
    //Call<Shareitem> getData(@Query("page") int num);
    //Call<AuthResponse> setAuth(@Body AuthRequest authRequest);

    //Call<AuthResponse> setAuth(@Field("to") String ID);

    @POST("/api/v1/auth/send-sms")
    Call<AuthResponse> setAuth(@Body AuthRequest authRequest);



    @POST("/api/v1/auth")
    Call<Loginitem> LoginRequest(@Body LoginRequest loginRequest);

}
