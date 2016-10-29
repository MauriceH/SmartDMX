package de.maurice144.smartdmx.dmxdevice.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothConnection {

    private static final String LOG_TAG = "Bluetooth";

    private final Object lockObj = new Object();
    private final BluetoothSocket socket;

    private OutputStream stream_out = null;
    private InputStream stream_in = null;

    private OnBluetoothConnectionLostHandler bluetoothConnectionLostHandler;


    BluetoothConnection(BluetoothSocket socket) {
        this.socket = socket;

    }

    public void setOnBluetoothConnectionLostHandler(OnBluetoothConnectionLostHandler bluetoothConnectionLostHandler) {
        this.bluetoothConnectionLostHandler = bluetoothConnectionLostHandler;
    }

    public void connect() throws IOException {
        try {
            synchronized (lockObj) {
                socket.connect();
                if(socket.isConnected()) {
                    initStreams();
                }
            }
            if(socket.isConnected()) {
                initConnectionWatcher();
            }
        } catch (IOException e) {
            connectionLost();
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        synchronized (lockObj) {
            bluetoothConnectionLostHandler = null;
            connectionLost();
        }
    }

    private void connectionLost() {
        synchronized (lockObj) {
            if(stream_out != null) {
                try {
                    stream_out.close();
                } catch (IOException ignore) {
                }
            }
            stream_out = null;
            if(stream_in != null) {
                try {
                    stream_in.close();
                } catch (IOException ignore) {
                }
            }
            stream_in = null;
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException ignore) {
                }
            }
        }
        if(bluetoothConnectionLostHandler != null) {
            OnBluetoothConnectionLostHandler handler = bluetoothConnectionLostHandler;
            bluetoothConnectionLostHandler = null;
            handler.ConnectionLost(this);
        }
    }

    private void initStreams() throws IOException {
        try {
            stream_out = socket.getOutputStream();
            stream_in = socket.getInputStream();
            Log.d(LOG_TAG, "Streams created");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Stream creation error!", e);
            throw e;
        }
    }


    public void send(byte[] data) throws IOException {
        try {
            stream_out.write(data);
            stream_out.flush();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while sending data", e);
            connectionLost();
        }
    }


    private void initConnectionWatcher() throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Runnable runnable = new Runnable() {
                    final Handler handler = new Handler();
                    public void run() {
                        try {
                            Log.e(LOG_TAG,Thread.currentThread().getName());
                            int read = stream_in.read();
                            if (read == 0) {
                                Log.d(LOG_TAG, "Watcher detects: connection closed.");
                                return;
                            }
                        } catch (IOException e) {
                            Log.w(LOG_TAG, "Watcher detects: connection lost!");
                            connectionLost();
                            return;
                        }
                        handler.postDelayed(this, 100);
                    }
                };
                Log.e(LOG_TAG,Thread.currentThread().getName());
                runnable.run();
            }
        });
        thread.setName("Reader");
        thread.start();
    }



    public boolean isConnected() {
        return socket.isConnected();
    }
}
