package com.ecolumbia.gsdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.*;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
import dji.sdk.api.DJIError;
import dji.sdk.interfaces.DJIGeneralListener;

public class ConnectToDroneActivity extends AppCompatActivity {

    private static final String TAG = "DjiGs";
    private static int permissionResult;
    private static String permissionResultDescription;
    private int droneModel = 0;
    private Button btnConnectToDrone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_drone);
        btnConnectToDrone = (Button) findViewById(R.id.btn_ConnectToDrone);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnConnectToDrone.setEnabled(false);
        btnConnectToDrone.setText("Connect to Drone");
        InitializeDrone();
        new ActivateDJISDK().execute();
    }


    private class ActivateDJISDK extends AsyncTask<Void, Integer, String> {
        String CheckPermissionSuccess = "no permission registered";

        protected String doInBackground(Void... params) {
            try {
                DJIDrone.checkPermission(getApplicationContext(), new DJIGeneralListener() {
                    @Override
                    public void onGetPermissionResult(int result) {
                        permissionResult = result;
                        permissionResultDescription = DJIError.getCheckPermissionErrorDescription(result);
                        CheckPermissionSuccess = permissionResultDescription;
                    }
                });
            } catch (Exception e) {
                permissionResult = -999;
                permissionResultDescription = e.getMessage();
                CheckPermissionSuccess = "Error checking permission: " + permissionResultDescription;
            }

            return CheckPermissionSuccess;
        }

        protected void onPostExecute(String result) {
            if (permissionResult == 0) {
                btnConnectToDrone.setText("Connect to Drone" + " - " + permissionResultDescription);
                btnConnectToDrone.setEnabled(true);
            } else {
                btnConnectToDrone.setText("Permission not acquired: " + Integer.toString(permissionResult) + permissionResultDescription);
            }
        }
    }

    public void btn_ConnectToDrone_onClick(View view) {
        // Intent for the activity to open when user selects the notification
        Intent intentGroundStation = new Intent(ConnectToDroneActivity.this, GroundStationControlActivity.class);
        intentGroundStation.putExtra("DroneModelId", droneModel);
        intentGroundStation.putExtra("PermissionResult", permissionResult);
        intentGroundStation.putExtra("PermissionResultDescription", permissionResultDescription);
        // If permission has not been obtained, then try the permission again and wait 2 seconds to see if permission can be obtained.
        if (permissionResultDescription == null || permissionResultDescription.equalsIgnoreCase("")) {
            permissionResultDescription = "";
        }
        if (permissionResult == 0) {
            PendingIntent pendingIntent =
                    TaskStackBuilder.create(this)
                            // add all of DetailsActivity's parents to the stack,
                            // followed by DetailsActivity itself
                            .addNextIntentWithParentStack(intentGroundStation)
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentIntent(pendingIntent);
            startActivity(intentGroundStation);
        } else {
            if (permissionResult != 0) {
                Toast.makeText(getApplicationContext(), "Unable to start ground station because DJI has not granted permission for this application: " + permissionResult + " : " + permissionResultDescription, Toast.LENGTH_SHORT).show();
            }
            if (!permissionResultDescription.equalsIgnoreCase("Check permission successful")) {
                Toast.makeText(getApplicationContext(), "Unable to start ground station because of permission string: " + permissionResult + " - " + permissionResultDescription, Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void InitializeDrone() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_Connect);
        String radioValue = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        if (radioValue.equalsIgnoreCase(getString(R.string.Phantom_3_Pro))) {
            droneModel = R.string.Phantom_3_Pro;
        } else if (radioValue.equalsIgnoreCase(getString(R.string.Inspire_1))) {
            droneModel = R.string.Inspire_1;
        } else if (radioValue.equalsIgnoreCase(getString(R.string.Phantom_3_Adv))) {
            droneModel = R.string.Phantom_3_Adv;
        } else if (radioValue.equalsIgnoreCase(getString(R.string.Matrice_100))) {
            droneModel = R.string.Matrice_100;
        }
        switch (droneModel) {
            case R.string.Phantom_3_Pro: {
                DJIDrone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Phantom3_Professional);
                // The SDK initiation for Phantom 3 professional
                break;
            }
            case R.string.Inspire_1: {
                DJIDrone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Inspire1);
                // The SDK initiation for Phantom 3 inspire
                break;
            }
            case R.string.Phantom_3_Adv: {
                DJIDrone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Phantom3_Advanced);
                // The SDK initiation for Phantom 3 Advanced
                break;
            }
            case R.string.Matrice_100: {
                DJIDrone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_M100);
                // The SDK initiation for Matrice 100.
                break;
            }
            default: {
                break;
            }

        }
    }

    private static boolean first = false;
    private Timer ExitTimer = new Timer();

    class ExitCleanTask extends TimerTask {

        @Override
        public void run() {

            Log.e("ExitCleanTask", "Run in!!!! ");
            first = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "onKeyDown KEYCODE_BACK");

            if (first) {
                first = false;
                finish();
            } else {
                first = true;
                Toast.makeText(ConnectToDroneActivity.this, getText(R.string.press_again_exit), Toast.LENGTH_SHORT).show();
                ExitTimer.schedule(new ExitCleanTask(), 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
