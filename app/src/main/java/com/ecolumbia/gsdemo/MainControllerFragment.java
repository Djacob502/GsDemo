package com.ecolumbia.gsdemo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                if (djiMcErrorType == DJIMainControllerTypeDef.DJIMcErrorType.Mc_Unknown_Error) {
                    b.putString("message", "Disconnected");
                    msg.what = DISCONNECTED;
                } else {
                    b.putString("message", "Connected");
                    msg.what = CONNECTED;
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
