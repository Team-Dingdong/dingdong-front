package org.techtown.dingdong.mypage;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;

import java.util.ArrayList;
import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    ArrayList<Sales> items = new ArrayList<Sales>();




    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Button btn_mypage_sales = viewGroup.findViewById(R.id.btn_mypage_sales);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mypage_sales,viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btn_mypage_sales){
                    if(btn_mypage_sales.getText() == "거래 확정하기"){
                        //거래 확정하기
                        DialogClick(view);



                    }
                    else if(btn_mypage_sales.getText() == "평가하기"){
                        //평가하기 창으로 넘어가기
                    }
                }
            }
        });
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Sales item){
        items.add(item);
    }

    public void setItems(ArrayList<Sales> items){
        this.items = items;
    }
    public Sales getItems(int position){
        return items.get(position);
    }

    public void setItem(int position, Sales item){
        items.set(position, item);
    }

    public void DialogClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("다이얼로그").setMessage("다이얼로그가 보인다면 성공입니다. 축하합니다!");
        builder.setPositiveButton("성공", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(view.getContext().getApplicationContext(), "Yeah!!", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("실패", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(view.getContext().getApplicationContext(),"Try again!", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNeutralButton("Nuetral", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(view.getContext().getApplicationContext(),"neutral click", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = builder.create(); alertDialog.show();
    }


}
