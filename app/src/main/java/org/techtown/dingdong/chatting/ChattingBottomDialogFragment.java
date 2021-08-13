package org.techtown.dingdong.chatting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;


public class ChattingBottomDialogFragment extends BottomSheetDialogFragment {


    ImageButton btn_gallery, btn_planning, btn_delete;
    LinearLayout sec_delete, sec_gallery, sec_planning;
    Context context;
    onInteractionListener mListener;
    private final int BTN_GALLERY = 1;


    interface onInteractionListener{
        void onButtonChoice(int choice);

    }

    public ChattingBottomDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        if(context instanceof onInteractionListener ){
            mListener = (onInteractionListener) context;
        }
        else {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chatting_bottom_dialog, container, false);

        btn_gallery = v.findViewById(R.id.btn_gallery);

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonChoice(BTN_GALLERY);

            }
        });


        return v;
    }


}