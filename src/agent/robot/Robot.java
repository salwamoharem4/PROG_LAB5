
package agent.robot;

import agent.BaseAgent;
import core.Location;
import core.Skill;
import mission.Mission;

import java.util.Set;

public abstract class Robot extends BaseAgent {

    public static final int MAX_BATTERY = 100;
    private final Set<Skill> skills;
    private int batteryLevel;
    private boolean damaged = false;

    protected Robot(int id, String name, Location location, Set<Skill> skills, int batteryLevel) {
        super(id, name, location);
        this.skills = skills;

        checkBattery(batteryLevel);//checks whether the battery level is valid before storing it.
        this.batteryLevel = batteryLevel;
    }

    private static void checkBattery(int level) {
        if (level < 0 || level > MAX_BATTERY) {
            throw new IllegalArgumentException("Invalid battery level");
        }
    }

    @Override
    public Set<Skill> getSkills() {
        return skills;
    }

    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public boolean isEnoughBatteryLevel(int required) {
        checkBattery(required);
        return batteryLevel >= required;
    }


// Decrease battery level.
public void drainBattery(int amount) {
    if (amount < 0) {
        throw new IllegalArgumentException("Amount must be non-negative");
    }

    if (amount > batteryLevel) {
        throw new IllegalArgumentException("Not enough battery");
    }

    batteryLevel = batteryLevel - amount;
}


    // Recharge the battery without going above 100.
    public void recharge(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        batteryLevel = Math.min(MAX_BATTERY, batteryLevel + amount);//Math.min so if the amount excceds MAX_BATTERY(100) it chooses the min which is 100
    }

    // Mark the robot as damaged.
    public void breakDown() {
        damaged = true;
    }

    // Mark the robot as repaired.
    public void repair() {
        damaged = false;
    }

    @Override
    public boolean isUsable() {
        return !damaged && batteryLevel > 0;
    }

    // The robot must have enough battery for the mission.
    @Override
    protected boolean meetsMissionConditions(Mission mission) {
        return isEnoughBatteryLevel(mission.getRequiredBattery());
    }

    public boolean isRobot(){

        return true;
    }



}
