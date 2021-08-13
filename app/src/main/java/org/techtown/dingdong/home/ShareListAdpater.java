package org.techtown.dingdong.home;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

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

        holder.place.setText(share.getPlace());
        holder.personnel.setText(Integer.toString(share.getPersonnel_actual()) + "/" + Integer.toString(share.getPersonnel_capacity()));
        holder.hashtags.setText(share.getHashtag());
        Glide.with(holder.image.getContext())
                .load(share.getImages()[0])
                .into(holder.image);
        holder.title.setText(share.getTitle());
        holder.price.setText(share.getPrice());
        holder.date.setText(share.getDate());

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(v.getContext(), ShareDetailActivity.class));

                }
            });

            place = itemView.findViewById(R.id.tv_place);
            title = itemView.findViewById(R.id.tv_title);
            personnel = itemView.findViewById(R.id.tv_pepolenum);
            hashtags = itemView.findViewById(R.id.tv_hashtag);
            price = itemView.findViewById(R.id.tv_price);
            image = itemView.findViewById(R.id.imageView);
            date = itemView.findViewById(R.id.tv_date);
        }
    }
}
