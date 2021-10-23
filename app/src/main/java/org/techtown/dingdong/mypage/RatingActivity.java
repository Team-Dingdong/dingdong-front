package org.techtown.dingdong.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.chatting.ChatUser;
import org.techtown.dingdong.chatting.ChatUserAdapter;
import org.techtown.dingdong.chatting.ChatUserResponse;
import org.techtown.dingdong.chatting.UserListActivity;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.home.ShareResponse;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;
import org.techtown.dingdong.profile.UserProfileActivity;
import org.techtown.dingdong.profile.UserProfileResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tv_title, tv_hashtag, tv_price, tv_date;
    ImageView imageView;
    ArrayList<ChatUser> chatUsers = new ArrayList<>();
    ArrayList<String> ratings = new ArrayList<>();
    UserRatingAdapter userRatingAdapter;
    Button btn_finish;
    Boolean getcheck;
    ImageButton btn_back;
    Token token;
    String id;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        recyclerView = findViewById(R.id.recycler_user);
        btn_finish = findViewById(R.id.btn_finish);
        btn_back = findViewById(R.id.ic_back);
        imageView = findViewById(R.id.imageView);
        tv_date = findViewById(R.id.tv_date);
        tv_hashtag = findViewById(R.id.tv_hashtag);
        tv_price = findViewById(R.id.tv_price);
        tv_title = findViewById(R.id.tv_title);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);
        token.setContext(RatingActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        //setDummy();

        recyclerView.setLayoutManager(new LinearLayoutManager(RatingActivity.this, LinearLayoutManager.VERTICAL, false));
        userRatingAdapter = new UserRatingAdapter(chatUsers, RatingActivity.this);
        recyclerView.setAdapter(userRatingAdapter);

        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, RatingActivity.this);
        Call<UserProfileResponse> call = apiinterface.getUserProfile();
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {

                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().equals("PROFILE_READ_SUCCESS")) {
                        UserProfileResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        username = res.getData().getNickname();
                    }
                }else{

                    Log.d("실패", new Gson().toJson(response.errorBody()));
                    Log.d("실패", response.toString());
                    Log.d("실패", String.valueOf(response.code()));
                    Log.d("실패", response.message());
                    Log.d("실패", String.valueOf(response.raw().request().url().url()));
                    Log.d("실패", new Gson().toJson(response.raw().request()));


                }

            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {

                Log.d("외않되", String.valueOf(t));

            }
        });

        getUserList(token);
        setShare(token);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(RatingActivity.this);

                dialog.setMessage("유저 평가는 수정이 불가능합니다.")
                        .setTitle("평가를 완료하시겠어요?")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Dialog", "아니오");
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Dialog", "네");
                                getcheck = true;
                                for(int i=0;i<chatUsers.size();i++){
                                    //ratings.add(i,chatUsers.get(i).getRating());
                                    if(chatUsers.get(i).getRating().equals("NONE")){
                                        getcheck = false;
                                        break;
                                    }
                                }

                                if(getcheck){
                                    for(int i=0; i<chatUsers.size();i++){
                                        if(getcheck){
                                        rateUser(token,i);
                                        }else{
                                            break;
                                        }
                                    }
                                    if(getcheck){
                                    Toast.makeText(RatingActivity.this,"평가 완료",Toast.LENGTH_LONG).show();
                                    finish();
                                    }
                                }
                                else{
                                    Toast.makeText(RatingActivity.this,"평가가 완료되지 않았습니다. 평가를 완료해주세요.",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        })
                        .show();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void rateUser(Token token, int i){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, RatingActivity.this);
        UserRatingRequest userRatingRequest = new UserRatingRequest(chatUsers.get(i).getRating());
        Call<ResponseBody> call = apiinterface.ratingUser(Integer.parseInt(chatUsers.get(i).getId()), userRatingRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code() == 201) {

                }else{
                    Log.d("실패", new Gson().toJson(response.errorBody()));
                    Log.d("실패", response.toString());
                    Log.d("실패", String.valueOf(response.code()));
                    Log.d("실패", response.message());
                    Log.d("실패", String.valueOf(response.raw().request().url().url()));
                    Log.d("실패", new Gson().toJson(response.raw().request()));
                    getcheck = false;
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("외않되", String.valueOf(t));
                getcheck = false;

            }
        });

    }

    private void getUserList(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, RatingActivity.this);
        Call<ChatUserResponse> call = apiinterface.getChatUser(Integer.parseInt(id));
        call.enqueue(new Callback<ChatUserResponse>() {
            @Override
            public void onResponse(Call<ChatUserResponse> call, Response<ChatUserResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().equals("CHAT_ROOM_USER_READ_SUCCESS")) {
                        ChatUserResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        ArrayList<ChatUser> temp = new ArrayList<>();
                        for(int i=0; i < res.getChatUsers().size() ; i++){
                            if(!res.getChatUsers().get(i).getUsername().equals(username)){
                                temp.add(res.getChatUsers().get(i));
                            }
                        }
                        chatUsers.addAll(temp);
                        userRatingAdapter.notifyDataSetChanged();

                    }
                }else{
                    Log.d("실패", new Gson().toJson(response.errorBody()));
                    Log.d("실패", response.toString());
                    Log.d("실패", String.valueOf(response.code()));
                    Log.d("실패", response.message());
                    Log.d("실패", String.valueOf(response.raw().request().url().url()));
                    Log.d("실패", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<ChatUserResponse> call, Throwable t) {

            }
        });
    }

    private void setShare(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, RatingActivity.this);
        Call<ShareResponse> call = apiinterface.getShare(Integer.parseInt(id));
        call.enqueue(new Callback<ShareResponse>() {
            @Override
            public void onResponse(Call<ShareResponse> call, Response<ShareResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().equals("POST_READ_SUCCESS")) {
                        ShareResponse res = response.body();
                        Share share = res.getShare();
                        tv_title.setText(share.getTitle());
                        tv_date.setText(share.getDate());
                        tv_price.setText(priceFormat(share.getPrice()));
                        Glide.with(RatingActivity.this)
                                .load(share.getImage1())
                                .into(imageView);
                        List<String> hashtag = share.getHashtag();
                        String str="";
                        for(int i=0; i < hashtag.size(); i++){
                            str += hashtag.get(i);
                        }
                        tv_hashtag.setText(str);
                    }
                }
                else{

                }
            }

            @Override
            public void onFailure(Call<ShareResponse> call, Throwable t) {

            }
        });
    }



    public String priceFormat(String price){

        DecimalFormat df = new DecimalFormat("#,###");
        String res_price = "";
        res_price = df.format(Double.parseDouble(price.replaceAll(",","")));

        return res_price;
    }

}