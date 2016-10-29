package de.maurice144.smartdmx.dmxdevice;


import java.io.IOException;

import de.maurice144.smartdmx.dmxdevice.bluetooth.BluetoothConnectionResult;
import de.maurice144.smartdmx.dmxdevice.bluetooth.BluetoothManager;

public class SmartDmxDeviceManager {

    public static final int STATUS_NOTCONNECTED = 0;
    public static final int STATUS_CONNECTIONFAILED = 1;
    public static final int STATUS_CONNECTED = 2;
    public static final int STATUS_CONNECTIONLOST = 3;

    public static final int FAIL_NO_FAIL = 0;
    public static final int FAIL_NO_BT = 1;
    public static final int FAIL_BT_DISABLED = 2;
    public static final int FAIL_TIMEOUT = 3;
    public static final int FAIL_UNKNOWN = 4;



    private static final Object lockObj = new Object();

    private final BluetoothManager bluetoothManager;
    private SmartDmxDevice device;
    private SmartDmxDeviceInfo deviceInfo;
    private int status;
    private int failstatus;

    private DeviceManagerStatusChangedHandler statusHandler;

    public SmartDmxDeviceManager(){
        status = STATUS_NOTCONNECTED;
        bluetoothManager = new BluetoothManager();
    }

    public void setStatusHandler(DeviceManagerStatusChangedHandler statusHandler) {
        this.statusHandler = statusHandler;
    }

    private void changeStatus(int status, int failStatus) {
        synchronized (lockObj) {
            this.status = status;
            this.failstatus = failStatus;
        }
        if(statusHandler != null) {
            statusHandler.onStatusChanged(status);
        }
    }


    public void connect(SmartDmxDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        try {
            BluetoothConnectionResult connection = bluetoothManager.createConnection(deviceInfo.getMacAddress());
            if(connection.isFailed()) {
                changeStatus(STATUS_CONNECTIONFAILED,connection.getFailReason());
                return;
            }
            synchronized (lockObj) {
                device = new SmartDmxDevice(deviceInfo, connection.getConnection(), new ConnectionLostHandler() {
                    @Override
                    public void onConnectionLost(SmartDmxDevice device) {
                        SmartDmxDeviceManager.this.onConnectionLost(device);
                    }
                });
            }
            changeStatus(STATUS_CONNECTED,FAIL_NO_FAIL);
        } catch (IOException e) {
            e.printStackTrace();
            changeStatus(STATUS_CONNECTIONFAILED, FAIL_TIMEOUT);
        }
    }

    public void disconnect() {
        try {
            if(device != null) {
                device.close();
                device = null;
            }
            changeStatus(STATUS_NOTCONNECTED,FAIL_NO_FAIL);
        } catch (Exception ignored) {
        }
    }

    private void onConnectionLost(SmartDmxDevice device) {
        synchronized (lockObj) {
            if(!this.device.equals(device)) {
                return;
            }
            changeStatus(STATUS_CONNECTIONLOST,FAIL_NO_FAIL);
        }
    }

    public int getFailstatus() {
        return failstatus;
    }

    public int getStatus() {
        return status;
    }

    public SmartDmxDevice getDevice() {
        return device;
    }

    public SmartDmxDeviceInfo getDeviceInfo() {
        return deviceInfo;
    }
}
