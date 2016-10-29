package de.maurice144.smartdmx.service.message.device;


import de.maurice144.smartdmx.dmxdevice.SmartDmxDeviceInfo;
import de.maurice144.smartdmx.dmxdevice.SmartDmxDeviceManager;
import de.maurice144.smartdmx.service.message.ServiceMessageHandler;

public class DeviceMessageHandler implements ServiceMessageHandler<DeviceMessage> {

    private final SmartDmxDeviceManager deviceManager;

    public DeviceMessageHandler(SmartDmxDeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @Override
    public void handleMessage(DeviceMessage message) {
        if(message instanceof ConnectDeviceMessage) {
            handleConnectMessage((ConnectDeviceMessage) message);
            return;
        }
        if(message instanceof DisconnectDeviceMessage) {
            handleDisconnectMessage();
        }
    }

    private void handleConnectMessage(ConnectDeviceMessage message) {
        SmartDmxDeviceInfo info = new SmartDmxDeviceInfo(message.getMacAddress(),message.getName());
        deviceManager.connect(info);
    }

    private void handleDisconnectMessage() {
        deviceManager.disconnect();
    }

}
