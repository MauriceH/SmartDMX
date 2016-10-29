package de.maurice144.smartdmx.service.message;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import java.util.HashMap;

import de.maurice144.smartdmx.service.message.device.ConnectDeviceMessage;

public class MessageSerializer {

    private static final String MESSAGE_TYPE_KEY = "MESSAGE_TYPE_KEY";
    public static final String MESSAGE_OBJECT = "MESSAGE_OBJECT";

    private static HashMap<String,Class<? extends MessageBase>> typeCache;

    public MessageSerializer() {
        initCache();
    }

    private static void initCache() {
        if(typeCache != null) {
            return;
        }
        typeCache = new HashMap<>();
        addCacheEntry(ConnectDeviceMessage.class);
    }

    private static void addCacheEntry(Class<? extends MessageBase> type) {
        typeCache.put(getTypeKey(type), type);
    }

    private static String getTypeKey(Class<? extends MessageBase> type) {
        return type.getSimpleName();
    }


    public void serializeToBundle(Bundle data, MessageBase message) {
        data.putString(MESSAGE_TYPE_KEY, getTypeKey(message.getClass()));
        data.putSerializable(MESSAGE_OBJECT,message);
    }

    @org.jetbrains.annotations.Contract("null -> null")
    private MessageBase deserializeFromBundle(Bundle data) {
        if(data == null) return null;
        if(!data.containsKey(MESSAGE_TYPE_KEY)) return null;
        if(!data.containsKey(MESSAGE_OBJECT)) return null;
        String typeKey = data.getString(MESSAGE_TYPE_KEY);
        Class<? extends MessageBase> aClass = typeCache.get(typeKey);
        return (MessageBase) data.getSerializable(MESSAGE_OBJECT);
    }

    public MessageBase deserializeMessage(Message msg) {
        Bundle data = msg.getData();
        return deserializeFromBundle(data);
    }

    public MessageBase deserializeIntent(Intent intent) {
        Bundle data = intent.getExtras();
        return deserializeFromBundle(data);
    }

}
