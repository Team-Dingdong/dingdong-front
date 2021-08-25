package org.techtown.dingdong.mytown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.techtown.dingdong.R;

public class TownRecyclActivity extends AppCompatActivity {
    //문자열 배열 만들기
    String [] townname = new String[2];
    int validation=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_recycl);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        TownAdapter adapter = new TownAdapter();
        //adapter.addItem(new Town("돈암동");, 버튼 클릭시 데이터 전달되도록 !

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnTownItemClickListener() {
            @Override
            public void onItemClick(TownAdapter.ViewHoldder holder, View view, int position) {
                Town item = adapter.getItem(position); //아이템 글릭 시 어댑터에서 해당 아이템의 town 객체 가져오기
                if(townname.length == 2){
                    Toast.makeText(getApplicationContext(),"동은 최대 두 개까지만 선택할 수 있습니다",Toast.LENGTH_SHORT);
                }
                else{
                    //중복검사하고 비워주기
                    for(int i=0; i<2; i++){
                        if(item.name == townname[i]){
                                validation =1;
                            if(i==0 && townname[1] != null){
                                townname[0] = null;
                                townname[0] = townname[1];
                                townname[1] = null;
                            }
                            else {
                                townname[i] = null;
                            }
                        }

                    }
                    //중복검사 끝낸 배열에서 빈자리에 추가해주기
                    for(int i=0; i<2; i++){

                        if(townname[i] == null){
                            townname[i] = item.name;
                        }
                    }

            }
        };
    });
}}