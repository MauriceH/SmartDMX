package de.maurice144.smartdmx.service;


import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import de.maurice144.smartdmx.MainActivity;
import de.maurice144.smartdmx.R;
import de.maurice144.smartdmx.dmxdevice.DeviceManagerStatusChangedHandler;
import de.maurice144.smartdmx.dmxdevice.SmartDmxDeviceManager;
import de.maurice144.smartdmx.service.message.MessageBase;
import de.maurice144.smartdmx.service.message.device.DeviceMessage;
import de.maurice144.smartdmx.service.message.device.DeviceMessageHandler;


public class DmxService extends DmxServiceBase {

    public static final int NOTIFICATION_ID = 1;

    private SmartDmxDeviceManager deviceManager;
    private DeviceMessageHandler deviceMessageHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        deviceManager = new SmartDmxDeviceManager();
        deviceMessageHandler = new DeviceMessageHandler(deviceManager);

        deviceManager.setStatusHandler(new DeviceManagerStatusChangedHandler() {
            @Override
            public void onStatusChanged(int status) {
                statusChanged(status);
            }
        });
    }

    @Override
    void onMessage(MessageBase message) {
        Log.e(TAG,message.toString());
        if(message instanceof DeviceMessage) {
            deviceMessageHandler.handleMessage((DeviceMessage) message);
        }
    }

    private void statusChanged(int status) {
        Log.w(TAG,"CONNECTION STATE CHANGED: " + String.valueOf(status));
        if(status == SmartDmxDeviceManager.STATUS_CONNECTED) {
            onConnected();
        } else if(status == SmartDmxDeviceManager.STATUS_CONNECTIONLOST) {
            onConnectionLost();
        } else if(status == SmartDmxDeviceManager.STATUS_NOTCONNECTED) {
            stopForeground(true);
        } else {
            onConnectionFailed();
        }
    }

    private void onConnected() {
        NotificationCompat.Builder builder = createNotification();
        builder.setContentText(String.format("Connected to %s",deviceManager.getDevice().getInfo().getName()));
        startForeground(NOTIFICATION_ID,builder.build());
    }

    private void onConnectionLost() {
        NotificationCompat.Builder builder = createNotification();
        builder.setContentText(String.format("Lost connection to %s",deviceManager.getDevice().getInfo().getName()));
        startForeground(NOTIFICATION_ID,builder.build());
    }

    private void onConnectionFailed() {
        NotificationCompat.Builder builder = createNotification();
        builder.setContentText(String.format("Connection failed to %s",deviceManager.getDevice().getInfo().getName()));
        startForeground(NOTIFICATION_ID,builder.build());
    }

    @NonNull
    private NotificationCompat.Builder createNotification() {
        PendingIntent resultPendingIntent = getActionIntent();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(resultPendingIntent);
        builder.setSmallIcon(getNotificationIcon());
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.deficon));
        builder.setContentTitle(getString(R.string.app_name));
        return builder;
    }

    private PendingIntent getActionIntent() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.deficon : R.drawable.deficon;
    }



}
