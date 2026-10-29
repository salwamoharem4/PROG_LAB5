package agent.human;

import core.Capability;
import core.Location;
import core.Skill;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class Engineer extends Human {

    private static final Set<Capability> CAPABILITIES =
            Collections.unmodifiableSet(EnumSet.of(Capability.REPAIR, Capability.FIRE_RESPONSE));

    public Engineer(int id, String name, Location location,
                    HealthStatus healthStatus, Set<Skill> skills, int fatigueLevel) {
        super(id, name, location, healthStatus, skills, fatigueLevel);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return CAPABILITIES;
    }
}
