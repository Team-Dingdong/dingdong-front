package org.techtown.dingdong.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private ArrayList<ChatUser> chatUsers;
    private RecyclerView recyclerView;
    ChatUserAdapter chatUserAdapter;
    Boolean ismaster = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setDummy();

        btn_back = findViewById(R.id.ic_back);
        recyclerView = findViewById(R.id.recycler_user);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chatUserAdapter = new ChatUserAdapter(chatUsers, this);
        recyclerView.setAdapter(chatUserAdapter);

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
                                    if(chatUser.getIsmaster() == false){
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
                finish();
            }
        });
    }

    private void setDummy(){

       chatUsers = new ArrayList<>();
       chatUsers.add(new ChatUser("젤리","https://cdn.pixabay.com/photo/2016/08/24/21/33/gummybears-1618073_1280.jpg",true));
       chatUsers.add(new ChatUser("원선","https://cdn.pixabay.com/photo/2019/10/15/21/34/cat-4552983_1280.jpg",false));
       chatUsers.add(new ChatUser("김나나","https://cdn.pixabay.com/photo/2016/08/24/21/33/gummybears-1618073_1280.jpg",false));
       chatUsers.add(new ChatUser("다루","https://cdn.pixabay.com/photo/2019/10/15/21/34/cat-4552983_1280.jpg",false));



    }
}