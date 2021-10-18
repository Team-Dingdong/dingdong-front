package org.techtown.dingdong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.chatting.ChattingFragment;
import org.techtown.dingdong.home.HomeFragment;
import org.techtown.dingdong.mypage.myPageFragment;
import org.techtown.dingdong.profile.profileFragment;

public class MainActivity extends AppCompatActivity {

    ChattingFragment chattingFragment;
    HomeFragment homeFragment;
    myPageFragment myPageFragment;
    profileFragment profileFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottom_home:
                        setFrag(0);
                        break;
                    case R.id.bottom_chatting:
                        setFrag(1);
                        break;
                    case R.id.bottom_profile:
                        setFrag(2);
                        break;
                    case R.id.bottom_mypage:
                        setFrag(3);
                        break;
                }
                return false;
            }
        });

        homeFragment = new HomeFragment().newInstance(1);
        chattingFragment = new ChattingFragment();
        profileFragment = new profileFragment();
        myPageFragment = new myPageFragment();

        setFrag(0); //첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 결정
    }



    //프래그먼트 교체가 일어나는 실행문이다.
    private void setFrag(int n) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (n) {
            case 0:
                fragmentTransaction.replace(R.id.container, homeFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.container, chattingFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.container, profileFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentTransaction.replace(R.id.container, myPageFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }



}