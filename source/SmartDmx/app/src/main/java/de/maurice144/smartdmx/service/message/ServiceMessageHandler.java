package de.maurice144.smartdmx.service.message;


public interface ServiceMessageHandler<MessageType> {

    void handleMessage(MessageType message);

}
