package org.techtown.dingdong.mytown;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;

import java.util.ArrayList;

public class TownAdapter extends RecyclerView.Adapter<TownAdapter.ViewHoldder> {
    ArrayList<Town> items = new ArrayList<Town>();

    @NonNull
    @NotNull
    @Override
    public ViewHoldder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = inflater.inflate(R.layout.town_item, viewGroup, false);

        return new ViewHoldder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHoldder viewholder, int position) {

        Town item= items.get(position);
        viewholder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Town item){
        items.add(item);
    }

    public void setItems(ArrayList<Town> items){
        this.items = items;
    }
    public Town getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, Town item){
        items.set(position, item);
    }

    public class ViewHoldder extends RecyclerView.ViewHolder {
        Button button;

        public ViewHoldder(@NonNull @NotNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.town_btn);
        }

        public void setItem(Town item){
            button.setText(item.getName());
        }
    }
}
