package org.techtown.dingdong.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.techtown.dingdong.R;

public class LoginActivity extends AppCompatActivity {

    EditText edt_number_validation;
    Button btn_validation;
    String authNumber;

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
                authNumber= edt_number_validation.getText().toString();
                if(authNumber.length() == 6){
                    btn_validation.setEnabled(true);
                }
                else{
                    btn_validation.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btn_validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버로 폰번호,인증번호 전달
                //현재 validation.java를 volley로 작성해놔서 letrofit으로 고칠지 미정
                

            }
        });
    }
}