package org.techtown.dingdong.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

public class SearchResultActivity extends AppCompatActivity {

    private TextView tv_result;
    private String searchword;
    private ImageButton btn_back, btn_search;
    ShareListAdpater shareListAdpater;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        searchword = intent.getStringExtra("searchword");

        btn_back = findViewById(R.id.ic_back);
        btn_search = findViewById(R.id.btn_search);

        tv_result = findViewById(R.id.tv_searchword);
        recyclerView = findViewById(R.id.recycler_searchres);
        tv_result.setText(searchword);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken", "");
        String refresh = pref.getString("oauth.refreshtoken", "");
        String expire = pref.getString("oauth.expire", "");
        String tokentype = pref.getString("oauth.tokentype", "");

        Token token = new Token(access, refresh, expire, tokentype);

        //SearchRequest searchRequest = new SearchRequest(searchword);


        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,SearchResultActivity.this);
        Call<PostResponse> call = apiinterface.getSearchData(searchword);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("SEARCH_SUCCESS")){
                        PostResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));

                        ArrayList<Share> mList = new ArrayList<>();
                        mList = res.getData().getShare();
                        //String json = new Gson().toJson(res.getData().getShare());
                        setShareListRecycler(recyclerView, mList);

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


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void setShareListRecycler(RecyclerView sharelistrecycler, ArrayList<Share> sharelist){


        sharelistrecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        shareListAdpater = new ShareListAdpater(this, sharelist);
        sharelistrecycler.setAdapter(shareListAdpater);

    }
}