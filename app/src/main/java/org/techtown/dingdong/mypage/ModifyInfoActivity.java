package org.techtown.dingdong.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.dingdong.R;

public class ModifyInfoActivity extends AppCompatActivity {

    Button btn_modify, btn_correct, btn_quit, btn_logout, btn_auth;
    LinearLayout ln_modify, ln_auth;
    TextView tv_phone;
    EditText et_modify, et_auth;
    ImageButton btn_back;
    private String phonenum, authnum;

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
                    //tv_phone.setText(phonenum);
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
                startActivity(new Intent(ModifyInfoActivity.this, LeaveActivity.class));
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
                            }
                        }).show();
            }
        });


    }
}