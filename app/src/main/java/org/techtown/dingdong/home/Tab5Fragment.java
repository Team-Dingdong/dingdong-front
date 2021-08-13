package org.techtown.dingdong.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.techtown.dingdong.R;

import java.util.ArrayList;

//기타탭
public class Tab5Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView sharelistrecycler;
    ShareListAdpater shareListAdpater;
    private ArrayList<Share> sharelist_data, sharelist_latest, sharelist_deadline;
    private LinearLayout btn_trans;
    private TextView tv_align;

    public Tab5Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab5Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab5Fragment newInstance(String param1, String param2) {
        Tab5Fragment fragment = new Tab5Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        View v = inflater.inflate(R.layout.fragment_tab5, container, false);
        sharelistrecycler = v.findViewById(R.id.sharelist);
        btn_trans = v.findViewById(R.id.trans);
        tv_align = v.findViewById(R.id.align);

        setDummy();

        setShareListRecycler(sharelistrecycler, sharelist_deadline);

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