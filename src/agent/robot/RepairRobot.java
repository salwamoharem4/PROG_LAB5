package agent.robot;

public class RepairRobot extends Robot {
    private boolean hasSpareParts;

   

    public RepairRobot(String id, String name, String currentLocation, boolean available, boolean damaged,
            int batteryLevel, boolean hasSpareParts) {
        super(id, name, currentLocation, available, damaged, batteryLevel);
        this.hasSpareParts = hasSpareParts;
    }

    public boolean isHasSpareParts() {
        return hasSpareParts;
    }

    public void setHasSpareParts(boolean hasSpareParts) {
        this.hasSpareParts = hasSpareParts;
    }

    public boolean canRepair(int requiredBattery) {
        if (!isAvailable() || isDamaged())
            return false;
        if (!isEnoughBatteryLevel(requiredBattery))
            return false;
        if (!hasSpareParts)
            return false;
        return true;
    }
    
}
