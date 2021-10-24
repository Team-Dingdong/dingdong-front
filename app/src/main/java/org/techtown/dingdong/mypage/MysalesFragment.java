package org.techtown.dingdong.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.PostResponse;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;
import org.techtown.dingdong.profile.HistoryResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MysalesFragment extends Fragment {
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    ArrayList<Share> salesList = new ArrayList<>();
    Token token;

    public MysalesFragment() {
    }

    public static MysalesFragment newInstance() {
        MysalesFragment fragment = new MysalesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mysales, container, false);
        recyclerView = v.findViewById(R.id.recycler_sales);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);
        token.setContext(getActivity());


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        historyAdapter = new HistoryAdapter(salesList,getActivity(),"mysales");
        historyAdapter.setListener(new HistoryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, String id) {

                setConfirm(token, id);

            }
        });
        recyclerView.setAdapter(historyAdapter);

        getHistory(token);


        return v;
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

                Log.d("tag",t.toString());
            }
        });

    }

    public void setConfirm(Token token, String id){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, getActivity());
        Call<ResponseBody> call = apiinterface.setConfirm(Integer.parseInt(id));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    Toast.makeText(getActivity(), "거래가 확정되었습니다. 평가를 진행해주세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),RatingActivity.class);
                    intent.putExtra("id",id);
                    getActivity().startActivity(intent);

                }else if(response.code() == 403){
                    Toast.makeText(getActivity(), "이미 확정된 거래입니다.", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("tag",t.toString());

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getHistory(token);
    }

    public void setDummy(){
        salesList = new ArrayList<>();
        salesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","3"));
        salesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","5"));
        salesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","3"));
        salesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","1"));

    }
}