package org.techtown.dingdong.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.sql.Timestamp;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.parseColor;

public class PlanningActivity extends AppCompatActivity {

    private ImageButton btn_timepick, btn_datepick, btn_back;
    private Button btn_finish;
    private TextView tv_info, tv_date, tv_time;
    private EditText et_place;
    private String gettime, getlocal, getdate, getmin, gethour;
    private String y="yyyy", m="mm", d="dd", h="hh", mm="mm", place="place";
    private String date = y + "-" + m + "-" + d, time = h + "시 " + mm + "분";
    private String info = null;
    private String id;
    private Token token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token  = new Token(access,refresh,expire,tokentype);
        token.setContext(PlanningActivity.this);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        btn_datepick = findViewById(R.id.btn_datepick);
        btn_timepick = findViewById(R.id.btn_timepick);
        tv_info = findViewById(R.id.tv_info);
        btn_back = findViewById(R.id.ic_back);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        et_place = findViewById(R.id.et_place);
        btn_finish = findViewById(R.id.btn_finish);


        tv_info.setVisibility(View.GONE);


        getInfo(token);



        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        String cur = curTime.toString();
        String year =  cur.substring(0,4); //2021-08-23 11:12
        String mon = cur.substring(5,7);
        String day = cur.substring(8,10);
        String hour = cur.substring(11,13);
        String min = cur.substring(14,16);


        //날짜 선택 팝업 띄우기

        //데이트 피커 리스너 세팅
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if(monthOfYear < 9){
                    m = "0" + Integer.toString(monthOfYear + 1);
                }else{
                    m = Integer.toString(monthOfYear + 1);
                }
                if(dayOfMonth < 10){
                    d = "0" + Integer.toString(dayOfMonth);
                }else{
                    d = Integer.toString(dayOfMonth);
                }
                y = Integer.toString(year);


                date = y + "-" + m + "-" + d;
                info = makeInfo(date, time, place);
                tv_info.setText(info);
                tv_date.setText(date);
                //getd = true;
                //setBtnFinish(gett, getd, getp);
            }
        };

        //데이트 피커 팝업 세팅
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,dateSetListener,Integer.parseInt(year), Integer.parseInt(mon)-1, Integer.parseInt(day));


        btn_datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();

            }
        });


        //타임피커 리스너 세팅

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if(hourOfDay < 10){
                    h = "0" + Integer.toString(hourOfDay);
                }else {
                    h = Integer.toString(hourOfDay);
                }
                if(minute < 10){
                    mm = "0" + Integer.toString(minute);
                }else{
                mm = Integer.toString(minute);
                }

                time = h + ":" + mm + ":00";
                info = makeInfo(date, time, place);
                tv_info.setText(info);
                tv_time.setText(time);
                //gett = true;
                //setBtnFinish(gett, getd, getp);

            }
        };

        //타임피커 팝업 세팅

        TimePickerDialog timePickerDialog = new TimePickerDialog(PlanningActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar, timeSetListener,
                Integer.parseInt(hour),Integer.parseInt(min), false);


        btn_timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timePickerDialog.show();

            }
        });


        et_place.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                place = et_place.getText().toString();
                info = makeInfo(date, time, place);
                tv_info.setText(info);
                //getp = true;
                //setBtnFinish(gett, getd, getp);

            }
        });



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_finish.setEnabled(true);


        //#B2FFE2

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_date.getText().toString().length() > 0 && et_place.getText().toString().length() > 0 && tv_time.getText().toString().length() > 0){
                    if(Integer.parseInt(mm) < 10){
                        //gettime = h + ":" + mm + ":00";
                    }
                    sendInfo(token);

                }
                else{
                    Toast.makeText(PlanningActivity.this, "항목을 다 채워주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private String makeInfo(String date, String time, String place){
        tv_info.setVisibility(View.VISIBLE);
        return date + ", " + time + "에 " + place + "에서";
    }

    /*private void setBtnFinish(Boolean time, Boolean place, Boolean date){
        if(time && place && date){
            btn_finish.setBackgroundColor(Color.parseColor("#B2FFE2"));
        }
    }*/

    public void getInfo(Token token) {
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, PlanningActivity.this);
        Call<ChatPromiseResponse> call = apiinterface.getPromise(Integer.parseInt(id));
        call.enqueue(new Callback<ChatPromiseResponse>() {
            @Override
            public void onResponse(Call<ChatPromiseResponse> call, Response<ChatPromiseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().equals("CHAT_PROMISE_READ_SUCCESS")) {
                        ChatPromiseResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        getdate = res.getData().getPromiseDate();
                        gethour = res.getData().getPromiseTime().substring(0, 2);
                        getmin = res.getData().getPromiseTime().substring(3, 5);
                        Log.d("성공", date);

                        et_place.setText(getlocal);
                        gettime = gethour + "시 " + getmin + "분";
                        getlocal = res.getData().getPromiseLocal();
                        info = makeInfo(getdate, gettime, getlocal);
                        tv_date.setText(getdate);
                        tv_time.setText(gettime);
                        tv_info.setText(info);

                    }
                } else if(response.message().equals("CHAT_ROOM_NOT_FOUND")) {
                    Log.w("planning,getPromise","해당 채팅방을 찾을 수 없습니다.");

                } else if(response.message().equals("CHAT_JOIN_NOT_FOUND")) {
                    Log.w("planning,getPromise","해당 사용자가 채팅방에 속해있지 않습니다.");

                } else if(response.message().equals("CHAT_PROMISE_NOT_FOUND")) {
                    Log.w("planning,getPromise","해당 채팅 약속을 찾을 수 없습니다.");
                }

            }

            @Override
            public void onFailure(Call<ChatPromiseResponse> call, Throwable t) {

                Log.d("planning,getPromise", String.valueOf(t));

            }
        });
    }

    public void sendInfo(Token token){
        ChatPromiseRequest chatPromiseRequest = new ChatPromiseRequest(tv_date.getText().toString(),tv_time.getText().toString(),et_place.getText().toString());
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, PlanningActivity.this);
        Call<ResponseBody> call = apiinterface.setPromise(Integer.parseInt(id), chatPromiseRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {

                        Toast.makeText(PlanningActivity.this, "생성이 완료되었습니다.",Toast.LENGTH_LONG).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);


                    }
                }else if(response.message().equals("CHAT_PROMISE_UPDATE_FAIL_CONFIRMED")){
                    Toast.makeText(PlanningActivity.this, "약속이 확정되어 수정할 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
                else if(response.message().equals("CHAT_ROOM_NOT_OWNER")){
                    Toast.makeText(PlanningActivity.this, "해당 채팅방의 방장이 아닙니다.",Toast.LENGTH_SHORT).show();
                }
                else if(response.message().equals("CHAT_ROOM_NOT_FOUND")){
                    Toast.makeText(PlanningActivity.this, "해당 채팅방을 찾을 수 없습니다.",Toast.LENGTH_SHORT).show();
                }else if(response.message().equals("CHAT_PROMISE_NOT_FOUND")){
                    Toast.makeText(PlanningActivity.this, "해당 채팅 약속을 찾을 수 없습니다",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("planning,setprom", String.valueOf(t));


            }
        });

    }
}