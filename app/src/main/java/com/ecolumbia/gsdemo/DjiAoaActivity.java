package com.ecolumbia.gsdemo;

import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;
import dji.midware.usb.P3.UsbAccessoryService;

public class DjiAoaActivity extends AppCompatActivity {

    private static boolean isStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new View(this));

        if (isStarted) {

        }else {

            isStarted = true;
            ServiceManager.getInstance();
            UsbAccessoryService.registerAoaReceiver(this);
            Intent intent = new Intent(DjiAoaActivity.this, ConnectToDroneActivity.class);
            startActivity(intent);

        }

        Intent aoaIntent = getIntent();
        if (aoaIntent!=null) {
            String action = aoaIntent.getAction();
            if (action== UsbManager.ACTION_USB_ACCESSORY_ATTACHED ||
                    action==Intent.ACTION_MAIN) {
                Intent attachedIntent=new Intent();
                attachedIntent.setAction(DJIUsbAccessoryReceiver.ACTION_USB_ACCESSORY_ATTACHED);
                sendBroadcast(attachedIntent);
            }
        }

        finish();
    }

}
