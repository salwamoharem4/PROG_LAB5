package agent.robot;

public class Drone extends Robot {
    private boolean communicationAvailable;
    private boolean cameraAvailable;
    public Drone(String id, String name, String currentLocation, boolean available, boolean damaged, int batteryLevel,
            boolean communicationAvailable, boolean cameraAvailable) {
        super(id, name, currentLocation, available, damaged, batteryLevel);
        this.communicationAvailable = communicationAvailable;
        this.cameraAvailable = cameraAvailable;
    }
    public boolean isCommunicationAvailable() {
        return communicationAvailable;
    }
    public void setCommunicationAvailable(boolean communicationAvailable) {
        this.communicationAvailable = communicationAvailable;
    }
    public boolean isCameraAvailable() {
        return cameraAvailable;
    }
    public void setCameraAvailable(boolean cameraAvailable) {
        this.cameraAvailable = cameraAvailable;
    }
    
    public boolean canPerformAerialMission(int requiredBattery) {
        if (!isAvailable() || isDamaged()) {
            return false;
        }
        if (!isEnoughBatteryLevel(requiredBattery)) {
            return false;
        }
        if (!communicationAvailable || !cameraAvailable) {
            return false;
        }
        return true;
    }
}
