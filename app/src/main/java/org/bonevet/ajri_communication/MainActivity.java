package org.bonevet.ajri_communication;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    Button btRelays;
    Physicaloid mPhysicaloid;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btRelays = (Button) findViewById(R.id.btRelays);
        mPhysicaloid = new Physicaloid(this);
        mPhysicaloid.setBaudrate(9600);
        btRelays.setTag(1);
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);
        setEnabledUi(false);
        if (!mPhysicaloid.isOpened()) {
            if (mPhysicaloid.open()) {
                setEnabledUi(true);
                btRelays.setClickable(true);
                btRelays.setText("Ndalur");
            }
        }
    }

    public void onClickSwitch(View v) {
        final int status = (Integer) v.getTag();
        if (status == 1) {
            btRelays.setText("Kontakt");
            btRelays.setBackgroundResource(R.drawable.round2);
            String str = "1";
            byte[] buf = str.getBytes();
            mPhysicaloid.write(buf, buf.length);
            v.setTag(2);
            btRelays.setClickable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btRelays.setClickable(true);
                }
            },1700);

        } else if (status == 2) {
            btRelays.setText("Ndezur");
            btRelays.setBackgroundResource(R.drawable.round3);
            String str = "2";
            byte[] buf = str.getBytes();
            mPhysicaloid.write(buf, buf.length);
            v.setTag(3);
            btRelays.setClickable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btRelays.setClickable(true);
                }
            },1700);
        } else {
            btRelays.setText("Ndalur");
            btRelays.setBackgroundResource(R.drawable.round1);
            String str = "0";
            byte[] buf = str.getBytes();
            mPhysicaloid.write(buf, buf.length);
            v.setTag(1);
            btRelays.setClickable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btRelays.setClickable(true);
                }
            },1700);
        }
    }

    private void setEnabledUi(boolean on) {
        if (on) {
            btRelays.setEnabled(true);
        } else {
            btRelays.setClickable(false);
            btRelays.setBackgroundResource(R.drawable.round1);
            btRelays.setText("Lidhu");

        }
    }

    private void closeDevice() {
        if(mPhysicaloid.close()) {
            setEnabledUi(false);
            mPhysicaloid.clearReadListener();
        }
    }

    private void openDevice() {
        if (!mPhysicaloid.isOpened()) {
            if (mPhysicaloid.open()) { // default 9600bps
                setEnabledUi(true);
                btRelays.setClickable(true);
            }

        }
        else {
            setEnabledUi(false);
            btRelays.setClickable(false);
            btRelays.setBackgroundResource(R.drawable.round1);
            btRelays.setText("Lidhu");
        }
    }

    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();

        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
            openDevice();
            setEnabledUi(true);
            btRelays.setClickable(true);
            btRelays.setText("Ndalur");

        }
    }

    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                closeDevice();
                mPhysicaloid.clearReadListener();
                setEnabledUi(false);
                btRelays.setText("Lidhu");
                btRelays.setBackgroundResource(R.drawable.round1);
            }
        }
    };
}
