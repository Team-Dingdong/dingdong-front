package org.techtown.dingdong.login_register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.SettingInjectorService;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.chatting.Chat;
import org.techtown.dingdong.chatting.ChatType;
import org.techtown.dingdong.chatting.ChattingActivity;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;
import org.techtown.dingdong.profile.UserProfileRequest;
import org.techtown.dingdong.profile.UserProfileResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class SetProfileActivity extends AppCompatActivity {
    ImageView img_profile;
    EditText et_nickname;
    Button btn_finish;
    ImageButton btn_back, btn_upload;
    final int PERMISSIONS_REQUEST = 1005;
    private final int OPEN_GALLERY = 201;
    Uri imageuri;
    private String state;
    Boolean doiimgcorrect = false;
    CardView cardView;
    String old_nickname=",";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        img_profile = findViewById(R.id.img_profile);
        btn_upload = findViewById(R.id.btn_upload);
        btn_back = findViewById(R.id.ic_back);
        et_nickname = findViewById(R.id.et_nickname);
        btn_finish = findViewById(R.id.btn_finish);
        cardView = findViewById(R.id.cardView);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken", "");
        String refresh = pref.getString("oauth.refreshtoken", "");
        String expire = pref.getString("oauth.expire", "");
        String tokentype = pref.getString("oauth.tokentype", "");

        Token token = new Token(access, refresh, expire, tokentype);
        token.setContext(SetProfileActivity.this);

        Intent intent = getIntent();
        state = intent.getStringExtra("state");

        if(state.equals("signup")){
            btn_back.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            btn_upload.setVisibility(View.GONE);
        }
        else{
            getProfile(token);
        }

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permisson = ContextCompat.checkSelfPermission(SetProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permisson == PackageManager.PERMISSION_DENIED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(SetProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    }else{
                        ActivityCompat.requestPermissions(SetProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
                    }

                }
                else{
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, OPEN_GALLERY);

                }
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("signup") && et_nickname.getText().toString().length() > 0){
                    //updateProfile(token);
                    setProfile(token);
                }
                else if(state.equals("correct") && et_nickname.getText().toString().length() > 0){
                    updateProfile(token);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this,"승인이 허가되어 있지 않습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && data != null && data.getData() != null){
            switch(requestCode) {
                case OPEN_GALLERY:
                    Log.e("single choice", String.valueOf(data.getData()));
                    imageuri = data.getData();
                    doiimgcorrect = true;
                    Glide.with(SetProfileActivity.this)
                            .load(imageuri)
                            .into(img_profile);

            }
        }
    }

    public void getProfile(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, SetProfileActivity.this);
        Call<UserProfileResponse> call = apiinterface.getUserProfile();
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("PROFILE_READ_SUCCESS")) {
                        UserProfileResponse.Data res = response.body().getData();
                        et_nickname.setText(res.getNickname());
                        old_nickname = res.getNickname();
                        Glide.with(SetProfileActivity.this)
                                .load(res.getProfileImg())
                                .into(img_profile);
                    }
                }
                else{
                    Log.d("실패", new Gson().toJson(response.errorBody()));
                    Log.d("실패", response.toString());
                    Log.d("실패", String.valueOf(response.code()));
                    Log.d("실패", response.message());
                    Log.d("실패", String.valueOf(response.raw().request().url().url()));
                    Log.d("실패", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {

                Log.d("외않되", String.valueOf(t));

            }
        });

    }

    public void updateProfile(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,SetProfileActivity.this);
        MultipartBody.Part img = null;
        MultipartBody.Part nickname = null;
        if(doiimgcorrect){
            //이미지를 수정한경우 , 닉네임은 수정하지 않은 경우 수정한 경우
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(SetProfileActivity.this.getContentResolver(), imageuri);
                File file = getResize(bitmap, Integer.toString((int) System.currentTimeMillis()).replace("-",""));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                img = MultipartBody.Part.createFormData("profileImage", file.getName(), requestBody);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            if(!et_nickname.getText().toString().equals(old_nickname)){
                nickname = MultipartBody.Part.createFormData("nickname", et_nickname.getText().toString());
            }

        }
        else{
            if(!et_nickname.getText().toString().equals(old_nickname)){
                nickname = MultipartBody.Part.createFormData("nickname", et_nickname.getText().toString());
            }
        }

        Call<ResponseBody> call = apiinterface.setUpdateProfile(img, nickname);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code() == 200) {
                        Log.d("성공", new Gson().toJson(response.code()));
                        Log.d("성공", String.valueOf(response.raw().request().url().url()));
                        Log.d("성공", new Gson().toJson(response.raw().request()));

                        Toast.makeText(SetProfileActivity.this,"프로필 설정이 완료되었습니다.",Toast.LENGTH_LONG).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                //Intent intent = new Intent(SetProfileActivity.this, MainActivity.class);
                                //startActivity(intent);
                            }
                        }, 1000);
                }else if(response.code() == 409){
                    Toast.makeText(SetProfileActivity.this,"이미 사용중인 닉네임입니다. 다른 닉네임을 설정해주세요.",Toast.LENGTH_SHORT).show();
                }
                else{
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

    private void setProfile(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, SetProfileActivity.this);
        UserProfileRequest userProfileRequest = new UserProfileRequest(et_nickname.getText().toString());
        Call<ResponseBody> call = apiinterface.setProfile(userProfileRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code() == 201) {
                        Log.d("성공", new Gson().toJson(response.code()));
                        Log.d("성공", String.valueOf(response.raw().request().url().url()));
                        Log.d("성공", new Gson().toJson(response.raw().request()));

                        Toast.makeText(SetProfileActivity.this,"프로필 설정이 완료되었습니다.",Toast.LENGTH_LONG).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SetProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, 1000);
                    }else if(response.code() == 409) {
                        Toast.makeText(SetProfileActivity.this,"이미 사용중인 닉네임입니다. 다른 닉네임을 설정해주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else{
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

    private File getResize(Bitmap bitmap, String name) throws IOException {
        File storage = getCacheDir();
        String filename = name + ".jpg";
        File imgfile = new File(storage, filename);

        Log.d("getuploadimg","resizing");

        imgfile.createNewFile();
        FileOutputStream out = new FileOutputStream(imgfile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
        out.close();


        return new File(getCacheDir() + "/" + filename);

    }
}