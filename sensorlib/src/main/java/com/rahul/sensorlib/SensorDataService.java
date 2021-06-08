package com.rahul.sensorlib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.rahul.sensorserver.IRemoteServiceCallback;
import com.rahul.sensorserver.ISensorService;

public class SensorDataService {
    private static final String TAG = "SensorConnection";

    ISensorService sensorService;
    SensorConnection sensorConnection;

    static SensorDataService sensorDataService;// = SensorDataService();
    private ISensorDataCallback iSensorDataCallback;

    public static SensorDataService getInstance() {
        if (sensorDataService == null) {
            sensorDataService = new SensorDataService();
        }
        return sensorDataService;
    }

    IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
        @Override
        public void valueChanged(float[] value) throws RemoteException {
            Log.e(TAG, "valueChangedvalueChanged");
            Log.e(TAG, value + "");
            if (iSensorDataCallback != null) {
                iSensorDataCallback.onValueUpdate(value);
            }
        }
    };

    public void registerSensor(Activity activity) {
        sensorConnection = new SensorConnection();
        Intent intent = new Intent("com.rahul.sensorserver.ISensorService");
        intent.setPackage("com.rahul.sensorserver");
        intent.putExtra("version", "1.0");
        activity.bindService(intent, sensorConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Activity activity) {
        if (sensorService != null && mCallback != null) {
            try {
                sensorService.unRegisterCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        activity.unbindService(sensorConnection);
    }

    public void setListener(ISensorDataCallback iSensorDataCallback) {
        this.iSensorDataCallback = iSensorDataCallback;
    }

    private class SensorConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            sensorService = ISensorService.Stub.asInterface(iBinder);
            Log.d(TAG, "onServiceConnected");
            try {
                Log.d(TAG, "registerCallback");
                sensorService.registerCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sensorService = null;
            Log.d(TAG, "onServiceDisconnected");
        }
    }
}
