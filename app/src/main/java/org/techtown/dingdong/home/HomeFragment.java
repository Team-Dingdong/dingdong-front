package org.techtown.dingdong.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    private ImageButton btn_edit, cat1, cat2, cat3, cat4, btn_trans, btn_search;
    private RecyclerView sharelistrecycler;
    ShareListAdpater shareListAdpater;
    private List<Share> list = null;
    private ArrayList<Share> sharelist_data, sharelist_latest, sharelist_deadline;
    private TextView tv_align, tv_region;
    private Spinner select_region;
    private String selected_region;
    private Boolean trans = true; //버튼 선택시 true(최신순) -> false(마감임)
    String[] region = {"미아2동", "안암동"};


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment contentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        btn_edit = v.findViewById(R.id.btn_edit);
        sharelistrecycler = v.findViewById(R.id.sharelist);
        btn_trans = v.findViewById(R.id.btn_trans);
        tv_align = v.findViewById(R.id.tv_align);
        tv_region = v.findViewById(R.id.tv_region);
        select_region = v.findViewById(R.id.select_region);
        cat1 = v.findViewById(R.id.cat1);
        cat2 = v.findViewById(R.id.cat2);
        cat3 = v.findViewById(R.id.cat3);
        cat4 = v.findViewById(R.id.cat4);
        btn_search = v.findViewById(R.id.ic_search);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        Token token = new Token(access,refresh,expire,tokentype);

        Log.d("토큰", String.valueOf(access));

        setCreatedData(token);


        tv_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trans){
                    //최신순병렬일때 마감임박순을 불러오기
                    tv_align.setText("마감임박순");
                    setEndTimeData(token);
                    trans = false; //마감임박순 병렬로 바꾸기
                }
                else{
                    //마감임박순병렬일때 최신순을 불러오기
                    tv_align.setText("최신순");
                    setCreatedData(token);
                    trans = true; //최신순병렬로 바꾸기
                }
            }
        });



        //동네 선택 스피너 세팅
        ArrayAdapter<String> regionadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, region){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =  super.getView(position, convertView, parent);

                ((TextView) v).setTextColor(Color.WHITE);

                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
                View v =  super.getDropDownView(position, convertView, parent);


                return v;
            }
        };
        regionadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        select_region.setAdapter(regionadapter);
        select_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_region = region[position];
                tv_region.setText(selected_region);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //setDummy();
        //setShareListRecycler(sharelistrecycler, mList);


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra("id","0");
                //intent.putExtra("id", sharelist.get(position).getId());
                startActivity(intent);
                //startActivity(new Intent(getActivity(), ShareDetailActivity.class));
            }
        });

        btn_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).replaceFragment(MainFragment.newInstance(0));

            }
        });

        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(MainFragment.newInstance(1));

            }
        });

        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(MainFragment.newInstance(2));

            }
        });

        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(MainFragment.newInstance(3));

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchBarActivity.class));

            }
        });

        return v;
    }

    public void setShareListRecycler(RecyclerView sharelistrecycler, ArrayList<Share> sharelist){


        sharelistrecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        shareListAdpater = new ShareListAdpater(getActivity(), sharelist);
        sharelistrecycler.setAdapter(shareListAdpater);

    }

    public void setCreatedData(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());

        Call<PostResponse> call = apiinterface.getCreatedData(0);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        ArrayList<Share> mList = new ArrayList<>();
                        mList = res.getData().getShare();
                        //String json = new Gson().toJson(res.getData().getShare());
                        setShareListRecycler(sharelistrecycler, mList);

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

    public void setEndTimeData(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());

        Call<PostResponse> call = apiinterface.getEndData(0);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        ArrayList<Share> mList = new ArrayList<>();
                        mList = res.getData().getShare();
                        //String json = new Gson().toJson(res.getData().getShare());
                        setShareListRecycler(sharelistrecycler, mList);

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



    public void setDummy(){
        /*sharelist_latest = new ArrayList<>();
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
        }*/

    }





}
