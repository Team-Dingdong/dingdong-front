package org.techtown.dingdong.chatting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.HomeFragment;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingFragment extends Fragment {


    //private ArrayList<Chat> chats;
    private ArrayList<ChatRoom> chatRooms = new ArrayList<>();
    private RecyclerView recyclerView;
    ChatRoomListAdapter chatRoomListAdapter;
    Boolean ismaster = true;
    Token token;

    public ChattingFragment() {
        // Required empty public constructor
    }

    public static ChattingFragment newInstance() {
        ChattingFragment fragment = new ChattingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_chatting, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access,refresh,expire,tokentype);
        token.setContext(getActivity());

        Log.d("토큰", String.valueOf(access));



        recyclerView = v.findViewById(R.id.recycler_chathome);

        setChatRooms(token);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                final ChatRoom chatRoom = chatRoomListAdapter.getChatRooms().get(position);

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

                                    String roomid = chatRoomListAdapter.getChatRooms().get(position).getId();
                                    Apiinterface apiinterface = Api.createService(Apiinterface.class, token, getActivity());
                                    Call<ResponseBody> call = apiinterface.exitChatRoom(Integer.parseInt(roomid));
                                    call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                if (response.code() == 200) {
                                                    Log.d("채팅방 나가기 성공", new Gson().toJson(response.code()));
                                                    chatRoomListAdapter.removeItem(position);
                                                    Toast.makeText(getActivity(),"퇴장되었습니다.",Toast.LENGTH_LONG).show();
                                                }

                                            }else if(response.code() == 403){
                                                    final Snackbar snackbar = Snackbar.make(v,"방장은 거래를 나갈 수 없습니다.\n" + "채팅방-더보기-나눔파기를 통해 나눔을 파기해주세요.", Snackbar.LENGTH_INDEFINITE);
                                                    snackbar.setAction("확인", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            snackbar.dismiss();
                                                        }
                                                    });
                                                    TextView tvs = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                                                    tvs.setTextSize(13);
                                                    snackbar.show();

                                            }else if(response.code() == 400){
                                                Toast.makeText(getActivity(),"해당 거래 약속 때문에 퇴장할 수 없습니다.",Toast.LENGTH_LONG).show();
                                            }else if(response.code() == 404){
                                                Toast.makeText(getActivity(),"해당 채팅방을 찾을 수 없습니다.",Toast.LENGTH_LONG).show();
                                            }else{
                                                Log.d("chatF,exit", new Gson().toJson(response.errorBody()));
                                                Log.d("chatF,exit", response.toString());
                                                Log.d("chatF,exit", String.valueOf(response.code()));
                                                Log.d("chatF,exit", response.message());
                                                Log.d("chatF,exit", String.valueOf(response.raw().request().url().url()));
                                                Log.d("chatF,exit", new Gson().toJson(response.raw().request()));

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Log.d("chatF,exit", String.valueOf(t));
                                        }
                                    });


                            }
                        }).show();

                chatRoomListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }

        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setChatRooms(token);
    }


    public void setChatRecycler(RecyclerView recyclerView, ArrayList<ChatRoom> chatRooms){

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        chatRoomListAdapter = new ChatRoomListAdapter(getActivity(), chatRooms);
        recyclerView.setAdapter(chatRoomListAdapter);

    }


    public void setChatRooms(Token token){

        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, getActivity());
        Call<ChatRoomResponse> call = apiinterface.getChatRoomList();

        call.enqueue(new Callback<ChatRoomResponse>() {
            @Override
            public void onResponse(Call<ChatRoomResponse> call, Response<ChatRoomResponse> response) {

                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("CHAT_ROOM_READ_ALL_SUCCESS")){

                        ChatRoomResponse res = response.body();

                        //Log.d("성공", new Gson().toJson(res));

                        chatRooms = res.getChatRooms();
                        setChatRecycler(recyclerView, chatRooms);

                    }
                }
                else{

                    Log.d("chatF,getChatRL", new Gson().toJson(response.errorBody()));
                    Log.d("chatF,getChatRL", response.toString());
                    Log.d("chatF,getChatRL", String.valueOf(response.code()));
                    Log.d("chatF,getChatRL", response.message());
                    Log.d("chatF,getChatRL", String.valueOf(response.raw().request().url().url()));
                    Log.d("chatF,getChatRL", new Gson().toJson(response.raw().request()));

                }

            }

            @Override
            public void onFailure(Call<ChatRoomResponse> call, Throwable t) {

                Log.d("chatF,getChatRL", String.valueOf(t));

            }
        });


    }
}
