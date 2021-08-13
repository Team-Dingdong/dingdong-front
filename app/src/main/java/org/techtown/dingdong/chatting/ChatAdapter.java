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
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Chat> myChatList = null;

    public ChatAdapter(ArrayList<Chat> myChatList) {
        this.myChatList = myChatList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(viewType == ChatType.ViewType.LEFT_CONTENT){

            view = inflater.inflate(R.layout.item_chat_left, parent, false);
            return new LeftViewHolder(view);
        }
        else if(viewType == ChatType.ViewType.RIGHT_CONTENT){
            view = inflater.inflate(R.layout.item_chat_right, parent, false);
            return new RightViewHolder(view);

        }
        else if(viewType == ChatType.ViewType.CENTER_CONTENT){

            view = inflater.inflate(R.layout.item_chat_center, parent, false);
            return new CenterViewHolder(view);
        }
        else if(viewType == ChatType.ViewType.RIGHT_CONTENT_IMG){

            view = inflater.inflate(R.layout.item_chat_left_image, parent, false);
            return new LeftIMGViewHolder(view);

        }
        else{

            view = inflater.inflate(R.layout.item_chat_right_image, parent, false);
            return new RightIMGViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LeftViewHolder){
            ((LeftViewHolder)holder).tv_username.setText(myChatList.get(position).getName());
            ((LeftViewHolder)holder).tv_content.setText(myChatList.get(position).getContent());
            ((LeftViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());
            Glide.with(((LeftViewHolder) holder).profile.getContext())
                    .load(myChatList.get(position).getProfile())
                    .into(((LeftViewHolder) holder).profile);
            if(myChatList.get(position).getMaster() == Boolean.TRUE){
                ((LeftViewHolder)holder).master.setVisibility(View.VISIBLE);
            }

        }
        else if(holder instanceof RightViewHolder){
            ((RightViewHolder)holder).tv_content.setText(myChatList.get(position).getContent());
            ((RightViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());

        }
        else if(holder instanceof CenterViewHolder){
            ((CenterViewHolder)holder).tv_content.setText(myChatList.get(position).getContent());
            ((CenterViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());
        }
        else if(holder instanceof LeftIMGViewHolder){
            ((LeftIMGViewHolder)holder).tv_username.setText(myChatList.get(position).getName());
            Glide.with(((LeftIMGViewHolder) holder).img_content.getContext())
                    .load(myChatList.get(position).getContent())
                    .into(((LeftIMGViewHolder) holder).img_content);
            ((LeftIMGViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());
            Glide.with(((LeftIMGViewHolder) holder).profile.getContext())
                    .load(myChatList.get(position).getProfile())
                    .into(((LeftIMGViewHolder) holder).profile);
            if(myChatList.get(position).getMaster() == Boolean.TRUE){
                ((LeftIMGViewHolder)holder).master.setVisibility(View.VISIBLE);
            }
        }
        else{
            Glide.with(((RightIMGViewHolder) holder).img_content.getContext())
                    .load(myChatList.get(position).getContent())
                    .into(((RightIMGViewHolder) holder).img_content);
            ((RightIMGViewHolder)holder).tv_time.setText(myChatList.get(position).getTime());
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

    public class LeftViewHolder extends RecyclerView.ViewHolder{

        ImageView profile, master;
        TextView tv_username;
        TextView tv_content;
        TextView tv_time;

        public LeftViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.userimage);
            tv_username = itemView.findViewById(R.id.username);
            tv_content = itemView.findViewById(R.id.chat);
            tv_time = itemView.findViewById(R.id.time);
            master = itemView.findViewById(R.id.ismaster);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{

        TextView tv_content;
        TextView tv_time;
        public RightViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.chat);
            tv_time = itemView.findViewById(R.id.time);
        }
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder{

        TextView tv_content;
        TextView tv_time;
        public CenterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.chat);
            tv_time = itemView.findViewById(R.id.time);
        }
    }

    public class LeftIMGViewHolder extends RecyclerView.ViewHolder{

        ImageView profile, master;
        TextView tv_username;
        ImageView img_content;
        TextView tv_time;
        public LeftIMGViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.userimage);
            tv_username = itemView.findViewById(R.id.username);
            img_content = itemView.findViewById(R.id.chat_img);
            tv_time = itemView.findViewById(R.id.time);
            master = itemView.findViewById(R.id.ismaster);
        }
    }

    public class RightIMGViewHolder extends RecyclerView.ViewHolder{

        ImageView img_content;
        TextView tv_time;

        public RightIMGViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img_content = itemView.findViewById(R.id.chat_img);
            tv_time = itemView.findViewById(R.id.time);
        }
    }


}
