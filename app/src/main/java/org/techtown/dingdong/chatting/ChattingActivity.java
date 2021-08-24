package org.techtown.dingdong.chatting;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import org.techtown.dingdong.R;
import org.techtown.dingdong.home.ImageUploadAdapter;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity implements ChattingBottomDialogFragment.onInteractionListener{
    private ArrayList<Chat> chats;
    private RecyclerView recycler_chat;
    private ImageButton btn_plus, btn_send, btn_back;
    private EditText et_message;
    private LinearLayout view_plus;
    private final int OPEN_GALLERY = 201;
    ChattingAdapter chatAdapter;
    Uri imageUri;
    private String message;
    private String id = "1";
    private Boolean ismaster = true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);


        setDummy();

        recycler_chat = findViewById(R.id.chatting_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        chatAdapter = new ChattingAdapter(chats);
        recycler_chat.setLayoutManager(manager);
        recycler_chat.setAdapter(chatAdapter);
        recycler_chat.scrollToPosition(chatAdapter.getItemCount()-1);


        btn_plus = findViewById(R.id.btn_plus);
        btn_send = findViewById(R.id.btn_send);
        et_message = findViewById(R.id.et_chat);
        btn_back = findViewById(R.id.btn_back);


        final ChattingBottomDialogFragment chattingBottomDialogFragment = new ChattingBottomDialogFragment(getApplicationContext(), ismaster);


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
                Chat chat = new Chat(message,"다루","아","2021-08-24 17:00:33.822", Boolean.TRUE, ChatType.ViewType.RIGHT_CONTENT);
                chatAdapter.addItem(chat);
                recycler_chat.scrollToPosition(chatAdapter.getItemCount()-1);
            }
        });


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
                break;
            case 2:
                Intent intent2 = new Intent(ChattingActivity.this,PlanningActivity.class);
                intent2.putExtra("id",id);
                startActivity(intent2);
                break;
            case 3:
                break;

        }


    }
}