package org.techtown.dingdong.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import org.techtown.dingdong.R;

public class slideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        ViewPager pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2); //3개까지 caching

        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), 1);

        mySalesFragment mySalesFragment = new mySalesFragment();
        adapter.addItem(mySalesFragment);

        myPurchaseFragment myPurchaseFragment = new myPurchaseFragment();
        adapter.addItem(myPurchaseFragment);

        pager.setAdapter(adapter);


    }

}