
package agent;

import core.EmergencyType;
import core.Location;
import mission.Mission;

import java.util.Collections;

// Contains the common fields and methods for all agents.
public abstract class BaseAgent implements Agent {
/*In this design I used protected cuz to let only subclasses of BaseAgent access the required info,
since public let any class or subclass acess the required info,I used final to prevent Human or Robot 
from overriding the method and accidentally changing the availability rule */
    private final int id;
    private final String name;
    private Location location;
    private boolean available = true;


    protected BaseAgent(int id, String name, Location location) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (location == null) {
            throw new IllegalArgumentException("Invalid location");
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

    @Override
    public void moveTo(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Invalid location");
        }
        this.location = location;
    }

    // The agent is available if it is not on a mission and is usable.
    @Override
    public final boolean isAvailable() {
        return available && isUsable();
    }

    @Override
    public void assign() {
        if (!isAvailable()) {
            throw new IllegalStateException(name + " is not available");
        }
        available = false;
    }

    @Override
    public void release() {
        available = true;
    }

    // Checks if the agent can perform the mission.
    @Override//final prevents something from being changed or overridden
    public final boolean canPerform(Mission mission) {
        if (mission == null || !isAvailable()) {
            return false;
        }

        if (Collections.disjoint(getCapabilities(), mission.getRequiredCapabilities())) {
            return false;
        }

        return meetsMissionConditions(mission);
    }

    // Subclasses can add extra conditions for their missions.
    protected boolean meetsMissionConditions(Mission mission) {
        return true;
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type != null
                && isAvailable()
                && getCapabilities().contains(type.getRequiredCapability());
    }

    @Override
    //Prints the Info of the agent 
    public String toString() {
        return getClass().getSimpleName() + "[" + id + ", " + name + ", at " + location.getName() + "]";
    }


    public void setAvailability(boolean b){

        this.available=b;

    }

}
