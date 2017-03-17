package com.cs506.BringColorToLife;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by AlexMotl on 3/14/17.
 */

public class CVDFilterMenuFragment extends Fragment {

    //interface
    CVDFilterMenuCallBack mCallback;

    //buttons
    FloatingActionButton cvd1Button;
    FloatingActionButton cvd2Button;
    FloatingActionButton backButton;

    public interface CVDFilterMenuCallBack {
        void closeCVDFilterMenuFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (CVDFilterMenuCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //connect fragment to xml layout
        View view = inflater.inflate(R.layout.cvdfilters_menu_fragment, container, false);

        cvd1Button = (FloatingActionButton) view.findViewById(R.id.cvd1MenuButton);
        cvd2Button = (FloatingActionButton) view.findViewById(R.id.cvd2MenuButton);
        backButton = (FloatingActionButton) view.findViewById(R.id.cvdMenuBackButton);

        //declare and initialize button animations
        final Animation mainMenuShowButtonAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_button_show);
        final Animation mainMenuHideButtonAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_button_hide);

        cvd1Button.startAnimation(mainMenuShowButtonAnim);
        cvd2Button.startAnimation(mainMenuShowButtonAnim);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvd1Button.startAnimation(mainMenuHideButtonAnim);
                cvd2Button.startAnimation(mainMenuHideButtonAnim);
            }
        });

        mainMenuHideButtonAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mCallback.closeCVDFilterMenuFragment();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        return view;
    }


}
