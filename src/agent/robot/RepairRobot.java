package agent.robot;

import core.Capability;
import core.Location;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class RepairRobot extends Robot {

    private static final Set<Capability> CAPABILITIES =
            Collections.unmodifiableSet(EnumSet.of(Capability.REPAIR,Capability.FIRE_RESPONSE));

    public RepairRobot(int id, String name, Location location, int batteryLevel) {
        super(id, name, location, Collections.emptySet(), batteryLevel);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return CAPABILITIES;
    }
}
