package agent.human;

import core.Capability;
import core.Location;
import core.Skill;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class MedicalOfficer extends Human {

    private static final Set<Capability> CAPABILITIES =
            Collections.unmodifiableSet(EnumSet.of(Capability.MEDICAL));

    public MedicalOfficer(int id, String name, Location location,
                          HealthStatus healthStatus, Set<Skill> skills, int fatigueLevel) {
        super(id, name, location, healthStatus, skills, fatigueLevel);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return CAPABILITIES;
    }
}
