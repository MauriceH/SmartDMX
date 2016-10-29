package de.maurice144.smartdmx.dmxdevice;


import de.maurice144.smartdmx.dmxdevice.bluetooth.BluetoothConnection;
import de.maurice144.smartdmx.dmxdevice.bluetooth.OnBluetoothConnectionLostHandler;

public class SmartDmxDevice {

    private final SmartDmxDeviceInfo device;
    private final BluetoothConnection connection;
    private ConnectionLostHandler disconnectedHandler;


    public SmartDmxDevice(SmartDmxDeviceInfo device,BluetoothConnection connection, final ConnectionLostHandler disconnectedHandler) {
        this.device = device;
        this.connection = connection;
        this.disconnectedHandler = disconnectedHandler;
        connection.setOnBluetoothConnectionLostHandler(new OnBluetoothConnectionLostHandler() {
            @Override
            public void ConnectionLost(BluetoothConnection connection) {
                ConnectionLostHandler handler = SmartDmxDevice.this.disconnectedHandler;
                if(handler != null) {
                    handler.onConnectionLost(SmartDmxDevice.this);
                }
            }
        });
    }

    public void close() {
        if(connection != null) {
            connection.close();
        }
        disconnectedHandler = null;
    }

    public SmartDmxDeviceInfo getInfo() {
        return device;
    }
}
