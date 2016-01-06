package com.ecolumbia.gsdemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import dji.midware.data.manager.P3.ServiceManager;
import dji.sdk.api.DJIDrone;

public class GroundStationControlActivity extends AppCompatActivity {

    private final static String TAG_FRAGMENT_MAINCONTROLLER = "Fragment_MAINCONTROLLER";
    private final static String TAG_FRAGMENT_INITIALIZATION = "Fragment_INITIALIZATION";
    private final static String TAG_FRAGMENT_MOTOR = "Fragment_Motor";
    private Menu menu = null;
    private Bundle bundleActivity;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, DjiAoaActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_station_control);

        Intent intent = getIntent();

        // The new bundleActivity stores all the intent bundle data values.
        // Also the connectionSuccessDescription is added to bundleActivity
        bundleActivity = new Bundle();
        bundleActivity.putString(getString(R.string.TAG_DRONE_MODEL_ID), intent.getStringExtra(getString(R.string.TAG_DRONE_MODEL_ID)));
        bundleActivity.putInt(getString(R.string.TAG_PERMISSION_RESULT), intent.getIntExtra(getString(R.string.TAG_PERMISSION_RESULT), 999));
        bundleActivity.putString(getString(R.string.TAG_PERMISSION_RESULT_DESCRIPTION), intent.getStringExtra(getString(R.string.TAG_PERMISSION_RESULT_DESCRIPTION)));
        String connectionSuccessDescription = ConnectToDrone();
        bundleActivity.putString(getString(R.string.TAG_CONNECTION_SUCCESS_DESCRIPTION), connectionSuccessDescription);

        // Do not show back arrow or home button on the toolbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }


    @Override
    protected void onPause() {
        super.onPause();

        // Remove the fragments if loaded.
        RemoveAllFragments();
        DJIDrone.disconnectToDrone();
        ServiceManager.getInstance().pauseService(true);

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
        menu.findItem(R.id.menu_main_controller).setChecked(SharedPref.readFromPreferences(getApplicationContext(), TAG_FRAGMENT_MAINCONTROLLER, true));
        menu.findItem(R.id.menu_initialization).setChecked( SharedPref.readFromPreferences(getApplicationContext(), TAG_FRAGMENT_INITIALIZATION, true));
        menu.findItem(R.id.menu_motor).setChecked( SharedPref.readFromPreferences(getApplicationContext(), TAG_FRAGMENT_MOTOR, true));
        AddAllFragments();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_main_controller:
                if (item.isChecked()) {
                    item.setChecked(false);
                    SharedPref.saveToPreferences(getApplicationContext(), TAG_FRAGMENT_MAINCONTROLLER, false);
                    RemoveOneFragment(TAG_FRAGMENT_MAINCONTROLLER);
                } else {
                    item.setChecked(true);
                    SharedPref.saveToPreferences(getApplicationContext(), TAG_FRAGMENT_MAINCONTROLLER, true);
                    AddOneFragment(TAG_FRAGMENT_MAINCONTROLLER);
                }
                break;
            case R.id.menu_initialization:
                if (item.isChecked()) {
                    item.setChecked(false);
                    SharedPref.saveToPreferences(getApplicationContext(), TAG_FRAGMENT_INITIALIZATION, false);
                    RemoveOneFragment(TAG_FRAGMENT_INITIALIZATION);
                } else {
                    item.setChecked(true);
                    SharedPref.saveToPreferences(getApplicationContext(), TAG_FRAGMENT_INITIALIZATION, true);
                    AddOneFragment(TAG_FRAGMENT_INITIALIZATION);
                }
                break;
            case R.id.menu_motor:
                if (item.isChecked()) {
                    item.setChecked(false);
                    SharedPref.saveToPreferences(getApplicationContext(), TAG_FRAGMENT_MOTOR, false);
                    RemoveOneFragment(TAG_FRAGMENT_MOTOR);
                } else {
                    item.setChecked(true);
                    SharedPref.saveToPreferences(getApplicationContext(), TAG_FRAGMENT_MOTOR, true);
                    AddOneFragment(TAG_FRAGMENT_MOTOR);
                }
                break;
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
        boolean sharedPref;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // Add the fragment only if the fragment does not exist and the menu item has been checked.
        switch (fragmentTag) {
            case TAG_FRAGMENT_MAINCONTROLLER:
                sharedPref = SharedPref.readFromPreferences(getApplicationContext(), TAG_FRAGMENT_MAINCONTROLLER, true);
                Fragment fragmentMainController = fm.findFragmentByTag(TAG_FRAGMENT_MAINCONTROLLER);
                if (fragmentMainController == null) {
                    if (menu != null) {
                        MenuItem menu_MainController = menu.findItem(R.id.menu_main_controller);
                        if (menu_MainController != null) {
                            if (sharedPref) {
                                MainControllerFragment mainControllerFragment = new MainControllerFragment();
                                ft.add(R.id.content_MainController, mainControllerFragment, TAG_FRAGMENT_MAINCONTROLLER);
                            }
                        }
                    }
                }
                break;
            case TAG_FRAGMENT_INITIALIZATION:
                sharedPref = SharedPref.readFromPreferences(getApplicationContext(), TAG_FRAGMENT_INITIALIZATION, true);
                Fragment fragmentInitialization = fm.findFragmentByTag(TAG_FRAGMENT_INITIALIZATION);
                if (fragmentInitialization == null) {
                    if (menu != null) {
                        MenuItem menu_Initialization = menu.findItem(R.id.menu_initialization);
                        if (menu_Initialization != null) {
                            if (sharedPref) {
                                InitializationFragment initializationFragment = new InitializationFragment();
                                initializationFragment.setArguments(bundleActivity);
                                ft.add(R.id.content_Initialization, initializationFragment, TAG_FRAGMENT_INITIALIZATION);
                            }
                        }
                    }
                }
                break;
            case TAG_FRAGMENT_MOTOR:
                sharedPref = SharedPref.readFromPreferences(getApplicationContext(), TAG_FRAGMENT_MOTOR, true);
                Fragment fragmentMotor = fm.findFragmentByTag(TAG_FRAGMENT_MOTOR);
                if (fragmentMotor == null) {
                    if (menu != null) {
                        MenuItem menu_Motor = menu.findItem(R.id.menu_motor);
                        if (menu_Motor != null) {
                            if (sharedPref) {
                                MotorFragment motorFragment = new MotorFragment();
                                ft.add(R.id.content_Motor, motorFragment, TAG_FRAGMENT_MOTOR);
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
        AddOneFragment(TAG_FRAGMENT_INITIALIZATION);
        AddOneFragment(TAG_FRAGMENT_MOTOR);
    }


    private void RemoveOneFragment(String fragmentTag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment checkFragment = fm.findFragmentByTag(fragmentTag);
        if (checkFragment != null) {
            ft.remove(checkFragment);
        }
        ft.commit();
    }

    private void RemoveAllFragments() {
        RemoveOneFragment(TAG_FRAGMENT_MAINCONTROLLER);
        RemoveOneFragment(TAG_FRAGMENT_INITIALIZATION);
        RemoveOneFragment(TAG_FRAGMENT_MOTOR);
    }




}
