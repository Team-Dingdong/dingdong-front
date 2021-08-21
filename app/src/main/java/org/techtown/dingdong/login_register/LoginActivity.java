package org.techtown.dingdong.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.dingdong.R;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edt_number_validation;
    Button btn_validation;
    String authNumber;
    Button startbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //버튼, 텍스트에딧 연결
        edt_number_validation = findViewById(R.id.edt_number_validation);
        btn_validation = findViewById(R.id.btn_validation);

        //인텐트, 객체 받아오기
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");

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

                LoginRequest loginRequest = new LoginRequest(phoneNumber, authNumber);
                Apiinterface apiinterface = Api.getClient().create(Apiinterface.class);
                Call<Loginitem> call = apiinterface.LoginRequest(loginRequest);

                call.enqueue(new Callback<Loginitem>() {
                    @Override
                    public void onResponse(Call<Loginitem> call, Response<Loginitem> response) {
                        if(response.isSuccessful() && response.body() != null){
                            Loginitem loginitem = response.body();
                            LoginResponse loginResponse = loginitem.loginResponse;
                            int res = loginitem.status;

                            if(res == 201){
                                //회원가입

                            }
                            else if(res == 200){

                                //로그인
                            }
                            else if(res == 400){
                                //시간초과
                            }
                            else{
                                //기타예외
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Loginitem> call, Throwable t) {

                    }
                });



            }
        });
    }
}