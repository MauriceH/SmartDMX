package de.maurice144.smartdmx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import de.maurice144.smartdmx.service.DmxServiceController;
import de.maurice144.smartdmx.service.message.device.ConnectDeviceMessage;
import de.maurice144.smartdmx.service.message.device.DisconnectDeviceMessage;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "FRAGDUINO";

    private boolean lampState = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "Bluetest: OnCreate");


        Button connectButton = (Button) findViewById(R.id.bt_connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
            }
        });

        Button disconnectButton = (Button) findViewById(R.id.bt_disconnect);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
            }
        });

        DmxServiceController.startService(this);

        Button sendButton = (Button) findViewById(R.id.bt_senden);
        sendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    sendLamp(lampState);
                    lampState = !lampState;
                }
                return false;
            }
        });



    }

    public void connect() {
        DmxServiceController.sendMessage(this,new ConnectDeviceMessage("98:D3:32:70:A2:F2", "SmartDmx1"));
    }

    public void sendLamp(boolean down) {


    }


    public void disconnect() {
        DmxServiceController.sendMessage(this,new DisconnectDeviceMessage());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy. Trenne Verbindung, falls vorhanden");
    }

}