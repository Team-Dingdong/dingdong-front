package org.techtown.dingdong.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.techtown.dingdong.R;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOrRegisterActivity extends AppCompatActivity {
    private EditText et_phone;
    private Button btn_transfer;
    String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        et_phone = findViewById(R.id.et_phone);
        btn_transfer = findViewById(R.id.btn_transfer);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                message= et_phone.getText().toString();
                if(message.length() == 11){
                    btn_transfer.setEnabled(true);
                }
                else{
                    btn_transfer.setEnabled(false);
                }
            }
        });

        //클릭하면 서버로 번호 전달, 인증번호받고
        btn_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼 클릭하면 인증하는 화면으로 전환하고, 인증인텐트에 번호 전달, 인증하는 인텐트에서는 서버연결하고 서버로 번호 전달

                message= et_phone.getText().toString();

                AuthRequest authRequest = new AuthRequest(message);
                //Log.d("tag", message);
                Apiinterface apiinterface = Api.createService(Apiinterface.class);
                Call<AuthResponse> call = apiinterface.setAuth(authRequest);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                        if(response.isSuccessful()){
                            if(response.body().result.equals("CREATED")){

                                AuthResponse result = response.body();
                                AuthResponse.Data data = result.data;
                                String time = data.requestTime;
                                //Log.d("성공", String.valueOf(response.body()));
                                Intent intent = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
                                intent.putExtra("phoneNumber", message);
                                intent.putExtra("time", time);
                                startActivity(intent);
                            }
                        }
                        else{
                            Log.d("logorreg,setauth", new Gson().toJson(response.errorBody()));
                            Log.d("logorreg,setauth", response.toString());
                            Log.d("logorreg,setauth", String.valueOf(response.code()));
                            Log.d("logorreg,setauth", response.message());
                            Log.d("logorreg,setauth", String.valueOf(response.raw().request().url().url()));
                            Log.d("logorreg,setauth", new Gson().toJson(response.raw().request()));
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {

                        Log.d("logorreg,setauth", t.toString());

                    }
                });

            }
        });

    }




}