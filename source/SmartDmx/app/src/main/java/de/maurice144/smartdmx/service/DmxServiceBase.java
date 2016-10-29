package de.maurice144.smartdmx.service;


import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import de.maurice144.smartdmx.service.message.MessageBase;
import de.maurice144.smartdmx.service.message.MessageLooperHandler;
import de.maurice144.smartdmx.service.message.MessageSerializer;

public abstract class DmxServiceBase extends Service{

    public static final String TAG = "DmxServiceBase";

    private Looper commandLooper;
    private ServiceHandler serviceHandler;
    private MessageSerializer messageSerializer;


    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("SmartDMXServiceHandler",Thread.NORM_PRIORITY);
        thread.start();
        messageSerializer = new MessageSerializer();
        commandLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(commandLooper);
        Log.d(TAG,"Service created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String additionalLogInfo = " ";
        if(intent != null) {
            String action = intent.getAction();
            if(action != null) {
                additionalLogInfo += "Action: " + action + "; ";
            } else {
                additionalLogInfo += "NO ACTION";
            }
            MessageBase messageBase = messageSerializer.deserializeIntent(intent);
            if(messageBase != null) {
                serviceHandler.sendMessage(messageBase);
            }
        } else {
            additionalLogInfo += "NO INTENT";
        }
        Log.d(TAG,"Start command" + additionalLogInfo);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"Component binded");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.w(TAG,"Service destroyed!");
        super.onDestroy();
    }


    private final class ServiceHandler extends MessageLooperHandler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(MessageBase message) {
            onMessage(message);
        }
    }

    abstract void onMessage(MessageBase message);

}
