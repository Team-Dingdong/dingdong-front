package org.techtown.dingdong.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView sharelistrecycler;
    ShareListAdpater shareListAdpater;
    private ArrayList<Share> sharelist_data, sharelist_latest = new ArrayList<>();
    private LinearLayout btn_trans;
    private TextView tv_align;
    private Boolean trans = true;
    private CircularProgressIndicator pgbar;
    private ArrayList<Share> mList = new ArrayList<>();
    int page = 0;
    Boolean loading = false;
    ArrayList<Share> createdList = new ArrayList<>();
    ArrayList<Share> endtimeList = new ArrayList<>();
    Token token;

    public Tab4Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab4Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab4Fragment newInstance(String param1, String param2) {
        Tab4Fragment fragment = new Tab4Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        Log.d("토큰", String.valueOf(access));

        token = new Token(access,refresh,expire,tokentype);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab4, container, false);
        sharelistrecycler = v.findViewById(R.id.sharelist);
        btn_trans = v.findViewById(R.id.trans);
        tv_align = v.findViewById(R.id.tv_align);
        pgbar = v.findViewById(R.id.progressbar);

        setCreatedData(token);



        sharelistrecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if((recyclerView.getAdapter().getItemCount() %5) == 0 && newState == recyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)){
                    if(!loading){ //로딩중이 아닐때만
                        pgbar.setVisibility(v.VISIBLE);
                        page ++;
                        pgbar.show();
                        if(trans){
                            //최신순 병렬일때 다음페이지 불러오기
                            loading = true;
                            setCreatedData(token);
                        }
                        else{
                            //마감임박순병렬일때 다음페이지 불러오기
                            loading = true;
                            setEndTimeData(token);
                        }
                    }

                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

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

        //setDummy();

        //setShareListRecycler(sharelistrecycler, sharelist_deadline);

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
        Call<PostResponse> call = apiinterface.getEndCategoryData(4);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        if(page == 0){
                            endtimeList = res.getData().getShare();
                            //String json = new Gson().toJson(res.getData().getShare());
                            setShareListRecycler(sharelistrecycler, endtimeList);}
                        else{
                            endtimeList.addAll(res.getData().getShare());
                            //String json = new Gson().toJson(res.getData().getShare());
                            shareListAdpater.notifyDataSetChanged();
                        }

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

        loading = false;


    }

    public void setCreatedData(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());
        Call<PostResponse> call = apiinterface.getCreatedCategoryData(4);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        if(page == 0){
                            createdList = res.getData().getShare();
                            //String json = new Gson().toJson(res.getData().getShare());
                            setShareListRecycler(sharelistrecycler, createdList);
                        }
                        else{
                            createdList.addAll(res.getData().getShare());
                            //String json = new Gson().toJson(res.getData().getShare());
                            shareListAdpater.notifyDataSetChanged();

                        }

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

        loading = false;


    }

    public void setDummy(){
        for (int i = 0; i < 5; i++) {
            sharelist_latest.add( new Share("hello","https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
                    "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                    "https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg",
                    "감자를 나눠주고 싶습니다 ~~~~~","2021-08-22 17:00:33.822","30000","우리집앞","4","3","1")
        );
        }



    }
}