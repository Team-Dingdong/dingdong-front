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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MypurchasesFragment extends Fragment {

    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    ArrayList<Share> purchasesList = new ArrayList<>();
    Token token;

    public static MypurchasesFragment newInstance() {
        MypurchasesFragment fragment = new MypurchasesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypurchases, container, false);
        recyclerView = v.findViewById(R.id.recycler_purchases);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");


        token = new Token(access,refresh,expire,tokentype);
        token.setContext(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        historyAdapter = new HistoryAdapter(purchasesList,getActivity(),"mypurchases");
        /*
        historyAdapter.setListener(new HistoryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, String id) {
                Toast.makeText(getActivity(),id + ": 거래를 확정하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),RatingActivity.class);
                intent.putExtra("id",id);
                getActivity().startActivity(intent);

            }
        });
        */


        recyclerView.setAdapter(historyAdapter);

        getHistory(token);

        return v;
    }

    public void getHistory(Token token) {
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, getActivity());
        Call<HistoryResponse> call = apiinterface.getPurchasesHistory();
        call.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        HistoryResponse res = response.body();
                        //Log.d("성공", new Gson().toJson(res));

                        purchasesList.addAll(res.getHistorys());
                        historyAdapter.notifyDataSetChanged();
                    }

                }else{
                    Log.d("mypur,getPurcHistory", new Gson().toJson(response.errorBody()));
                    Log.d("mypur,getPurcHistory", response.toString());
                    Log.d("mypur,getPurcHistory", String.valueOf(response.code()));
                    Log.d("mypur,getPurcHistory", response.message());
                    Log.d("mypur,getPurcHistory", String.valueOf(response.raw().request().url().url()));
                    Log.d("mypur,getPurcHistory", new Gson().toJson(response.raw().request()));
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
    }

    public void setDummy(){
        purchasesList = new ArrayList<>();
        purchasesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","3"));
        purchasesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","5"));
        purchasesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","3"));
        purchasesList.add(new Share("hhi","","","","apdls","2021-08-25T23:55:11","20000","","5","1"));

    }
}