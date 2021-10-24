package org.techtown.dingdong.mytown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TownActivity extends AppCompatActivity {



    Button button;
    ImageButton imgbtn_1, imgbtn_2, imgbtn_AddTown1, imgbtn_AddTown2 , btn_back;
    TextView tv_town1, tv_town2;
    String town1, town2;
    String num_town1, num_town2;
    String state;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town);

        //btn_back = findViewById(R.id.ic_back);
        button = findViewById(R.id.button);
        imgbtn_1 = findViewById(R.id.imgbtn_1);
        imgbtn_2 = findViewById(R.id.imgbtn_2);
        imgbtn_AddTown1 = findViewById(R.id.imgbtn_AddTown1);
        imgbtn_AddTown2 = findViewById(R.id.imgbtn_AddTown2);
        tv_town1 = findViewById(R.id.tv_town1);
        tv_town2 = findViewById(R.id.tv_town2);
        btn_back = findViewById(R.id.ic_back);


        SharedPreferences pref = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        Token token = new Token(access,refresh,expire,tokentype);

        town1="";
        town2="";

        Intent intent = getIntent();
        state = intent.getStringExtra("state");


       if(town1.equals("")){
            imgbtn_AddTown1.setVisibility(View.VISIBLE);
            imgbtn_1.setEnabled(false);
            imgbtn_1.setVisibility(View.INVISIBLE);
            tv_town1.setVisibility(View.INVISIBLE);
       }
       else {
            imgbtn_AddTown1.setVisibility(View.INVISIBLE);
            imgbtn_1.setEnabled(true);
            imgbtn_1.setVisibility(View.VISIBLE);
            tv_town1.setVisibility(View.VISIBLE);
        }

        if(town2.equals("")){
            imgbtn_AddTown2.setVisibility(View.VISIBLE);
            imgbtn_2.setEnabled(false);
            imgbtn_2.setVisibility(View.INVISIBLE);
            tv_town2.setVisibility(View.INVISIBLE);
        }
        else {
            imgbtn_AddTown2.setVisibility(View.INVISIBLE);
            imgbtn_2.setEnabled(true);
            imgbtn_2.setVisibility(View.VISIBLE);
            tv_town2.setVisibility(View.VISIBLE);
            button.setClickable(true);
            button.setBackgroundTintList(getResources().getColorStateList(R.color.blue));
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        imgbtn_AddTown1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TownWhenStartActivity.class);
               intent.putExtra("what", true);
                startActivityForResult(intent, 1);
                imgbtn_AddTown1.setVisibility(view.INVISIBLE);


            }
        });

        imgbtn_AddTown2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_town1.getText() == ""){
                    Toast.makeText(getApplicationContext(), "첫 번째 동을 먼저 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), TownWhenStartActivity.class);
                    intent.putExtra("what", false);
                    startActivityForResult(intent, 2);
                    imgbtn_AddTown2.setVisibility(view.INVISIBLE);
                }
            }
        });

        imgbtn_1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
              tv_town1.setText("");
              tv_town1.setVisibility(View.INVISIBLE);
              imgbtn_1.setEnabled(false);
              imgbtn_1.setVisibility(View.INVISIBLE);
              imgbtn_AddTown1.setVisibility(View.VISIBLE);
                button.setClickable(false);
                button.setBackgroundTintList(getResources().getColorStateList(R.color.grey));

            }
        });
        imgbtn_2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                tv_town2.setText("");
                tv_town2.setVisibility(View.INVISIBLE);
                imgbtn_2.setEnabled(false);
                imgbtn_2.setVisibility(View.INVISIBLE);
                imgbtn_AddTown2.setVisibility(View.VISIBLE);
                button.setClickable(false);
                button.setBackgroundTintList(getResources().getColorStateList(R.color.grey));
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버로 리퀘스트
                int local1, local2;
                local1 = Integer.parseInt(num_town1);
                local2 = Integer.parseInt(num_town2);
                AuthLocalRequest authLocalRequest = new AuthLocalRequest(local1, local2);
                Apiinterface apiinterface = Api.createService(Apiinterface.class, token, TownActivity.this);
                Call<AuthLocalResponse> call = apiinterface.authLocal(authLocalRequest);
                call.enqueue(new Callback<AuthLocalResponse>() {

                    @Override
                    public void onResponse(Call<AuthLocalResponse> call, Response<AuthLocalResponse> response) {
                        if(response.body().code.equals("LOCAL_CREATE_SUCCESS")){
                            Toast.makeText(getApplicationContext(),"동네 설정이 완료됐습니다.",Toast.LENGTH_LONG);
                            Log.d("TAG","동네설정 성공");

                            Intent intent = new Intent(TownActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthLocalResponse> call, Throwable t) {

                    }
                });

                //화면전환
                Intent intent = new Intent(TownActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode > 0 ) {
            switch (resultCode) {
                case 1:
                    town1= intent.getStringExtra("town");
                    num_town1= intent.getStringExtra("num");
                    tv_town1.setText(town1);
                    tv_town1.setVisibility(View.VISIBLE);
                    imgbtn_1.setVisibility(View.VISIBLE);
                    imgbtn_1.setEnabled(true);
                    imgbtn_AddTown2.setEnabled(true);
                    imgbtn_AddTown1.setVisibility(View.INVISIBLE);
                    if(town1.equals("")){
                        imgbtn_AddTown1.setVisibility(View.VISIBLE);
                        imgbtn_1.setEnabled(false);
                        imgbtn_1.setVisibility(View.INVISIBLE);
                        tv_town1.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 2:
                    String pre = intent.getStringExtra("town");

                    if(town1.equals(pre)){
                        //diglog로 변경할지

                        Toast.makeText(this , "첫 번째 동과 다른 동을 선택해주세요.",Toast.LENGTH_SHORT).show();
                       //두번째 버튼 이미지 재설정
                        tv_town2.setText("");
                        tv_town2.setVisibility(View.INVISIBLE);
                        imgbtn_2.setEnabled(false);
                        imgbtn_2.setVisibility(View.INVISIBLE);
                        imgbtn_AddTown2.setVisibility(View.VISIBLE);


                    }
                    else if(!town1.equals(pre) &&!pre.equals("")) {
                        town2= intent.getStringExtra("town");
                        num_town2= intent.getStringExtra("num");
                        tv_town2.setText(town2);
                        tv_town2.setVisibility(View.VISIBLE);
                        imgbtn_2.setVisibility(View.VISIBLE);
                        imgbtn_2.setEnabled(true);
                        button.setClickable(true);
                        button.setBackgroundTintList(getResources().getColorStateList(R.color.blue));
                        Log.d("동 확인",town2 + num_town2);
                    }
                    else if(pre.equals("")) {
                            imgbtn_AddTown2.setVisibility(View.VISIBLE);
                            imgbtn_2.setEnabled(false);
                            imgbtn_2.setVisibility(View.INVISIBLE);
                            tv_town2.setVisibility(View.INVISIBLE);

                    }

                    break;



            }
        }

    }



}