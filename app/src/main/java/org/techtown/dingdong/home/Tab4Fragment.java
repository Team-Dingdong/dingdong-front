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

//생필품탭
public class Tab4Fragment extends Fragment {


    private RecyclerView sharelistrecycler;
    ShareListAdpater shareListAdpater;
    private ArrayList<Share> sharelist_data;
    private LinearLayout btn_trans;
    private TextView tv_align;
    private Boolean trans = true;
    int page = 0;
    ArrayList<Share> createdList = new ArrayList<>();
    ArrayList<Share> endtimeList = new ArrayList<>();
    Token token;
    NestedScrollView nestedScrollView;
    ProgressBar pgbar;
    Boolean loading;
    ArrayList<Share> shareList = new ArrayList<>();
    int Id;


    public Tab4Fragment() {

    }


    public static Tab4Fragment newInstance(int region) {
        Tab4Fragment fragment = new Tab4Fragment();
        Bundle args = new Bundle();
        args.putInt("region",region);
        fragment.setArguments(args);
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
        View v = inflater.inflate(R.layout.fragment_tab4, container, false);
        Id = this.getArguments().getInt("region");
        sharelistrecycler = v.findViewById(R.id.sharelist);
        btn_trans = v.findViewById(R.id.trans);
        tv_align = v.findViewById(R.id.tv_align);
        nestedScrollView = v.findViewById(R.id.scrollView);
        pgbar = v.findViewById(R.id.pgbar);

        sharelistrecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        shareListAdpater = new ShareListAdpater(getActivity(), shareList);
        sharelistrecycler.setAdapter(shareListAdpater);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);
        token.setContext(getActivity());

        Log.d("토큰", String.valueOf(access));

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


        setCreatedData(token);
        trans = true;

        tv_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trans){
                    //최신순병렬일때 마감임박순을 불러오기
                    page = 0;
                    tv_align.setText("마감임박순");
                    shareList = new ArrayList<>();
                    shareListAdpater = new ShareListAdpater(getActivity(), shareList);
                    sharelistrecycler.setAdapter(shareListAdpater);
                    sharelistrecycler.scrollToPosition(0);
                    setEndTimeData(token);
                    trans = false; //마감임박순 병렬로 바꾸기
                }
                else{
                    //마감임박순병렬일때 최신순을 불러오기
                    page = 0;
                    tv_align.setText("최신순");
                    shareList = new ArrayList<>();
                    shareListAdpater = new ShareListAdpater(getActivity(), shareList);
                    sharelistrecycler.setAdapter(shareListAdpater);
                    sharelistrecycler.scrollToPosition(0);
                    setCreatedData(token);
                    trans = true; //최신순병렬로 바꾸기
                }
            }
        });

        btn_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trans){
                    //최신순병렬일때 마감임박순을 불러오기
                    page = 0;
                    tv_align.setText("마감임박순");
                    shareList = new ArrayList<>();
                    shareListAdpater = new ShareListAdpater(getActivity(), shareList);
                    sharelistrecycler.setAdapter(shareListAdpater);
                    sharelistrecycler.scrollToPosition(0);
                    setEndTimeData(token);
                    trans = false; //마감임박순 병렬로 바꾸기
                }
                else{
                    //마감임박순병렬일때 최신순을 불러오기
                    page = 0;
                    tv_align.setText("최신순");
                    shareList = new ArrayList<>();
                    shareListAdpater = new ShareListAdpater(getActivity(), shareList);
                    sharelistrecycler.setAdapter(shareListAdpater);
                    sharelistrecycler.scrollToPosition(0);
                    setCreatedData(token);
                    trans = true; //최신순병렬로 바꾸기
                }

            }
        });


        return v;
    }

    public void setEndTimeData(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());
        Call<PostResponse> call = apiinterface.getEndCategoryData(4, Id, page);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        if(page == 0 && !shareList.isEmpty()){
                            shareList = new ArrayList<>();
                        }

                        pgbar.setVisibility(View.GONE);
                        shareList.addAll(res.getData().getShare());
                        shareListAdpater.notifyDataSetChanged();
                        Log.d("성공", new Gson().toJson(response.raw().request()));

                    }

                }else if(response.code() == 404){
                    Log.w("tab4,getEndD","해당 동네를 찾을 수 없습니다.");

                }else{
                    Log.d("tab4,getEndD", new Gson().toJson(response.errorBody()));
                    Log.d("tab4,getEndD", response.toString());
                    Log.d("tab4,getEndD", String.valueOf(response.code()));
                    Log.d("tab4,getEndD", response.message());
                    Log.d("tab4,getEndD", String.valueOf(response.raw().request().url().url()));
                    Log.d("tab4,getEndD", new Gson().toJson(response.raw().request()));
                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d("tab4,getEndD", String.valueOf(t));

            }
        });

        loading = false;


    }

    public void setCreatedData(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());
        Call<PostResponse> call = apiinterface.getCreatedCategoryData(4, Id,page);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        if(page == 0 && !shareList.isEmpty()){
                            shareList = new ArrayList<>();
                        }

                        pgbar.setVisibility(View.GONE);
                        shareList.addAll(res.getData().getShare());
                        shareListAdpater.notifyDataSetChanged();
                        Log.d("성공", new Gson().toJson(response.raw().request()));

                    }

                }else if(response.code() == 404){
                    Log.w("tab4,getCreatedD","해당 동네를 찾을 수 없습니다.");

                }else{
                    Log.d("tab4,getCreatedD", new Gson().toJson(response.errorBody()));
                    Log.d("tab4,getCreatedD", response.toString());
                    Log.d("tab4,getCreatedD", String.valueOf(response.code()));
                    Log.d("tab4,getCreatedD", response.message());
                    Log.d("tab4,getCreatedD", String.valueOf(response.raw().request().url().url()));
                    Log.d("tab4,getCreatedD", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d("tab4,getCreatedD", String.valueOf(t));

            }
        });
        loading = false;

    }

}