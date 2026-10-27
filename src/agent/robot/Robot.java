package agent.robot;

import agent.Agent;

public abstract class Robot extends Agent{
    private int batteryLevel;
    private boolean damaged;
    public Robot(String id, String name, String currentLocation, boolean available, boolean damaged, int batteryLevel) {
         super(id, name, currentLocation, available);      
        if (batteryLevel < 0 || batteryLevel > 100) {
            throw new IllegalArgumentException("Invalid battery Level");
        }
        this.batteryLevel = batteryLevel;
        this.damaged = damaged;
    }
    public int getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(int batteryLevel) {
         if (batteryLevel < 0 || batteryLevel > 100) {
            throw new IllegalArgumentException("Invalid battery Level");
        }
        this.batteryLevel = batteryLevel;
    }
    public boolean isDamaged() {
        return damaged;
    }
    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }
    public boolean isEnoughBatteryLevel(int batteryLevel){
        if (batteryLevel < 0 || batteryLevel > 100) {
            throw new IllegalArgumentException("Invalid battery Level");
        }
        if (this.batteryLevel<batteryLevel){
            return false;
        }
        return true;
    }
}
