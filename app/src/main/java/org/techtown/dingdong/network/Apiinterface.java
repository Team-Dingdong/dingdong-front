package org.techtown.dingdong.network;

import org.techtown.dingdong.home.EditResponse;
import org.techtown.dingdong.home.PostRequest;
import org.techtown.dingdong.home.PostResponse;
import org.techtown.dingdong.home.SearchRequest;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.home.ShareResponse;
import org.techtown.dingdong.login_register.AuthRequest;
import org.techtown.dingdong.login_register.AuthResponse;
import org.techtown.dingdong.login_register.LoginRequest;
import org.techtown.dingdong.login_register.LoginResponse;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Apiinterface {

    @GET("/api/v1/post")
    Call<PostResponse> getData(@Query("page") int num);


    @GET("/api/v1/post/sorted_by=desc(createdDate)")
    Call<PostResponse> getCreatedData(@Query("page") int num);


    @GET("/api/v1/post/sorted_by=desc(endDate)")
    Call<PostResponse> getEndData(@Query("page") int num);


    @GET("/api/v1/post/category/{id}")
    Call<PostResponse> getCategoryData(@Path("id") int id);
    //Call<AuthResponse> setAuth(@Body AuthRequest authRequest);
    //Call<AuthResponse> setAuth(@Field("to") String ID);

    @POST("/api/v1/post")
    Call<EditResponse> setPost(@Body PostRequest postRequest);


    @GET("/api/v1/post/{id}")
    Call<ShareResponse> getShare(@Path("id") int id);

    @POST("/api/v1/auth/send-sms")
    Call<AuthResponse> setAuth(@Body AuthRequest authRequest);


    @PATCH("/api/v1/post/{id}")
    Call<ResponseBody> setPatch(@Body PostRequest postRequest, @Path("id") int id);


    @Multipart
    @PATCH("/api/v1/upload/post/{id}")
    Call<ResponseBody> uploadImg(@Part ArrayList<MultipartBody.Part> files, @Path("id") int id);


    @POST("/api/v1/auth")
    Call<LoginResponse> LoginRequest(@Body LoginRequest loginRequest);


    @GET("/api/v1/post/search")
    Call<PostResponse> getSearchData(@Query(value = "keyword") String keyword);


    //@POST("/api/v1/auth")
    //Call<LoginResponse> LoginRequest(@Body LoginRequest loginRequest);

}
