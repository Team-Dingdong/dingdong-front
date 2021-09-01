package org.techtown.dingdong.chatting;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.home.ImageUploadAdapter;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import io.reactivex.CompletableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static android.content.ContentValues.TAG;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.CLOSED;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.ERROR;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED;

public class ChattingActivity extends AppCompatActivity implements ChattingBottomDialogFragment.onInteractionListener{
    private ArrayList<Chat> chats = new ArrayList<>();
    private RecyclerView recycler_chat;
    private TextView tv_people;
    private ImageView img_people;
    private ImageButton btn_plus, btn_send, btn_back;
    private EditText et_message;
    private LinearLayout view_plus;
    private final int OPEN_GALLERY = 201;
    ChattingAdapter chatAdapter;
    Uri imageUri;
    private String message;
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

        Log.d("토큰", String.valueOf(access));

        //stompManager = new StompManager("ws://3.38.61.13:8080/ws-stomp/websocket", token);
        //stompManager.connect();


        List<StompHeader> header = new ArrayList<>();
        header.add(new StompHeader("Authorization","Bearer " + token.getAccessToken()));

        //Map<String, String> headers = new HashMap<>();
        //headers.put("Authorization", "Bearer " + token.getAccessToken());
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://3.38.61.13:8080/ws-stomp/websocket");
        ///stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://3.38.61.13:8080/ws-stomp/websocket", headers);
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
        //stompManager.subscribeTopic();

        //stompManager.subscribeTopic("/topic/greetings", stompMessage -> Log.d("greeting", "Greeting incoming: " + stompMessage));


        gson = new GsonBuilder().create();


        /*Disposable disTopi = stompClient.topic("/topic/chat/room/1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    Log.d("distop", "Received " + stompMessage.getPayload());

                }, throwable -> {
                    Log.e("distop", "Error on subscribe topic", throwable);
                });*/


       stompClient.topic("/topic/chat/room/" + id, header)
                .doOnError(throwable -> {
                    Log.e("distop", "Error on subscribe topic", throwable);
                })
                .subscribe(new Subscriber<StompMessage>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d("onsubs",s.toString());

                    }

                    @Override
                    public void onNext(StompMessage stompMessage) {

                        JsonParser parser = new JsonParser();
                        Object obj = parser.parse(stompMessage.getPayload());
                        Log.d("onnext", (String) obj);

                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("onerror", "Error on user data topic", t);

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        compositeDisposable.add(disposable);
        //compositeDisposable.add(distop);

        stompClient.connect(header);


        //setDummy();

        recycler_chat = findViewById(R.id.chatting_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        chatAdapter = new ChattingAdapter(chats);
        chatAdapter.setHasStableIds(true);
        recycler_chat.setAdapter(chatAdapter);
        recycler_chat.setLayoutManager(manager);



        btn_plus = findViewById(R.id.btn_plus);
        btn_send = findViewById(R.id.btn_send);
        et_message = findViewById(R.id.et_chat);
        btn_back = findViewById(R.id.btn_back);

        tv_people = findViewById(R.id.tv_people);
        img_people = findViewById(R.id.btn_people);

        img_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChattingActivity.this, UserListActivity.class));
            }
        });


        tv_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChattingActivity.this, UserListActivity.class));

            }
        });


        chattingBottomDialogFragment = new ChattingBottomDialogFragment(getApplicationContext(), ismaster);


        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chattingBottomDialogFragment.show(getSupportFragmentManager(), chattingBottomDialogFragment.getTag());

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = et_message.getText().toString();
                et_message.setText("");
                Timestamp time = new Timestamp(System.currentTimeMillis());
                String t = time.toString();
                Chat chat = new Chat(message,"다루","아",t, Boolean.TRUE, ChatType.ViewType.RIGHT_CONTENT);
                chatAdapter.addItem(chat);
                recycler_chat.scrollToPosition(chatAdapter.getItemCount()-1);
                try {
                    sendStomp(message);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
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
                    Chat chat = new Chat(imageUri.toString(),"다루","아","2021-08-25 17:00:33.822", Boolean.TRUE, ChatType.ViewType.RIGHT_CONTENT_IMG);
                    chatAdapter.addItem(chat);
                    recycler_chat.scrollToPosition(chatAdapter.getItemCount()-1);

        }
    }
    }



    private void setDummy(){
        chats = new ArrayList<>();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String t = time.toString();
        String d = "2021-08-22 17:00:33.822";
        Log.d("time",t);
        //객체 추가
        chats.add(new Chat("안녕하세요 여러분","원선","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                d,Boolean.TRUE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("넵 안녕하세요","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                d,Boolean.FALSE,ChatType.ViewType.RIGHT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                d,Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,Boolean.TRUE,ChatType.ViewType.LEFT_CONTENT_IMG ));
        chats.add(new Chat("https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,Boolean.FALSE,ChatType.ViewType.RIGHT_CONTENT_IMG ));
        chats.add(new Chat("노원구청어쩌구저쩌구에서 만나요!","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,Boolean.FALSE,ChatType.ViewType.RIGHT_CONTENT_PLAN));
        chats.add(new Chat("노원구청어쩌구저쩌구에서 만나요!","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                t,Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT_PLAN));

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
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChattingActivity.this);

                dialog.setMessage("나눔을 정말 파기하시겠어요? 무통보 파기는 신고 사유가 될 수 있습니다.")
                        .setTitle("나눔 삭제")
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
                            }
                        }).show();
                break;

        }

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

    public void sendStomp(String message) throws JsonProcessingException {

        Log.d("sendstomp","데이터센딩");
        Map<String, Object> msg = new HashMap<>();
        msg.put("roomId",id);
        msg.put("type","TALK");
        msg.put("message",message);

        ObjectMapper mapper = new ObjectMapper();
        String jsonstring = mapper.writeValueAsString(msg);

        JsonStringEncoder encoder = JsonStringEncoder.getInstance();
        char[] jsonres = encoder.quoteAsString(jsonstring);
        String res = new String(jsonres);

        Log.d("sendstomp",res);
        stompClient.send("/pub/chat/message",res).subscribe();
        Log.d("sendstomp","데이터센딩완료");

    }

    protected CompletableTransformer applySchedulers(){
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }



}