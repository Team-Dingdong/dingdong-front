package org.techtown.dingdong.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.PostResponse;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.home.ShareListAdpater;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mySalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mySalesFragment extends Fragment {

    RecyclerView recyclerView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private PageAdapter adapter;
    private SalesAdapter salesAdapter;
    private ArrayList<Sales> saleslist = new ArrayList<>();
    Token token;
    public mySalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mySalfesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mySalesFragment newInstance(String param1, String param2) {
        mySalesFragment fragment = new mySalesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        ArrayList<Sales> saleslist= new ArrayList<>();
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_my_sales, container, false);




        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);

        Log.d("토큰", String.valueOf(access));

        recyclerView = view.findViewById(R.id.rv_sales);

        setDummy();





        return view;
    }

    public void setRecycler(RecyclerView recyclerView, ArrayList<Sales> saleslist){


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        SalesAdapter salesAdapter = new SalesAdapter(getActivity(), saleslist); //어댑터 수정해야함
        recyclerView.setAdapter(salesAdapter);

    }


    public void gettUserPost(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());
        Call<SalesResponse> call = apiinterface.getSales();
        call.enqueue(new Callback<SalesResponse>() {
            @Override
            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().getResult().equals("POST_READ_SUCCESS")){
                        SalesResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        ArrayList<Sales> sales = res.getData().getSales();
                        SalesAdapter salesAdapter = new SalesAdapter(getActivity(), sales);

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
            public void onFailure(Call<SalesResponse> call, Throwable t) {

            }
        });
    }

    public void setDummy(){
        ArrayList<Sales> dummy = new ArrayList<>();
        dummy.add(new Sales("2021-01-01","감자를 나누고 싶어요","이미지","ㅇㅇ","5"));
        dummy.add(new Sales("2021-01-01","감자를 나누고 싶어요","이미지","ㅇㅇ","5"));
        dummy.add(new Sales("2021-01-01","감자를 나누고 싶어요","이미지","ㅇㅇ","5"));
        dummy.add(new Sales("2021-01-01","감자를 나누고 싶어요","이미지","ㅇㅇ","5"));
        dummy.add(new Sales("2021-01-01","감자를 나누고 싶어요","이미지","ㅇㅇ","5"));

        setRecycler(recyclerView, dummy);
    }


}