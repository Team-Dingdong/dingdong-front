package org.techtown.dingdong.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.oss.licenses.OssLicensesActivity;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.PrefManager;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.EditActivity;
import org.techtown.dingdong.login_register.LoginActivity;
import org.techtown.dingdong.login_register.LoginOrRegisterActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyInfoActivity extends AppCompatActivity {

    Button btn_modify, btn_correct, btn_quit, btn_logout, btn_auth;
    LinearLayout ln_modify, ln_auth;
    TextView tv_phone, tv_oss, tv_ossinfo;
    EditText et_modify, et_auth;
    ImageButton btn_back;
    private String phonenum, authnum;
    String license = "The MIT License\n" +
            "\n" +
            "Copyright (c) 2021 <copyright holders>\n" +
            "\n" +
            "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
            "of this software and associated documentation files (the \"Software\"), to deal\n" +
            "in the Software without restriction, including without limitation the rights\n" +
            "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
            "copies of the Software, and to permit persons to whom the Software is\n" +
            "furnished to do so, subject to the following conditions:\n" +
            "\n" +
            "The above copyright notice and this permission notice shall be included in\n" +
            "all copies or substantial portions of the Software.\n" +
            "\n" +
            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
            "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
            "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
            "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
            "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
            "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n" +
            "THE SOFTWARE.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        btn_modify = findViewById(R.id.btn_modify);
        btn_correct = findViewById(R.id.btn_correct);
        btn_quit = findViewById(R.id.btn_quit);
        ln_modify = findViewById(R.id.modify);
        et_modify = findViewById(R.id.et_modify);
        tv_phone = findViewById(R.id.tv_phone);
        btn_back = findViewById(R.id.ic_back);
        btn_logout = findViewById(R.id.btn_logout);
        ln_auth = findViewById(R.id.auth);
        et_auth = findViewById(R.id.et_auth);
        btn_auth = findViewById(R.id.btn_auth);
        tv_oss = findViewById(R.id.tv_oss);
        tv_ossinfo = findViewById(R.id.tv_ossinfo);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken", "");
        String refresh = pref.getString("oauth.refreshtoken", "");
        String expire = pref.getString("oauth.expire", "");
        String tokentype = pref.getString("oauth.tokentype", "");

        Token token = new Token(access, refresh, expire, tokentype);
        token.setContext(ModifyInfoActivity.this);

        tv_ossinfo.setText(license);


        btn_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_modify.setVisibility(View.VISIBLE);
                ln_auth.setVisibility(View.GONE);
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_modify.getText().toString().length() == 11){
                    phonenum = et_modify.getText().toString();
                    ln_modify.setVisibility(View.GONE);
                    ln_auth.setVisibility(View.VISIBLE);
                    btn_correct.setText("다시받기");
                }else{
                    Toast.makeText(ModifyInfoActivity.this,"11자리 전화번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //탈퇴하기 액티비티로
                //startActivity(new Intent(ModifyInfoActivity.this, LeaveActivity.class));

                //탈퇴하기 다이얼로그를 띄운다
                AlertDialog.Builder dialog = new AlertDialog.Builder(ModifyInfoActivity.this);

                dialog.setMessage("계정의 모든 정보가 삭제됩니다. \n탈퇴하시겠습니까?")
                        .setTitle("탈퇴하기")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Dialog", "아니오");
                            }
                        })
                        .setNegativeButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //네를 눌렀을시
                                Log.i("Dialog", "네");
                                Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ModifyInfoActivity.this);
                                Call<ResponseBody> call = apiinterface.leaveUser();
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            if (response.code() == 200) {
                                                pref.edit().putBoolean("oauth.loggedin",false).apply();
                                                pref.edit().putString("oauth.accesstoken", "").apply();
                                                pref.edit().putString("oauth.refreshtoken", "").apply();
                                                pref.edit().putString("oauth.expire", "").apply();
                                                pref.edit().putString("oauth.tokentype", "").apply();

                                                Toast.makeText(ModifyInfoActivity.this, "탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ModifyInfoActivity.this, LoginOrRegisterActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
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

                                    }
                                });

                            }
                        }).show();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_auth.getText().toString().length() == 6){
                    authnum = et_auth.getText().toString();
                    Toast.makeText(ModifyInfoActivity.this,"전화번호 수정이 완료되었습니다.",Toast.LENGTH_LONG).show();
                    ln_auth.setVisibility(View.GONE);
                    btn_correct.setText("수정하기");
                    tv_phone.setText(phonenum);

                }
                else{
                    Toast.makeText(ModifyInfoActivity.this,"6자리 인증번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });

        tv_oss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModifyInfoActivity.this, OssLicensesMenuActivity.class));
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ModifyInfoActivity.this);

                dialog.setMessage("로그아웃하시겠습니까?")
                        .setTitle("로그아웃")
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

                                Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ModifyInfoActivity.this);
                                Call<ResponseBody> call = apiinterface.logoutUser();
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            if (response.code() == 200) {
                                                pref.edit().putBoolean("oauth.loggedin",false).apply();
                                                pref.edit().putString("oauth.accesstoken", "").apply();
                                                pref.edit().putString("oauth.refreshtoken", "").apply();
                                                pref.edit().putString("oauth.expire", "").apply();
                                                pref.edit().putString("oauth.tokentype", "").apply();
                                                Toast.makeText(ModifyInfoActivity.this, "로그아웃이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ModifyInfoActivity.this, LoginOrRegisterActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
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

                                    }
                                });
                            }
                        }).show();
            }
        });


    }
}