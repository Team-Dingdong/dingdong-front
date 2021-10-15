package org.techtown.dingdong.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.techtown.dingdong.R;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

public class LeaveActivity extends AppCompatActivity {
    Button btn_quit;
    ImageButton btn_back;
    EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        btn_quit = findViewById(R.id.btn_quit);
        et_phone = findViewById(R.id.et_modify);
        btn_back = findViewById(R.id.ic_back);

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_phone.getText().toString().length() == 11){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(LeaveActivity.this);

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
                                    Log.i("Dialog", "네");
                                }
                            }).show();

                }else{
                    Toast.makeText(LeaveActivity.this,"11자리 전화번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                }
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