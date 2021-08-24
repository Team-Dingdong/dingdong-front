package org.techtown.dingdong.mytown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import org.techtown.dingdong.R;

public class TownRecyclActivity extends AppCompatActivity {

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
    }
}