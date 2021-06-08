package com.rahul.sensorclient;

import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rahul.sensorlib.ISensorDataCallback;
import com.rahul.sensorlib.SensorDataService;

public class SensorClientActivity extends AppCompatActivity {

    private final String TAG = "SensorClientActivity";

    TextView txtSensorValue;
    private SensorDataService instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        txtSensorValue = (TextView) findViewById(R.id.txtSensorValue);

        this.instance = SensorDataService.getInstance();
        instance.registerSensor(this);
        instance.setListener(new ISensorDataCallback() {
            @Override
            public void onValueUpdate(float[] values) {
                String senosorValue = "X->    " + values[0] + "\nY->   " + values[1] + "\nZ->   " + values[2];
                runOnUiThread(() -> {
                    try {
                        txtSensorValue.setText(senosorValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.unbindService(this);
    }

}