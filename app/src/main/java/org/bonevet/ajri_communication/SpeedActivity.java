package org.bonevet.ajri_communication;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.physicaloid.lib.Physicaloid;

public class SpeedActivity extends AppCompatActivity {

    TextView vl_speed, vl_rpm;
    Physicaloid mPhysicaloid;
    String ard_read;
    String[] vlerat;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
        vl_speed = (TextView) findViewById(R.id.vl_speed);
        vl_rpm = (TextView) findViewById(R.id.vl_rpm);
        speed();
        rpm();
    }

    private void speed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vl_speed.setText("Hello");
            }
        }, 1000);
    }

    private void rpm() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vl_rpm.setText("Hello");
            }
        }, 1000);
    }
}
