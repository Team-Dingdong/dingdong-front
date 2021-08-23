package org.techtown.dingdong.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    private Spinner select_category, select_personnel, select_region;
    private String selected_category, res_price, res_detail, res_title, res_place, res_hash, selected_region;
    private int selected_personnel = 2, category = 1;
    private RecyclerView recycler_image;
    private EditText et_title, et_detail, et_price, et_place, et_hashtag1, et_hashtag2, et_hashtag3, et_hashtag4, et_hashtag5 ;
    private TextView tv_region;
    private ImageButton btn_imgupload, btn_back;
    private Button btn_enroll;
    ArrayList<Uri> uriList = new ArrayList<>();
    ImageUploadAdapter imageUploadAdapter;


    String[] categories = {"과일·채소", "육류·계란", "간식류", "생필품", "기타"};
    String[] personnels = {"1", "2", "3", "4"};
    String[] region = {"미아2동", "안암동"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        select_category = findViewById(R.id.category);
        select_personnel = findViewById(R.id.personnel);
        btn_imgupload = findViewById(R.id.btn_imgupload);
        select_region = findViewById(R.id.select_region);
        tv_region = findViewById(R.id.tv_region);
        btn_back = findViewById(R.id.ic_back);
        btn_enroll = findViewById(R.id.btn_enroll);
        et_detail = findViewById(R.id.et_detail);
        et_title = findViewById(R.id.et_title);
        et_place = findViewById(R.id.et_place);



        //뒤로가기 눌렀을때
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //동네 선택 스피너 세팅
        ArrayAdapter<String> regionadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, region);
        regionadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        select_region.setAdapter(regionadapter);
        select_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_region = region[position];
                tv_region.setText(selected_region);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //카테고리 선택 스피너 세팅
        ArrayAdapter<String> categoryadapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categories
        );

        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        select_category.setAdapter(categoryadapter);

        select_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_category = categories[position];
                category = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //인원 수 선택 스피너 세팅
        ArrayAdapter<String> personneladapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, personnels
        );

        personneladapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        select_personnel.setAdapter(personneladapter);
        select_personnel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_personnel = Integer.parseInt(personnels[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //이미지 갤러리부터 업로드 가능하도록 세팅함
        btn_imgupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });

        //선택한 이미지를 리사이클러 뷰를 통해 확인할 수 있도록
        recycler_image = findViewById(R.id.image_recycler);

        et_price = findViewById(R.id.et_price);
        et_hashtag1 = findViewById(R.id.et_hashtag1);
        et_hashtag2 = findViewById(R.id.et_hashtag2);
        et_hashtag3 = findViewById(R.id.et_hashtag3);
        et_hashtag4 = findViewById(R.id.et_hashtag4);
        et_hashtag5 = findViewById(R.id.et_hashtag5);

        //가격 입력시 자동 포맷팅
        DecimalFormat df = new DecimalFormat("#,###");
        res_price = "";
        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(res_price)){
                    res_price = df.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    et_price.setText(res_price);
                    et_price.setSelection(res_price.length());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //해시태그 세팅 (이전게 채워져야 다음 에딧 입력 가능하도록)
        et_hashtag1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 ){
                    et_hashtag2.setEnabled(true);
                }
                else{
                    et_hashtag2.setEnabled(false);
                }

            }
        });


        et_hashtag2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 ){
                    et_hashtag3.setEnabled(true);
                }
                else{
                    et_hashtag3.setEnabled(false);
                }

            }
        });

        et_hashtag3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 ){
                    et_hashtag4.setEnabled(true);
                }
                else{
                    et_hashtag4.setEnabled(false);
                }

            }
        });

        et_hashtag4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 ){
                    et_hashtag5.setEnabled(true);
                }
                else{
                    et_hashtag5.setEnabled(false);
                }

            }
        });


        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_place.getText().length() > 0 && et_title.getText().length() >0 &&
                        et_detail.getText().length()>0 &&et_price.getText().length() >0 ){

                    res_price = et_price.getText().toString().replace(",","");

                    PostRequest postRequest = new PostRequest(et_title.getText().toString(), selected_personnel,
                           Integer.parseInt(res_price), et_detail.getText().toString(),
                            et_place.getText().toString(), category);


                    SharedPreferences pref = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
                    String access = pref.getString("oauth.accesstoken","");
                    String refresh = pref.getString("oauth.refreshtoken","");
                    String expire = pref.getString("oauth.expire","");
                    String tokentype = pref.getString("oauth.tokentype","");

                    Token token = new Token(access,refresh,expire,tokentype);

                    Log.d("토큰", String.valueOf(access));


                    Apiinterface apiinterface = Api.createService(Apiinterface.class,token,EditActivity.this);
                    Call<ResponseBody> call = apiinterface.setPost(postRequest);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if(response.isSuccessful()){
                                Log.d("성공","등록이완료됨");
                                Toast.makeText(EditActivity.this, "등록이 완료되었습니다.", Toast.LENGTH_LONG).show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);

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
                            Log.d("외않되","응?" );
                        }
                    });


                }
                else {
                    Toast.makeText(getApplicationContext(), "게시물이 다 채워지지 않았어요!", Toast.LENGTH_LONG).show();
                }
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){
            Toast.makeText(getApplicationContext(),"이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{
            if(data.getClipData() == null){
                Log.e("single choice", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                imageUploadAdapter = new ImageUploadAdapter(uriList, getApplicationContext());
                recycler_image.setAdapter(imageUploadAdapter);
                recycler_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            }
            else{
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                //갤러리 내부에서 선택한 아이템의 개수와 기선택된 아이템의 개수가 3개를 넘지 않게끔
                if(clipData.getItemCount() + uriList.size() > 3){
                    Toast.makeText(getApplicationContext(), "사진은 3장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.e("MultiImageActivity", "multiple choice");

                    for (int i = 0; i<clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();

                        try{
                            uriList.add(imageUri);
                        } catch (Exception e){
                            Log.e("MultiImageActivity", "File select error", e);
                        }
                    }

                    //이미지 리사이클러뷰 세팅
                    imageUploadAdapter = new ImageUploadAdapter(uriList, getApplicationContext());
                    recycler_image.setAdapter(imageUploadAdapter);
                    recycler_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                }
            }
        }
    }
}