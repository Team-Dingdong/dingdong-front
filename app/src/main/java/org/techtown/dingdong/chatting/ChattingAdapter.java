package org.techtown.dingdong.chatting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private String prev = "0";

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
        else if(viewType == ChatType.ViewType.RIGHT_CONTENT_IMG){

            view = inflater.inflate(R.layout.item_chat_right_image, parent, false);
            return new ChattingAdapter.RightIMGViewHolder(view);
        }
        else if(viewType == ChatType.ViewType.LEFT_CONTENT_PLAN){

            view = inflater.inflate(R.layout.item_chat_left_planning, parent, false);
            return new ChattingAdapter.LeftPLANViewHolder(view);

        }
        else{
            view = inflater.inflate(R.layout.item_chat_right_planning, parent, false);
            return new ChattingAdapter.RightPLANViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChattingAdapter.ChatViewHolder holder, int position) {


        String mytime = myChatList.get(position).getTime().substring(11,16);
        String mydate = myChatList.get(position).getTime().substring(0,10);//2021-08-23 17:00:33.822
        String cur = myChatList.get(position).getTime().substring(8,10);
        if(position != 0){
        prev = myChatList.get(position-1).getTime().substring(8,10);}
        else{
            prev = "0";
        }
        Log.d("비교",prev + cur);

        if(holder instanceof ChattingAdapter.LeftViewHolder){
            if(!cur.equals(prev)){
                //latestdate = myChatList.get(position).getTime().substring(8,10);
                ((ChattingAdapter.LeftViewHolder)holder).tv_date.setVisibility(View.VISIBLE);
                ((ChattingAdapter.LeftViewHolder)holder).tv_date.setText(mydate);
            }
            ((ChattingAdapter.LeftViewHolder)holder).tv_username.setText(myChatList.get(position).getName());
            ((ChattingAdapter.LeftViewHolder)holder).tv_content.setText(myChatList.get(position).getContent());
            ((ChattingAdapter.LeftViewHolder)holder).tv_time.setText(mytime);
            Glide.with(((ChattingAdapter.LeftViewHolder) holder).profile.getContext())
                    .load(myChatList.get(position).getProfile())
                    .into(((ChattingAdapter.LeftViewHolder) holder).profile);
            if(myChatList.get(position).getOwner().equals("TRUE")){
                ((ChattingAdapter.LeftViewHolder)holder).master.setVisibility(View.VISIBLE);
            }

        }
        else if(holder instanceof ChattingAdapter.RightViewHolder){
            if(!cur.equals(prev)){
                //latestdate = myChatList.get(position).getTime().substring(8,10);
                ((ChattingAdapter.RightViewHolder)holder).tv_date.setVisibility(View.VISIBLE);
                ((ChattingAdapter.RightViewHolder)holder).tv_date.setText(mydate);
            }
            ((ChattingAdapter.RightViewHolder)holder).tv_content.setText(myChatList.get(position).getContent());
            ((ChattingAdapter.RightViewHolder)holder).tv_time.setText(mytime);

        }
        else if(holder instanceof ChattingAdapter.CenterViewHolder){
            if(!cur.equals(prev)){
                //latestdate = myChatList.get(position).getTime().substring(8,10);
                ((ChattingAdapter.CenterViewHolder)holder).tv_date.setVisibility(View.VISIBLE);
                ((ChattingAdapter.CenterViewHolder)holder).tv_date.setText(mydate);
            }
            ((ChattingAdapter.CenterViewHolder)holder).tv_content.setText(myChatList.get(position).getContent());
            if(myChatList.get(position).getType().equals("ENTER")){
                ((ChattingAdapter.CenterViewHolder)holder).tv_time.setVisibility(View.GONE);
            }
            else{
            ((ChattingAdapter.CenterViewHolder)holder).tv_time.setText(mytime);}
        }
        else if(holder instanceof ChattingAdapter.LeftIMGViewHolder){
            if(!cur.equals(prev)){
                //latestdate = myChatList.get(position).getTime().substring(8,10);
                ((ChattingAdapter.LeftIMGViewHolder)holder).tv_date.setVisibility(View.VISIBLE);
                ((ChattingAdapter.LeftIMGViewHolder)holder).tv_date.setText(mydate);
            }
            ((ChattingAdapter.LeftIMGViewHolder)holder).tv_username.setText(myChatList.get(position).getName());
            Glide.with(((ChattingAdapter.LeftIMGViewHolder) holder).img_content.getContext())
                    .load(myChatList.get(position).getContent())
                    .into(((ChattingAdapter.LeftIMGViewHolder) holder).img_content);
            ((ChattingAdapter.LeftIMGViewHolder)holder).tv_time.setText(mytime);
            Glide.with(((ChattingAdapter.LeftIMGViewHolder) holder).profile.getContext())
                    .load(myChatList.get(position).getProfile())
                    .into(((ChattingAdapter.LeftIMGViewHolder) holder).profile);
            if(myChatList.get(position).getOwner().equals("TRUE")){
                ((ChattingAdapter.LeftIMGViewHolder)holder).master.setVisibility(View.VISIBLE);
            }
        }
        else if(holder instanceof ChattingAdapter.RightIMGViewHolder){
            if(!cur.equals(prev)){
                //latestdate = myChatList.get(position).getTime().substring(8,10);
                ((ChattingAdapter.RightIMGViewHolder)holder).tv_date.setVisibility(View.VISIBLE);
                ((ChattingAdapter.RightIMGViewHolder)holder).tv_date.setText(mydate);
            }
            Glide.with(((ChattingAdapter.RightIMGViewHolder) holder).img_content.getContext())
                    .load(myChatList.get(position).getContent())
                    .into(((ChattingAdapter.RightIMGViewHolder) holder).img_content);
            ((ChattingAdapter.RightIMGViewHolder)holder).tv_time.setText(mytime);
        }
        else if(holder instanceof ChattingAdapter.LeftPLANViewHolder){
            if(!cur.equals(prev)){
                //latestdate = myChatList.get(position).getTime().substring(8,10);
                ((ChattingAdapter.LeftPLANViewHolder)holder).tv_date.setVisibility(View.VISIBLE);
                ((ChattingAdapter.LeftPLANViewHolder)holder).tv_date.setText(mydate);
            }

            ((ChattingAdapter.LeftPLANViewHolder)holder).tv_username.setText(myChatList.get(position).getName());
            ((ChattingAdapter.LeftPLANViewHolder)holder).tv_time.setText(mytime);
            Glide.with(((ChattingAdapter.LeftPLANViewHolder) holder).profile.getContext())
                    .load(myChatList.get(position).getProfile())
                    .into(((ChattingAdapter.LeftPLANViewHolder) holder).profile);
            ((ChattingAdapter.LeftPLANViewHolder)holder).tv_info.setText(myChatList.get(position).getContent());
            if(myChatList.get(position).getOwner().equals("TRUE")){
                ((ChattingAdapter.LeftPLANViewHolder)holder).master.setVisibility(View.VISIBLE);
        } }
        else{
            if(!cur.equals(prev)){
                //latestdate = myChatList.get(position).getTime().substring(8,10);
                ((ChattingAdapter.RightPLANViewHolder)holder).tv_date.setVisibility(View.VISIBLE);
                ((ChattingAdapter.RightPLANViewHolder)holder).tv_date.setText(mydate);
            }
            ((ChattingAdapter.RightPLANViewHolder)holder).tv_time.setText(mytime);
            ((ChattingAdapter.RightPLANViewHolder)holder).tv_info.setText(myChatList.get(position).getContent());

        }

    }

    @Override
    public int getItemCount() {
        return myChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(myChatList.get(position).getType().equals("ENTER")){
            return ChatType.ViewType.CENTER_CONTENT;
        }
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
        TextView tv_date;
        public RightIMGViewHolder(View view) {
            super(view);
            img_content = view.findViewById(R.id.chat_img);
            tv_time = view.findViewById(R.id.time);
            tv_date = view.findViewById(R.id.tv_date);
        }
    }

    public class LeftIMGViewHolder extends ChatViewHolder {
        ImageView profile, master;
        TextView tv_username;
        ImageView img_content;
        TextView tv_time;
        TextView tv_date;
        public LeftIMGViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.userimage);
            tv_username = view.findViewById(R.id.username);
            img_content = view.findViewById(R.id.chat_img);
            tv_time = view.findViewById(R.id.time);
            master = view.findViewById(R.id.ismaster);
            tv_date = view.findViewById(R.id.tv_date);
        }
    }

    public class LeftViewHolder extends ChatViewHolder {
        ImageView profile, master;
        TextView tv_username;
        TextView tv_content;
        TextView tv_time;
        TextView tv_date;
        public LeftViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.userimage);
            tv_username = view.findViewById(R.id.username);
            tv_content = view.findViewById(R.id.chat);
            tv_time = view.findViewById(R.id.time);
            master = view.findViewById(R.id.ismaster);
            tv_date = view.findViewById(R.id.tv_date);
        }
    }

    public class RightViewHolder extends ChatViewHolder {
        TextView tv_content;
        TextView tv_time;
        TextView tv_date;
        public RightViewHolder(View view) {
            super(view);
            tv_content = view.findViewById(R.id.chat);
            tv_time = view.findViewById(R.id.time);
            tv_date = view.findViewById(R.id.tv_date);
        }
    }

    public class CenterViewHolder extends ChatViewHolder {
        TextView tv_content;
        TextView tv_time;
        TextView tv_date;
        public CenterViewHolder(View view) {
            super(view);
            tv_content = view.findViewById(R.id.chat);
            tv_time = view.findViewById(R.id.time);
            tv_date = view.findViewById(R.id.tv_date);
        }
    }

    public class LeftPLANViewHolder extends ChatViewHolder{

        ImageView profile, master;
        TextView tv_username;
        TextView tv_time;
        TextView tv_date;
        TextView tv_info, tv_status, tv_personnel;
        Button btn_ok;

        public LeftPLANViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.userimage);
            tv_username = view.findViewById(R.id.username);
            tv_time = view.findViewById(R.id.time);
            master = view.findViewById(R.id.ismaster);
            tv_date = view.findViewById(R.id.tv_date);
            btn_ok = view.findViewById(R.id.btn_ok);
            tv_info = view.findViewById(R.id.tv_info);
            tv_personnel = view.findViewById(R.id.tv_personnel);
        }
    }

    public class RightPLANViewHolder extends ChatViewHolder{

        TextView tv_time;
        TextView tv_date;
        TextView tv_info, tv_status, tv_personnel;
        Button btn_ok;

        public RightPLANViewHolder(View view) {
            super(view);
            tv_time = view.findViewById(R.id.time);
            tv_date = view.findViewById(R.id.tv_date);
            btn_ok = view.findViewById(R.id.btn_ok);
            tv_info = view.findViewById(R.id.tv_info);
            tv_personnel = view.findViewById(R.id.tv_personnel);
        }
    }


    public void addItem(Chat chat){
        myChatList.add(chat);
        notifyDataSetChanged();
    }
}
