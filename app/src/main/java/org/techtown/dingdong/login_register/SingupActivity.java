package org.techtown.dingdong.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;
import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingupActivity extends AppCompatActivity {

    TextView tv_nick;
    String message;
    Button btn_ok;
    ImageButton imgbtn_profile;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.acesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        Token token = new Token(access, refresh, expire, tokentype);

        tv_nick = (TextView)findViewById(R.id.tv_nick);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        et = (EditText)findViewById(R.id.et);
        imgbtn_profile = (ImageButton)findViewById(R.id.imgbtn_profile);

        tv_nick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                message= tv_nick.getText().toString();
                if(message.length() <=15){
                    AuthNickRequset authNickRequset = new AuthNickRequset(message);
                    Log.d("tag", message);
                    Apiinterface apiinterface = Api.createService(Apiinterface.class);
                    Call<AuthNickResponse> call = apiinterface.AuthNickRequest(authNickRequset);
                    call.enqueue(new Callback<AuthNickResponse>() {
                        @Override
                        public void onResponse(Call<AuthNickResponse> call, Response<AuthNickResponse> response) {

                            if(response.isSuccessful()){

                                if(response.body().code.equals("NICKNAME_CREATE_SUCCESS")){

                                    btn_ok.setEnabled(true);

                                }
                                else if(response.body().code.equals("NICKNAME_DUPLICATION")){
                                    //edittext
                                    et.setText("다른 닉네임을 설정해주세요.");
                                }
                            }
                            else{
                                Log.d("문제발생", String.valueOf(response));
                            }



                        }

                        @Override
                        public void onFailure(Call<AuthNickResponse> call, Throwable t) {

                            Log.d("tag", t.toString());

                        }
                    });

                }
                else{
                    btn_ok.setEnabled(false);
                }
            }
        });

        imgbtn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //화면 전환
            }
        });

    }
}