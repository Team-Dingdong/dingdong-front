package org.techtown.dingdong.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.chatting.ChattingActivity;
import org.techtown.dingdong.home.PostResponse;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.login_register.SetProfileActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.mypage.HistoryAdapter;
import org.techtown.dingdong.mypage.UserRatingResponse;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    TextView tv_like, tv_dislike, tv_nickname;
    ImageView img_profile;
    RecyclerView recyclerView;
    Token token;
    ArrayList<Share> salesList = new ArrayList<>();
    HistoryAdapter historyAdapter;
    private String id = "1";
    ImageButton btn_back, btn_more;
    String username, nickname="띵동";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tv_dislike = findViewById(R.id.tv_dislike);
        tv_like = findViewById(R.id.tv_like);
        tv_nickname = findViewById(R.id.tv_nickname);
        img_profile = findViewById(R.id.img_profile);
        recyclerView = findViewById(R.id.recycler_sales);
        btn_back = findViewById(R.id.ic_back);
        btn_more = findViewById(R.id.ic_more);

        SharedPreferences pref = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);
        token.setContext(UserProfileActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Log.d("토큰", id);


        recyclerView.setLayoutManager(new LinearLayoutManager(UserProfileActivity.this, LinearLayoutManager.VERTICAL, false));
        historyAdapter = new HistoryAdapter(salesList,UserProfileActivity.this,"profile");
        recyclerView.setAdapter(historyAdapter);

        getUser(token); //클라이언트 정보
        getHistory(token);
        getProfile(token); //프로필 정보
        getRating(token);

        if(nickname.equals("탈퇴한 회원")){
            btn_more.setVisibility(View.GONE);
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!username.equals(nickname)){
                    final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                    getMenuInflater().inflate(R.menu.menu_user_profile, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                /*case R.id.block:
                                    popupMenu.dismiss();
                                    break;*/

                                case R.id.report:
                                    popupMenu.dismiss();
                                    final EditText editText = new EditText(UserProfileActivity.this);
                                    //글자수 제한을 위한 인풋필터 코드 처리
                                    InputFilter[] inputFilters = new InputFilter[1];
                                    inputFilters[0] = new InputFilter.LengthFilter(30);
                                    editText.setFilters(inputFilters);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                                    builder.setTitle("신고 사유를 입력하세요.").setMessage("30자 이내로 입력이 가능합니다.");
                                    builder.setView(editText);
                                    builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.d("신고사유",editText.getText().toString());
                                            if(editText.getText().toString().length() > 0){
                                            Apiinterface apiinterface = Api.createService(Apiinterface.class, token, UserProfileActivity.this);
                                            MultipartBody.Part reason = MultipartBody.Part.createFormData("reason", editText.getText().toString());
                                            Call<ResponseBody> call = apiinterface.reportUser(Integer.parseInt(id), reason);
                                            call.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if(response.isSuccessful() && response.body() != null){
                                                        if(response.code() == 200) {
                                                            Toast.makeText(UserProfileActivity.this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else if(response.code() == 403){
                                                        Toast.makeText(UserProfileActivity.this, "본인은 신고할 수 없습니다", Toast.LENGTH_SHORT).show();

                                                    }else if(response.code() == 404){
                                                        Toast.makeText(UserProfileActivity.this, "해당 유저 정보를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();

                                                    }else if(response.code() == 409){
                                                        Toast.makeText(UserProfileActivity.this, "이미 신고한 사용자입니다", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Log.d("userporf.report", new Gson().toJson(response.errorBody()));
                                                        Log.d("userporf.report", response.toString());
                                                        Log.d("userporf.report", String.valueOf(response.code()));
                                                        Log.d("userporf.report", response.message());
                                                        Log.d("userporf.report", String.valueOf(response.raw().request().url().url()));
                                                        Log.d("userporf.report", new Gson().toJson(response.raw().request()));
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                    Log.d("userporf.report",t.toString());

                                                }
                                            });
                                        }
                                        else{
                                                Toast.makeText(UserProfileActivity.this, "사유를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                            }}
                                    }).show();
                                    break;
                            }

                            return false;
                        }
                    });

                    popupMenu.show();
                }else{

                    final PopupMenu popupMenu2 = new PopupMenu(getApplicationContext(), v);
                    getMenuInflater().inflate(R.menu.menu_user_profile_mine, popupMenu2.getMenu());
                    popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.correct:
                                    Intent intent = new Intent(UserProfileActivity.this, SetProfileActivity.class);
                                    intent.putExtra("state","correct");
                                    startActivity(intent);
                                    popupMenu2.dismiss();
                                    break;
                            }

                            return false;
                        }
                    });

                    popupMenu2.show();

                }

            }
        });

    }

    public void getProfile(Token token){
        Log.d("getprofile", id + "hello");
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, UserProfileActivity.this);
        Call<UserProfileResponse> call = apiinterface.getOtherUserProfile(Integer.parseInt(id));
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("PROFILE_READ_SUCCESS")) {
                        UserProfileResponse.Data res = response.body().getData();
                        Log.d("성공", new Gson().toJson(res));
                        nickname = res.getNickname();
                        if(nickname.equals("null")){
                            tv_nickname.setText("탈퇴한 회원");
                        }else{
                            tv_nickname.setText(res.getNickname());
                        }
                        Glide.with(UserProfileActivity.this)
                                .load(res.getProfileImg())
                                .into(img_profile);
                    }
                }else if(response.code() == 404){
                    Log.w("userprof,getother","해당 프로필을 찾을 수 없습니다");
                    Toast.makeText(UserProfileActivity.this, "해당 프로필을 찾을 수 없습니다.", Toast.LENGTH_LONG);
                    finish();
                }
                else{
                    Log.d("userprof,getother", new Gson().toJson(response.errorBody()));
                    Log.d("userprof,getother", response.toString());
                    Log.d("userprof,getother", String.valueOf(response.code()));
                    Log.d("userprof,getother", response.message());
                    Log.d("userprof,getother", String.valueOf(response.raw().request().url().url()));
                    Log.d("userprof,getother", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.d("userprof,getother", String.valueOf(t));

            }
        });

    }

    public void getRating(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, UserProfileActivity.this);
        Call<UserRatingResponse> call = apiinterface.getUserRating(Integer.parseInt(id));
        call.enqueue(new Callback<UserRatingResponse>() {
            @Override
            public void onResponse(Call<UserRatingResponse> call, Response<UserRatingResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("RATING_READ_SUCCESS")) {
                        UserRatingResponse.Data res = response.body().getData();
                        tv_like.setText(res.getGood());
                        tv_dislike.setText(res.getBad());
                    }
                }
                else if(response.code() == 404){
                    Log.w("userprof,getrating","해당 유저에 대한 평가를 찾을 수 없습니다");
                }
                else{
                    Log.d("userprof,getrating", new Gson().toJson(response.errorBody()));
                    Log.d("userprof,getrating", response.toString());
                    Log.d("userprof,getrating", String.valueOf(response.code()));
                    Log.d("userprof,getrating", response.message());
                    Log.d("userprof,getrating", String.valueOf(response.raw().request().url().url()));
                    Log.d("userprof,getrating", new Gson().toJson(response.raw().request()));
                }

            }

            @Override
            public void onFailure(Call<UserRatingResponse> call, Throwable t) {
                Log.d("userprof,getrating", String.valueOf(t));
            }
        });
    }

    public void getUser(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, UserProfileActivity.this);
        Call<UserProfileResponse> call = apiinterface.getUserProfile();
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {

                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().equals("PROFILE_READ_SUCCESS")) {
                        UserProfileResponse res = response.body();
                        username = res.getData().getNickname();
                    }
                }else if(response.code() == 404){
                    Log.w("userprof,getuserprof","해당 프로필을 찾을 수 없습니다");
                }
                else{

                    Log.d("userprof,getuserprof", new Gson().toJson(response.errorBody()));
                    Log.d("userprof,getuserprof", response.toString());
                    Log.d("userprof,getuserprof", String.valueOf(response.code()));
                    Log.d("userprof,getuserprof", response.message());
                    Log.d("userprof,getuserprof", String.valueOf(response.raw().request().url().url()));
                    Log.d("userprof,getuserprof", new Gson().toJson(response.raw().request()));

                }

            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {

                Log.d("userprof,getuserprof", String.valueOf(t));

            }
        });

    }

    public void getHistory(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, UserProfileActivity.this);
        Call<PostResponse> call = apiinterface.getUserSalesHistory(Integer.parseInt(id));
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        if(!salesList.isEmpty()){
                            salesList = new ArrayList<>();
                        }

                        salesList.addAll(res.getData().getShare());
                        historyAdapter.notifyDataSetChanged();
                    }

                }else{
                    Log.d("userprof,getsaleshis", new Gson().toJson(response.errorBody()));
                    Log.d("userprof,getsaleshis", response.toString());
                    Log.d("userprof,getsaleshis", String.valueOf(response.code()));
                    Log.d("userprof,getsaleshis", response.message());
                    Log.d("userprof,getsaleshis", String.valueOf(response.raw().request().url().url()));
                    Log.d("userprof,getsaleshis", new Gson().toJson(response.raw().request()));
                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}