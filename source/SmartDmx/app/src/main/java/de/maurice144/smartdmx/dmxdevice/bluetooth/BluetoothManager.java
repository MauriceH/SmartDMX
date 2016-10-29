package de.maurice144.smartdmx.dmxdevice.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

public class BluetoothManager {

    private final BluetoothAdapter adapter;
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothManager() {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothConnectionResult createConnection(String macAddress) throws IOException {
        if(adapter == null) return new BluetoothConnectionResult(BluetoothConnectionResult.FAIL_NOBLUETOOTH);
        if(!adapter.isEnabled()) return new BluetoothConnectionResult(BluetoothConnectionResult.FAIL_BLUETOOTHDISABLED);
        adapter.cancelDiscovery();
        BluetoothDevice remote_device = adapter.getRemoteDevice(macAddress);
        BluetoothSocket socket = remote_device.createInsecureRfcommSocketToServiceRecord(uuid);
        BluetoothConnection connection = new BluetoothConnection(socket);
        connection.connect();
        return new BluetoothConnectionResult(connection);
    }

}
