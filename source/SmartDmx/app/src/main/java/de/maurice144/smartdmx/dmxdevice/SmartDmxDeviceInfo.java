package de.maurice144.smartdmx.dmxdevice;


public class SmartDmxDeviceInfo {

    private final String macAddress;
    private final String name;

    public SmartDmxDeviceInfo(String macAddress, String name) {
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
