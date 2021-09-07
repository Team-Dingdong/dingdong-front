package org.techtown.dingdong.chatting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class ChattingActivity extends AppCompatActivity implements ChattingBottomDialogFragment.onInteractionListener{
    private ArrayList<Chat> chats = new ArrayList<>();
    private RecyclerView recycler_chat;
    private TextView tv_people, tv_title;
    private ImageView img_people;
    private ImageButton btn_plus, btn_send, btn_back;
    private EditText et_message;
    private LinearLayout view_plus;
    private final int OPEN_GALLERY = 201;
    ChattingAdapter chatAdapter;
    Uri imageUri;
    private String message, username, userprofile, isowner="TRUE", ownername="test_nickname2";
    private String id = "1";
    private Boolean ismaster = true;
    ChattingBottomDialogFragment chattingBottomDialogFragment;
    private Gson gson;
    public static String WS_URL = "ws://3.38.61.13:8080/ws-stomp/websocket";
    private Token token;
    private StompManager stompManager;
    StompClient stompClient;
    CompositeDisposable compositeDisposable;
    private static final Pattern PATTERN_HEADER = Pattern.compile("([^:\\n\\r]+)\\s*:\\s*([^:\\n\\r]+)");
    Boolean isUnexpectedClosed;



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

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Log.d("토큰", id);
        Log.d("토큰", String.valueOf(access));

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
                            //toast("Stomp connection opened");
                            break;
                        case ERROR:
                            Log.d("lifecycle", String.valueOf(lifecycleEvent.getException()));
                            Log.e("connecterror", "Stomp connection error", lifecycleEvent.getException());
                            if(lifecycleEvent.getException().getMessage().contains("EOF")){
                                isUnexpectedClosed=true;
                            }
                            //toast("Stomp connection error");
                            break;
                        case CLOSED:
                            //toast("Stomp connection closed");
                            Log.d("lifecycle","###########offline");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            //toast("Stomp failed server heartbeat");
                            Log.d("lifecycle","failedserverheartbeat");
                            break;
                    }

                },
                        throwable -> {
                            Log.d("stpmsgthrowinlifecycle",new Gson().toJson(throwable));

                        });

        Log.d("chatstomp","자니..? 왜응답이없어");

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
            Log.d("onnext",jsonObject.toString());
            //Log.d("onnext",stomp);


            if(type.equals("TALK") && !sender.equals(username)){
                //isowner = (sender.equals(ownername)) ? "TRUE" : "FALSE";
                Chat chat = new Chat(msg,sender,"아",new Timestamp(System.currentTimeMillis()).toString(), (sender.equals(ownername)) ? "TRUE" : "FALSE", ChatType.ViewType.LEFT_CONTENT);
                addItem(chat);
                Log.d("talk","get");
                }else if(type.equals("ENTER")){
                Chat chat = new Chat(msg,sender,"아",new Timestamp(System.currentTimeMillis()).toString(), "FALSE", ChatType.ViewType.CENTER_CONTENT);
                addItem(chat);
            }
            }, throwable -> { Log.e("chat", "Error on subscribe topic", throwable); }
        );



        /*Disposable disTopi = stompClient.topic("/topic/chat/room/1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    Log.d("distop", "Received " + stompMessage.getPayload());

                }, throwable -> {
                    Log.e("distop", "Error on subscribe topic", throwable);
                });*/


        compositeDisposable.add(disposable);
        compositeDisposable.add(dispTopic);

        stompClient.connect(header);
        //setDummy();
        recycler_chat = findViewById(R.id.chatting_recycler);
        //setChatRecycler(recycler_chat, chats);
        btn_plus = findViewById(R.id.btn_plus);
        btn_send = findViewById(R.id.btn_send);
        et_message = findViewById(R.id.et_chat);
        btn_back = findViewById(R.id.btn_back);
        tv_people = findViewById(R.id.tv_people);
        img_people = findViewById(R.id.btn_people);
        tv_title = findViewById(R.id.tv_title);

        initChatRoom(token);
        setChats(token);

        img_people.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick (View v){
                            Intent intent = new Intent(ChattingActivity.this, UserListActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                    }
                    });



        tv_people.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick (View v){
                        Intent intent = new Intent(ChattingActivity.this, UserListActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);

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
                            //Chat chat = new Chat(message, "다루", "아", "2021-08-24 17:00:33.822", Boolean.TRUE, ChatType.ViewType.RIGHT_CONTENT);
                        //chatAdapter.addItem(chat);
                        //recycler_chat.scrollToPosition(chatAdapter.getItemCount() - 1);
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
                    Chat chat = new Chat(imageUri.toString(),username,userprofile,new Timestamp(System.currentTimeMillis()).toString(), (username.equals(ownername)) ? "TRUE" : "FALSE", ChatType.ViewType.RIGHT_CONTENT_IMG);
                    chatAdapter.addItem(chat);
                    recycler_chat.scrollToPosition(chatAdapter.getItemCount()-1);

        }
    } }




    private void setDummy(){
        chats = new ArrayList<>();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String t = time.toString();
        String d = "2021-08-22 17:00:33.822";
        Log.d("time",t);
        //객체 추가
        chats.add(new Chat("안녕하세요 여러분","원선","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                d,"FALSE",ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("넵 안녕하세요","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                d,"FALSE",ChatType.ViewType.RIGHT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                d,"FALSE",ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,"FALSE",ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,"FALSE",ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,"FALSE",ChatType.ViewType.LEFT_CONTENT_IMG ));
        chats.add(new Chat("https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,"FALSE",ChatType.ViewType.RIGHT_CONTENT_IMG ));
        chats.add(new Chat("노원구청어쩌구저쩌구에서 만나요!","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,"FALSE",ChatType.ViewType.RIGHT_CONTENT_PLAN));
        chats.add(new Chat("노원구청어쩌구저쩌구에서 만나요!","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,"FALSE",ChatType.ViewType.LEFT_CONTENT_PLAN));

    }

    @Override
    public void onButtonChoice(int choice) {
        switch (choice){
            case 1:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, OPEN_GALLERY);
                chattingBottomDialogFragment.dismiss();
                break;
            case 2:
                Intent intent2 = new Intent(ChattingActivity.this,PlanningActivity.class);
                intent2.putExtra("id",id);
                startActivity(intent2);
                chattingBottomDialogFragment.dismiss();
                break;
            case 3:
                chattingBottomDialogFragment.dismiss();
                break;

        }

    }


    private void setChatRecycler(RecyclerView recyclerView, ArrayList<Chat> chats){
        chatAdapter = new ChattingAdapter(chats, username);
        chatAdapter.setHasStableIds(true);
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
                        chats = res.getChats();
                        setChatRecycler(recycler_chat, chats);

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
            public void onFailure(Call<ChatResponse> call, Throwable t) {

                Log.d("외않되", String.valueOf(t));

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

        Log.d("sendstomp","데이터센딩");

        stompClient.send(new StompMessage(StompCommand.SEND, Arrays.asList(new StompHeader(StompHeader.DESTINATION, "/pub/chat/message"),
                new StompHeader("Authorization","Bearer " + token.getAccessToken())), jsonObject.toString())).subscribe();

        Chat chat = new Chat(message,username,userprofile,new Timestamp(System.currentTimeMillis()).toString(), (username.equals(ownername)) ? "TRUE" : "FALSE", ChatType.ViewType.RIGHT_CONTENT);
        addItem(chat);


        Log.d("sendstomp","데이터센딩완료");

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

    public void getUser(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ChattingActivity.this);
        Call<UserProfileResponse> call = apiinterface.getUserProfile();
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {

                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().equals("PROFILE_READ_SUCCESS")) {
                        UserProfileResponse res = response.body();
                        Log.d("성공", new Gson().toJson(res));
                        username = res.getData().getNickname();
                        userprofile = res.getData().getProfileImg();
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
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {

                Log.d("외않되", String.valueOf(t));

            }
        });

    }


}