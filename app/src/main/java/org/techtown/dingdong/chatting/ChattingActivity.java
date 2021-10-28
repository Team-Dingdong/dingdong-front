package org.techtown.dingdong.chatting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.MainActivity;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.EditActivity;
import org.techtown.dingdong.home.HomeFragment;
import org.techtown.dingdong.home.ShareDetailActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;
import org.techtown.dingdong.profile.UserProfileResponse;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class ChattingActivity extends AppCompatActivity implements ChattingBottomDialogFragment.onInteractionListener, ChattingAdapter.onListItemSelectedInterface{
    private ArrayList<Chat> chats = new ArrayList<>();
    private RecyclerView recycler_chat;
    private LinearLayout sec_info;
    private TextView tv_people, tv_title, tv_info;
    private ImageView img_people, btn_alarm;
    private ImageButton btn_plus, btn_send, btn_back;
    private EditText et_message;
    private LinearLayout view_plus;
    private final int OPEN_GALLERY = 201;
    ChattingAdapter chatAdapter;
    Uri imageUri;
    private String message="hi", username="다루", userprofile="", isowner="false";
    private String id = "1";
    private Boolean ismaster = true, isvisible = false;
    ChattingBottomDialogFragment chattingBottomDialogFragment;
    private Gson gson;
    public static String WS_URL = "ws://3.38.61.13:8080/ws-stomp/websocket";
    private Token token;
    private StompManager stompManager;
    StompClient stompClient;
    CompositeDisposable compositeDisposable;
    private static final Pattern PATTERN_HEADER = Pattern.compile("([^:\\n\\r]+)\\s*:\\s*([^:\\n\\r]+)");
    Boolean isUnexpectedClosed;
    final int PERMISSIONS_REQUEST = 1005;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token  = new Token(access,refresh,expire,tokentype);
        token.setContext(ChattingActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        getUser(token);


        List<StompHeader> header = new ArrayList<>();
        header.add(new StompHeader("Authorization","Bearer " + token.getAccessToken()));

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://3.38.61.13:8080/ws-stomp/websocket");
        stompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);


       resetSubscriptions();


        Disposable disposable = stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                            Log.d("lifecycle",new Gson().toJson(lifecycleEvent.getHandshakeResponseHeaders()));
                            Log.d("lifecycle",new Gson().toJson(lifecycleEvent.getType()));
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d("lifecycle","###########online");
                            break;
                        case ERROR:
                            Log.d("lifecycle", String.valueOf(lifecycleEvent.getException()));
                            Log.e("connecterror", "Stomp connection error", lifecycleEvent.getException());
                            if(lifecycleEvent.getException().getMessage().contains("EOF")){
                                isUnexpectedClosed=true;
                            }
                            break;
                        case CLOSED:
                            Log.d("lifecycle","###########offline");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.d("lifecycle","failedserverheartbeat");
                            break;
                    }

                },
                        throwable -> {
                            Log.d("stpmsgthrowinlifecycle",new Gson().toJson(throwable));

                        });


        gson = new GsonBuilder().create();

        Disposable dispTopic = stompClient.topic("/topic/chat/room/" + id, header)
                .doOnError(throwable -> {
                    Log.e("distop", "Error on subscribe topic", throwable);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage ->{

            JSONObject jsonObject = new JSONObject(topicMessage.getPayload());

            String msg = jsonObject.getString("message");
            String type = jsonObject.getString("type");
            String sender = jsonObject.getString("sender");
            String profileUrl = jsonObject.getString("profileImageUrl");
            //Log.d("onnext",jsonObject.toString());


            if(type.equals("TALK") && !sender.equals(username)){
                Chat chat = new Chat(msg,sender,profileUrl,new Timestamp(System.currentTimeMillis()).toString(), "FALSE", ChatType.ViewType.LEFT_CONTENT);
                addItem(chat);
                //Log.d("talk","get");
                }
            else if(type.equals("ENTER")){
                Chat chat = new Chat(msg,sender,profileUrl,new Timestamp(System.currentTimeMillis()).toString(), "FALSE", ChatType.ViewType.CENTER_CONTENT);
                addItem(chat);
                initChatRoom(token);
            }
            else if(type.equals("PROMISE")){
                Chat chat = new Chat(msg, sender,profileUrl,new Timestamp(System.currentTimeMillis()).toString(), "FALSE", ChatType.ViewType.LEFT_CONTENT_PLAN);
                addItem(chat);
            }
            else if(type.equals("PROMISE_CONFIRMED")) {
                Chat chat = new Chat(msg, sender, profileUrl, new Timestamp(System.currentTimeMillis()).toString(), "FALSE", ChatType.ViewType.LEFT_CONTENT);
                addItem(chat);
                getInfo(token);
            }
            else if(type.equals("QUIT")){
                Chat chat = new Chat(msg, sender,profileUrl,new Timestamp(System.currentTimeMillis()).toString(), "FALSE", ChatType.ViewType.CENTER_CONTENT);
                addItem(chat);
                initChatRoom(token);
            }

            }, throwable -> { Log.e("chat", "Error on subscribe topic", throwable); }
        );




        compositeDisposable.add(disposable);
        compositeDisposable.add(dispTopic);

        stompClient.connect(header);
        recycler_chat = findViewById(R.id.chatting_recycler);
        btn_plus = findViewById(R.id.btn_plus);
        btn_send = findViewById(R.id.btn_send);
        et_message = findViewById(R.id.et_chat);
        btn_back = findViewById(R.id.btn_back);
        tv_people = findViewById(R.id.tv_people);
        img_people = findViewById(R.id.btn_people);
        tv_title = findViewById(R.id.tv_title);
        tv_info = findViewById(R.id.tv_info);
        sec_info = findViewById(R.id.sec_info);
        btn_alarm = findViewById(R.id.btn_alarm);

        chatAdapter = new ChattingAdapter(chats, username, this);
        chatAdapter.setHasStableIds(true);
        recycler_chat.setAdapter(chatAdapter);
        recycler_chat.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recycler_chat.scrollToPosition(chatAdapter.getItemCount()-1);

        initChatRoom(token);
        setChats(token);
        getInfo(token);

        img_people.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick (View v){
                            Intent intent = new Intent(ChattingActivity.this, UserListActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            stompClient.disconnect();

                    }
                    });



        tv_people.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick (View v){
                        Intent intent = new Intent(ChattingActivity.this, UserListActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        stompClient.disconnect();


                    }
                    });


        chattingBottomDialogFragment =new ChattingBottomDialogFragment(getApplicationContext(),ismaster);

        btn_plus.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick (View v){
                        chattingBottomDialogFragment.show(getSupportFragmentManager(), chattingBottomDialogFragment.getTag());

                    }
                    });

        btn_back.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick (View v){
                        finish();
                    }
                    });


        btn_send.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick (View v){
                        message = et_message.getText().toString();
                        et_message.setText("");
                            try {
                                sendStomp(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isvisible == true){
                    //텍스트뷰가 보이는 상태일때
                    tv_info.setVisibility(View.GONE);
                    sec_info.setBackgroundColor(Color.parseColor("#00000000"));
                    isvisible = false;
                }
                else{
                    tv_info.setVisibility(View.VISIBLE);
                    sec_info.setBackgroundColor(Color.parseColor("#B2FFE2"));
                    isvisible = true;

                }
            }
        });


    }

    private void addItem(Chat chat){
        chats.add(chat);
        chatAdapter.notifyDataSetChanged();
        recycler_chat.smoothScrollToPosition(chatAdapter.getItemCount()-1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && data != null && data.getData() != null){
            switch(requestCode) {
                case OPEN_GALLERY:
                    Log.e("single choice", String.valueOf(data.getData()));
                    imageUri = data.getData();
                    Chat chat = new Chat(imageUri.toString(),username,userprofile,new Timestamp(System.currentTimeMillis()).toString(), isowner.toUpperCase(), ChatType.ViewType.RIGHT_CONTENT_IMG);
                    chatAdapter.addItem(chat);
                    recycler_chat.scrollToPosition(chatAdapter.getItemCount()-1);

        }
    } }


    @Override
    public void onButtonChoice(int choice) {
        switch (choice){
            case 1:
                int permisson = ContextCompat.checkSelfPermission(ChattingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permisson == PackageManager.PERMISSION_DENIED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(ChattingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    }else{
                        ActivityCompat.requestPermissions(ChattingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
                    }

                }else{
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, OPEN_GALLERY);
                chattingBottomDialogFragment.dismiss();
                }
                break;
            case 2:
                Intent intent2 = new Intent(ChattingActivity.this,PlanningActivity.class);
                intent2.putExtra("id",id);
                startActivity(intent2);
                chattingBottomDialogFragment.dismiss();
                break;
            case 3:
                Apiinterface apiinterface = Api.createService(Apiinterface.class,token, ChattingActivity.this);

                Call<ResponseBody> call = apiinterface.deleteShare(Integer.parseInt(id));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {

                            if (response.code() == 200) {
                                Log.d("성공", new Gson().toJson(response.code()));

                                Toast.makeText(ChattingActivity.this, "삭제가 완료되었습니다.", Toast.LENGTH_LONG).show();

                                //핸들러를 통한 액티비티 종료 시점 조절
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);
                            }
                        }else if(response.code() == 404){
                            Toast.makeText(ChattingActivity.this, "해당 포스트를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                            //Log.d("sharedetail,del", "해당 포스트를 찾을 수 없습니다.");
                        }else if(response.code() == 400){
                            Toast.makeText(ChattingActivity.this, "해당 권한이 없습니다.", Toast.LENGTH_LONG).show();
                            //Log.d("sharedetail,del", "해당 권한이 없습니다.");
                        }
                        else {
                            Log.d("sharedetail,del", new Gson().toJson(response.errorBody()));
                            Log.d("sharedetail,del", response.toString());
                            Log.d("sharedetail,del", String.valueOf(response.code()));
                            Log.d("sharedetail,del", response.message());
                            Log.d("sharedetail,del", String.valueOf(response.raw().request().url().url()));
                            Log.d("sharedetail,del", new Gson().toJson(response.raw().request()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Log.d("sharedetail,del", String.valueOf(t));

                    }
                });
                chattingBottomDialogFragment.dismiss();
                break;
        }

    }


    private void setChatRecycler(RecyclerView recyclerView, ArrayList<Chat> chats){
        chatAdapter = new ChattingAdapter(chats, username, this);
        chatAdapter.setHasStableIds(false);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.scrollToPosition(chatAdapter.getItemCount()-1);
    }

    private void setChats(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ChattingActivity.this);
        Call<ChatResponse> call = apiinterface.getChats(Integer.parseInt(id));

        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getResult().equals("CHAT_MESSAGE_READ_SUCCESS")) {

                        ChatResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        chats.addAll(res.getChats());
                        setChatRecycler(recycler_chat, chats);

                    }
                }else if(response.code() == 404){
                    Toast.makeText(ChattingActivity.this, "조회할 수 없는 채팅방입니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Log.d("chatting,getchats", new Gson().toJson(response.errorBody()));
                    Log.d("chatting,getchats", response.toString());
                    Log.d("chatting,getchats", String.valueOf(response.code()));
                    Log.d("chatting,getchats", response.message());
                    Log.d("chatting,getchats", String.valueOf(response.raw().request().url().url()));
                    Log.d("chatting,getchats", new Gson().toJson(response.raw().request()));
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Log.d("chatting,getchats", String.valueOf(t));

            }
        });




    }


    @Override
    protected void onDestroy() {
        stompClient.disconnect();
        stompClient = null;
        super.onDestroy();

    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    public void sendStomp(String message) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId",id);
        jsonObject.put("sender","username");
        jsonObject.put("type","TALK");
        jsonObject.put("message",message);


        stompClient.send(new StompMessage(StompCommand.SEND, Arrays.asList(new StompHeader(StompHeader.DESTINATION, "/pub/chat/message"),
                new StompHeader("Authorization","Bearer " + token.getAccessToken())), jsonObject.toString())).subscribe();

        Chat chat = new Chat(message,username,userprofile,new Timestamp(System.currentTimeMillis()).toString(), isowner.toUpperCase(), ChatType.ViewType.RIGHT_CONTENT);
        addItem(chat);


    }

    protected CompletableTransformer applySchedulers() {
            return upstream -> upstream
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

    public void initChatRoom(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ChattingActivity.this);
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
                        isowner = chatRoom.getIsOwner();
                        if(isowner.equals("true")){
                            ismaster = true;
                        }else{
                            ismaster = false;
                        }
                    }
                }else if(response.code() == 404){
                    Toast.makeText(ChattingActivity.this, "조회할 수 없는 채팅방입니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Log.d("chatting,getChatRoom", new Gson().toJson(response.errorBody()));
                    Log.d("chatting,getChatRoom", response.toString());
                    Log.d("chatting,getChatRoom", String.valueOf(response.code()));
                    Log.d("chatting,getChatRoom", response.message());
                    Log.d("chatting,getChatRoom", String.valueOf(response.raw().request().url().url()));
                    Log.d("chatting,getChatRoom", new Gson().toJson(response.raw().request()));

                }

            }

            @Override
            public void onFailure(Call<ChatRoomInformResponse> call, Throwable t) {

                Log.d("chatting,getChatRoom", String.valueOf(t));

            }
        });

    }

    public void getUser(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ChattingActivity.this);
        Call<UserProfileResponse> call = apiinterface.getUserProfile();
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {

                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().equals("PROFILE_READ_SUCCESS")) {
                        UserProfileResponse res = response.body();
                        //Log.d("성공", new Gson().toJson(res));
                        username = res.getData().getNickname();
                        userprofile = res.getData().getProfileImg();
                    }
                }else if(response.code() == 404){
                    Log.w("chatting,getuserprof","해당 프로필을 찾을 수 없습니다.");
                }

            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {

                Log.d("chatting,getuserprof", String.valueOf(t));

            }
        });

    }

    public void votePromise(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ChattingActivity.this);
        Call<ResponseBody> call = apiinterface.votePromise(Integer.parseInt(id));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 201) {
                        ResponseBody res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        Toast.makeText(ChattingActivity.this,"약속에 동의하셨습니다.",Toast.LENGTH_SHORT).show();

                    }
                }else if(response.code() == 400){
                    Toast.makeText(ChattingActivity.this,"약속 투표가 진행중이지 않습니다",Toast.LENGTH_SHORT).show();
                }else if(response.code() == 409){
                    Toast.makeText(ChattingActivity.this,"이미 기동의한 약속입니다.",Toast.LENGTH_SHORT).show();
                }else if(response.code() == 404){
                    Toast.makeText(ChattingActivity.this,"동의할 수 없는 약속입니다.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("chatting,voteprom", String.valueOf(t));

            }
        });

    }

    public void getInfo(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ChattingActivity.this);
        Call<ChatPromiseResponse> call = apiinterface.getPromise(Integer.parseInt(id));
        call.enqueue(new Callback<ChatPromiseResponse>() {
            @Override
            public void onResponse(Call<ChatPromiseResponse> call, Response<ChatPromiseResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().equals("CHAT_PROMISE_READ_SUCCESS")) {
                        ChatPromiseResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        String date = res.getData().getPromiseDate();
                        String hour = res.getData().getPromiseTime().substring(0,2);
                        String min = res.getData().getPromiseTime().substring(3,5);
                        String local = res.getData().getPromiseLocal();
                        String info = date + ", " + hour + "시 " + min + "분에 " + local + "에서 만나요";
                        tv_info.setVisibility(View.VISIBLE);
                        sec_info.setBackgroundColor(Color.parseColor("#B2FFE2"));
                        isvisible = true;
                        tv_info.setText(info);

                    }
                } else if(response.code() == 404){
                    Toast.makeText(ChattingActivity.this, "조회할 수 없는 채팅방입니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Log.d("chatting,getpromise", new Gson().toJson(response.errorBody()));
                    Log.d("chatting,getpromise", response.toString());
                    Log.d("chatting,getpromise", String.valueOf(response.code()));
                    Log.d("chatting,getpromise", response.message());
                    Log.d("chatting,getpromise", String.valueOf(response.raw().request().url().url()));
                    Log.d("chatting,getpromise", new Gson().toJson(response.raw().request()));

                }
            }

            @Override
            public void onFailure(Call<ChatPromiseResponse> call, Throwable t) {

                Log.d("chatting,getpromise", String.valueOf(t));

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(ChattingActivity.this,"승인이 허가되었습니다.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ChattingActivity.this,"승인이 허가되어 있지 않습니다. 설정 - 권한에서 설정해주세요. ",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @Override
    public void onItemSelected(View v) {
        votePromise(token);
    }
}