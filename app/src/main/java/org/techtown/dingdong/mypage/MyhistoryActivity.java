package org.techtown.dingdong.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.techtown.dingdong.R;
import org.techtown.dingdong.home.PagerAdapter;

public class MyhistoryActivity extends AppCompatActivity {
    ImageButton btn_back;
    ViewPager2 viewPager;
    PagerAdapter pagerAdapter;
    private String[] tabs = new String[]{"판매내역","구매내역"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhistory);
        btn_back = findViewById(R.id.ic_back);
        viewPager = findViewById(R.id.pager);

        Fragment mysalesFragment = new MysalesFragment().newInstance();
        Fragment mypurchaseFragment = new MypurchasesFragment().newInstance();

        TabLayout tabLayout = findViewById(R.id.tablayout);
        pagerAdapter = new PagerAdapter(this);
        pagerAdapter.addFrag(mysalesFragment);
        pagerAdapter.addFrag(mypurchaseFragment);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabs[position])).attach();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}