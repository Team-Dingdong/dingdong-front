package org.techtown.dingdong.mypage;

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

public class ProfileSalesAdapter extends RecyclerView.Adapter<ProfileSalesAdapter.ViewHolder>{
    ArrayList<Sales> salelist;
    Sales sales;

    public ProfileSalesAdapter(ArrayList<Sales> salelist) {
        this.salelist = salelist;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_sales, parent, false);
        ViewHolder holder= new ProfileSalesAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProfileSalesAdapter.ViewHolder holder, int position) {
    sales = salelist.get(position);
    }

    @Override
    public int getItemCount() {
        return salelist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_profile_group,tv_profile_sale_postTitle, tv_profile_sale_date;
        ImageView iv_profile_sale_img;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
