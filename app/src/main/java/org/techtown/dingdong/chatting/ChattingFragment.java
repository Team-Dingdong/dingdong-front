package org.techtown.dingdong.chatting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;

import java.util.ArrayList;

public class ChattingFragment extends Fragment {


    private ArrayList<Chat> chats;
    private ArrayList<ChatRoom> chatRooms;
    private RecyclerView recyclerView;
    ChatRoomListAdapter chatRoomListAdapter;

    public ChattingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_chatting, container, false);

        setDummy();

        recyclerView = v.findViewById(R.id.recycler_chathome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        chatRoomListAdapter = new ChatRoomListAdapter(getActivity(), chatRooms);
        recyclerView.setAdapter(chatRoomListAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                final ChatRoom chatRoom = chatRoomListAdapter.getChatRooms().get(position);

                //chatRoomListAdapter.removeItem(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                dialog.setMessage("채팅방에서 정말 나가시겠어요? \n무통보 퇴장은 신고 사유가 될 수 있습니다.")
                        .setTitle(chatRoom.getTitle())
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Dialog", "아니오");
                            }
                        })
                        .setNegativeButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Dialog", "네");
                                chatRoomListAdapter.removeItem(position);

                            }
                        }).show();

                chatRoomListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }

        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        return v;
    }

    public void setDummy(){
        chats = new ArrayList<>();
        chats.add(new Chat("안녕하세요 여러분","원선","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 1:30",Boolean.TRUE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("넵 안녕하세요","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 2:30",Boolean.FALSE,ChatType.ViewType.RIGHT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 3:30",Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 4:30",Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 5:30",Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT ));

        chatRooms = new ArrayList<>();
        chatRooms.add(new ChatRoom("https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",chats,"감자를 나누고 싶어요","3"));
        chatRooms.add(new ChatRoom("https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510_1280.jpg",chats,"양파를 나누고 싶어요","2"));
        chatRooms.add(new ChatRoom("https://cdn.pixabay.com/photo/2021/08/08/10/34/ocean-6530523__480.jpg",chats,"물을 나누고 싶어요","3"));
        chatRooms.add(new ChatRoom("https://cdn.pixabay.com/photo/2016/08/11/08/43/potatoes-1585060__480.jpg",chats,"뭐든 나누고 싶어요","2"));


    }
}
