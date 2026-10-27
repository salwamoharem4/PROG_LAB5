package agent.robot;

public class Rover extends Robot {

    private boolean cameraAvailable;
    private boolean scientificEquipment;

    public Rover(String id, String name, String currentLocation,
                 boolean available, boolean damaged, int batteryLevel,
                 boolean cameraAvailable, boolean scientificEquipment) {

        super(id, name, currentLocation, available, damaged, batteryLevel);

        this.cameraAvailable = cameraAvailable;
        this.scientificEquipment = scientificEquipment;
    }

    public boolean isCameraAvailable() {
        return cameraAvailable;
    }

    public void setCameraAvailable(boolean cameraAvailable) {
        this.cameraAvailable = cameraAvailable;
    }

    public boolean hasScientificEquipment() {
        return scientificEquipment;
    }

    public void setScientificEquipment(boolean scientificEquipment) {
        this.scientificEquipment = scientificEquipment;
    }

    public boolean canExplore(int requiredBattery) {
        if (!isAvailable() || isDamaged()) {
            return false;
        }

        if (!isEnoughBatteryLevel(requiredBattery)) {
            return false;
        }

        if (!cameraAvailable || !scientificEquipment) {
            return false;
        }

        return true;
    }
}