package org.techtown.dingdong.mytown;

import android.view.View;

public interface OnTownItemClickListener {

    void onItemClick(TownAdapter.ViewHoldder holder, View view, int position);
}
