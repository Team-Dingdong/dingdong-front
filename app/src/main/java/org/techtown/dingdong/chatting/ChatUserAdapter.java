package org.techtown.dingdong.chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;

import java.util.ArrayList;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {
    private ArrayList<ChatUser> chatUsers;
    Context context;
    ChatUser chatuser;

    public ArrayList<ChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(ArrayList<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public ChatUserAdapter(ArrayList<ChatUser> chatUsers, Context context) {
        this.chatUsers = chatUsers;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatUserAdapter.ViewHolder holder, int position) {
        chatuser = chatUsers.get(position);

        if(chatuser.getIsmaster().equals("true")){
            holder.ismaster.setVisibility(View.VISIBLE);
        }

        Glide.with(holder.img_profile.getContext())
                .load(chatuser.getImgUrl())
                .into(holder.img_profile);
        holder.tv_username.setText(chatuser.getUsername());


    }

    public void removeItem(int position) {
        chatUsers.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ChatUser item, int position) {
        chatUsers.add(position,item);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return chatUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile, ismaster;
        TextView tv_username;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ismaster = itemView.findViewById(R.id.ismaster);
            img_profile = itemView.findViewById(R.id.img_profile);
            tv_username = itemView.findViewById(R.id.tv_user);
        }
    }
}
