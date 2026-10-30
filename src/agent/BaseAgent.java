
package agent;

import core.EmergencyType;
import core.Location;
import mission.Mission;

import java.util.Collections;

public abstract class BaseAgent implements Agent {

    private final int id;
    private final String name;
    private Location location;
    private boolean available = true;

    protected BaseAgent(int id, String name, Location location) {
        if (id <= 0) {
            throw new IllegalArgumentException("Agent ID must be positive.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Agent name cannot be empty.");
        }

        this.id = id;
        this.name = name;
        this.location = location;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailability(boolean available) {
        this.available = available;
    }

    @Override
    public final boolean canPerform(Mission mission) {

        if (mission == null || !isAvailable()) {
            return false;
        }

        if (Collections.disjoint(
                getCapabilities(),
                mission.getRequiredCapabilities())) {
            return false;
        }

        return meetsMissionConditions(mission);
    }

    protected boolean meetsMissionConditions(Mission mission) {

        if (getSkills() == null
                || !getSkills().containsAll(mission.getRequiredSkills())) {
            return false;
        }

        if (getBatteryLevel() < mission.getRequiredBattery()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canHandle(EmergencyType type) {

        return type != null
                && isAvailable()
                && getCapabilities().contains(type.getRequiredCapability());
    }
}
