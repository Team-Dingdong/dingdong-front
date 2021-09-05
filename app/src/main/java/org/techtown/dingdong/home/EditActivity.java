package org.techtown.dingdong.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    public int status = 0;

    //수정삭제

    private Spinner select_category, select_personnel, select_region;
    private String selected_category, res_price, res_detail, res_title, res_place, res_hash=null, selected_region;
    private int selected_personnel = 2, category = 1;
    private RecyclerView recycler_image;
    private EditText et_title, et_detail, et_price, et_place, et_hashtag1, et_hashtag2, et_hashtag3, et_hashtag4, et_hashtag5 ;
    private TextView tv_region, tv_btn;
    private ImageButton btn_imgupload, btn_back;
    private Button btn_enroll;
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> imgList = new ArrayList<>();
    ImageUploadAdapter imageUploadAdapter;
    private String id;


    String[] categories = {"과일·채소", "육류·계란", "간식류", "생필품", "기타"};
    String[] personnels = {"1", "2", "3", "4", "5"};
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
        tv_btn = findViewById(R.id.post_text);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken", "");
        String refresh = pref.getString("oauth.refreshtoken", "");
        String expire = pref.getString("oauth.expire", "");
        String tokentype = pref.getString("oauth.tokentype", "");

        Token token = new Token(access, refresh, expire, tokentype);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");


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



        //게시물 수정일 경우 포스트 데이터 가져오기
        if(Integer.parseInt(id) != 0) {
            setShare(token);
            tv_btn.setText("나눔 수정하기");
        }


        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res_hash = "#" + et_hashtag1.getText().toString() + "#" +  et_hashtag2.getText().toString() +
                        "#" + et_hashtag3.getText().toString() + "#" +  et_hashtag4.getText().toString() + "#" + et_hashtag5.getText().toString();
                res_hash = res_hash.replace("#####","").replace("####","").replace("###","").replace("##","");
                res_hash = res_hash.replace("##","#");

                if(et_place.getText().length() > 0 && et_title.getText().length() >0 &&
                        et_detail.getText().length()>0 && et_price.getText().length() >0 && res_hash.length() >0){

                    //9,999로 받아오기 때문에 Integer로 변환하기 위해 ','를 없애줌
                    res_price = et_price.getText().toString().replace(",","");


                    if(Integer.parseInt(id) != 0) {
                        setPatch(token);
                    }else{
                        //게시물 작성일경우
                        setPost(token);
                    }


                }
                else {
                    Toast.makeText(getApplicationContext(), "게시물이 다 채워지지 않았어요!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setPatch(Token token){

        //리퀘스트 생성
        PostRequest postRequest = new PostRequest(et_title.getText().toString(), selected_personnel,
                Integer.parseInt(res_price), et_detail.getText().toString(),
                et_place.getText().toString(), category, res_hash);


        //토큰을 이용해 통신하도록 레트로핏 통신 클래스에 전달
        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,EditActivity.this);

        Call<ResponseBody> call = apiinterface.setPatch(postRequest, Integer.parseInt(id));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("성공","수정이완료됨");

                    uploadImage(token, Integer.parseInt(id));

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

    private void setPost(Token token){

        //리퀘스트 생성
        PostRequest postRequest = new PostRequest(et_title.getText().toString(), selected_personnel,
                Integer.parseInt(res_price), et_detail.getText().toString(),
                et_place.getText().toString(), category, res_hash);


        //토큰을 이용해 통신하도록 레트로핏 통신 클래스에 전달
        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,EditActivity.this);
        Call<EditResponse> call = apiinterface.setPost(postRequest);
        call.enqueue(new Callback<EditResponse>() {
            @Override
            public void onResponse(Call<EditResponse> call, Response<EditResponse> response) {

                if(response.isSuccessful()){

                   EditResponse res = response.body();
                   String resId = res.getId();
                   Log.d("성공",resId);

                   uploadImage(token, Integer.parseInt(resId));

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
            public void onFailure(Call<EditResponse> call, Throwable t) {

                Log.d("외않되","응?" );

            }
        });

        //uploadImage(token, 2);



    }

    private void setShare(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,EditActivity.this);

        Call<ShareResponse> call = apiinterface.getShare(Integer.parseInt(id));
        call.enqueue(new Callback<ShareResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<ShareResponse> call, Response<ShareResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        ShareResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        Share share;
                        share = res.getShare();

                        et_detail.setText(share.getMaintext());
                        et_title.setText(share.getTitle());
                        et_price.setText(share.getPrice());
                        et_place.setText(share.getPlace());
                        int capa = Integer.parseInt(share.getPersonnelcapacity()) -1;
                        select_personnel.setSelection(capa);
                        select_category.setSelection(2);
                        //String json = new Gson().toJson(res.getData().getShare());

                        if(share.getImage1()!=null){
                        if(!share.getImage1().contains("default_post.png")){

                            imgList.add(share.getImage1());

                            //uriList.add(Uri.parse(share.getImage1()));

                            if(!share.getImage2().contains("default_post.png")){
                                imgList.add(share.getImage2());
                                //uriList.add(Uri.parse(share.getImage2()));
                            }
                            if(!share.getImage3().contains("default_post.png")){
                                imgList.add(share.getImage3());
                                //uriList.add(Uri.parse(share.getImage3()));
                                //!share.getImage3().equals("null")
                            }

                            //이미지 리사이클러뷰 세팅
                            imageUploadAdapter = new ImageUploadAdapter(imgList, getApplicationContext());
                            recycler_image.setAdapter(imageUploadAdapter);
                            recycler_image.setLayoutManager(new LinearLayoutManager(EditActivity.this, LinearLayoutManager.HORIZONTAL, false));

                        }}
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
            public void onFailure(Call<ShareResponse> call, Throwable t) {

                Log.d("외않되", String.valueOf(t));

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
                //uriList.add(imageUri);
                imgList.add(imageUri.toString());

                imageUploadAdapter = new ImageUploadAdapter(imgList, getApplicationContext());
                recycler_image.setAdapter(imageUploadAdapter);
                recycler_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            }
            else{
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                //갤러리 내부에서 선택한 아이템의 개수와 기선택된 아이템의 개수의 합이 3개를 넘지 않게끔
                if(clipData.getItemCount() + imgList.size() > 3){
                    Toast.makeText(getApplicationContext(), "사진은 3장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.e("MultiImageActivity", "multiple choice");

                    for (int i = 0; i<clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();

                        try{
                            //uriList.add(imageUri);
                            imgList.add(imageUri.toString());
                        } catch (Exception e){
                            Log.e("MultiImageActivity", "File select error", e);
                        }
                    }

                    //이미지 리사이클러뷰 세팅
                    imageUploadAdapter = new ImageUploadAdapter(imgList, getApplicationContext());
                    recycler_image.setAdapter(imageUploadAdapter);
                    recycler_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                }
            }
        }
    }

    private File getResize(Bitmap bitmap, String name) throws IOException {
        File storage = getCacheDir();
        String filename = name + ".jpg";
        File imgfile = new File(storage, filename);

        Log.d("getuploadimg","resizing");

        imgfile.createNewFile();
        FileOutputStream out = new FileOutputStream(imgfile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
        out.close();


        return new File(getCacheDir() + "/" + filename);

    }

    private void uploadImage(Token token, int id) {

        imgList = imageUploadAdapter.getData();
        uriList = new ArrayList<>();
        for (int i=0; i<imgList.size(); i++){
            Uri uri = Uri.parse(imgList.get(i));
            uriList.add(uri);
        }

        if(uriList.size() != 0) {
            ArrayList<MultipartBody.Part> uplist = new ArrayList<>();
            //ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < uriList.size(); i++) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditActivity.this.getContentResolver(), uriList.get(i));
                    File file = getResize(bitmap, Integer.toString((int) System.currentTimeMillis()).replace("-",""));

                    Log.d("uploadimg","resizing");
                    Log.d("uploadimg", "file == " + file.getName());

                    //files.add(file);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    uplist.add(MultipartBody.Part.createFormData("files", file.getName(), requestBody));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Apiinterface apiinterface = Api.createService(Apiinterface.class, token, EditActivity.this);
            Call<ResponseBody> call = apiinterface.uploadImg(uplist, id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        if (response.code() == 200) {
                            Log.d("이미지등록성공", new Gson().toJson(response.code()));

                            onfinish(1);

                        }


                    } else {

                        Log.d("실패", new Gson().toJson(response.errorBody()));
                        Log.d("실패", response.toString());
                        Log.d("실패", String.valueOf(response.code()));
                        Log.d("실패", response.message());
                        Log.d("실패", String.valueOf(response.raw().request().url().url()));
                        Log.d("실패", new Gson().toJson(response.raw().request()));

                        onfinish(1);


                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Log.d("외않되", String.valueOf(t));
                    onfinish(1);

                }
            });


        }
        else{onfinish(1);}


    }

    public void onfinish(int status){
        if(status == 1){
        Toast.makeText(EditActivity.this, "등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
        //핸들러를 통한 액티비티 종료 시점 조절
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);

    }
    }

}
