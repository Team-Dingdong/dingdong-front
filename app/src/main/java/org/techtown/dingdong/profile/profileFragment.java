package org.techtown.dingdong.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.PostResponse;
import org.techtown.dingdong.login_register.LoginActivity;
import org.techtown.dingdong.login_register.LoginOrRegisterActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.mytown.changetownActivity;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class profileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button startbtn, townbtn;

    Button btn_img;
    TextView name, number_good, number_bad;
    ImageView profile_img;
    private String TAG;
    String nickname, img, bio;
    int good, bad;
    private int id_view;

    private static final int PICK_FROM_CAMERA = 0;

    private static final int PICK_FROM_ALBUM = 1;

    private static final int CROP_FROM_IMAGE = 2;

    private Uri mImageCaptureUri;
    private String absoultePath;


    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment contentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        startbtn= v.findViewById(R.id.button);

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                //startActivity(new Intent(getActivity(), LoginOrRegisterActivity.class));
            }
        });

        townbtn= v.findViewById(R.id.changetown);
        btn_img= v.findViewById(R.id.btn_img);
        townbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), changetownActivity.class));
            }
        });
        //본인 프로필 조회
        name = (TextView) getView().findViewById(R.id.name);
        profile_img = (ImageView)getView().findViewById(R.id.profile_img);
        number_good = (TextView)getView().findViewById(R.id.number_good);
        number_bad = (TextView)getView().findViewById(R.id.number_bad);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        Token token = new Token(access,refresh,expire,tokentype);
        Log.d("토큰", String.valueOf(access));

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());
        Call<ProfileResponse> call = apiinterface.getData();
        call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                   if(response.body().getCode().equals("PROFILE_READ_SUCCESS")) {
                       Log.d(TAG, "프로필 조회 성공");
                       nickname = response.body().getData().nickname;
                       img = response.body().getData().profileImageUrl;
                       bio = response.body().getData().profile_bio;
                   }
                   else{Log.d(TAG, "프로필 조회 실패");}
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {

                }
            });

            name.setText(nickname);

            new DownloadFilesTask().execute("img");

            Call<MyLatingResponse> callLate = apiinterface.getLating();
            callLate.enqueue(new Callback<MyLatingResponse>() {
                @Override
                public void onResponse(Call<MyLatingResponse> call, Response<MyLatingResponse> response) {
                    if(response.body().getCode().equals("RATING_READ_SUCCESS")) {
                        Log.d(TAG, "평가 조회 성공");
                        good = response.body().getData().good;
                        bad = response.body().getData().bad;

                    }
                    else{Log.d(TAG, "평가 조회 실패");}
                }

                @Override
                public void onFailure(Call<MyLatingResponse> call, Throwable t) {

                }
            });
            number_good.setText(good);
            number_bad.setText(bad);

            btn_img.setOnClickListener(this);
            return v;
        }

        //서버에서 파일 받아오기
        private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bmp = null;
                try {
                    String img_url = strings[0]; //url of the image
                    URL url = new URL(img_url);
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(Bitmap result) {
                // doInBackground 에서 받아온 total 값 사용 장소
                profile_img.setImageBitmap(result);
            }
        }


        public void onClick(View v){
            id_view = v.getId();
            if(v.getId() == R.id.btn_img){
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
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

                new AlertDialog.Builder(getActivity())
                        .setTitle("프로필 이미지 선택")
                        .setNegativeButton("사진촬영", cameraListener)

                        .setNeutralButton("앨범선택", albumListener)

                        .setPositiveButton("취소", cancelListener)

                        .show();


            }
            else if(v.getId() == R.id.btn_nickname){
                //////
                return;
            }
        }
        public void doTakePhotoAction(){ // 카메라 촬영 후 이미지 가져오기{

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            // 임시로 사용할 파일의 경로를 생성

            String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));


            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

            startActivityForResult(intent, PICK_FROM_CAMERA);

        }

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


            switch(requestCode)

            {

                case PICK_FROM_ALBUM:

                {
                    mImageCaptureUri = data.getData();

                    Log.d("SmartWheel",mImageCaptureUri.getPath().toString());
                }


                case PICK_FROM_CAMERA:

                {
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

                    startActivityForResult(intent, CROP_FROM_IMAGE); // CROP_FROM_CAMERA case문 이동

                    break;

                }

                case CROP_FROM_IMAGE :
                {
                    // 크롭이 된 이후의 이미지를 넘겨 받습니다.

                    // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에

                    // 임시 파일을 삭제합니다.

                    if(resultCode != RESULT_OK) {

                        return;

                    }


                    final Bundle extras = data.getExtras();


                    // CROP된 이미지를 저장하기 위한 FILE 경로

                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+

                            "/SmartWheel/"+System.currentTimeMillis()+".jpg";


                    if(extras != null)

                    {

                        Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP

                        profile_img.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌


                        storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.

                        absoultePath = filePath;

                        break;


                    }

                    // 임시 파일 삭제

                    File f = new File(mImageCaptureUri.getPath());

                    if(f.exists())

                    {

                        f.delete();

                    }

                }


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

                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,

                        Uri.fromFile(copyFile)));



                out.flush();

                out.close();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }












