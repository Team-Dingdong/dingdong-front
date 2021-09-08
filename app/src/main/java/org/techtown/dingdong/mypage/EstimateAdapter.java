package org.techtown.dingdong.mypage;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;

import java.util.ArrayList;

public class EstimateAdapter extends RecyclerView.Adapter<EstimateAdapter.ViewHolder> {
    ArrayList<Estimate> estimatelist;
    Context context;
    Estimate estimate;
    Button good, bad;
    String color_good= "회색";
    String color_bad = "회색";
    Boolean select_btn = false;
    ArrayList<Boolean> selectlist_good = new ArrayList<>();
    ArrayList<Boolean> selectlist_bad = new ArrayList<>();

    public EstimateAdapter(ArrayList<Estimate> estimatelist, Context context) {
        this.estimatelist = estimatelist;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public EstimateAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estimate, parent, false);
        ViewHolder holder = new EstimateAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EstimateAdapter.ViewHolder holder, int position) {
        estimate = estimatelist.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        holder.ibtn_estimate_goob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                select_btn = true;
                selectlist_good.add(position, select_btn);
                if(select_btn== true) {
                    Log.d("TAG", "평가버튼클릭");
                    holder.ibtn_estimate_bad.setBackgroundColor(Color.parseColor("#C5C5C5"));
                    holder.ibtn_estimate_goob.setBackgroundColor(Color.parseColor("#73BAD6"));

                    //color_good = "민트";
                    //holder.ibtn_estimate_bad.set
                }else{
                    holder.ibtn_estimate_bad.setBackgroundColor(Color.parseColor("#73BAD6"));
                    holder.ibtn_estimate_goob.setBackgroundColor(Color.parseColor("#C5C5C5"));
                }

            }
        });

        holder.ibtn_estimate_bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_btn = false;
                selectlist_bad.add(position, select_btn);
                if(select_btn== true) {
                    Log.d("TAG", "평가버튼클릭");
                    holder.ibtn_estimate_bad.setBackgroundColor(Color.parseColor("#C5C5C5"));
                    holder.ibtn_estimate_goob.setBackgroundColor(Color.parseColor("#73BAD6"));
                    //color_good = "민트";
                    //holder.ibtn_estimate_bad.set
                }else{
                    holder.ibtn_estimate_bad.setBackgroundColor(Color.parseColor("#73BAD6"));
                    holder.ibtn_estimate_goob.setBackgroundColor(Color.parseColor("#C5C5C5"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return estimatelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_estimate;
        ImageView iv_estimate;
        ImageButton ibtn_estimate_goob, ibtn_estimate_bad;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_estimate = itemView.findViewById(R.id.tv_estimate);
            iv_estimate = itemView.findViewById(R.id.iv_estimate);
            ibtn_estimate_goob = itemView.findViewById(R.id.ibtn_estimate_goob);
            ibtn_estimate_bad = itemView.findViewById(R.id.ibtn_estimate_bad);


        }
    }

    public ArrayList<Boolean> getSelectlist_good(){

        return selectlist_good;
    }

    public ArrayList<Boolean> getSelectlist_bad(){

        return selectlist_bad;
    }




}
