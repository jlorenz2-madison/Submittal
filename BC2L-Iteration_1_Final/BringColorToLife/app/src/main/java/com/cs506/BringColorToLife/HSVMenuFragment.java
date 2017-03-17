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
import android.widget.SeekBar;


/**
 * Created by AlexMotl on 3/09/17.
 */

public class HSVMenuFragment extends Fragment {

    //declare callback interface used to communicate with main acticity
    HSVMenuCallBack mCallback;

    //declare menu buttons
    FloatingActionButton resetButton;
    FloatingActionButton backButton;

    //declare hsv sliders
    SeekBar hueSlider;
    SeekBar saturationSlider;
    SeekBar vibranceSlider;

    public interface HSVMenuCallBack {
        void closeHSVMenuFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (HSVMenuCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //connect fragment to xml layout
        View view = inflater.inflate(R.layout.hsv_menu_fragment, container, false);

        //initialize menu buttons
        resetButton = (FloatingActionButton) view.findViewById(R.id.hsvResetButton);
        backButton = (FloatingActionButton) view.findViewById(R.id.hsvMenuBackButton);

        //initialize hsv seekbars
        hueSlider = (SeekBar) view.findViewById(R.id.hueSlider);
        saturationSlider = (SeekBar) view.findViewById(R.id.saturationSlider);
        vibranceSlider = (SeekBar) view.findViewById(R.id.vibranceSilder);

        //declare and initialize button animations
        final Animation mainMenuShowButton = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_button_show);
        final Animation mainMenuHideButtonAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_button_hide);

        //start initial animation for when the fragment opens.
        resetButton.startAnimation(mainMenuShowButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButton.startAnimation(mainMenuHideButtonAnim);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //reset seekbar values back to initial 50%
                hueSlider.setProgress(50);
                saturationSlider.setProgress(50);
                vibranceSlider.setProgress(50);
            }
        });

        mainMenuHideButtonAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mCallback.closeHSVMenuFragment();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        return view;
    }
}
