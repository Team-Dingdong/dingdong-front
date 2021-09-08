package org.techtown.dingdong.network;

import org.techtown.dingdong.chatting.ChatPromiseRequest;
import org.techtown.dingdong.chatting.ChatPromiseResponse;
import org.techtown.dingdong.chatting.ChatResponse;
import org.techtown.dingdong.chatting.ChatRoomInformResponse;
import org.techtown.dingdong.chatting.ChatRoomResponse;
import org.techtown.dingdong.chatting.ChatUserResponse;
import org.techtown.dingdong.home.EditResponse;
import org.techtown.dingdong.home.PostRequest;
import org.techtown.dingdong.home.PostResponse;
import org.techtown.dingdong.home.ShareResponse;
import org.techtown.dingdong.login_register.AuthNickRequset;
import org.techtown.dingdong.login_register.AuthNickResponse;
import org.techtown.dingdong.login_register.AuthRequest;
import org.techtown.dingdong.login_register.AuthResponse;
import org.techtown.dingdong.login_register.LoginRequest;
import org.techtown.dingdong.login_register.LoginResponse;
import org.techtown.dingdong.login_register.ProfileUploadResponse;
import org.techtown.dingdong.profile.UserProfileResponse;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("/api/v1/post/category/sorted_by=desc(endDate)/{id}")
    Call<PostResponse> getEndCategoryData(@Path("id") int id);

    @GET("/api/v1/post/category/sorted_by=desc(createdDate)/{id}")
    Call<PostResponse> getCreatedCategoryData(@Path("id") int id);

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
    //Call<ResponseBody> uploadImg(@PartMap HashMap<String, RequestBody> partmap, @Path("id") int id);


    @POST("/api/v1/auth")
    Call<LoginResponse> LoginRequest(@Body LoginRequest loginRequest);


    @GET("/api/v1/post/search")
    Call<PostResponse> getSearchData(@Query(value = "keyword") String keyword);


    @GET("/api/v1/chat/room")
    Call<ChatRoomResponse> getChatRoomList();

    @GET("/api/v1/chat/message/{roomId}")
    Call<ChatResponse> getChats(@Path("roomId") int id);

    @DELETE("/api/v1/chat/room/{roomId}")
    Call<ResponseBody> exitChatRoom(@Path("roomId") int id);

    @GET("/api/v1/chat/user/{roomId}")
    Call<ChatUserResponse> getChatUser(@Path("roomId") int id);

    @GET("/api/v1/chat/room/{roomId}")
    Call<ChatRoomInformResponse> getChatRoom(@Path("roomId") int id);


    @GET("/api/v1/profile")
    Call<UserProfileResponse> getUserProfile();

    @DELETE("/api/v1/post/{id}")
    Call<ResponseBody> deleteShare(@Path("id") int id);

    @POST("/api/v1/chat/room/{id}")
    Call<ResponseBody> enterChatRoom(@Path("id") int id);

    @GET("/api/v1/chat/promise/{id}")
    Call<ChatPromiseResponse> getPromise(@Path("id") int id);

    @PATCH("/api/v1/chat/promise/{id}")
    Call<ResponseBody> setPromise(@Path("id") int id, @Body ChatPromiseRequest chatPromiseRequest);


    //@POST("/api/v1/auth")
    //Call<LoginResponse> LoginRequest(@Body LoginRequest loginRequest);

   // @PATCH("/api/v1/upload/profile/:id")
    //Call<ProfileImgResponse>

    @Multipart
    @PATCH("/api/v1/upload/profile/{id}")
    Call<ProfileUploadResponse> ProfileUploadRequest(@Part MultipartBody.Part file, @Path("id") int id);

    @POST("/api/v1/auth/nickname")
    Call<AuthNickResponse> AuthNickRequest(@Body AuthNickRequset authNickRequset);

}
