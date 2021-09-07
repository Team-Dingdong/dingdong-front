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
    private String y="yyyy", m="mm", d="dd", h="hh", mm="mm", place="place";
    private String date = y + "-" + m + "-" + d, time = h + "시 " + mm + "분";
    private String info = null;
    private Boolean getp = false, gett = false, getd = false;
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


        tv_info.setText(null);


        //getInfo(token);



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
                y = Integer.toString(year);
                m = Integer.toString(monthOfYear);
                d = Integer.toString(dayOfMonth);


                date = y + "-" + m + "-" + d;
                info = makeInfo(date, time, place);
                tv_info.setText(info);
                tv_date.setText(date);
                //getd = true;
                //setBtnFinish(gett, getd, getp);
            }
        };

        //데이트 피커 팝업 세팅
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,dateSetListener,Integer.parseInt(year), Integer.parseInt(mon), Integer.parseInt(day));


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

                h = Integer.toString(hourOfDay);
                mm = Integer.toString(minute);

                time = h + "시 " + mm + "분";
                info = makeInfo(date, time, place);
                tv_info.setText(info);
                tv_time.setText(h + ":" + mm);
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


        //#B2FFE2

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_date.getText().toString().length() > 0 && et_place.getText().toString().length() > 0 && tv_time.getText().toString().length() > 0){
                    sendInfo(token);

                }
                else{
                    Toast.makeText(PlanningActivity.this, "항목을 다 채워주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private String makeInfo(String date, String time, String place){
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
                        String date = res.getData().getPromiseDate();
                        String hour = res.getData().getPromiseTime().substring(0, 2);
                        String min = res.getData().getPromiseTime().substring(3, 5);
                        String local = res.getData().getPromiseLocal();
                        String info = makeInfo(date, hour + "시 " + min + "분", local);
                        tv_date.setText(date);
                        tv_time.setText(hour + "시 " + min + "분");
                        tv_info.setText(info);
                        et_place.setText(local);

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
            public void onFailure(Call<ChatPromiseResponse> call, Throwable t) {

                Log.d("외않되", String.valueOf(t));

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
                    if (response.code() == 201) {

                        Toast.makeText(PlanningActivity.this, "생성이 완료되었습니다.",Toast.LENGTH_LONG).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);


                    }
                }else{

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

                Log.d("외않되", String.valueOf(t));


            }
        });

    }
}