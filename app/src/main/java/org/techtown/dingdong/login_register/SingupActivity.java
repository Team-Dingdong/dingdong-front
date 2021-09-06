package org.techtown.dingdong.login_register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.HomeFragment;
import org.techtown.dingdong.home.MainFragment;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingupActivity extends AppCompatActivity {

    TextView tv_nick;
    String message;
    Button btn_ok, btn_check;
    ImageButton imgbtn_profile;
    EditText et;
    int id_view;
    private Uri mImageCaptureUri;
    ImageView iv_profile;
    private String absoultePath;
    String receiveMsg;

    private static final int PICK_FROM_CAMERA = 0;

    private static final int PICK_FROM_ALBUM = 1;

    private static final int CROP_FROM_iMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.acesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        Token token = new Token(access, refresh, expire, tokentype);

        tv_nick = (TextView)findViewById(R.id.tv_nick);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        et = (EditText)findViewById(R.id.et);
        imgbtn_profile = (ImageButton)findViewById(R.id.imgbtn_profile);
        btn_check = (Button)findViewById(R.id.btn_check);

        imgbtn_profile.setOnClickListener(this::onClick);
        btn_ok.setOnClickListener(this::onClick);
        btn_check.setOnClickListener(this::onClick);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                btn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        message= et.getText().toString();
                        if(message.length() <=15){
                            AuthNickRequset authNickRequset = new AuthNickRequset(message);
                            Log.d("tag", message);
                            Apiinterface apiinterface = Api.createService(Apiinterface.class);
                            Call<AuthNickResponse> call = apiinterface.AuthNickRequest(authNickRequset);
                            call.enqueue(new Callback<AuthNickResponse>() {
                                @Override
                                public void onResponse(Call<AuthNickResponse> call, Response<AuthNickResponse> response) {

                                    String url = "";
                                    InputStream is = null;
                                    try {
                                        is = new URL(url).openStream();
                                        BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                                        String str;
                                        StringBuffer buffer = new StringBuffer();
                                        while ((str = rd.readLine()) != null) {
                                            buffer.append(str);
                                        }
                                        receiveMsg = buffer.toString();
                                    } catch (IOException e) {
                                        e.printStackTrace(); }



                                    if(response.isSuccessful()){

                                        if(response.body().code.equals("NICKNAME_CREATE_SUCCESS")){

                                            btn_ok.setEnabled(true);
                                            tv_nick.setText("닉네임 설정이 완료됐습니다.");

                                        }
                                        else if(response.body().code.equals("NICKNAME_DUPLICATION")){
                                            //edittext
                                            tv_nick.setText("다른 닉네임을 설정해주세요.");
                                        }
                                    }
                                    else{
                                        Log.d("문제발생", String.valueOf(response) + receiveMsg);
                                    }



                                }

                                @Override
                                public void onFailure(Call<AuthNickResponse> call, Throwable t) {

                                    Log.d("tag", t.toString());

                                }
                            });

                        }
                        else{
                            btn_ok.setEnabled(false);
                        }
                    }
                });

            }

        });


    }

    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기

    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        // 임시로 사용할 파일의 경로를 생성

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));


        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

        startActivityForResult(intent, PICK_FROM_CAMERA);

    }


    /**

     * 앨범에서 이미지 가져오기

     */

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기

    {

        // 앨범 호출

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent, PICK_FROM_ALBUM);

    }


    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);


        if(resultCode != RESULT_OK)

            return;


        switch(requestCode) {

            case PICK_FROM_ALBUM: {

                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.

                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();

                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());

            }


            case PICK_FROM_CAMERA: {

                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.

                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");

                intent.setDataAndType(mImageCaptureUri, "image/*");


                // CROP할 이미지를 200*200 크기로 저장

                intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기

                intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기

                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율

                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율

                intent.putExtra("scale", true);

                intent.putExtra("return-data", true);

                startActivityForResult(intent, CROP_FROM_iMAGE); // CROP_FROM_CAMERA case문 이동


                break;

            }

            case CROP_FROM_iMAGE: {

                // 크롭이 된 이후의 이미지를 넘겨 받습니다.

                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에

                // 임시 파일을 삭제합니다.

                if (resultCode != RESULT_OK) {

                    return;

                }


                final Bundle extras = data.getExtras();


                // CROP된 이미지를 저장하기 위한 FILE 경로

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +

                        "/SmartWheel/" + System.currentTimeMillis() + ".jpg";


                if (extras != null) {

                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP

                    iv_profile.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌


                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.

                    absoultePath = filePath;

                    break;


                }

                // 임시 파일 삭제

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) {

                    f.delete();

                }


            }
            default:{
                ProfileUploadRequset();
            }

        }

        }







    public void onClick(View v){
        id_view = v.getId();
        if(v.getId() == R.id.imgbtn_profile) {
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    doTakePhotoAction();

                }

            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {

                    doTakeAlbumAction();



                }

            };


            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }

            };


            new AlertDialog.Builder(this)

                    .setTitle("업로드할 이미지 선택")

                    .setNegativeButton("사진촬영", cameraListener)

                    .setNeutralButton("앨범선택", albumListener)

                    .setPositiveButton("취소", cancelListener)

                    .show();


        }
        else if(v.getId() == R.id.btn_ok){
            //다음 화면으로 넘어가기기
            iv_profile.setVisibility(View.GONE);
            imgbtn_profile.setVisibility(View.GONE);
            tv_nick.setVisibility(View.GONE);
            et.setVisibility(View.GONE);
            btn_ok.setVisibility(View.GONE);
            btn_check.setVisibility(View.GONE);

            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.Signup, fragment).commit();


       }

    }
    private void storeCropImage(Bitmap bitmap, String filePath) {

        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel";

        File directory_SmartWheel = new File(dirPath);


        if(!directory_SmartWheel.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)

            directory_SmartWheel.mkdir();


        File copyFile = new File(filePath);

        BufferedOutputStream out = null;


        try {


            copyFile.createNewFile();

            out = new BufferedOutputStream(new FileOutputStream(copyFile));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);


            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,

                    Uri.fromFile(copyFile)));



            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
    //이미지 서버로 보내기
    private void ProfileUploadRequset(){
        File file = new File(mImageCaptureUri.getPath());
        InputStream inputStream = null;
        try {
            inputStream = getApplicationContext().getContentResolver().openInputStream(mImageCaptureUri);
        }catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("data", file.getName() ,requestBody);
        AuthNickRequset authNickRequset = new AuthNickRequset(message);
        Log.d("tag", "이미지 서버 전송 시도");
        Apiinterface apiinterface = Api.createService(Apiinterface.class);
        Call<ProfileUploadResponse> call = apiinterface.ProfileUploadRequest(uploadFile);
        call.enqueue(new Callback<ProfileUploadResponse>() {
            @Override
            public void onResponse(Call<ProfileUploadResponse> call, Response<ProfileUploadResponse> response) {
                if(response.isSuccessful()){

                    if(response.body().code.equals("IMAGE_UPLOAD_SUCCESS")){

                       Log.d("tag","이미지 서버 전송 성공");

                    }

                }
                else{
                    Log.d("문제발생", String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<ProfileUploadResponse> call, Throwable t) {

            }
        });
    }

}