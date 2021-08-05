package org.techtown.dingdong.home;

import android.content.Context;
import android.net.Uri;
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

import java.util.ArrayList;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {

    private ArrayList<Uri> mData = null;
    private Context mcontext = null;

    public ImageUploadAdapter(ArrayList<Uri> mData, Context mcontext) {
        this.mData = mData;
        this.mcontext = mcontext;
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

        Uri image_uri =mData.get(position);

        Glide.with(mcontext)
                .load(image_uri)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
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
}
