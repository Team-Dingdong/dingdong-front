package org.techtown.dingdong.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.Token;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstimateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstimateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    Button btn_estimate;
    private EstimateAdapter estimateAdapter;
    private ArrayList<Estimate> estimatelist= new ArrayList<>();

    Token token;

    public EstimateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstimateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EstimateFragment newInstance(String param1, String param2) {
        EstimateFragment fragment = new EstimateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        ArrayList<Estimate> estimatelist= new ArrayList<>();
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
        View view = inflater.inflate(R.layout.fragment_estimate, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);

        recyclerView = view.findViewById(R.id.recyclerView2);
        btn_estimate = view.findViewById(R.id.btn_estimate);

        setDummy();

        btn_estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Boolean> select_good = estimateAdapter.getSelectlist_good();
                ArrayList<Boolean> select_bad = estimateAdapter.getSelectlist_bad();
                for(int i=0; i< select_good.size(); i++){
                   if(select_good.get(i) == true){
                       //사용자 평가하기

                   }
                   if(select_bad.get(i) == true){
                       //사용자 평가하기
                   }
               }

            }
        });


        return view;
    }

    public void setRecyclerView(RecyclerView recyclerView, ArrayList<Estimate> estimatelist){

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        EstimateAdapter estimateAdapter = new EstimateAdapter(estimatelist, getActivity());
        recyclerView.setAdapter(estimateAdapter);

        //estimateAdapter.

    }

    public void setDummy(){
        ArrayList<Estimate> dummy = new ArrayList<>();
        dummy.add(new Estimate("null", "감자"));
        dummy.add(new Estimate("null", "양파"));
        dummy.add(new Estimate("null", "당근"));

        setRecyclerView(recyclerView, dummy);
    }

    public void estimateUser(Token token){

      //  estimatatelist
    }
}