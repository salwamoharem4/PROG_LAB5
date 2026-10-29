package mission.missiontype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.Capability;
import core.Location;
import core.Skill;
import core.Surface;
import facilities.Equipment;
import mission.BaseMission;
import mission.MissionStatus;
import resource.ResourceType;

// Represents a rescue mission to locate and recover a stranded entity.
// Requires RESCUE capability (e.g. Rover), available personnel, and sufficient vehicle battery.
public class RescueMission extends BaseMission {

    // Rescue missions require RESCUE capability.
    private static final Set<Capability> DEFAULT_CAPABILITIES =
            Collections.unmodifiableSet(EnumSet.of(Capability.RESCUE));

    // Full constructor.
    public RescueMission(int id, String name, int duration,
                         Set<Skill> requiredSkills, int requiredBattery,
                         Map<ResourceType, Integer> requiredResources,
                         Location strandedLocation, List<Equipment> requiredEquipment) {
        super(id,
              name,
              MissionType.RESCUE,
              MissionStatus.PENDING,
              duration,
              0,
              null,
              DEFAULT_CAPABILITIES,
              requiredSkills != null ? requiredSkills : EnumSet.noneOf(Skill.class),
              requiredBattery,
              requiredResources != null ? requiredResources : new EnumMap<>(ResourceType.class),
              strandedLocation != null ? strandedLocation : Surface.INSTANCE,
              requiredEquipment != null ? requiredEquipment : new ArrayList<>(),
              new ArrayList<>());
    }

    // Convenience constructor for surface rescue missions.
    public RescueMission(int id, String name, int duration,
                         int requiredBattery, Location strandedLocation) {
        this(id,
             name,
             duration,
             EnumSet.noneOf(Skill.class),
             requiredBattery,
             new EnumMap<>(ResourceType.class),
             strandedLocation != null ? strandedLocation : Surface.INSTANCE,
             new ArrayList<>());
    }
}
