package com.ecolumbia.gsdemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dji.midware.data.manager.P3.ServiceManager;
import dji.sdk.api.DJIDrone;

public class GroundStationControlActivity extends AppCompatActivity {

    private final static String TAG_FRAGMENT_MAINCONTROLLER = "Fragment_MAINCONTROLLER";
    private Menu menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_station_control);

        String connectionSuccessDescription = ConnectToDrone();


    }


    @Override
    protected void onPause() {
        super.onPause();
        //DJIDrone.disconnectToDrone();
        ServiceManager.getInstance().pauseService(true);

        // Remove the fragments if loaded.
        RemoveAllFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceManager.getInstance().pauseService(false);

        // Add the fragment and if the menu item is checked.
        AddAllFragments();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceManager.getInstance().pauseService(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ground_station_control, menu);
        this.menu = menu;
        AddAllFragments();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_main_controller) {
            if (item.isChecked()) {
                item.setChecked(false);
                RemoveOneFragment(TAG_FRAGMENT_MAINCONTROLLER);
            } else {
                item.setChecked(true);
                AddOneFragment(TAG_FRAGMENT_MAINCONTROLLER);
            }

        }

        return super.onOptionsItemSelected(item);
    }


    private String ConnectToDrone() {
        // The SDK initiation
        String ConnectionSuccessDescription;
        try {

            boolean connectionSuccess = DJIDrone.connectToDrone(); // Connect to the drone

            if (connectionSuccess) {
                ConnectionSuccessDescription = "Yes";
            } else {
                ConnectionSuccessDescription = "No";
            }
        } catch (Exception e) {
            ConnectionSuccessDescription = "Connection Error: " + e.getMessage();
        }

        return ConnectionSuccessDescription;
    }


    private void AddOneFragment(String fragmentTag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // Add the fragment only if the fragment does not exist and the menu item has been checked.
        switch (fragmentTag) {
            case TAG_FRAGMENT_MAINCONTROLLER:
                Fragment fragmentMainController = fm.findFragmentByTag(TAG_FRAGMENT_MAINCONTROLLER);
                if (fragmentMainController == null) {
                    if (menu != null) {
                        MenuItem menu_MainController = menu.findItem(R.id.menu_main_controller);
                        if (menu_MainController != null) {
                            if (menu_MainController.isChecked()) {
                                MainControllerFragment mainControllerFragment = new MainControllerFragment();
                                ft.add(R.id.content_MainController, mainControllerFragment, TAG_FRAGMENT_MAINCONTROLLER);
                            }
                        }
                    }
                }
                break;
        }

        ft.commit();
    }

    private void AddAllFragments() {
        AddOneFragment(TAG_FRAGMENT_MAINCONTROLLER);
    }


    private void RemoveOneFragment(String fragmentTag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment checkFragment = fm.findFragmentByTag(TAG_FRAGMENT_MAINCONTROLLER);
        if (checkFragment != null) {
            ft.remove(checkFragment);
        }
        ft.commit();
    }

    private void RemoveAllFragments() {
        RemoveOneFragment(TAG_FRAGMENT_MAINCONTROLLER);
    }


}
