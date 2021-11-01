package org.techtown.dingdong.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.chatting.ChattingActivity;
import org.techtown.dingdong.chatting.UserListActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Target;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.Url;

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
    final int PERMISSIONS_REQUEST = 1005;
    FrameLayout frameLayout_region;
    String resId;


    String[] categories = {"과일·채소", "육류·계란", "간식류", "생필품", "기타"};
    String[] personnels = {"2", "3", "4", "5"};
    //String[] region = {"미아2동", "안암동"};


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
        frameLayout_region = findViewById(R.id.linearLayout5);

        List<String> where = new ArrayList<String>();
        where.add("동네선택");

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken", "");
        String refresh = pref.getString("oauth.refreshtoken", "");
        String expire = pref.getString("oauth.expire", "");
        String tokentype = pref.getString("oauth.tokentype", "");

        Token token = new Token(access, refresh, expire, tokentype);
        token.setContext(EditActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");



        //뒤로가기 눌렀을때
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                int permisson = ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permisson == PackageManager.PERMISSION_DENIED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    }else{
                        ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
                    }

                }else{
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
                }
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
            frameLayout_region.setVisibility(View.GONE);
        }
        else{

            Apiinterface apiinterface = Api.createService(Apiinterface.class, token, EditActivity.this);
            Call<LocalResponse> call = apiinterface.getLocal();
            call.enqueue(new Callback<LocalResponse>() {
                @Override
                public void onResponse(Call<LocalResponse> call, Response<LocalResponse> response) {
                    if(response.isSuccessful() && response.body() != null){
                        if(response.body().getResult().equals("LOCAL_READ_SUCCESS")){
                            LocalResponse res = response.body();
                            LocalResponse.Data data1 = res.getData().get(0);
                            LocalResponse.Data data2 = res.getData().get(1);
                            where.add(data1.getName());
                            where.add(data2.getName());
                            String[] region = new String[where.size()];
                            where.toArray(region);


                            ArrayAdapter<String> regionadapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_spinner_item, region);
                            regionadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            select_region.setAdapter(regionadapter);
                            select_region.setSelection(0);
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
                        }
                    }
                }

                @Override
                public void onFailure(Call<LocalResponse> call, Throwable t) {

                }
            });

        }


        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res_hash = "#" + et_hashtag1.getText().toString() + "#" +  et_hashtag2.getText().toString() +
                        "#" + et_hashtag3.getText().toString() + "#" +  et_hashtag4.getText().toString() + "#" + et_hashtag5.getText().toString();
                res_hash = res_hash.replace("#####","").replace("####","").replace("###","").replace("##","");
                res_hash = res_hash.replace("##","#");

                if(et_place.getText().length() > 0 && et_title.getText().length() >0 &&
                        et_detail.getText().length()>0 && et_price.getText().length() >0 && res_hash.length() >0 && select_region.getSelectedItemPosition() != 0){

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
                    Toast.makeText(getApplicationContext(), "게시물이 다 채워지지 않았어요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setPatch(Token token){
        ArrayList<MultipartBody.Part> uplist = new ArrayList<>();
        if(imgList.size()!=0){
            uriList = new ArrayList<>();
            for (int i=0; i<imgList.size(); i++){
                Uri uri = Uri.parse(imgList.get(i));
                uriList.add(uri);
            }
        }
        for (int i = 0, j = 0; i < imgList.size(); i++) {
            try {
                Log.d("1",imgList.get(i).toString());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditActivity.this.getContentResolver(), uriList.get(i));
                File file = getResize(bitmap, Integer.toString((int) System.currentTimeMillis()).replace("-", ""));
                Log.d("uploadimg","resizing");
                Log.d("uploadimg", "file == " + file.getName());
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                uplist.add(MultipartBody.Part.createFormData("postImages", file.getName(), requestBody));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<MultipartBody.Part> input = new ArrayList<>();
        input.add(MultipartBody.Part.createFormData("title", et_title.getText().toString()));
        input.add(MultipartBody.Part.createFormData("people", Integer.toString(selected_personnel)));
        input.add(MultipartBody.Part.createFormData("cost", res_price));
        input.add(MultipartBody.Part.createFormData("bio",et_detail.getText().toString()));
        input.add(MultipartBody.Part.createFormData("location",et_place.getText().toString()));
        input.add(MultipartBody.Part.createFormData("categoryId",Integer.toString(category)));
        input.add(MultipartBody.Part.createFormData("postTag",res_hash));


        //토큰을 이용해 통신하도록 레트로핏 통신 클래스에 전달
        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,EditActivity.this);
        Call<ResponseBody> call = apiinterface.setPatch(input, uplist, Integer.parseInt(id));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    resId = id;
                    //Log.d("성공","수정이완료됨");
                    onfinish(1);

                }else if(response.code() == 404){
                    Toast.makeText(EditActivity.this,"해당 포스트를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Log.d("edit,setpatch", new Gson().toJson(response.errorBody()));
                    Log.d("edit,setpatch", response.toString());
                    Log.d("edit,setpatch", String.valueOf(response.code()));
                    Log.d("edit,setpatch", response.message());
                    Log.d("edit,setpatch", String.valueOf(response.raw().request().url().url()));
                    Log.d("edit,setpatch", new Gson().toJson(response.raw().request()));

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("외않되","응?" );
            }
        });


    }

    private void setPost(Token token){

        ArrayList<MultipartBody.Part> uplist = new ArrayList<>();

        if(imgList.size()!=0){
            uriList = new ArrayList<>();
            for (int i=0; i<imgList.size(); i++){
                Uri uri = Uri.parse(imgList.get(i));
                uriList.add(uri);
            }
        }
        for (int i = 0, j = 0; i < uriList.size(); i++) {
            try {
                    Log.d("1",uriList.get(i).toString());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditActivity.this.getContentResolver(), uriList.get(i));
                    File file = getResize(bitmap, Integer.toString((int) System.currentTimeMillis()).replace("-", ""));
                    Log.d("uploadimg","resizing");
                    Log.d("uploadimg", "file == " + file.getName());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    uplist.add(MultipartBody.Part.createFormData("postImages", file.getName(), requestBody));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<MultipartBody.Part> input = new ArrayList<>();
        input.add(MultipartBody.Part.createFormData("title", et_title.getText().toString()));
        input.add(MultipartBody.Part.createFormData("people", Integer.toString(selected_personnel)));
        input.add(MultipartBody.Part.createFormData("cost", res_price));
        input.add(MultipartBody.Part.createFormData("bio",et_detail.getText().toString()));
        input.add(MultipartBody.Part.createFormData("location",et_place.getText().toString()));
        input.add(MultipartBody.Part.createFormData("categoryId",Integer.toString(category)));
        input.add(MultipartBody.Part.createFormData("postTag",res_hash));

        //토큰을 이용해 통신하도록 레트로핏 통신 클래스에 전달
        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,EditActivity.this);
        Call<EditResponse> call = apiinterface.setPost(input, uplist, select_region.getSelectedItemPosition());
        call.enqueue(new Callback<EditResponse>() {
            @Override
            public void onResponse(Call<EditResponse> call, Response<EditResponse> response) {

                if(response.isSuccessful()){

                   EditResponse res = response.body();
                   resId = res.getData().getId();
                   Log.d("성공",resId);
                   onfinish(1);

                }else if(response.code() == 404){
                    Toast.makeText(EditActivity.this,"해당 동네를 찾을 수 없습니다. 동네 선택을 확인해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Log.d("edit,setpost", new Gson().toJson(response.errorBody()));
                    Log.d("edit,setpost", response.toString());
                    Log.d("edit,setpost", String.valueOf(response.code()));
                    Log.d("edit,setpost", response.message());
                    Log.d("edit,setpost", String.valueOf(response.raw().request().url().url()));
                    Log.d("edit,setpost", new Gson().toJson(response.raw().request()));

                }

            }

            @Override
            public void onFailure(Call<EditResponse> call, Throwable t) {
                Log.d("edit,setpost",t.toString());

            }
        });




    }

    private void setShare(Token token){
        //게시물 수정일 경우 기존 정보 가져오기

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,EditActivity.this);

        Call<ShareResponse> call = apiinterface.getShare(Integer.parseInt(id));
        call.enqueue(new Callback<ShareResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<ShareResponse> call, Response<ShareResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        ShareResponse res = response.body();
                        //Log.d("성공", new Gson().toJson(res));

                        Share share;
                        share = res.getShare();

                        et_detail.setText(share.getMaintext());
                        et_title.setText(share.getTitle());
                        et_price.setText(share.getPrice());
                        et_place.setText(share.getPlace());
                        int capa = Integer.parseInt(share.getPersonnelcapacity()) -2;
                        select_personnel.setSelection(capa);
                        List<String> hashtag = share.getHashtag();
                        for(int i=0; i < hashtag.size(); i++){
                            switch (i){
                                case 0:
                                    et_hashtag1.setText(hashtag.get(0).replace("#",""));
                                    break;
                                case 1:
                                    et_hashtag2.setText(hashtag.get(1).replace("#",""));
                                    break;
                                case 2:
                                    et_hashtag3.setText(hashtag.get(2).replace("#",""));
                                    break;
                                case 3:
                                    et_hashtag4.setText(hashtag.get(3).replace("#",""));
                                    break;
                                case 4:
                                    et_hashtag5.setText(hashtag.get(4).replace("#",""));
                                    break;
                            }
                        }

                        switch(share.getCategory()){
                            case "과일,채소":
                                select_category.setSelection(0);
                                break;
                            case "육류,계란":
                                select_category.setSelection(1);
                                break;
                            case "간식":
                                select_category.setSelection(2);
                                break;
                            case "생필품":
                                select_category.setSelection(3);
                                break;
                            case "기타":
                                select_category.setSelection(4);
                                break;
                            default:
                                select_category.setSelection(0);
                                break;
                        }

                    }

                }else if(response.code() == 404){
                    Toast.makeText(EditActivity.this,"해당 포스트를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else{
                    Log.d("edit,getshare", new Gson().toJson(response.errorBody()));
                    Log.d("edit,getshare", response.toString());
                    Log.d("edit,getshare", String.valueOf(response.code()));
                    Log.d("edit,getshare", response.message());
                    Log.d("edit,getshare", String.valueOf(response.raw().request().url().url()));
                    Log.d("edit,getshare", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<ShareResponse> call, Throwable t) {

                Log.d("edit,getshare", String.valueOf(t));

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
        out.close();


        return new File(getCacheDir() + "/" + filename);

    }


    private void uploadImage(Token token, int id) throws IOException {

        if(!imageUploadAdapter.getData().isEmpty()){
        imgList = imageUploadAdapter.getData();
        uriList = new ArrayList<>();
        for (int i=0; i<imgList.size(); i++){
            Uri uri = Uri.parse(imgList.get(i));
            uriList.add(uri);
        }

        if(uriList.size() != 0) {
            ArrayList<MultipartBody.Part> uplist = new ArrayList<>();
            ArrayList<MultipartBody.Part> upurl = new ArrayList<>();
            for (int i = 0, j = 0; i < uriList.size(); i++) {
                try {
                    if(!uriList.get(i).toString().contains("amazonaws")) {
                        Log.d("1",uriList.get(i).toString());
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditActivity.this.getContentResolver(), uriList.get(i));
                        File file = getResize(bitmap, Integer.toString((int) System.currentTimeMillis()).replace("-", ""));
                        Log.d("uploadimg","resizing");
                        Log.d("uploadimg", "file == " + file.getName());
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        uplist.add(MultipartBody.Part.createFormData("postImages", file.getName(), requestBody));
                    }
                    else{
                        Log.d("2",uriList.get(i).toString());
                        Log.d("uploadimg","reuploading");
                        upurl.add(MultipartBody.Part.createFormData("image_urls", uriList.get(i).toString()));

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Apiinterface apiinterface = Api.createService(Apiinterface.class, token, EditActivity.this);
            Call<ResponseBody> call = apiinterface.uploadImg(uplist, upurl,id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        if (response.code() == 200) {
                            Log.d("이미지등록성공", new Gson().toJson(response.code()));
                            Log.d("성공", String.valueOf(response.raw().request().url().url()));
                            Log.d("성", new Gson().toJson(response.raw().request()));

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


    }

    public void onfinish(int status){
        if(status == 1){
        Toast.makeText(EditActivity.this, "등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
        //핸들러를 통한 액티비티 종료 시점 조절
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //finish();
                Intent intent = new Intent(EditActivity.this, ShareDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id",resId);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }
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




}
