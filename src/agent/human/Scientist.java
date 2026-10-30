package agent.human;

import core.Capability;
import core.Location;
import core.Skill;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class Scientist extends Human {
//static so the variable belong to this class 
//Now Every scientist has the same capabilities: RESEARCH and EXPLORE.
    private static final Set<Capability> CAPABILITIES =
            Collections.unmodifiableSet(EnumSet.of(Capability.RESEARCH, Capability.EXPLORE));
//EnumSet.of(Capability.RESEARCH, Capability.EXPLORE) creates a set contains explore and research,
// Collections.unmodifiableSet doesnt allow modification to the set for example we cant now remove ar add to the set
    public Scientist(int id, String name, Location location,
                     HealthStatus healthStatus, Set<Skill> skills, int fatigueLevel) {
        super(id, name, location, healthStatus, skills, fatigueLevel);
    }

    // Return the capabilities of a scientist.
    @Override
    public Set<Capability> getCapabilities() {
        return CAPABILITIES;
    }
}
