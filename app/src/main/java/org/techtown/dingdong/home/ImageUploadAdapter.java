package org.techtown.dingdong.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {

    private ArrayList<String> Data = null;
    private ArrayList<Bitmap> postData = null;
    Context context;
    private int position;

    public ImageUploadAdapter(ArrayList<String> mData, Context mcontext) {
        this.Data = mData;
        this.context = mcontext;
    }

    public ArrayList<String> getData() {
        return Data;
    }

    public void setData(ArrayList<String> data) {
        Data = data;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_image, parent, false);
        ImageUploadAdapter.ViewHolder vh = new ImageUploadAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageUploadAdapter.ViewHolder holder, int position) {

        String image_uri =Data.get(position);

        Glide.with(context)
                .load(image_uri)
                .into(holder.image);

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

        /*if(image_uri != null){
        BitmapDrawable drawable = (BitmapDrawable) holder.image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        postData.add(getResize(bitmap));
        Log.d("비트맵","리사이즈가 잘됨");
        }*/

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageButton btn_delete;

       ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            btn_delete = itemView.findViewById(R.id.btn_delete);

        }

    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void removeItem(int position){
        Data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,Data.size());
    }




}
