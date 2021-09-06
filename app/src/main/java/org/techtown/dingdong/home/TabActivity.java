package org.techtown.dingdong.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;

public class TabActivity extends AppCompatActivity {

    ImageButton btn_back, btn_search;
    ViewPager2 viewPager;
    PagerAdapter pagerAdapter;
    int pos;
    private String[] tabs = new String[]{"과일·채소", "육류·계란", "간식", "생필품", "기타"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Intent intent = getIntent();
        String getpos = intent.getStringExtra("id");
        pos = Integer.parseInt(getpos);

        Fragment fragment1 = new Tab1Fragment().newInstance("", "");
        Fragment fragment2 = new Tab2Fragment().newInstance("", "");
        Fragment fragment3 = new Tab3Fragment().newInstance("", "");
        Fragment fragment4 = new Tab4Fragment().newInstance("", "");

        TabLayout tabLayout = findViewById(R.id.tablayout);
        btn_back = findViewById(R.id.btn_back);
        btn_search = findViewById(R.id.btn_search);
        viewPager = findViewById(R.id.pager);

        pagerAdapter = new PagerAdapter(this);
        pagerAdapter.addFrag(fragment1);
        pagerAdapter.addFrag(fragment2);
        pagerAdapter.addFrag(fragment3);
        pagerAdapter.addFrag(fragment4);

        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabs[position])).attach();


        viewPager.setCurrentItem(pos);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TabActivity.this, SearchBarActivity.class));

            }
        });


    }


}