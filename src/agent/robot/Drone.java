package agent.robot;

import core.Capability;
import core.Location;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class Drone extends Robot {

    private static final Set<Capability> CAPABILITIES =
            Collections.unmodifiableSet(EnumSet.of(Capability.AERIAL_RECON));

    public Drone(int id, String name, Location location, int batteryLevel) {
        super(id, name, location, batteryLevel);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return CAPABILITIES;
    }
}
