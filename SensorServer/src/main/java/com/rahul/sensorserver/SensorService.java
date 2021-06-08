package com.rahul.sensorserver;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class SensorService extends Service implements SensorEventListener {
    private final String TAG = SensorService.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private IRemoteServiceCallback cb;
    //As per given in mail 8ms
    int samplingPeriodUs = 8 * 1000;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate", "onCreate");
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        mSensorManager.registerListener(this, mAccelerometer, samplingPeriodUs);
    }

    @Override
    public IBinder onBind(Intent intent) {
        final String version = intent.getExtras().getString("version");

        return new ISensorService.Stub() {
            @Override
            public void registerCallback(IRemoteServiceCallback _cb) throws RemoteException {
                cb = _cb;
                Log.d(TAG, "IRemoteServiceCallback");
            }

            @Override
            public void unRegisterCallback(IRemoteServiceCallback _cb) throws RemoteException {
                cb = null;
            }
        };
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Log.e(TAG, event.toString());
        int type = event.sensor.getType();
        switch (type) {
            case Sensor.TYPE_ROTATION_VECTOR:
                /* Log.e(TAG, "event.toString()");*/
                if (cb != null) {
                    try {
                        cb.valueChanged(event.values);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e(TAG, sensor.toString() + " accuracy" + accuracy);
    }
}
