package org.techtown.dingdong.home;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.techtown.dingdong.R;

public class SearchBarActivity extends AppCompatActivity {

    private EditText et_search;
    private ImageButton btn_search, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);


        et_search = findViewById(R.id.editText);
        btn_search = findViewById(R.id.btn_search);
        btn_back = findViewById(R.id.ic_back);


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_search.getText().toString().length()>0){
                    Intent intent = new Intent(SearchBarActivity.this,SearchResultActivity.class);
                    intent.putExtra("searchword",et_search.getText().toString());
                    startActivity(intent);

                }
                else{
                    Toast.makeText(SearchBarActivity.this,"검색어를 입력해주세요.",Toast.LENGTH_LONG).show();
                }

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}