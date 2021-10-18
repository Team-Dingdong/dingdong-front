package org.techtown.dingdong;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TutorialAdapter extends FragmentStateAdapter {

    private List<Integer> layouts = new ArrayList<>();

    public TutorialAdapter(@NonNull @NotNull FragmentActivity fragmentActivity, List<Integer> layouts) {
        super(fragmentActivity);
        this.layouts = layouts;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return TutorialFragment.newInstance(layouts.get(position));
    }

    @Override
    public int getItemCount() {
        return layouts.size();
    }
}
