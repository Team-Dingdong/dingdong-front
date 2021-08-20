package org.techtown.dingdong.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ImageButton btn_edit;
    private RecyclerView sharelistrecycler;
    ShareListAdpater shareListAdpater;
    private ArrayList<Share> sharelist_data, sharelist_latest, sharelist_deadline;
    private LinearLayout btn_trans, fruit;
    private TextView tv_align;


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
        btn_trans = v.findViewById(R.id.trans);
        tv_align = v.findViewById(R.id.align);
        fruit = v.findViewById(R.id.fruit);


        setDummy();

        setShareListRecycler(sharelistrecycler, sharelist_deadline);


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditActivity.class));
            }
        });

        btn_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).replaceFragment(MainFragment.newInstance());

            }
        });

        return v;
    }

    public void setShareListRecycler(RecyclerView sharelistrecycler, ArrayList<Share> sharelist){


        sharelist_data = new ArrayList<>();
        sharelist_data = sharelist;
        sharelistrecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        shareListAdpater = new ShareListAdpater(getActivity(), sharelist_data);
        sharelistrecycler.setAdapter(shareListAdpater);

    }




    public void setDummy(){
        sharelist_latest = new ArrayList<>();
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
        }

    }





}
