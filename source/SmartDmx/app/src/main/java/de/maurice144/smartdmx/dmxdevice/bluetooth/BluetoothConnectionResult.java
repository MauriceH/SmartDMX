package de.maurice144.smartdmx.dmxdevice.bluetooth;


public class BluetoothConnectionResult {

    public static final int FAIL_NOFAIL = 0;
    public static final int FAIL_NOBLUETOOTH = 1;
    public static final int FAIL_BLUETOOTHDISABLED = 2;



    private final boolean failed;
    private final int failReason;
    private final BluetoothConnection connection;

    public BluetoothConnectionResult(BluetoothConnection connection) {
        this.connection = connection;
        failed = false;
        failReason = FAIL_NOFAIL;
    }

    public BluetoothConnectionResult(int failReason) {
        failed = true;
        this.failReason = failReason;
        connection = null;
    }

    public boolean isFailed() {
        return failed;
    }

    public int getFailReason() {
        return failReason;
    }

    public BluetoothConnection getConnection() {
        return connection;
    }
}
