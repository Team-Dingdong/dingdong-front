package org.techtown.dingdong.mypage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;
import org.techtown.dingdong.chatting.ChatUser;
import org.techtown.dingdong.chatting.ChatUserAdapter;

import java.util.ArrayList;

public class UserRatingAdapter extends RecyclerView.Adapter<UserRatingAdapter.ViewHolder> {
    private ArrayList<ChatUser> chatUsers;
    ChatUser chatUser;
    Context context;

    public UserRatingAdapter(ArrayList<ChatUser> chatUsers, Context context) {
        this.chatUsers = chatUsers;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_userrating, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserRatingAdapter.ViewHolder holder, int position) {
        chatUser = chatUsers.get(position);

        Glide.with(holder.img_profile.getContext())
                .load(chatUser.getImgUrl())
                .into(holder.img_profile);
        if(chatUser.getUsername().equals("leave")){
            holder.tv_username.setText("탈퇴한 회원");
        }else{
            holder.tv_username.setText(chatUser.getUsername());
        }
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.btn_good){
                    chatUsers.get(position).setRating("GOOD");
                    Toast.makeText(context,chatUsers.get(position).getRating(),Toast.LENGTH_SHORT);

                }
                else if(checkedId==R.id.btn_bad){
                    chatUsers.get(position).setRating("BAD");
                    Toast.makeText(context,chatUsers.get(position).getRating(),Toast.LENGTH_SHORT);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return chatUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile;
        TextView tv_username;
        RadioGroup radioGroup;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img_profile = itemView.findViewById(R.id.img_profile);
            tv_username = itemView.findViewById(R.id.tv_user);
            radioGroup = itemView.findViewById(R.id.radiogrp);
        }
    }
}
