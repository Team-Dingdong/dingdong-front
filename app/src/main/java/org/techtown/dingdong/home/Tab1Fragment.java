package org.techtown.dingdong.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//과일채소탭
public class Tab1Fragment extends Fragment {



    private RecyclerView sharelistrecycler;
    ShareListAdpater shareListAdpater;
    private ArrayList<Share> sharelist_data, sharelist_latest, sharelist_deadline;
    private LinearLayout btn_trans;
    private TextView tv_align;
    private Boolean trans = true;
    int page = 0;
    Boolean loading = false;
    ArrayList<Share> createdList = new ArrayList<>();
    ArrayList<Share> endtimeList = new ArrayList<>();
    Token token;
    NestedScrollView nestedScrollView;
    ProgressBar pgbar;



    public Tab1Fragment() {
        // Required empty public constructor
    }


    public static Tab1Fragment newInstance(String param1, String param2) {
        Tab1Fragment fragment = new Tab1Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab1, container, false);
        sharelistrecycler = v.findViewById(R.id.sharelist);
        btn_trans = v.findViewById(R.id.trans);
        tv_align = v.findViewById(R.id.tv_align);
        nestedScrollView = v.findViewById(R.id.scrollView);
        pgbar = v.findViewById(R.id.pgbar);


        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);

        Log.d("토큰", String.valueOf(access));

        setCreatedData(token);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    if(loading == false) page++;
                    pgbar.setVisibility(View.VISIBLE);
                    if(trans){
                        //최신순 병렬일때
                        loading = true;
                        setCreatedData(token);

                    }else{
                        //마감임박순 병렬일때
                        loading = true;
                        setEndTimeData(token);
                    }

                }
            }
        });


        tv_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trans){
                    //최신순병렬일때 마감임박순을 불러오기
                    page = 0;
                    tv_align.setText("마감임박순");
                    setEndTimeData(token);
                    trans = false; //마감임박순 병렬로 바꾸기
                }
                else{
                    //마감임박순병렬일때 최신순을 불러오기
                    page = 0;
                    tv_align.setText("최신순");
                    setCreatedData(token);
                    trans = true; //최신순병렬로 바꾸기
                }
            }
        });



        return v;
    }

    public void setShareListRecycler(RecyclerView sharelistrecycler, ArrayList<Share> sharelist){


        sharelist_data = new ArrayList<>();
        sharelist_data = sharelist;
        sharelistrecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        shareListAdpater = new ShareListAdpater(getActivity(), sharelist_data);
        sharelistrecycler.setAdapter(shareListAdpater);

    }

    public void setEndTimeData(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());
        Call<PostResponse> call = apiinterface.getEndCategoryData(1, page);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        pgbar.setVisibility(View.GONE);

                        if(page == 0){
                            endtimeList = res.getData().getShare();
                            setShareListRecycler(sharelistrecycler, endtimeList);
                            loading = false;
                        }
                        else{
                            endtimeList = res.getData().getShare();
                            setShareListRecycler(sharelistrecycler, endtimeList);
                            loading = false;}


                        Log.d("성공", new Gson().toJson(response.raw().request()));

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
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d("외않되", String.valueOf(t));

            }
        });


    }

    public void setCreatedData(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());
        Call<PostResponse> call = apiinterface.getCreatedCategoryData(1, page);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        pgbar.setVisibility(View.GONE);

                        if(page == 0){
                            createdList = res.getData().getShare();
                            setShareListRecycler(sharelistrecycler, createdList);
                            loading = false;
                        }
                        else{
                            createdList = res.getData().getShare();
                            setShareListRecycler(sharelistrecycler, createdList);
                            loading = false;

                        }
                        Log.d("성공", new Gson().toJson(response.raw().request()));

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
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d("외않되", String.valueOf(t));

            }
        });


    }

    public void setDummy(){/*
        sharelist_latest = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sharelist_latest.add(new Share("감자를 나누고 싶어요",new String[]{
                    "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
                    "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                    "https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg"
            }, "감자를 제발 나눠주고 싶네요 \n 집에 너무 많아가지고 힘들어요...","7분전","#감자 #나눠요","20,000","노원구청앞",4,2));
        }

        sharelist_deadline = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sharelist_deadline.add(new Share("양파를 나누고 싶어요",new String[]{
                    "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                    "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
                    "https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg"
            }, "감자를 제발 나눠주고 싶네요 \n 집에 너무 많아가지고 힘들어요...","7분전","#양파 #나눠요","10,000","울집앞",4,3));
        }
*/
    }
}