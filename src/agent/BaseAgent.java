package agent;

import core.EmergencyType;
import core.Location;
import mission.Mission;

import java.util.Collections;

// Contains the common fields and methods for all agents.
public abstract class BaseAgent implements Agent {

    private final int id;
    private final String name;
    private Location location;
    private boolean available = true;      // false while the agent is working on a mission

    protected BaseAgent(int id, String name, Location location) {
        if (id <= 0) {
            throw new IllegalArgumentException("Agent ID must be positive.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Agent name cannot be empty.");
        }

        if (location == null) {
            throw new IllegalArgumentException("Agent location cannot be null.");
        }

        this.id = id;
        this.name = name.trim();
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

    // Required by the Agent interface.
    @Override
    public void moveTo(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Invalid location");
        }
        this.location = location;
    }

    // Kept from your version, so existing calls to setLocation still work.
    public void setLocation(Location location) {
        moveTo(location);
    }

    // The agent is available if it is not on a mission and is usable.
    // final so Human or Robot cannot accidentally change the availability rule.
    @Override
    public final boolean isAvailable() {
        return available && isUsable();
    }

    // Kept from your version (used by emergency code to free responders).
    @Override
    public void setAvailability(boolean available) {
        this.available = available;
    }

    // Assign the agent to a mission.
    @Override
    public void assign() {
        if (!isAvailable()) {
            throw new IllegalStateException(name + " is not available");
        }
        available = false;
    }

    // Release the agent when the mission ends.
    @Override
    public void release() {
        available = true;
    }

    // Defaults: Human overrides isHuman() and Robot overrides isRobot().
    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public boolean isRobot() {
        return false;
    }

    // Checks if the agent can perform the mission.
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

    // Kept from your version (skills + battery), with one fix:
    // skills only apply to humans and battery only applies to robots.
    // Otherwise a robot (no skills) would fail every mission that needs a skill,
    // and a human (battery 0) would fail every mission that needs battery.
    protected boolean meetsMissionConditions(Mission mission) {
        if (isHuman()
                && (getSkills() == null
                || !getSkills().containsAll(mission.getRequiredSkills()))) {
            return false;
        }

        if (isRobot() && getBatteryLevel() < mission.getRequiredBattery()) {
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + ", " + name + ", at " + location.getName() + "]";
    }
}