package org.techtown.dingdong.profile;


import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.PostResponse;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.login_register.SetProfileActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.mypage.HistoryAdapter;
import org.techtown.dingdong.mypage.UserRatingResponse;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profileFragment extends Fragment {

    Button btn_setprofile;
    TextView tv_like, tv_dislike, tv_nickname;
    ImageView img_profile;
    RecyclerView recyclerView;
    Token token;
    ArrayList<Share> salesList = new ArrayList<>();
    HistoryAdapter historyAdapter;


    public profileFragment() {
    }

    public static profileFragment newInstance() {
        profileFragment fragment = new profileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        btn_setprofile = v.findViewById(R.id.btn_setprofile);
        tv_dislike = v.findViewById(R.id.tv_dislike);
        tv_like = v.findViewById(R.id.tv_like);
        tv_nickname = v.findViewById(R.id.tv_nickname);
        img_profile = v.findViewById(R.id.img_profile);
        recyclerView = v.findViewById(R.id.recycler_sales);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);
        token.setContext(getActivity());

        Log.d("토큰", String.valueOf(access));


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        historyAdapter = new HistoryAdapter(salesList,getActivity(),"profile");
        recyclerView.setAdapter(historyAdapter);

        getHistory(token);
        getProfile(token);
        getRating(token);

        btn_setprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetProfileActivity.class);
                intent.putExtra("state","correct");
                startActivity(intent);
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

    public void getHistory(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, getActivity());
        Call<HistoryResponse> call = apiinterface.getSalesHistory();
        call.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {

                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        HistoryResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        if(!salesList.isEmpty()){
                            salesList = new ArrayList<>();
                        }

                        salesList.addAll(res.getHistorys());
                        historyAdapter.notifyDataSetChanged();
                    }

                }else{
                    Log.d("실패", new Gson().toJson(response.errorBody()));
                    Log.d("실패", response.toString());
                    Log.d("실패", String.valueOf(response.code()));
                    Log.d("실패", response.message());
                    Log.d("실패", String.valueOf(response.raw().request().url().url()));
                    Log.d("실패", new Gson().toJson(response.raw().request()));
                }

            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getHistory(token);
        getProfile(token);
        getRating(token);
    }
}












