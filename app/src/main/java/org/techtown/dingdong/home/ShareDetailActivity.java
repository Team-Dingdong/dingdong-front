package org.techtown.dingdong.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.techtown.dingdong.R;

public class ShareDetailActivity extends AppCompatActivity {


    private ViewPager2 sliderImageViewPager;
    private LinearLayout layoutIndicator;
    private String[] images = new String[]{
            "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
            "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
            "https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg"
    };

    private String detail = "코로나19 예방접종 대응추진단(추진단)의 ‘코로나19 예방접종 8~9월 시행계획’에 따르면, 1972년 1월 1일생부터 2003년 12월 31일 출생자 1천777만 명이 접종 대상자다. 사전예약은 8월 9일부터 18일까지 10개 대상군으로 나눠 실시된다.\n" +
            "\n" +
            "날짜별 예약 대상은 해당 날짜 끝자리와 생년월일 끝자리가 일치하는 사람으로 지정된다. 가령, 예약이 시작되는 9일의 경우, 생년월일 끝자리가 9인 사람들이 예약 대상이다. 날짜별 예약은 오후 8시부터 이튿날 오후 6시까지 진행된다.";
    private String title = "18~49세 다음달 9일부터 10부제";
    private TextView tv_detail, tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);

        sliderImageViewPager = findViewById(R.id.image_slider);
        layoutIndicator = findViewById(R.id.layout_indicators);
        tv_detail = findViewById(R.id.tv_detail);
        tv_title = findViewById(R.id.tv_title);

        tv_detail.setText(detail);
        tv_title.setText(title);

        sliderImageViewPager.setOffscreenPageLimit(1);
        sliderImageViewPager.setAdapter(new ImageSliderAdapter(this, images));

        sliderImageViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        setupIndicators(images.length);


    }

    private void setupIndicators(int length) {
        ImageView [] indicators = new ImageView[length];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for(int i=0; i<childCount; i++){
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if(i == position){
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }

    }
}