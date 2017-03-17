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
import android.widget.TextView;


/**
 * Created by AlexMotl on 3/08/17.
 */

public class MainMenuFragment extends Fragment {

    //declare callback interface used to communicate with main activity
    MainMenuCallBack mCallback;

    //declare main menu buttons
    FloatingActionButton hsvButton;
    FloatingActionButton addFilterButton;
    FloatingActionButton cvdButton;
    FloatingActionButton yourFiltersButton;
    FloatingActionButton preferencesButton;
    FloatingActionButton backButton;

    //declare text for main menu buttons
    TextView hsvText;
    TextView addFilterText;
    TextView cvdText;
    TextView yourFiltersText;
    TextView preferencesText;

    public interface MainMenuCallBack {
        void closeMainMenuFragment();
        void openHSVMenuFragment();
        void openYourFiltersMenuFragment();
        void saveCustomFilter();
        void openCVDMenuFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (MainMenuCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);

        //initialize menu buttons
        hsvButton = (FloatingActionButton) view.findViewById(R.id.mainMenuHSVButton);
        addFilterButton = (FloatingActionButton) view.findViewById(R.id.mainMenuAddFilterButton);
        cvdButton = (FloatingActionButton) view.findViewById(R.id.mainMenuCVDButton);
        yourFiltersButton = (FloatingActionButton) view.findViewById(R.id.mainMenuYFButton);
        preferencesButton = (FloatingActionButton) view.findViewById(R.id.mainMenuPreferencesButton);
        backButton = (FloatingActionButton) view.findViewById(R.id.mainMenuBackButton);

        //initialize text views
        hsvText = (TextView) view.findViewById(R.id.hsvMenuText);
        addFilterText = (TextView) view.findViewById(R.id.addFilterMenuText);
        cvdText = (TextView) view.findViewById(R.id.cvdMenuText);
        yourFiltersText = (TextView) view.findViewById(R.id.yourFiltersMenuText);
        preferencesText = (TextView) view.findViewById(R.id.prefMenuText);

        //define and initialize animations
        final Animation mainMenuShowButton = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_button_show);
        final Animation mainMenuHideButtonAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_button_hide);

        //animations for buttons opening
        hsvButton.startAnimation(mainMenuShowButton);
        addFilterButton.startAnimation(mainMenuShowButton);
        cvdButton.startAnimation(mainMenuShowButton);
        yourFiltersButton.startAnimation(mainMenuShowButton);
        preferencesButton.startAnimation(mainMenuShowButton);

        //animations for text views opening
        hsvText.startAnimation(mainMenuShowButton);
        addFilterText.startAnimation(mainMenuShowButton);
        cvdText.startAnimation(mainMenuShowButton);
        yourFiltersText.startAnimation(mainMenuShowButton);
        preferencesText.startAnimation(mainMenuShowButton);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hsvButton.startAnimation(mainMenuHideButtonAnim);
                addFilterButton.startAnimation(mainMenuHideButtonAnim);
                cvdButton.startAnimation(mainMenuHideButtonAnim);
                yourFiltersButton.startAnimation(mainMenuHideButtonAnim);
                preferencesButton.startAnimation(mainMenuHideButtonAnim);
                hsvText.startAnimation(mainMenuHideButtonAnim);
                addFilterText.startAnimation(mainMenuHideButtonAnim);
                cvdText.startAnimation(mainMenuHideButtonAnim);
                yourFiltersText.startAnimation(mainMenuHideButtonAnim);
                preferencesText.startAnimation(mainMenuHideButtonAnim);
            }
        });

        hsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.openHSVMenuFragment();
            }
        });

        yourFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.openYourFiltersMenuFragment();
            }
        });

        addFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.saveCustomFilter();
            }
        });

        cvdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.openCVDMenuFragment();
            }
        });

        mainMenuHideButtonAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mCallback.closeMainMenuFragment();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        return view;
    }
}
