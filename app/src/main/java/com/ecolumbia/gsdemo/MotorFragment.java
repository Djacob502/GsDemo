package com.ecolumbia.gsdemo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIError;
import dji.sdk.interfaces.DJIExecuteResultCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class MotorFragment extends Fragment {


    private TextView tvMotorOn;
    private TextView tvMotorOff;
    public MotorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_motor, container, false);
        tvMotorOn = (TextView) v.findViewById(R.id.tvMotorOn);
        tvMotorOff = (TextView) v.findViewById(R.id.tvMotorOff);

        Button btnMotorOn = (Button) v.findViewById(R.id.btnMotorOn);
        Button btnMotorOff = (Button) v.findViewById(R.id.btnMotorOff);
        btnMotorOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorOn(v);
            }
        });
        btnMotorOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorOff(v);
            }
        });


        return v;
    }

    private void motorOn(View view){
        // Call the API
        DJIDrone.getDjiMainController().turnOnMotor(new DJIExecuteResultCallback() {
            @Override
            public void onResult(DJIError djiError) {
                Message msg = handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("message", "Motor On: " + djiError.errorCode + " - " + djiError.errorDescription);
                msg.what = 1;
                msg.setData(b);
                handler.sendMessage(msg);
            }
        });

    }

    private void motorOff(View view){
        // Call the API
        DJIDrone.getDjiMainController().turnOffMotor(new DJIExecuteResultCallback() {
            @Override
            public void onResult(DJIError djiError) {
                Message msg = handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("message", "Motor Off: " + djiError.errorCode + " - " + djiError.errorDescription);
                msg.what = 2;
                msg.setData(b);
                handler.sendMessage(msg);
            }
        });

    }

    static class MyInnerHandler extends Handler {
        WeakReference<MotorFragment> mFrag;

        MyInnerHandler(MotorFragment aFragment) {
            mFrag = new WeakReference<MotorFragment>(aFragment);
        }

        @Override
        public void handleMessage(Message message) {
            MotorFragment theFrag = mFrag.get();
            switch (message.what) {
                case 1:
                    theFrag.tvMotorOn.setText(message.getData().getString("message"));
                    theFrag.tvMotorOff.setText("");
                    break;
                case 2:
                    theFrag.tvMotorOff.setText(message.getData().getString("message"));
                    theFrag.tvMotorOn.setText("");
                    break;
            }//end switch
        }
    }
    MyInnerHandler handler = new MyInnerHandler(this);

}
