package de.maurice144.smartdmx.service.message.device;


import de.maurice144.smartdmx.service.message.MessageBase;

public class ConnectDeviceMessage extends MessageBase implements DeviceMessage {

    private final String macAddress;
    private final String name;

    public ConnectDeviceMessage(String macAddress, String name) {
        this.macAddress = macAddress;
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }
}
