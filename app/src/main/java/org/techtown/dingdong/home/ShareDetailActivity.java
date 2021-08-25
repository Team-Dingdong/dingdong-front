package org.techtown.dingdong.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.techtown.dingdong.R;

public class ShareDetailActivity extends AppCompatActivity {


    private ViewPager2 sliderImageViewPager;
    private SpringDotsIndicator indicator;
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
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);

        sliderImageViewPager = findViewById(R.id.image_slider);
        indicator = findViewById(R.id.indicator);
        tv_detail = findViewById(R.id.tv_detail);
        tv_title = findViewById(R.id.tv_title);
        btn_back = findViewById(R.id.ic_back);


        tv_detail.setText(detail);
        tv_title.setText(title);

        sliderImageViewPager.setOffscreenPageLimit(1);
        sliderImageViewPager.setAdapter(new ImageSliderAdapter(this, images));
        indicator.setViewPager2(sliderImageViewPager);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}