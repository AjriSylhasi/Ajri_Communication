package org.bonevet.ajri_communication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.physicaloid.lib.Physicaloid;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    Button btOpen, btClose, btRelays, btRead;
    TextView vl_intensity, vl_voltage, vl_battery, vl_temperature, vl_range, vl_speed, vl_rpm;
    String ard_read;
    String[] vlerat;
    Physicaloid mPhysicaloid;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btRelays= (Button) findViewById(R.id.btRelays);
        btOpen  = (Button) findViewById(R.id.btOpen);
        btClose = (Button) findViewById(R.id.btClose);
        btRead = (Button) findViewById(R.id.btRead);
        vl_intensity = (TextView) findViewById(R.id.vl_intensity);
        vl_voltage = (TextView) findViewById(R.id.vl_voltage);
        vl_battery = (TextView) findViewById(R.id.vl_battery);
        vl_temperature = (TextView) findViewById(R.id.vl_temperature);
        vl_range = (TextView) findViewById(R.id.vl_range);
        vl_speed = (TextView) findViewById(R.id.vl_speed);
        vl_rpm = (TextView) findViewById(R.id. vl_rpm);
        setEnabledUi(false);
        mPhysicaloid = new Physicaloid(this);
        mPhysicaloid.setBaudrate(9600);
        btRelays.setTag(1);
        btRelays.setText("Lidhu");
        btRelays.setBackgroundResource(R.drawable.round1);


    }

    public void onClickOpen(View v) {
        if(mPhysicaloid.open()) {
            setEnabledUi(true);
            btRelays.setText("Ndalur");
        } else {
            btRelays.setText("Lidhu");
//            Toast.makeText(this, "Nuk Mund Te Lidhet", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickRead(View v){
        byte[] buf = new byte[256];
        int readSize=0;
        readSize = mPhysicaloid.read(buf);
        if(readSize>0) {
            try {
                ard_read = new String(buf, "UTF-8");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            vlerat = ard_read.split("-");
            vl_intensity.setText(vlerat[0]);
            vl_voltage.setText(vlerat[1]);
            vl_battery.setText(vlerat[2]);
            vl_temperature.setText(vlerat[3]);
            vl_range.setText(vlerat[4]);
            vl_speed.setText(vlerat[5]);
            vl_rpm.setText(vlerat[6]);
        }
        btRead.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btRead.setEnabled(true);
            }
        }, 1200);
    }

    public void onClickSwitch(View v) {
        final int status =(Integer) v.getTag();
        if(status == 1) {
            btRelays.setText("Kontakt");
            btRelays.setBackgroundResource(R.drawable.round2);
            String str = "1";
            byte[] buf = str.getBytes();
            mPhysicaloid.write(buf, buf.length);
            btRead.setEnabled(true);
            v.setTag(2);
        } else if (status ==2) {
            btRelays.setText("Ndezur");
            btRelays.setBackgroundResource(R.drawable.round3);
            String str = "2";
            byte[] buf = str.getBytes();
            mPhysicaloid.write(buf, buf.length);
            v.setTag(3);
            btRead.setEnabled(true);
        } else {
            btRelays.setText("Ndalur");
            btRelays.setBackgroundResource(R.drawable.round1);
            String str = "0";
            byte[] buf = str.getBytes();
            mPhysicaloid.write(buf, buf.length);
            v.setTag(1);
            btRead.setEnabled(false);
            vl_intensity.setText("NoN");
            vl_voltage.setText("NoN");
            vl_battery.setText("NoN");
            vl_temperature.setText("NoN");
            vl_range.setText("NoN");
            vl_speed.setText("NoN");
            vl_rpm.setText("NoN");
        }
    }
    public void onClickClose(View v) {
        if(mPhysicaloid.close()) {
            mPhysicaloid.clearReadListener();
            setEnabledUi(false);
            btRelays.setText("Lidhu");
            btRelays.setBackgroundResource(R.drawable.round1);
        }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false);

            btClose.setEnabled(true);

            btRelays.setEnabled(true);
        } else {
            btOpen.setEnabled(true);

            btClose.setEnabled(false);

            btRelays.setEnabled(false);

        }
    }

    Handler mHandler = new Handler();
    private void tvAppend(TextView tv, String text) {
        final TextView ftv = tv;
        ftv.setText("");
        final String ftext = text;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }
}
