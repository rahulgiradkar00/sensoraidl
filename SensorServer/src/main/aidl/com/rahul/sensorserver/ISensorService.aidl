package com.rahul.sensorserver;


import com.rahul.sensorserver.IRemoteServiceCallback;

interface ISensorService {
  void registerCallback(IRemoteServiceCallback cb);
  void unRegisterCallback(IRemoteServiceCallback cb);
}