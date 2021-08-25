package org.techtown.dingdong.home;

import android.content.Context;
import android.content.Intent;
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
import org.techtown.dingdong.chatting.ChattingActivity;
import org.techtown.dingdong.chatting.PlanningActivity;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class ShareListAdpater extends RecyclerView.Adapter<ShareListAdpater.sharelistViewHolder> {

    private final ArrayList<Share> sharelist;
    private Context context;
    Share share;

    public ShareListAdpater(Context context, ArrayList<Share> sharelist) {

        this.context = context;
        this.sharelist = sharelist;
    }

    @NonNull
    @NotNull
    @Override
    public sharelistViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_share, parent, false);
        sharelistViewHolder holder = new ShareListAdpater.sharelistViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ShareListAdpater.sharelistViewHolder holder, int position) {

        share = sharelist.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShareDetailActivity.class);
                intent.putExtra("id", share.getId());
                context.startActivity(intent);
            }
        });


        holder.place.setText(share.getPlace());
        holder.personnel.setText(share.getPersonnelcapacity());
        //holder.personnel.setText(Integer.toString(share.getPersonnel_actual()) + "/" + Integer.toString(share.getPersonnel_capacity()));
        //holder.hashtags.setText(share.getHashtag());
        Glide.with(holder.image.getContext())
                .load(share.getImages())
                .into(holder.image);
        holder.title.setText(share.getTitle());
        holder.price.setText(priceFormat(share.getPrice()));
        holder.date.setText(calcDate(share.getDate()));

    }


    @Override
    public int getItemCount() {
        return sharelist.size();
    }

    public class sharelistViewHolder extends RecyclerView.ViewHolder{

        public TextView place, title, hashtags, price, personnel, date;
        public ImageView image;

        public sharelistViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            place = itemView.findViewById(R.id.tv_place);
            title = itemView.findViewById(R.id.tv_title);
            personnel = itemView.findViewById(R.id.tv_pepolenum);
            hashtags = itemView.findViewById(R.id.tv_hashtag);
            price = itemView.findViewById(R.id.tv_price);
            image = itemView.findViewById(R.id.imageView);
            date = itemView.findViewById(R.id.tv_date);
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

    public String priceFormat(String price){

        DecimalFormat df = new DecimalFormat("#,###");
        String res_price = "";
        res_price = df.format(Double.parseDouble(price.replaceAll(",","")));

        return res_price;
    }
}
