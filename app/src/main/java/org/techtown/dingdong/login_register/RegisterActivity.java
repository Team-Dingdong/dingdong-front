package org.techtown.dingdong.login_register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import org.techtown.dingdong.R;

public class RegisterActivity extends AppCompatActivity {
    ImageView imageview;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imageview = (ImageView)findViewById(R.id.iv);
        imageview.setBackground(new ShapeDrawable(new OvalShape()));
        imageview.setClipToOutline(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}