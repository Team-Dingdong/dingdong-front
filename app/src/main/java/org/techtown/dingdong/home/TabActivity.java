package org.techtown.dingdong.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.mypage.ModifyInfoActivity;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabActivity extends AppCompatActivity {

    ImageButton btn_back, btn_search;
    ViewPager2 viewPager;
    PagerAdapter pagerAdapter;
    TextView tv_region;
    int pos, Id;
    private String[] tabs = new String[]{"과일·채소", "육류·계란", "간식", "생필품", "기타"};
    String[] region = {"미아2동", "안암동"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Intent intent = getIntent();
        String getpos = intent.getStringExtra("id");
        Id = intent.getIntExtra("region",0);
        pos = Integer.parseInt(getpos);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken", "");
        String refresh = pref.getString("oauth.refreshtoken", "");
        String expire = pref.getString("oauth.expire", "");
        String tokentype = pref.getString("oauth.tokentype", "");

        Token token = new Token(access, refresh, expire, tokentype);
        token.setContext(TabActivity.this);


        Fragment fragment1 = new Tab1Fragment().newInstance(Id);
        Fragment fragment2 = new Tab2Fragment().newInstance(Id);
        Fragment fragment3 = new Tab3Fragment().newInstance(Id);
        Fragment fragment4 = new Tab4Fragment().newInstance(Id);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        btn_back = findViewById(R.id.btn_back);
        btn_search = findViewById(R.id.btn_search);
        viewPager = findViewById(R.id.pager);
        tv_region = findViewById(R.id.tv_region);


        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, TabActivity.this);
        Call<LocalResponse> call = apiinterface.getLocal();
        call.enqueue(new Callback<LocalResponse>() {
            @Override
            public void onResponse(Call<LocalResponse> call, Response<LocalResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("LOCAL_READ_SUCCESS")){
                        LocalResponse res = response.body();
                        LocalResponse.Data data = res.getData().get(Id-1);
                        tv_region.setText(data.getName());

                    }else{

                    }

                }
            }

            @Override
            public void onFailure(Call<LocalResponse> call, Throwable t) {

            }
        });

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