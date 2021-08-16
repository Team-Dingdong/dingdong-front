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

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ChatViewHolder> {
    private ArrayList<Chat> myChatList;

    public ChattingAdapter(ArrayList<Chat> myChatList) {
        this.myChatList = myChatList;
    }


    @NonNull
    @NotNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(viewType == ChatType.ViewType.LEFT_CONTENT){

            view = inflater.inflate(R.layout.item_chat_left, parent, false);
            return new ChattingAdapter.LeftViewHolder(view);
        }
        else if(viewType == ChatType.ViewType.RIGHT_CONTENT){
            view = inflater.inflate(R.layout.item_chat_right, parent, false);
            return new ChattingAdapter.RightViewHolder(view);

        }
        else if(viewType == ChatType.ViewType.CENTER_CONTENT){

            view = inflater.inflate(R.layout.item_chat_center, parent, false);
            return new ChattingAdapter.CenterViewHolder(view);
        }
        else if(viewType == ChatType.ViewType.LEFT_CONTENT_IMG){

            view = inflater.inflate(R.layout.item_chat_left_image, parent, false);
            return new ChattingAdapter.LeftIMGViewHolder(view);

        }
        else{

            view = inflater.inflate(R.layout.item_chat_right_image, parent, false);
            return new ChattingAdapter.RightIMGViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChattingAdapter.ChatViewHolder holder, int position) {

        if(holder instanceof ChattingAdapter.LeftViewHolder){
            ((ChattingAdapter.LeftViewHolder)holder).tv_username.setText(myChatList.get(position).getName());
            ((ChattingAdapter.LeftViewHolder)holder).tv_content.setText(myChatList.get(position).getContent());
            ((ChattingAdapter.LeftViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());
            Glide.with(((ChattingAdapter.LeftViewHolder) holder).profile.getContext())
                    .load(myChatList.get(position).getProfile())
                    .into(((ChattingAdapter.LeftViewHolder) holder).profile);
            if(myChatList.get(position).getMaster() == Boolean.TRUE){
                ((ChattingAdapter.LeftViewHolder)holder).master.setVisibility(View.VISIBLE);
            }

        }
        else if(holder instanceof ChattingAdapter.RightViewHolder){
            ((ChattingAdapter.RightViewHolder)holder).tv_content.setText(myChatList.get(position).getContent());
            ((ChattingAdapter.RightViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());

        }
        else if(holder instanceof ChattingAdapter.CenterViewHolder){
            ((ChattingAdapter.CenterViewHolder)holder).tv_content.setText(myChatList.get(position).getContent());
            ((ChattingAdapter.CenterViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());
        }
        else if(holder instanceof ChattingAdapter.LeftIMGViewHolder){
            ((ChattingAdapter.LeftIMGViewHolder)holder).tv_username.setText(myChatList.get(position).getName());
            Glide.with(((ChattingAdapter.LeftIMGViewHolder) holder).img_content.getContext())
                    .load(myChatList.get(position).getContent())
                    .into(((ChattingAdapter.LeftIMGViewHolder) holder).img_content);
            ((ChattingAdapter.LeftIMGViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());
            Glide.with(((ChattingAdapter.LeftIMGViewHolder) holder).profile.getContext())
                    .load(myChatList.get(position).getProfile())
                    .into(((ChattingAdapter.LeftIMGViewHolder) holder).profile);
            if(myChatList.get(position).getMaster() == Boolean.TRUE){
                ((ChattingAdapter.LeftIMGViewHolder)holder).master.setVisibility(View.VISIBLE);
            }
        }
        else{
            Glide.with(((ChattingAdapter.RightIMGViewHolder) holder).img_content.getContext())
                    .load(myChatList.get(position).getContent())
                    .into(((ChattingAdapter.RightIMGViewHolder) holder).img_content);
            ((ChattingAdapter.RightIMGViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());
        }

    }

    @Override
    public int getItemCount() {
        return myChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return myChatList.get(position).getViewType();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public ChatViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    public class RightIMGViewHolder extends ChatViewHolder {
        ImageView img_content;
        TextView tv_time;
        public RightIMGViewHolder(View view) {
            super(view);
            img_content = view.findViewById(R.id.chat_img);
            tv_time = view.findViewById(R.id.time);
        }
    }

    public class LeftIMGViewHolder extends ChatViewHolder {
        ImageView profile, master;
        TextView tv_username;
        ImageView img_content;
        TextView tv_time;
        public LeftIMGViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.userimage);
            tv_username = view.findViewById(R.id.username);
            img_content = view.findViewById(R.id.chat_img);
            tv_time = view.findViewById(R.id.time);
            master = view.findViewById(R.id.ismaster);
        }
    }

    public class LeftViewHolder extends ChatViewHolder {
        ImageView profile, master;
        TextView tv_username;
        TextView tv_content;
        TextView tv_time;
        public LeftViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.userimage);
            tv_username = view.findViewById(R.id.username);
            tv_content = view.findViewById(R.id.chat);
            tv_time = view.findViewById(R.id.time);
            master = view.findViewById(R.id.ismaster);
        }
    }

    public class RightViewHolder extends ChatViewHolder {
        TextView tv_content;
        TextView tv_time;
        public RightViewHolder(View view) {
            super(view);
            tv_content = view.findViewById(R.id.chat);
            tv_time = view.findViewById(R.id.time);
        }
    }

    public class CenterViewHolder extends ChatViewHolder {
        TextView tv_content;
        TextView tv_time;
        public CenterViewHolder(View view) {
            super(view);
            tv_content = view.findViewById(R.id.chat);
            tv_time = view.findViewById(R.id.time);
        }
    }


    public void addItem(Chat chat){
        myChatList.add(chat);
        notifyDataSetChanged();
    }
}
