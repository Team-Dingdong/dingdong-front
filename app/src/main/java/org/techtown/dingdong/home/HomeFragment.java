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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
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
import org.techtown.dingdong.login_register.LoginActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.mypage.ModifyInfoActivity;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    private ImageButton btn_edit, cat1, cat2, cat3, cat4, btn_trans, btn_search;
    private RecyclerView sharelistrecycler;
    ShareListAdpater shareListAdpater;
    private TextView tv_align, tv_region;
    private Spinner select_region;
    private String selected_region;
    private Boolean trans = true; //버튼 선택시 true(최신순) -> false(마감임)
    ArrayList<Share> createdList = new ArrayList<>();
    ArrayList<Share> endtimeList = new ArrayList<>();
    Token token;
    int page = 0;
    Boolean loading = false;
    NestedScrollView nestedScrollView;
    ProgressBar pgbar;
    ArrayList<Share> shareList = new ArrayList<>();
    int Id = 0;
    String[] region = new String[]{"동네택 선","지역1", "지역2"};

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(int id) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("regionid",id);
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

        Id = this.getArguments().getInt("regionid");
        List<String> where = new ArrayList<String>();
        where.add("동네선택");

        Log.d("home",region[0].toString());
        Log.d("home",region[1].toString());

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
        nestedScrollView = v.findViewById(R.id.scrollView);
        pgbar = v.findViewById(R.id.pgbar);

        sharelistrecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        shareListAdpater = new ShareListAdpater(getActivity(), shareList);
        sharelistrecycler.setAdapter(shareListAdpater);


        //토큰 불러오고 context 저장
        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);
        token.setContext(getActivity());

        Log.d("토큰", String.valueOf(access));
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, getActivity());
        Call<LocalResponse> call = apiinterface.getLocal();
        call.enqueue(new Callback<LocalResponse>() {
            @Override
            public void onResponse(Call<LocalResponse> call, Response<LocalResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("LOCAL_READ_SUCCESS")){
                        LocalResponse res = response.body();
                        LocalResponse.Data data1 = res.getData().get(0);
                        LocalResponse.Data data2 = res.getData().get(1);
                        where.add(data1.getName());
                        where.add(data2.getName());
                        region = new String[where.size()];
                        where.toArray(region);
                        tv_region.setText(region[Id]);
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
                                if(position != 0){
                                    Id = position;
                                    Log.d("selectid",Integer.toString(Id));

                                    Fragment fragment = new HomeFragment().newInstance(Id);
                                    MainActivity activity = (MainActivity) getActivity();
                                    activity.replaceFragment(fragment);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        select_region.setSelection(0);
                    }
                }else{

                }

            }

            @Override
            public void onFailure(Call<LocalResponse> call, Throwable t) {

            }
        });



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


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra("id","0");
                startActivity(intent);
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

        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),TabActivity.class);
                intent.putExtra("id","0");
                intent.putExtra("region",Id);
                startActivity(intent);

            }
        });

        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TabActivity.class);
                intent.putExtra("id","1");
                intent.putExtra("region",Id);
                startActivity(intent);

            }
        });

        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TabActivity.class);
                intent.putExtra("id","2");
                intent.putExtra("region",Id);
                startActivity(intent);

            }
        });

        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TabActivity.class);
                intent.putExtra("id","3");
                intent.putExtra("region",Id);
                startActivity(intent);

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

    public void setCreatedData(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());

        Call<PostResponse> call = apiinterface.getCreatedData(Id, page);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        //Log.d("성공", new Gson().toJson(res));

                        if(page == 0 && !shareList.isEmpty()){
                            shareList = new ArrayList<>();
                        }

                        pgbar.setVisibility(View.GONE);
                        shareList.addAll(res.getData().getShare());
                        shareListAdpater.notifyDataSetChanged();

                    }

                }else if(response.code() == 404){
                    Log.w("home,getCreatedD","해당 동네를 찾을 수 없습니다.");

                }else{
                    Log.d("home,getCreatedD", new Gson().toJson(response.errorBody()));
                    Log.d("home,getCreatedD", response.toString());
                    Log.d("home,getCreatedD", String.valueOf(response.code()));
                    Log.d("home,getCreatedD", response.message());
                    Log.d("home,getCreatedD", String.valueOf(response.raw().request().url().url()));
                    Log.d("home,getCreatedD", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d("home,getCreatedD", String.valueOf(t));

            }
        });

        loading = false;


    }

    public void setEndTimeData(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());

        Call<PostResponse> call = apiinterface.getEndData(Id, page);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        //Log.d("성공", new Gson().toJson(res));

                        if(page == 0 && !shareList.isEmpty()){
                            shareList = new ArrayList<>();
                        }

                        pgbar.setVisibility(View.GONE);
                        shareList.addAll(res.getData().getShare());
                        shareListAdpater.notifyDataSetChanged();

                    }

                }else if(response.code() == 404){
                    Log.w("home,getEndD","해당 동네를 찾을 수 없습니다.");

                }else{
                    Log.d("home,getEndD", new Gson().toJson(response.errorBody()));
                    Log.d("home,getEndD", response.toString());
                    Log.d("home,getEndD", String.valueOf(response.code()));
                    Log.d("home,getEndD", response.message());
                    Log.d("home,getEndD", String.valueOf(response.raw().request().url().url()));
                    Log.d("home,getEndD", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d("home,getEndD", String.valueOf(t));

            }
        });

        loading = false;

    }

    @Override
    public void onResume() {
        super.onResume();
        tv_region.setText(region[Id]);
        page = 0;
        tv_align.setText("최신순");
        shareList = new ArrayList<>();
        shareListAdpater = new ShareListAdpater(getActivity(), shareList);
        sharelistrecycler.setAdapter(shareListAdpater);
        sharelistrecycler.scrollToPosition(0);
        setCreatedData(token);
        trans = true;
    }
}
