package de.maurice144.smartdmx.service.message;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class MessageLooperHandler {

    private final ServiceHandler handler;
    private final MessageSerializer messageSerializer;

    public MessageLooperHandler(Looper looper) {
        handler = new ServiceHandler(looper);
        messageSerializer = new MessageSerializer();
    }

    public void sendMessage(MessageBase message) {
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();
        messageSerializer.serializeToBundle(data, message);
        msg.setData(data);
        handler.sendMessage(msg);
    }


    private final class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            MessageBase message = messageSerializer.deserializeMessage(msg);
            MessageLooperHandler.this.handleMessage(message);
        }
    }

    public abstract void handleMessage(MessageBase message);



}
