package org.techtown.dingdong.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.LoginRequest;
import org.techtown.dingdong.login_register.LoginResponse;
import org.techtown.dingdong.login_register.SetProfileActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.mytown.TownActivity;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;
import org.techtown.dingdong.profile.UserProfileActivity;
import org.techtown.dingdong.profile.UserProfileResponse;
import org.techtown.dingdong.profile.profileFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class myPageFragment extends Fragment{

    Button btn_mypage, btn_town, btn_history, btn_change, btn_setting;
    ImageView img_profile;
    TextView tv_nickname, tv_like, tv_dislike;
    Token token;

    public myPageFragment() {
        // Required empty public constructor
    }

    public static myPageFragment newInstance(String param1, String param2) {
        myPageFragment fragment = new myPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_mypage, container, false);

        TextView tv = v.findViewById(R.id.textView);
        btn_mypage= v.findViewById(R.id.btn_mypage);
        btn_town = v.findViewById(R.id.btn_town);
        btn_history = v.findViewById(R.id.btn_history);
        btn_change = v.findViewById(R.id.btn_change);
        tv_nickname = v.findViewById(R.id.tv_nickname);
        tv_like = v.findViewById(R.id.tv_like);
        tv_dislike = v.findViewById(R.id.tv_dislike);
        img_profile = v.findViewById(R.id.img_profile);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);
        token.setContext(getActivity());

        getProfile(token);
        getRating(token);


        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyhistoryActivity.class));
            }
        });


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ModifyInfoActivity.class));

            }
        });

        btn_town.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TownActivity.class));
            }
        });


        return v;
    }

    public void getProfile(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, getActivity());
        Call<UserProfileResponse> call = apiinterface.getUserProfile();
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("PROFILE_READ_SUCCESS")) {
                        UserProfileResponse.Data res = response.body().getData();
                        tv_nickname.setText(res.getNickname());
                        Glide.with(getActivity())
                                .load(res.getProfileImg())
                                .into(img_profile);
                    }
                }
                else{
                    Log.d("실패", new Gson().toJson(response.errorBody()));
                    Log.d("실패", response.toString());
                    Log.d("실패", String.valueOf(response.code()));
                    Log.d("실패", response.message());
                    Log.d("실패", String.valueOf(response.raw().request().url().url()));
                    Log.d("실패", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {

                Log.d("외않되", String.valueOf(t));

            }
        });


    }

    public void getRating(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, getActivity());
        Call<UserRatingResponse> call = apiinterface.getRating();
        call.enqueue(new Callback<UserRatingResponse>() {
            @Override
            public void onResponse(Call<UserRatingResponse> call, Response<UserRatingResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("RATING_READ_SUCCESS")) {
                        UserRatingResponse.Data res = response.body().getData();
                        tv_like.setText(res.getGood());
                        tv_dislike.setText(res.getBad());
                    }
                }
                else{
                    Log.d("실패", new Gson().toJson(response.errorBody()));
                    Log.d("실패", response.toString());
                    Log.d("실패", String.valueOf(response.code()));
                    Log.d("실패", response.message());
                    Log.d("실패", String.valueOf(response.raw().request().url().url()));
                    Log.d("실패", new Gson().toJson(response.raw().request()));
                }

            }

            @Override
            public void onFailure(Call<UserRatingResponse> call, Throwable t) {
                Log.d("외않되", String.valueOf(t));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfile(token);
        getRating(token);
    }
}
