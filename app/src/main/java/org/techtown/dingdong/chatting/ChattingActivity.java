package org.techtown.dingdong.chatting;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import org.techtown.dingdong.R;

import java.io.File;
import java.security.Timestamp;
import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity implements ChattingBottomDialogFragment.onInteractionListener{
    private ArrayList<Chat> chats;
    private RecyclerView recycler_chat;
    private ImageButton btn_plus;
    private LinearLayout view_plus;
    private final int OPEN_GALLERY = 201;
    ChatAdapter chatAdapter;
    Uri imgURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        setDummy();

        recycler_chat = findViewById(R.id.chatting_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        chatAdapter = new ChatAdapter(chats);
        recycler_chat.setLayoutManager(manager);
        recycler_chat.setAdapter(chatAdapter);
        recycler_chat.scrollToPosition(chats.size()-1);


        btn_plus = findViewById(R.id.btn_plus);

        final ChattingBottomDialogFragment chattingBottomDialogFragment = new ChattingBottomDialogFragment(getApplicationContext());


        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chattingBottomDialogFragment.show(getSupportFragmentManager(), chattingBottomDialogFragment.getTag());

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        /*if(requestCode != RESULT_OK ){
            return;
        }*/

    }

    private void setDummy(){
        chats = new ArrayList<>();
        chats.add(new Chat("안녕하세요 여러분","원선","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 2:30",Boolean.TRUE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("넵 안녕하세요","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 2:30",Boolean.FALSE,ChatType.ViewType.RIGHT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 2:30",Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 2:30",Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("반갑습니다!","다루","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 2:30",Boolean.FALSE,ChatType.ViewType.LEFT_CONTENT ));
        chats.add(new Chat("https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 2:30",Boolean.TRUE,ChatType.ViewType.LEFT_CONTENT_IMG ));
        chats.add(new Chat("https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg","정희","https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "오후 2:30",Boolean.FALSE,ChatType.ViewType.RIGHT_CONTENT_IMG ));

    }

    @Override
    public void onButtonChoice(int choice) {
        switch (choice){
            case 1:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,OPEN_GALLERY);
                break;
            case 2:
                break;
            case 3:
                break;

        }


    }
}