package org.techtown.dingdong.mypage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.Share;

import java.sql.Timestamp;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<Share> historyList;
    Context context;
    Share share;
    String state;
    private onItemClickListener listener = null;

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener{
        void onItemClick(int position, String id);
    }

    public HistoryAdapter(ArrayList<Share> historyList, Context context, String state) {
        this.historyList = historyList;
        this.context = context;
        this.state = state;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mysales, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HistoryAdapter.ViewHolder holder, int position) {
        share = historyList.get(position);

        if(state.equals("profile")||state.equals("mypurchases")){
            holder.tv_confirm.setVisibility(View.GONE);
        }

        holder.tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position, historyList.get(position).getId());
            }
        });

        holder.tv_title.setText(share.getTitle());
        holder.tv_parti.setText(share.getGatheredPeople()+"명 참여");
        holder.tv_date.setText(calcDate(share.getDate()));
        Glide.with(context)
                .load(share.getImage1())
                .into(holder.img_post);

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_parti, tv_title, tv_confirm;
        ImageView img_post;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_parti = itemView.findViewById(R.id.tv_participants);
            tv_confirm = itemView.findViewById(R.id.tv_confirm);
            tv_title = itemView.findViewById(R.id.tv_title);
            img_post = itemView.findViewById(R.id.imageView);
        }
    }

    public String calcDate(String date){
        String time = date;
        String result;
        time = time.substring(0,10) + " " +time.substring(11,19);
        Log.d("time",time);
        Timestamp posttime = Timestamp.valueOf(time);
        Timestamp curtime = new Timestamp(System.currentTimeMillis());
        long diff = curtime.getTime() - posttime.getTime();
        //long mysec = diff / 1000 % 60;
        long mymin = diff / (60 * 1000) % 60;
        long myhour = diff / (60 * 60 * 1000) % 24;
        long myday = diff / (24 * 60 * 60 * 1000);

        if(myday > 0){
            result = Integer.toString((int) myday) + "일전";

        }else if(myday <= 0 && myhour > 0){
            result = Integer.toString((int) myhour) + "시간" + "전";
        }
        else{
            result = Integer.toString((int) mymin) + "분" + "전";
        }

        return result;
    }
}
