package org.techtown.dingdong.mypage;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;

import java.util.ArrayList;


public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
    ArrayList<Sales> saleslist;
    private Context context;
    Sales sales;
    Button btn_mypage_sales;

    public SalesAdapter(Context context, ArrayList<Sales> saleslist){
        this.context = context;
        this.saleslist = saleslist;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mypage_sales, parent, false);
        ViewHolder holder = new SalesAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SalesAdapter.ViewHolder holder, int position) {
        sales =  saleslist.get(position);
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btn_mypage_sales){
                    if(btn_mypage_sales.getText() == "거래 확정하기"){
                        //거래 확정하기
                        DialogClick(view);




                    }
                    else if(btn_mypage_sales.getText() == "평가하기"){
                        //평가하기 창으로 넘어가기
                        //((MainActivity) getActivity()).replaceFragment(estimateFragment);
                        MainActivity activity = (MainActivity)context;
                        activity.replaceFragment(estimateFragment);

                    }
                }
            }
        });*/

        holder.btn_mypage_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.btn_mypage_sales.getText().toString().equals("거래 확정하기")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("거래 확정").setMessage("거래를 확정하겠습니까?");
                    builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "거래 확정을 해야 평가하기가 가능합니다.", Toast.LENGTH_LONG).show();
                        }
                    });

                    builder.setNeutralButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "거래가 확정됐습니다.", Toast.LENGTH_LONG).show();
                            holder.btn_mypage_sales.setText("평가하기");
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                else if(holder.btn_mypage_sales.getText().toString().equals("평가하기")){
                    MainActivity activity = (MainActivity)context;
                   // EstimateFragment estimateFragment = new EstimateFragment();
                   // activity.replaceFragment(estimateFragment);
                }
            }

        });
    }


    @Override
    public int getItemCount() {
        return saleslist.size();
    }



    public void DialogClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("거래 확정").setMessage("거래를 확정하겠습니까?");
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(view.getContext().getApplicationContext(), "거래 확정을 해야 평가하기가 가능합니다.", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNeutralButton("네", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(view.getContext().getApplicationContext(),"거래가 확정됐습니다.", Toast.LENGTH_LONG).show();
                btn_mypage_sales.setText("평가하기");
            }
        });
        AlertDialog alertDialog = builder.create(); alertDialog.show();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sale_date, tv_posttitle,tv_group;
        Button btn_mypage_sales;
        ImageView iv_post;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //tv = itemView.findViewById(R.id)
            tv_sale_date =itemView.findViewById(R.id.tv_profile_sale_date);
            tv_posttitle = itemView.findViewById(R.id.tv_profile_sale_postTitle);
            tv_group = itemView.findViewById(R.id.tv_profile_group);
            btn_mypage_sales = itemView.findViewById(R.id.btn_mypage_sales);
            iv_post = itemView.findViewById(R.id.iv_profile_sale_img);
        }
    }
}
