package org.techtown.dingdong.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.LoginRequest;
import org.techtown.dingdong.login_register.LoginResponse;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.mytown.changetownActivity;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;
import org.techtown.dingdong.profile.profileFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class myPageFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btn_mypage,btn_town, btn_history, btn_change_userdata, btn_setting;
    ImageView iv_mypage;
    TextView tv_mypage, tv_goobnum, tv_badnum;
    int get_id;

    mySalesFragment mySalesFragment = new mySalesFragment();
    changeUserDataFragment changeUserDataFragment = new changeUserDataFragment();
    settingFragment settingFragment = new settingFragment();
    profileFragment profileFragment = new profileFragment();

    public myPageFragment() {
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
    public static myPageFragment newInstance(String param1, String param2) {
        myPageFragment fragment = new myPageFragment();
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

        View v = inflater.inflate(R.layout.fragment_mypage, container, false);

        TextView tv = v.findViewById(R.id.textView);
        Button btn_mypage,btn_town, btn_history, btn_change_userdata, btn_setting;
        ImageView iv_mypage;
        TextView tv_mypage, tv_goobnum, tv_badnum;


        btn_mypage= v.findViewById(R.id.btn_mypage);
        btn_town = v.findViewById(R.id.btn_town);
        btn_history = v.findViewById(R.id.btn_history);
        btn_change_userdata = v.findViewById(R.id.btn_change_userdata);
        btn_setting = v.findViewById(R.id.btn_setting);
        iv_mypage = v.findViewById(R.id.iv_mypage);
        tv_mypage = v.findViewById(R.id.tv_mypage);
        tv_goobnum = v.findViewById(R.id.tv_goobnum);
        tv_badnum = v.findViewById(R.id.tv_badnum);
        iv_mypage = v.findViewById(R.id.iv_mypage);

        btn_mypage.setOnClickListener(this::onClick);
        iv_mypage.setOnClickListener(this::onClick); //작은
        btn_town.setOnClickListener(this::onClick);
        btn_history.setOnClickListener(this::onClick);
        btn_change_userdata.setOnClickListener(this::onClick);
        btn_setting.setOnClickListener(this::onClick);


        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        Token token = new Token(access,refresh,expire,tokentype);

        Log.d("토큰", String.valueOf(access));

        Apiinterface apiinterface = Api.createService(Apiinterface.class,token,getActivity());
        Call<LoginResponse> call = apiinterface.LoginRequest(new LoginRequest("01011111111","123456"));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("불러오기성공", new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });





        return v;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_mypage:{
                ((MainActivity) getActivity()).replaceFragment(profileFragment);
            }
            case R.id.iv_mypage: {
                ((MainActivity) getActivity()).replaceFragment(profileFragment);
            }
            case R.id.btn_town: {
                getActivity().startActivity(new Intent(getActivity(), changetownActivity.class));
            }
            case R.id.btn_history:{
                getActivity().startActivity(new Intent(getActivity(), slideActivity.class));
            }
            case R.id.btn_change_userdata:{
                ((MainActivity) getActivity()).replaceFragment(changeUserDataFragment);
            }
            case R.id.btn_setting:{
                ((MainActivity) getActivity()).replaceFragment(settingFragment);
            }
        }
    }
}
