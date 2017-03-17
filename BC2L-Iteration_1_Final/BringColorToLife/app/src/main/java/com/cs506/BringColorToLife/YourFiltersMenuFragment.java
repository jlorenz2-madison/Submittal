package com.cs506.BringColorToLife;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexMotl on 3/10/17.
 */

public class YourFiltersMenuFragment extends Fragment {

    //declare callback interface to communicate with main activity
    private YourFiltersMenuCallBack mCallback;

    //declare menu buttons
    private FloatingActionButton nextButton;
    private FloatingActionButton prevButton;
    private FloatingActionButton backButton;
    private FloatingActionButton deleteButton;

    Animation mainMenuShowButtonAnim;
    Animation mainMenuHideButtonAnim;


    //custom buttons list
    List<String> customFilterNames;

    //declare menu text views for buttons
    private TextView nextText;
    private TextView prevText;
    private TextView deleteText;

    View view;

    private int prevCustomLayoutAdded;

    private int numCustomFilters;
    private final int filterLimit = 3;

    //TODO: define an inner class CustomFilter to contain the filter, the name, and the ids. and used that for the list.

    public interface YourFiltersMenuCallBack {
        void closeYourFiltersMenuFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (YourFiltersMenuCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customFilterNames); // myObject - instance of MyObject
        prefsEditor.putString("customFilterList", json);
        prefsEditor.commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //connect fragment to xml layout

        view = inflater.inflate(R.layout.yourfilters_menu_fragment, container, false);
        customFilterNames = new ArrayList<>();

        //initialize menu buttons
        nextButton = (FloatingActionButton) view.findViewById(R.id.nextYFMenuButton);
        prevButton = (FloatingActionButton) view.findViewById(R.id.prevYFMenuButton);
        backButton = (FloatingActionButton) view.findViewById(R.id.yFBackButton);
        deleteButton = (FloatingActionButton) view.findViewById(R.id.deleteCustomButton);

        //initialize text fields
        nextText = (TextView) view.findViewById(R.id.nextPageMenuText);
        prevText = (TextView) view.findViewById(R.id.prevPageMenuText);
        deleteText = (TextView) view.findViewById(R.id.deleteCustomText);

        //declare and initialize button animations
        mainMenuShowButtonAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_button_show);
        mainMenuHideButtonAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_button_hide);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButton.startAnimation(mainMenuHideButtonAnim);
                nextButton.startAnimation(mainMenuHideButtonAnim);
                prevButton.startAnimation(mainMenuHideButtonAnim);
            }
        });

        //deletes oldest custom button.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customFilterNames.size() > 0) {
                    customFilterNames.remove(customFilterNames.size() - 1);
                }
            }
        });

        mainMenuHideButtonAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mCallback.closeYourFiltersMenuFragment();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (numCustomFilters == 0) {
            prevCustomLayoutAdded = R.id.NextPageMenuHolder;
        }

        SharedPreferences appSharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("customFilterList", "");
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> customFilters = gson.fromJson(json, type);

        //TODO: animate the custom filter buttons.
        //fragment opening animations
        nextButton.startAnimation(mainMenuShowButtonAnim);
        prevButton.startAnimation(mainMenuShowButtonAnim);
        deleteButton.startAnimation(mainMenuShowButtonAnim);
        nextText.startAnimation(mainMenuShowButtonAnim);
        prevText.startAnimation(mainMenuShowButtonAnim);
        deleteText.startAnimation(mainMenuShowButtonAnim);

        if (customFilters != null) {

            for (String name : customFilters) {
                createCustomFilterButton(view, name);
            }
            customFilterNames = customFilters;
        }

        String customFilterName = getCustomFilterName();
        if (customFilterName != null) {
            createCustomFilterButton(view, getCustomFilterName());
        }


        //TODO: create a snackbar that indicates a successful save to YourFilters Menu.
    }

    String getCustomFilterName() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("Bundle", "Bundle not null");
            String name = bundle.getString("customName");
            return name;
        } else {
            return null;
        }
    }



    /* Helper function that takes the current view, the desired custom filter name, and the current
     * layout above prevPageMenuHolder and creates a LinearLayout, FAB, and TextView and formats it like
     * the statically defined LL in yourfilters_menu_fragment.xml. Returns the R.id.XXX of the LL
     * just added to be using as input for the next call.
     * Note: the first call requires that lastLayoutAdded to be R.id.NextPageMenuHolder. */
    void createCustomFilterButton(View view, String name) {

        if (numCustomFilters == filterLimit) {
            //TODO: Add snack bar notification that user has reached limit to custom number of filters.
            return;
        }

        // convert 10dp (value used in yourfilters_menu_fragment.xml) for button and layout margins
        // to px (units used in setting margins via setMargins())
        Resources r = getResources();
        int dp10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        int dp20 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
        int dp15 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics());

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.yourFilterMenu);

        // create new Linear layout for new button and textview and set required width and height parameters.
        LinearLayout ll = new LinearLayout(getActivity());
        RelativeLayout.LayoutParams llparams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // set parameters used for formatting the new LinearLayout like the statically defined ones
        ll.setOrientation(LinearLayout.HORIZONTAL);
        int llId = getNextCustomHolderId();
        Log.d("NextCustomID:", String.valueOf(llId));
        Log.d("PrevCustomID:", String.valueOf(prevCustomLayoutAdded));
        ll.setId(llId);
        llparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        llparams.addRule(RelativeLayout.BELOW, prevCustomLayoutAdded);
        llparams.setMargins(0, 0, dp10, 0);

        // apply new parameters to the new linearlayout
        ll.setLayoutParams(llparams);

        // create a new FAB for the custom filter and set width and height parameters
        FloatingActionButton customFilterButton = new FloatingActionButton(getActivity());
        FrameLayout.LayoutParams cbparams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // set parameters used for formatting the new FAB like the statically defined ones
        cbparams.gravity = Gravity.CENTER;
        customFilterButton.setSize(FloatingActionButton.SIZE_MINI);
        customFilterButton.setId(getNextCustomButtonId());
        cbparams.setMargins(dp10, dp10, dp10, dp10);

        // apply new parameters to the new FAB
        customFilterButton.setLayoutParams(cbparams);

        // create text view that will display the name of the custom filter next to its corresponding
        // FAB
        TextView customFilterText = new TextView(getActivity());
        LinearLayout.LayoutParams tvparams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        customFilterText.setId(getNextCustomTextId());
        customFilterText.setText(name);
        tvparams.gravity = Gravity.CENTER;
        customFilterText.setTextColor(getResources().getColor(R.color.text));
        customFilterText.setBackgroundResource(R.drawable.text_background);
        customFilterText.setMinHeight(dp20);
        customFilterText.setGravity(Gravity.CENTER);

        //determine width of user defined custom filter name
        Rect bounds = new Rect();
        Paint textPaint = customFilterText.getPaint();
        textPaint.getTextBounds(name, 0, name.length(), bounds);
        int nameWidth = bounds.width();
        customFilterText.setMinWidth(nameWidth + dp15);

        customFilterText.setLayoutParams(tvparams);

        /* insert new linear layout into existing UI one button above the previous button. This
         * requires setting the prevButton to be below the new button and the new button to be below
         * the old button above the prevButton */
        LinearLayout prevHolder = (LinearLayout) view.findViewById(R.id.prevPageMenuHolder);
        RelativeLayout.LayoutParams pbparams = (RelativeLayout.LayoutParams) prevHolder.getLayoutParams();
        pbparams.addRule(RelativeLayout.BELOW, llId);
        prevHolder.setLayoutParams(pbparams);

        ll.addView(customFilterText);
        ll.addView(customFilterButton);
        parent.addView(ll);

        //set to newly added LL to be used for next call
        prevCustomLayoutAdded = ll.getId();
        numCustomFilters++;
        customFilterNames.add(name);
        Log.d("createButton()","created button: " + String.valueOf(numCustomFilters));

    }

    int getNextCustomHolderId() {
        switch (numCustomFilters) {
            case 0: return R.id.customFilter1Holder;
            case 1: return R.id.customFilter2Holder;
            case 2: return R.id.customFilter3Holder;
            case 3: return R.id.customFilter4Holder;
            case 4: return R.id.customFilter5Holder;
            case 5: return R.id.customFilter6Holder;
            case 6: return R.id.customFilter7Holder;
            case 7: return R.id.customFilter8Holder;
            case 8: return R.id.customFilter9Holder;
            default: return -1;
        }
    }

    int getNextCustomButtonId() {
        switch (numCustomFilters) {
            case 0: return R.id.customFilter1;
            case 1: return R.id.customFilter2;
            case 2: return R.id.customFilter3;
            case 3: return R.id.customFilter4;
            case 4: return R.id.customFilter5;
            case 5: return R.id.customFilter6;
            case 6: return R.id.customFilter7;
            case 7: return R.id.customFilter8;
            case 8: return R.id.customFilter9;
            default: return -1;
        }
    }

    int getNextCustomTextId() {
        switch (numCustomFilters) {
            case 0: return R.id.customFilter1Text;
            case 1: return R.id.customFilter2Text;
            case 2: return R.id.customFilter3Text;
            case 3: return R.id.customFilter4Text;
            case 4: return R.id.customFilter5Text;
            case 5: return R.id.customFilter6Text;
            case 6: return R.id.customFilter7Text;
            case 7: return R.id.customFilter8Text;
            case 8: return R.id.customFilter9Text;
            default: return -1;
        }
    }

}
