package org.techtown.dingdong.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private ArrayList<ChatUser> chatUsers;
    private RecyclerView recyclerView;
    ChatUserAdapter chatUserAdapter;
    private TextView tv_title, tv_people;
    private String id = "1";
    Boolean ismaster = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        Token token  = new Token(access,refresh,expire,tokentype);
        token.setContext(UserListActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        recyclerView = findViewById(R.id.recycler_user);
        tv_people = findViewById(R.id.tv_people);
        tv_title = findViewById(R.id.tv_title);

        initChatRoom(token);

        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, UserListActivity.this);
        Call<ChatUserResponse> call = apiinterface.getChatUser(Integer.parseInt(id));
        call.enqueue(new Callback<ChatUserResponse>() {
            @Override
            public void onResponse(Call<ChatUserResponse> call, Response<ChatUserResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().equals("CHAT_ROOM_USER_READ_SUCCESS")) {
                        ChatUserResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        chatUsers = res.getChatUsers();

                        recyclerView.setLayoutManager(new LinearLayoutManager(UserListActivity.this, LinearLayoutManager.VERTICAL, false));
                        chatUserAdapter = new ChatUserAdapter(chatUsers, UserListActivity.this);
                        recyclerView.setAdapter(chatUserAdapter);
                    }
                }else{
                    Log.d("실패", new Gson().toJson(response.errorBody()));
                    Log.d("실패", response.toString());
                    Log.d("실패", String.valueOf(response.code()));
                    Log.d("실패", response.message());
                    Log.d("실패", String.valueOf(response.raw().request().url().url()));
                    Log.d("실패", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<ChatUserResponse> call, Throwable t) {

            }
        });


        btn_back = findViewById(R.id.ic_back);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final ChatUser chatUser = chatUserAdapter.getChatUsers().get(position);

                if(ismaster){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UserListActivity.this);

                    dialog.setMessage("을 퇴장시키겠어요?")
                            .setTitle(chatUser.getUsername()+"님")
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
                                    if(chatUser.getIsmaster() == "fasle"){
                                    chatUserAdapter.removeItem(position);
                                    Toast.makeText(UserListActivity.this,"퇴장되었습니다.",Toast.LENGTH_LONG).show();}
                                    else{

                                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.view),"방장은 거래를 나갈 수 없습니다.\n" + "채팅방-더보기-나눔파기를 통해 나눔을 파기해주세요.", Snackbar.LENGTH_INDEFINITE);
                                        snackbar.setAction("확인", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                snackbar.dismiss();
                                            }
                                        });

                                        TextView tvs = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);

                                        tvs.setTextSize(13);

                                        snackbar.show();
                                        }
                                    }
                            })
                            .show();

                }
                else{
                    //Toast.makeText(UserListActivity.this,"퇴장은 방장이 요청할 수 있습니다.",Toast.LENGTH_LONG).show();
                }

                chatUserAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserListActivity.this, ChattingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

    public void initChatRoom(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, UserListActivity.this);
        Call<ChatRoomInformResponse> call = apiinterface.getChatRoom(Integer.parseInt(id));
        call.enqueue(new Callback<ChatRoomInformResponse>() {
            @Override
            public void onResponse(Call<ChatRoomInformResponse> call, Response<ChatRoomInformResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("CHAT_ROOM_READ_SUCCESS")) {
                        ChatRoomInformResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        ChatRoom chatRoom = res.getChatRoom();
                        tv_title.setText(chatRoom.getTitle());
                        tv_people.setText(chatRoom.getPersonnel());
                    }
                }else{
                    Log.d("실패", new Gson().toJson(response.errorBody()));
                    Log.d("실패", response.toString());
                    Log.d("실패", String.valueOf(response.code()));
                    Log.d("실패", response.message());
                    Log.d("실패", String.valueOf(response.raw().request().url().url()));
                    Log.d("실패", new Gson().toJson(response.raw().request()));

                }

            }

            @Override
            public void onFailure(Call<ChatRoomInformResponse> call, Throwable t) {

                Log.d("외않되", String.valueOf(t));

            }
        });

    }

}