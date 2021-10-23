package org.techtown.dingdong.login_register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.HomeFragment;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;
import org.techtown.dingdong.profile.profileFragment;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {

    View toolbar3;
    EditText edt_number_validation;
    Button btn_validation;
    String authNumber, phoneNumber;
    Button startbutton;
    private TextView tv_timer;
    private static final int MILLISINFUTURE = 301;
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private int count = 300;
    public CountDownTimer countDownTimer;
    String time = "2021-08-21T12:23:19.883418";
    String str;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //버튼, 텍스트에딧 연결
        edt_number_validation = findViewById(R.id.edt_number_validation);
        btn_validation = findViewById(R.id.btn_validation);
        tv_timer = findViewById(R.id.tv_timer);
        toolbar3 = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3);




        //인텐트, 객체 받아오기
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        time = intent.getStringExtra("time");
        
        //timestamp 형식에 맞게 변환함
        //5분 - (현재시간 - 요청시간)
        time = time.substring(0,10) + " " +time.substring(11,19);
        Timestamp curtime = new Timestamp(System.currentTimeMillis());
        Timestamp reqtime = Timestamp.valueOf(time);
        long myT = MILLISINFUTURE - (curtime.getTime() - reqtime.getTime())/1000%60;
        count = (int) myT;

        countDownTimer = new CountDownTimer( myT*1000,COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                str = String.valueOf(count/60) + ":" + String.valueOf((count%60<=9)?"0"+count%60:count%60);
                tv_timer.setText(str);
                count --;
            }

            @Override
            public void onFinish() {
                count = (int) myT;
            }
        };

        countDownTimer.start();




        //인증번호 6자리 입력하면 버튼 활성화
        edt_number_validation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                authNumber= edt_number_validation.getText().toString();
                if(authNumber.length() == 6){
                    btn_validation.setEnabled(true);
                }
                else{
                    btn_validation.setEnabled(false);
                }
            }
        });

        btn_validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버로 폰번호,인증번호 전달

                authNumber= edt_number_validation.getText().toString();
                //phoneNumber = "01022222222";

                LoginRequest loginRequest = new LoginRequest(phoneNumber, authNumber);
                Apiinterface apiinterface = Api.createService(Apiinterface.class);
                Call<LoginResponse> call = apiinterface.LoginRequest(loginRequest);
                Log.w(phoneNumber, authNumber);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {


                        if(response.isSuccessful()){

                            //기기 내부에 토큰 정보 저장
                            SharedPreferences preferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

                            Log.d("로그인성공", new Gson().toJson(response.body()));
                            LoginResponse.Data mToken = response.body().data;
                            Log.d("로그인성공", mToken.getAccessToken());
                            Token token = new Token(mToken.getAccessToken(),mToken.getRefreshToken(),mToken.getExpireIn(),mToken.getTokentype());
                            preferences.edit().putBoolean("oauth.loggedin",true).apply();
                            preferences.edit().putString("oauth.accesstoken", token.getAccessToken()).apply();
                            preferences.edit().putString("oauth.refreshtoken", token.getRefreshToken()).apply();
                            preferences.edit().putString("oauth.expire", token.getExpireIn()).apply();
                            preferences.edit().putString("oauth.tokentype", token.getGrantType()).apply();

                            if(response.body().result.equals("SIGNUP_SUCCESS")){

                                Log.d("회원가입성공", String.valueOf(response));
                                Intent intent_signup = new Intent(LoginActivity.this, SetProfileActivity.class);
                                intent_signup.putExtra("state","signup");
                                startActivity(intent_signup);
                            }

                            else if(response.body().result.equals("LOGIN_SUCCESS")){


                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                                return;


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
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d("실패", "외않되");

                    }


                });



            }
        });
    }
}