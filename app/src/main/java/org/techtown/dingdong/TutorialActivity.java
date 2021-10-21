package org.techtown.dingdong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.techtown.dingdong.login_register.LoginActivity;
import org.techtown.dingdong.login_register.LoginOrRegisterActivity;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager2 sliderImageViewPager;
    private SpringDotsIndicator indicator;
    private List<Integer> tutorialLayouts;
    private PrefManager prefManager;
    FragmentStateAdapter fragmentStateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Log.d("tutorialactivity","tut");

        prefManager = new PrefManager(TutorialActivity.this);
        if(!prefManager.isFirstTimeLaunch()){
            Log.d("tutorialactivity","nofirst");
            launchHomeScreen();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        sliderImageViewPager = findViewById(R.id.image_slider);
        indicator = findViewById(R.id.indicator);

        sliderImageViewPager.setOffscreenPageLimit(1);
        tutorialLayouts = new ArrayList<>();
        tutorialLayouts.add(R.layout.item_tutorial1);
        tutorialLayouts.add(R.layout.item_tutorial2);
        tutorialLayouts.add(R.layout.item_tutorial3);
        //tutorialLayouts.add(R.layout.item_tutorial4);
        tutorialLayouts.add(R.layout.item_tutorial);

        fragmentStateAdapter = new TutorialAdapter(TutorialActivity.this, tutorialLayouts);
        sliderImageViewPager.setAdapter(fragmentStateAdapter);
        indicator.setViewPager2(sliderImageViewPager);


    }

    public void launchHomeScreen(){
        prefManager.setFirstTimeLaunch(false);
        SharedPreferences preferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        if(preferences.getBoolean("oauth.loggedin",false)){
            //로그인 되어 있을 경우 메인액티비티로
            startActivity(new Intent(TutorialActivity.this, MainActivity.class));
            finish();
        }
        else{
            //로그인 안되어 있을때는 로그인액티비티로
            startActivity(new Intent(TutorialActivity.this, LoginOrRegisterActivity.class));
            finish();
        }
    }

}