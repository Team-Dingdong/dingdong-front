package org.techtown.dingdong.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import org.techtown.dingdong.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private Spinner select_category, select_personnel;
    private String selected_category, res_price, res_detail, res_title, res_place, res_hash;
    private int selected_personnel;
    private RecyclerView recycler_image;
    private EditText et_title, et_detail, et_price, et_hashtags, et_place;
    private ImageButton btn_imgupload;
    ArrayList<Uri> uriList = new ArrayList<>();
    ImageUploadAdapter imageUploadAdapter;


    String[] categories = {"과일·채소", "육류·계란", "간식류", "생필품", "기타"};
    String[] personnels = {"1", "2", "3", "4"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        select_category = findViewById(R.id.category);
        select_personnel = findViewById(R.id.personnel);
        btn_imgupload = findViewById(R.id.btn_imgupload);

        ArrayAdapter<String> categoryadapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categories
        );

        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        select_category.setAdapter(categoryadapter);

        select_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_category = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        recycler_image = findViewById(R.id.image_recycler);

        et_price = findViewById(R.id.et_price);
        et_hashtags = findViewById(R.id.et_hashtag);

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


        et_hashtags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //추후수정
                

                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(res_hash)){
                    res_hash = s.toString().replaceFirst(" ","\t\t#");
                    et_hashtags.setText(res_hash);
                    et_hashtags.setSelection(res_hash.length());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                int cnt = 0;
                int pos = s.toString().indexOf('#');
                while( pos != -1) {
                    cnt++;
                    pos = s.toString().indexOf('#',pos+1);
                }

                if(cnt == 5){
                    InputFilter inputFilter = new InputFilter.LengthFilter(s.toString().length());
                    InputFilter[] filters = new InputFilter[]{inputFilter};
                    et_hashtags.setFilters(filters);
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
                recycler_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

            }
            else{
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 3){
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

                    imageUploadAdapter = new ImageUploadAdapter(uriList, getApplicationContext());
                    recycler_image.setAdapter(imageUploadAdapter);
                    recycler_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
                }
            }
        }
    }
}