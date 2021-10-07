package org.techtown.dingdong.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.chatting.ChattingActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareDetailActivity extends AppCompatActivity {


    private ViewPager2 sliderImageViewPager;
    private SpringDotsIndicator indicator;
    private String[] images = new String[3];
    private String detail = "코로나19 예방접종 대응추진단(추진단)의 ‘코로나19 예방접종 8~9월 시행계획’에 따르면, 1972년 1월 1일생부터 2003년 12월 31일 출생자 1천777만 명이 접종 대상자다. 사전예약은 8월 9일부터 18일까지 10개 대상군으로 나눠 실시된다.\n" +
            "\n" +
            "날짜별 예약 대상은 해당 날짜 끝자리와 생년월일 끝자리가 일치하는 사람으로 지정된다. 가령, 예약이 시작되는 9일의 경우, 생년월일 끝자리가 9인 사람들이 예약 대상이다. 날짜별 예약은 오후 8시부터 이튿날 오후 6시까지 진행된다.";
    private String title = "18~49세 다음달 9일부터 10부제";
    private TextView tv_detail, tv_title, tv_userbio, tv_username, tv_like, tv_dislike, tv_place, tv_people, tv_people2, tv_info, tv_price, tv_category, tv_hashtag;
    private ImageButton btn_back, btn_more;
    private String id;
    private ImageView img_profile;
    private Button btn_enroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);

        sliderImageViewPager = findViewById(R.id.image_slider);
        indicator = findViewById(R.id.indicator);
        tv_detail = findViewById(R.id.tv_detail);
        tv_title = findViewById(R.id.tv_title);
        btn_back = findViewById(R.id.ic_back);
        btn_more = findViewById(R.id.ic_more);
        img_profile = findViewById(R.id.imageView2);
        tv_username = findViewById(R.id.textView4);
        tv_userbio = findViewById(R.id.textView5);
        tv_like = findViewById(R.id.tv_like);
        tv_dislike = findViewById(R.id.tv_dislike);
        tv_place = findViewById(R.id.tv_place);
        tv_people = findViewById(R.id.tv_pepolenum);
        tv_people2 = findViewById(R.id.tv_pepolenum2);
        tv_info = findViewById(R.id.tv_info);
        tv_price = findViewById(R.id.tv_price);
        btn_enroll = findViewById(R.id.btn_enroll);
        tv_category = findViewById(R.id.tv_category);
        tv_hashtag = findViewById(R.id.hashtag);


        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken", "");
        String refresh = pref.getString("oauth.refreshtoken", "");
        String expire = pref.getString("oauth.expire", "");
        String tokentype = pref.getString("oauth.tokentype", "");

        Token token = new Token(access, refresh, expire, tokentype);
        token.setContext(ShareDetailActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        setShare(token);

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                getMenuInflater().inflate(R.menu.menu_share_detail, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.correct:
                                Intent intent = new Intent(ShareDetailActivity.this, EditActivity.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                                popupMenu.dismiss();
                                break;
                            case R.id.delete:
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ShareDetailActivity.this);

                                dialog.setMessage("게시물을 삭제하시겠어요?")
                                        .setTitle("게시물 삭제")
                                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Dialog", "아니오");
                                            }
                                        })
                                        .setNegativeButton("네", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.i("Dialog", "네");

                                                Apiinterface apiinterface = Api.createService(Apiinterface.class,token,ShareDetailActivity.this);

                                                Call<ResponseBody> call = apiinterface.deleteShare(Integer.parseInt(id));
                                                call.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                                        if (response.isSuccessful()) {

                                                            if (response.code() == 200) {
                                                                Log.d("성공", new Gson().toJson(response.code()));

                                                                Toast.makeText(ShareDetailActivity.this, "삭제가 완료되었습니다.", Toast.LENGTH_LONG).show();

                                                                //핸들러를 통한 액티비티 종료 시점 조절
                                                                Handler handler = new Handler();
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        finish();
                                                                    }
                                                                }, 1000);

                                                            }


                                                        } else {

                                                            Log.d("실패", new Gson().toJson(response.errorBody()));
                                                            Log.d("실패", response.toString());
                                                            Log.d("실패", String.valueOf(response.code()));
                                                            Log.d("실패", response.message());
                                                            Log.d("실패", String.valueOf(response.raw().request().url().url()));
                                                            Log.d("실패", new Gson().toJson(response.raw().request()));

                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        Log.d("외않되", String.valueOf(t));

                                                    }
                                                });
                                            }
                                        }).show();
                                popupMenu.dismiss();
                                break;
                        }


                        return false;
                    }
                });
                popupMenu.show();
            }

        });



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Apiinterface apiinterface = Api.createService(Apiinterface.class,token,ShareDetailActivity.this);
                Log.d("postedid",id);
                Call<ResponseBody> call = apiinterface.enterChatRoom(Integer.parseInt(id));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful() && response.body() != null){
                            if(response.code() == 201){
                                Toast.makeText(ShareDetailActivity.this,"채팅방 입장 성공.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ShareDetailActivity.this, ChattingActivity.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                            }

                        }else{

                            if(response.code() == 409){
                                Toast.makeText(ShareDetailActivity.this,"이미 입장한 채팅방입니다.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ShareDetailActivity.this, ChattingActivity.class);
                                intent.putExtra("id",id);
                                startActivity(intent);

                            }
                            else if(response.code() == 404){
                                Toast.makeText(ShareDetailActivity.this,"해당 채팅방을 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
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

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Log.d("외않되", String.valueOf(t));

                    }
                });

            }
        });

    }

    private void setShare(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,ShareDetailActivity.this);

        Log.d("postedid",id);
        Call<ShareResponse> call = apiinterface.getShare(Integer.parseInt(id));
        call.enqueue(new Callback<ShareResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<ShareResponse> call, Response<ShareResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        ShareResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        Share share;
                        share = res.getShare();
                        String people = share.getGatheredPeople() + "/" + share.getPersonnelcapacity();

                        //String json = new Gson().toJson(res.getData().getShare());
                        tv_detail.setText(share.getMaintext());
                        tv_title.setText(share.getTitle());
                        tv_dislike.setText(share.getUserbad());
                        tv_like.setText(share.getUsergood());
                        tv_username.setText(share.getUsername());
                        tv_userbio.setText(share.getUsertext());
                        tv_place.setText(share.getPlace());
                        tv_people.setText(people);
                        tv_people2.setText(people);
                        tv_price.setText(priceFormat(share.getPrice()));
                        tv_info.setText("을 " +share.getPersonnelcapacity()+"명이서 띵해요");
                        Glide.with(ShareDetailActivity.this)
                                .load(share.getProfileImg())
                                .into(img_profile);
                        tv_category.setText(share.getCategory());
                        List<String> hashtag = share.getHashtag();

                        String str="";
                        for(int i=0; i < hashtag.size(); i++){
                            str += hashtag.get(i);
                        }
                        Log.d("myhash",str);
                        tv_hashtag.setText(str);



                        if(share.getImage1()!=null){
                        if(!share.getImage1().equals("null")){

                            List<String> list = new ArrayList<String>();

                            list.add(share.getImage1());

                            if(!share.getImage2().equals("null")){
                                list.add(share.getImage2());
                            }
                            if(!share.getImage3().equals("null")){
                                list.add(share.getImage3());
                            }

                            images = list.toArray(new String[0]);

                            sliderImageViewPager.setOffscreenPageLimit(1);
                            sliderImageViewPager.setAdapter(new ImageSliderAdapter(ShareDetailActivity.this, images));
                            indicator.setViewPager2(sliderImageViewPager);

                        }}
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
            public void onFailure(Call<ShareResponse> call, Throwable t) {

                Log.d("외않되", String.valueOf(t));

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