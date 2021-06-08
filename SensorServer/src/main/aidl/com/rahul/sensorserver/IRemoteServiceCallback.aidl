package com.rahul.sensorserver;

 interface IRemoteServiceCallback {
/**
 * Called when the service has a new value for you.
 */
 void valueChanged(inout  float[] value);
}
