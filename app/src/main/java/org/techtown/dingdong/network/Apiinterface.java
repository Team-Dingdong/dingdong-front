package org.techtown.dingdong.network;

import org.techtown.dingdong.home.PostResponse;
import org.techtown.dingdong.login_register.AuthNickRequset;
import org.techtown.dingdong.login_register.AuthNickResponse;
import org.techtown.dingdong.login_register.AuthRequest;
import org.techtown.dingdong.login_register.AuthResponse;
import org.techtown.dingdong.login_register.LoginRequest;
import org.techtown.dingdong.login_register.LoginResponse;
import org.techtown.dingdong.login_register.ProfileUploadResponse;
import org.techtown.dingdong.profile.MyLatingResponse;
import org.techtown.dingdong.profile.ProfileResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Apiinterface {

    @GET("/api/v1/post")
    Call<PostResponse> getData(@Query("page") int num);
    //Call<AuthResponse> setAuth(@Body AuthRequest authRequest);
    //Call<AuthResponse> setAuth(@Field("to") String ID);

    @POST("/api/v1/auth/send-sms")
    Call<AuthResponse> setAuth(@Body AuthRequest authRequest);


    @POST("/api/v1/auth")
    Call<LoginResponse> LoginRequest(@Body LoginRequest loginRequest);


    //@POST("/api/v1/auth")
    //Call<LoginResponse> LoginRequest(@Body LoginRequest loginRequest);

    @GET("/api/v1/profile")
    Call<ProfileResponse> getData();

    @GET("/api/v1/rating")
    Call<MyLatingResponse> getLating();

   // @PATCH("/api/v1/upload/profile/:id")
    //Call<ProfileImgResponse>

    //@Multipart
   // @PATCH("/api/v1/upload/profile/:id")
    //Call<ProfileUploadResponse> ProfileUploadRequest(@Part MultipartBody.Part file);

    @POST("/api/v1/auth/nickname")
    Call<AuthNickResponse> AuthNickRequest(@Body AuthNickRequset authNickRequset);

}
