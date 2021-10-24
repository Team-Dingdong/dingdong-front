package org.techtown.dingdong.network;

import org.techtown.dingdong.chatting.ChatPromiseRequest;
import org.techtown.dingdong.chatting.ChatPromiseResponse;
import org.techtown.dingdong.chatting.ChatResponse;
import org.techtown.dingdong.chatting.ChatRoomInformResponse;
import org.techtown.dingdong.chatting.ChatRoomResponse;
import org.techtown.dingdong.chatting.ChatUserResponse;
import org.techtown.dingdong.home.EditResponse;
import org.techtown.dingdong.home.LocalResponse;
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
import org.techtown.dingdong.login_register.TokenRefreshRequest;
import org.techtown.dingdong.mypage.UserRatingRequest;
import org.techtown.dingdong.mypage.UserRatingResponse;
import org.techtown.dingdong.profile.HistoryResponse;
import org.techtown.dingdong.profile.UserProfileRequest;
import org.techtown.dingdong.mytown.AuthLocalRequest;
import org.techtown.dingdong.mytown.AuthLocalResponse;
import org.techtown.dingdong.mytown.localResponse;
import org.techtown.dingdong.profile.UserProfileResponse;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Apiinterface {

    @GET("/api/v1/post")
    Call<PostResponse> getData(@Query("page") int num);


    @GET("/api/v1/post/sort=desc&sortby=createdDate&local/{id}")
    Call<PostResponse> getCreatedData(@Path("id") int id, @Query("page") int num);

    @GET("/api/v1/post/sort=desc&sortby=endDate&local/{id}")
    Call<PostResponse> getEndData(@Path("id") int id, @Query("page") int num);

    @POST("/api/v1/auth/reissue")
    Call<LoginResponse> getRefresh(@Body TokenRefreshRequest tokenRefreshRequest);

    @GET("/api/v1/post/sort=desc&sortby=category&endDate&local/{id}/{regionid}")
    Call<PostResponse> getCategoryData(@Path("id") int id);

    @GET("/api/v1/post/sort=desc&sortby=category&endDate&local/{id}/{regionid}")
    Call<PostResponse> getEndCategoryData(@Path("id") int id, @Path("regionid") int regionid, @Query("page") int num);

    @GET("/api/v1/post/sort=desc&sortby=category&createdDate&local/{id}/{regionid}")
    Call<PostResponse> getCreatedCategoryData(@Path("id") int id, @Path("regionid") int regionid, @Query("page") int num);

    @Multipart
    @POST("/api/v1/post/{id}")
    Call<EditResponse> setPost(@Part ArrayList<MultipartBody.Part> input, @Part ArrayList<MultipartBody.Part> files, @Path("id") int id);


    @GET("/api/v1/post/{id}")
    Call<ShareResponse> getShare(@Path("id") int id);

    @POST("/api/v1/auth/send-sms")
    Call<AuthResponse> setAuth(@Body AuthRequest authRequest);


    @Multipart
    @POST("/api/v1/post/edit/{id}")
    Call<ResponseBody> setPatch(@Part ArrayList<MultipartBody.Part> input, @Part ArrayList<MultipartBody.Part> files, @Path("id") int id);


    @Multipart
    @PATCH("/api/v1/upload/post/{id}")
    Call<ResponseBody> uploadImg(@Part ArrayList<MultipartBody.Part> files, @Part ArrayList<MultipartBody.Part> urls, @Path("id") int id);

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

    @GET("/api/v1/profile/{userId}")
    Call<UserProfileResponse> getOtherUserProfile(@Path("userId") int id);

    @Multipart
    @POST("/api/v1/profile")
    Call<ResponseBody> setUpdateProfile(@Part MultipartBody.Part file, @Part MultipartBody.Part nickname);

    @DELETE("/api/v1/post/{id}")
    Call<ResponseBody> deleteShare(@Path("id") int id);

    @POST("/api/v1/chat/room/{id}")
    Call<ResponseBody> enterChatRoom(@Path("id") int id);

    @GET("/api/v1/chat/promise/{id}")
    Call<ChatPromiseResponse> getPromise(@Path("id") int id);

    @POST("/api/v1/chat/promise/vote/{id}")
    Call<ResponseBody> votePromise(@Path("id") int id);

    @POST("/api/v1/chat/promise/{id}")
    Call<ResponseBody> setPromise(@Path("id") int id, @Body ChatPromiseRequest chatPromiseRequest);

    @PATCH("/api/v1/chat/promise/{id}")
    Call<ResponseBody> setPatchPromise(@Path("id") int id, @Body ChatPromiseRequest chatPromiseRequest);

    @Multipart
    @PATCH("/api/v1/upload/profile/{id}")
    Call<ProfileUploadResponse> ProfileUploadRequest(@Part MultipartBody.Part file, @Path("id") int id);

    @POST("/api/v1/auth/nickname")
    Call<AuthNickResponse> AuthNickRequest(@Body AuthNickRequset authNickRequset);

    @PATCH("/api/v1/auth/unsubscribe")
    Call<ResponseBody> leaveUser();

    @POST("/api/v1/rating/{id}")
    Call<ResponseBody> ratingUser(@Path("id") int id, @Body UserRatingRequest userRatingRequest);

    @GET("/api/v1/rating")
    Call<UserRatingResponse> getRating();

    @GET("/api/v1/post/user/sell")
    Call<HistoryResponse> getSalesHistory();

    @GET("/api/v1/post/user/buy")
    Call<HistoryResponse> getPurchasesHistory();

    @PATCH("/api/v1/auth/nickname")
    Call<ResponseBody> setProfile(@Body UserProfileRequest userProfileRequest);


    @GET("/api/v1/auth/local")
    Call<localResponse> getLocal(@Query("city") String city,
                                 @Query("district") String district);

    @POST("/api/v1/auth/local")
    Call<AuthLocalResponse> authLocal(@Body AuthLocalRequest authLocalRequest);

    @GET("/api/v1/auth/logout")
    Call<ResponseBody> logoutUser();

    @GET("/api/v1/profile/local")
    Call<LocalResponse> getLocal();

    @Multipart
    @POST("/api/v1/profile/report/{id}")
    Call<ResponseBody> reportUser(@Path("id") int id, @Part MultipartBody.Part nickname);

    @GET("/api/v1/post/user/{id}")
    Call<PostResponse> getUserSalesHistory(@Path("id") int id);

}
