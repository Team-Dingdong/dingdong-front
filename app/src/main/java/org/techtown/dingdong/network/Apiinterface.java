package org.techtown.dingdong.network;

import org.techtown.dingdong.login_register.AuthNickRequset;
import org.techtown.dingdong.login_register.AuthNickResponse;
import org.techtown.dingdong.login_register.AuthRequest;
import org.techtown.dingdong.login_register.AuthResponse;
import org.techtown.dingdong.login_register.LoginRequest;
import org.techtown.dingdong.login_register.LoginResponse;

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
    Call<LoginResponse> LoginRequest(@Body LoginRequest loginRequest);


    //@POST("/api/v1/auth")
    //Call<LoginResponse> LoginRequest(@Body LoginRequest loginRequest);

    @POST("/api/v1/auth/nickname")
    Call<AuthNickResponse> AuthNickRequest(@Body AuthNickRequset authNickRequset);
}
