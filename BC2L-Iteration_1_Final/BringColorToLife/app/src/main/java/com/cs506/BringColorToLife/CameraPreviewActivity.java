package com.cs506.BringColorToLife;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by AlexMotl on 3/06/17.
 */

public class CameraPreviewActivity extends FragmentActivity implements MainMenuFragment.MainMenuCallBack,
        HSVMenuFragment.HSVMenuCallBack, YourFiltersMenuFragment.YourFiltersMenuCallBack,
        CVDFilterMenuFragment.CVDFilterMenuCallBack, CameraPreviewFragment.CameraPreviewFragmentCallBack {

    //declare fragments used
    MainMenuFragment mainMenuFragment;
    HSVMenuFragment hsvMenuFragment;
    YourFiltersMenuFragment yourFiltersMenuFragment;
    CVDFilterMenuFragment cvdFilterMenuFragment;

    //used to manage transactions of fragments
    FragmentManager fragmentManager;

    boolean savedFilter;

    //declare buttons
    FloatingActionButton rootMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        rootMenuButton = (FloatingActionButton) findViewById(R.id.rootMenuButton);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragmentContainer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            mainMenuFragment = new MainMenuFragment();
            hsvMenuFragment = new HSVMenuFragment();
            yourFiltersMenuFragment = new YourFiltersMenuFragment();
            cvdFilterMenuFragment = new CVDFilterMenuFragment();
        }

        rootMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (mainMenuFragment != null && !mainMenuFragment.isVisible()) {
                    openMenuFragment(mainMenuFragment);
                    rootMenuButton.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    // used for managing the closing of individual fragments.
    public void closeMenuFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment).commit();
    }

    // used for managing the opening of individual fragments.
    public void openMenuFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment).commit();
        rootMenuButton.setVisibility(View.INVISIBLE);

    }

    // Main Menu Fragment Call Back Method Implementations
    public void closeMainMenuFragment() {
        closeMenuFragment(mainMenuFragment);
        rootMenuButton.setVisibility(View.VISIBLE);
    }

    public void openHSVMenuFragment() {
        closeMenuFragment(mainMenuFragment);
        openMenuFragment(hsvMenuFragment);
    }

    public void openCVDMenuFragment() {
        closeMenuFragment(mainMenuFragment);
        openMenuFragment(cvdFilterMenuFragment);
    }

    public void openYourFiltersMenuFragment() {
        if (!savedFilter) {
            YourFiltersMenuFragment x = new YourFiltersMenuFragment();
            yourFiltersMenuFragment = x;
        }
        closeMenuFragment(mainMenuFragment);
        openMenuFragment(yourFiltersMenuFragment);
        savedFilter = false;
    }

    public void saveCustomFilter() {

        YourFiltersMenuFragment test = new YourFiltersMenuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("customName", "Custom Name");
        test.setArguments(bundle);
        yourFiltersMenuFragment = test;
        savedFilter = true;
    }

    public void closeHSVMenuFragment() {
        closeMenuFragment(hsvMenuFragment);
        openMenuFragment(mainMenuFragment);
    }

    public void closeYourFiltersMenuFragment() {
        closeMenuFragment(yourFiltersMenuFragment);
        openMenuFragment(mainMenuFragment);
    }

    public void closeCVDFilterMenuFragment() {
        closeMenuFragment(cvdFilterMenuFragment);
        openMenuFragment(mainMenuFragment);
    }

    public void sendImageToCaptureActivity(String placeholder) {
        Intent intent = new Intent(this, CameraCaptureActivity.class);
        String message = placeholder;
        intent.putExtra("Captured Image", message);
        startActivity(intent);
    }
}
