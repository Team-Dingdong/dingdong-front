package org.techtown.dingdong.chatting;

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
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.home.ShareDetailActivity;
import org.techtown.dingdong.home.ShareListAdpater;

import java.util.ArrayList;

public class ChatRoomListAdapter extends RecyclerView.Adapter<ChatRoomListAdapter.chatRoomListViewHolder> {

    private final ArrayList<ChatRoom> chatRooms;
    private Context context;
    ChatRoom chatRoom;

    public ChatRoomListAdapter(Context context, ArrayList<ChatRoom> chatRooms) {
        this.context = context;
        this.chatRooms = chatRooms;
    }

    public ArrayList<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    @NonNull
    @NotNull
    @Override
    public chatRoomListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chatroom, parent, false);
        chatRoomListViewHolder holder = new chatRoomListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatRoomListAdapter.chatRoomListViewHolder holder, int position) {

        chatRoom = chatRooms.get(position);
        ArrayList<Chat> chats = chatRoom.getChats();

        holder.title.setText(chatRoom.getTitle());
        holder.personnel.setText(chatRoom.getPersonnel());
        holder.latestmsg.setText(chats.get(chats.size()-1).getContent());
        holder.latesttime.setText(chats.get(chats.size()-1).getTime());
        Glide.with(holder.img.getContext())
                .load(chatRoom.getImage())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public void removeItem(int position) {
        chatRooms.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ChatRoom item, int position) {
        chatRooms.add(position,item);
        notifyItemInserted(position);
    }

    public class chatRoomListViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, latestmsg, latesttime, personnel;
        public chatRoomListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(v.getContext(), ChattingActivity.class));
                }
            });

            img = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
            latesttime = itemView.findViewById(R.id.tv_date);
            latestmsg = itemView.findViewById(R.id.tv_msg);
            personnel = itemView.findViewById(R.id.tv_personnel);

        }
    }
}
