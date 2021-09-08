package org.techtown.dingdong.chatting;

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
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.home.ShareDetailActivity;
import org.techtown.dingdong.home.ShareListAdpater;

import java.sql.Timestamp;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChattingActivity.class);
                intent.putExtra("id",chatRooms.get(position).getId());
                Log.d("id",chatRooms.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.title.setText(chatRoom.getTitle());
        holder.personnel.setText(chatRoom.getPersonnel());
        holder.latestmsg.setText(chatRoom.getLastMsg());
        holder.latesttime.setText(calcDate(chatRoom.getLastChatTime()));
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

    public String calcDate(String date){
        if(date != null){
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

        return result;}
        else return null;
    }
}
