package org.techtown.dingdong.mytown;

import android.view.View;

public interface OnTownItemClickListener {
    public void onItemClick(TownAdapter.ViewHoldder holder, View view, int position);
}
