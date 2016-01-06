package com.ecolumbia.gsdemo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.MainController.DJIMainControllerSystemState;
import dji.sdk.api.MainController.DJIMainControllerTypeDef;
import dji.sdk.interfaces.DJIMcuErrorCallBack;
import dji.sdk.interfaces.DJIMcuUpdateStateCallBack;


public class MainControllerFragment extends android.app.Fragment {


    // screen objects
    TextView tvStatus;

    private final static int CONNECTED = 1;
    private final static int DISCONNECTED = 2;

    DJIMcuErrorCallBack mMcuErrorCallBack = null;

    public MainControllerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_controller, container, false);

        // Initialize screen objects
        tvStatus = (TextView) v.findViewById(R.id.tvStatus);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMcuErrorCallBack = new DJIMcuErrorCallBack() {
            @Override
            public void onError(DJIMainControllerTypeDef.DJIMcErrorType djiMcErrorType) {
                Message msg = handler.obtainMessage();
                Bundle b = new Bundle();
                switch(djiMcErrorType) {
                    case Mc_No_Error:
                        b.putString("message", "Connected with no error found");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Unknown_Error: // Mc_Unknown error is used as a proxy to determine if the drone is not turned on or in range.
                        b.putString("message", "Disconnected - returns unknown error");
                        msg.what = DISCONNECTED;
                        break;
                    case Mc_Compass_Error:
                        b.putString("message", "Connected - with compass error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Config_Error:
                        b.putString("message", "Connected - with config error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Compass_Calibration_Error:
                        b.putString("message", "Connected - with compass calibration error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Imu_Calibration_Error:r:
                        b.putString("message", "Connected - with IMU calibration error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Imu_Error:
                        b.putString("message", "Connected - with IMU error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Invalid_Battery_Communication_Error:
                        b.putString("message", "Connected - with invalid battery communication error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Invalid_Battery_Error:
                        b.putString("message", "Connected - with invalid battery error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Low_Battery_Error:
                        b.putString("message", "Connected - with low battery error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Pmu_Error:
                        b.putString("message", "Connected - with PMU error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Sensor_Error:
                        b.putString("message", "Connected - with sensor error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_SerialNum_Error:
                        b.putString("message", "Connected - with serial num error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Serious_Battery_Error:
                        b.putString("message", "Connected - with serious battery error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Transmitter_Calibration_Error:
                        b.putString("message", "Connected - with transmitter calibration error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_Transmitter_Error:
                        b.putString("message", "Connected - with transmitter error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_X1_Error:
                        b.putString("message", "Connected - with X1 error");
                        msg.what = CONNECTED;
                        break;
                    case Mc_X2_Error:
                        b.putString("message", "Connected - with X2 error");
                        msg.what = CONNECTED;
                        break;
                    default:
                        b.putString("message", "Connected with Error: " + djiMcErrorType.toString());
                        msg.what = CONNECTED;
                        break;

                }
                msg.setData(b);
                handler.sendMessage(msg);

            }
        };



        DJIDrone.getDjiMainController().startUpdateTimer(2000);
        DJIDrone.getDjiMainController().setMcuErrorCallBack(mMcuErrorCallBack);

    }

    @Override
    public void onResume() {
        super.onResume();
        DJIDrone.getDjiMainController().startUpdateTimer(2000);
        DJIDrone.getDjiMainController().setMcuErrorCallBack(mMcuErrorCallBack);
    }

    @Override
    public void onPause() {
        super.onPause();
        DJIDrone.getDjiMainController().stopUpdateTimer();
        DJIDrone.getDjiMainController().setMcuErrorCallBack(null);
    }

    static class mcuErrorHandler extends Handler {
        WeakReference<MainControllerFragment> mFrag;

        mcuErrorHandler(MainControllerFragment aFragment) {
            mFrag = new WeakReference<MainControllerFragment>(aFragment);
        }

        @Override
        public void handleMessage(Message message) {
            MainControllerFragment theFrag = mFrag.get();
            switch (message.what) {
                case CONNECTED:
                    theFrag.tvStatus.setText(message.getData().getString("message"));
                    break;
                case DISCONNECTED:
                    theFrag.tvStatus.setText(message.getData().getString("message"));
                    break;
            }

        }
    }

    mcuErrorHandler handler = new mcuErrorHandler(this);


}
