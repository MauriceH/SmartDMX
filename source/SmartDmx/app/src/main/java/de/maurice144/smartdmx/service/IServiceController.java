package de.maurice144.smartdmx.service;


import android.os.IBinder;

public interface IServiceController extends IBinder {

    void sendChannelChanges();

}
