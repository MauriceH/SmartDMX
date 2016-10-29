package de.maurice144.smartdmx.service;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import de.maurice144.smartdmx.service.message.MessageBase;
import de.maurice144.smartdmx.service.message.MessageSerializer;

public class DmxServiceController {


    public static void startService(Context context) {
        context.startService(new Intent(context,DmxService.class));
    }

    public static void sendMessage(Context context, MessageBase message) {
        Intent intent = new Intent(context, DmxService.class);
        MessageSerializer serializer = new MessageSerializer();
        Bundle data = new Bundle();
        serializer.serializeToBundle(data,message);
        intent.setAction("MSG:" + message.toString());
        intent.putExtras(data);
        context.startService(intent);
    }


}
