package org.techtown.dingdong.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.login_register.SetProfileActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.mypage.HistoryAdapter;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.util.ArrayList;

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

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);
        token.setContext(UserProfileActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Log.d("토큰", id);

        getProfile(token);

        setDummy();

        recyclerView.setLayoutManager(new LinearLayoutManager(UserProfileActivity.this, LinearLayoutManager.VERTICAL, false));
        historyAdapter = new HistoryAdapter(salesList,UserProfileActivity.this,"profile");
        recyclerView.setAdapter(historyAdapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                getMenuInflater().inflate(R.menu.menu_user_profile, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.block:
                                popupMenu.dismiss();
                                break;

                            case R.id.report:
                                popupMenu.dismiss();
                                break;
                        }

                        return false;
                    }
                });

                popupMenu.show();
            }
        });

    }

    public void getProfile(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, UserProfileActivity.this);
        Call<UserProfileResponse> call = apiinterface.getOtherUserProfile(Integer.parseInt(id));
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("PROFILE_READ_SUCCESS")) {
                        UserProfileResponse.Data res = response.body().getData();
                        tv_nickname.setText(res.getNickname());
                        Glide.with(UserProfileActivity.this)
                                .load(res.getProfileImg())
                                .into(img_profile);
                    }
                }
                else if(response.code() == 404){
                    Toast.makeText(UserProfileActivity.this, "해당 유저를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 200);
                }
                else{
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

            }
        });

    }

    public void setDummy(){
        salesList = new ArrayList<>();
        salesList.add(new Share("hhi","","","","apdls","2021-01-01 01:01","20000","","5","3"));
        salesList.add(new Share("hhi","","","","apdls","2021-01-01 01:01","20000","","5","5"));
        salesList.add(new Share("hhi","","","","apdls","2021-01-01 01:01","20000","","5","3"));
        salesList.add(new Share("hhi","","","","apdls","2021-01-01 01:01","20000","","5","1"));
    }
}