package org.techtown.dingdong.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    ViewPager2 viewPager;
    Context context;
    PagerAdapter pagerAdapter;
    private ImageButton btn_back;
    int pos;
    private String[] tabs = new String[]{"과일·채소", "육류·계란", "간식", "생필품", "기타"};

    public MainFragment(int pos) {
        this.pos = pos;
    }


    public static MainFragment newInstance(int pos) {
       return new MainFragment(pos);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        context = getActivity();
        Fragment fragment1 = new Tab1Fragment().newInstance("", "");
        Fragment fragment2 = new Tab2Fragment().newInstance("", "");
        Fragment fragment3 = new Tab3Fragment().newInstance("", "");
        Fragment fragment4 = new Tab4Fragment().newInstance("", "");
        //Fragment fragment5 = new Tab5Fragment().newInstance("", "");

        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.pager);
        btn_back = view.findViewById(R.id.btn_back);



        pagerAdapter = new PagerAdapter(this);
        pagerAdapter.addFrag(fragment1);
        pagerAdapter.addFrag(fragment2);
        pagerAdapter.addFrag(fragment3);
        pagerAdapter.addFrag(fragment4);
        //pagerAdapter.addFrag(fragment5);

        viewPager.setAdapter(pagerAdapter);


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabs[position])).attach();
        viewPager.setCurrentItem(pos);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(HomeFragment.newInstance("",""));
            }
        });


    }

}

