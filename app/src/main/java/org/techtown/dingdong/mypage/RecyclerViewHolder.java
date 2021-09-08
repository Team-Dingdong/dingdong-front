package org.techtown.dingdong.mypage;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {


    private TextView tv_sale_date, tv_group, tv_posttitle;
    private Button btn_mypage_sales;
    private ImageView iv_post;


    public RecyclerViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        tv_sale_date = itemView.findViewById(R.id.tv_profile_sale_date);
        tv_group = itemView.findViewById(R.id.tv_profile_group);
        tv_posttitle = itemView.findViewById(R.id.tv_profile_sale_postTitle);
        btn_mypage_sales = itemView.findViewById(R.id.btn_mypage_sales);
        iv_post = itemView.findViewById(R.id.iv_profile_sale_img);
    }

}
