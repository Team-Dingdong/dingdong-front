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

import java.util.ArrayList;

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
                //Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),RatingActivity.class);
                intent.putExtra("id",id);
                getActivity().startActivity(intent);

            }
        });
        recyclerView.setAdapter(historyAdapter);

        getHistory(token);


        return v;
    }

    public void getHistory(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, getActivity());
        Call<PostResponse> call = apiinterface.getSalesHistory();
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        salesList.addAll(res.getData().getShare());
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
            public void onFailure(Call<PostResponse> call, Throwable t) {

            }
        });
    }

    public void setDummy(){
        salesList = new ArrayList<>();
        salesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","3"));
        salesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","5"));
        salesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","3"));
        salesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","1"));

    }
}