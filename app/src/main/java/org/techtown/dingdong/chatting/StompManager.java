package org.techtown.dingdong.chatting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.Lifecycle;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.yanzhenjie.permission.Action;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.login_register.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class StompManager {

    private String url;
    private StompClient stompClient;
    private volatile HandlerThread handlerThread;
    public static final String TAG = "StompManager";
    private ServiceHandler serviceHandler;
    private Token token;
    //private String extoken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMTAxMTExMTExMSIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2MzAwNzQxODR9.aukE4zCSf8uCH_Bwl8vl2xC2qt0D_164YPR_-xFT-xeLIgFkjkEnVS7JTOtn3T_8w0-nYibm7K_YPxndOv3U0A";

    public StompManager(String url, Token token) {

        this.token = token;

        handlerThread = new HandlerThread("ConnectionService.HandlerThread");
        handlerThread.start();
        serviceHandler = new ServiceHandler(handlerThread.getLooper());

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token.getAccessToken()); //

        //stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url, headers); //"ws://3.38.61.13:8080/ws-stomp/websocket"

        stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> Log.d(TAG, "The error message is: " + throwable))
                .observeOn(Schedulers.computation())
                .subscribe(this::handleConnectionLifecycle);



    }

    public void connect(){
        //ArrayList<StompHeader> stompHeaders = new ArrayList<>();
        //stompHeaders.add(new StompHeader("Authorization", token.getGrantType() + " " + token.getAccessToken()));
        //headers.add(new StompHeader("Authorization",token.getGrantType() + " " + token.getAccessToken()));
        stompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        Log.d("connect","connecting");
        serviceHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("connect","connecting in other thread");
                stompClient.connect();
            }
        });

    }

    public void disconnect(){
        stompClient.disconnect();

    }

    public void subscribeTopic(String topic,FlowableSubscriber<StompMessage> handler){
        Log.d("substopic","subscribetopic");
        stompClient.topic("/topic/chat/room/1")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(handler);
    }

    public void handleConnectionLifecycle(LifecycleEvent event){
        Log.d("lifecycle",new Gson().toJson(event.getHandshakeResponseHeaders()));
        Log.d("lifecycle",new Gson().toJson(event.getType()));
        switch (event.getType()){
            case OPENED:
                Log.d("lifecycle","###########online");
                break;
            case ERROR:
                Log.d("lifecycle", String.valueOf(event.getException()));
                try{
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("lifecycle","sleep ex");
                }
                if(event.getException().getMessage().contains("EOF")){
                    Log.d("lifecycle","sleep ex");
                }
                break;
            case CLOSED:
                Log.d("lifecycle","###########offline");
                break;

        }

        Log.d("online","mychatty");

    }


    private final class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }

    public void send(String mapping, String message){
        Log.d("sendmsg","sendingmsg");
        stompClient.send(mapping, message).subscribe();

    }
}
