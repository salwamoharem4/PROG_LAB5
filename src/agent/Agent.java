package agent;

import core.Capability;
import core.EmergencyType;
import core.Location;
import mission.Mission;
import core.Skill;
import java.util.Collections;
import java.util.Set;

public interface Agent {

    int getId();

    String getName();

    Location getLocation();

    // Move the agent to a location.
    void moveTo(Location location);

    // Check whether the agent is in usable condition.
    boolean isUsable();

    // Available means not assigned to an active mission and usable.
    boolean isAvailable();

    Set<Capability> getCapabilities();//is set to prevent duplicate capabilities.

    // Skills possessed by this agent.
    default Set<Skill> getSkills() {
        return Collections.emptySet();
    }

    // Current battery level (0-100).
    default int getBatteryLevel() {
        return 0;
    }

    // Check whether the agent can perform a mission.
    boolean canPerform(Mission mission);

    // Check whether the agent can handle an emergency.
    boolean canHandle(EmergencyType type);

    // Assign the agent to a mission.
    void assign();

    // Release the agent when the mission ends.
    void release();




    boolean isHuman ();
    boolean isRobot();

    void setAvailability(boolean b);
}
