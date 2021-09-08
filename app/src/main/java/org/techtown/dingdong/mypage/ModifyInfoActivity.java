package org.techtown.dingdong.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.dingdong.R;

public class ModifyInfoActivity extends AppCompatActivity {

    Button btn_modify, btn_correct, btn_quit;
    LinearLayout ln_modify;
    TextView tv_phone;
    EditText et_modify;
    ImageButton btn_back;

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


        btn_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_modify.setVisibility(View.VISIBLE);
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_modify.getText().toString().length() == 11){
                    String phonenum = et_modify.getText().toString();
                    tv_phone.setText(phonenum);
                    ln_modify.setVisibility(View.GONE);
                    Toast.makeText(ModifyInfoActivity.this,"전화번호 수정이 완료되었습니다.",Toast.LENGTH_LONG).show();

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


    }
}