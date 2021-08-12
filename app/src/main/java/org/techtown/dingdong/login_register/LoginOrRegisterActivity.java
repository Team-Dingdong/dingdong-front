package org.techtown.dingdong.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.dingdong.R;

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
                message= btn_transfer.getText().toString();
                if(message.length() == 11){
                    btn_transfer.setEnabled(true);
                }
                else{
                    btn_transfer.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //클릭하면 서버로 번호 전달, 인증번호받고
        btn_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼 클릭하면 인증하는 화면으로 전환하고, 인증인텐트에 번호 전달, 인증하는 인텐트에서는 서버연결하고 서버로 번호 전달

                message= btn_transfer.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            Toast.makeText(getApplicationContext(),"인증문자가 발송됐습니다.",Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                //인증확인하는 화면으로 전환
                Intent intent = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
                intent.putExtra("phoneNumber", message);
                startActivity(intent);
            }
        });


    }
}