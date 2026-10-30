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

// Represents an exploration mission on the Martian surface.
public class ExplorationMission extends BaseMission {

    private static final Set<Capability> DEFAULT_CAPABILITIES =
            Collections.unmodifiableSet(EnumSet.of(Capability.EXPLORE));

    // Full constructor
    public ExplorationMission(int id, String name, int duration,
                              Set<Skill> requiredSkills, int requiredBattery,
                              Map<ResourceType, Integer> requiredResources,
                              Location targetLocation, List<Equipment> requiredEquipment) {
        super(id, name, MissionType.EXPLORATION, MissionStatus.PENDING,
                duration, 0, null,
                DEFAULT_CAPABILITIES,
                requiredSkills != null ? requiredSkills : EnumSet.noneOf(Skill.class),
                requiredBattery,
                requiredResources != null ? requiredResources : new EnumMap<>(ResourceType.class),
                targetLocation != null ? targetLocation : Surface.INSTANCE,
                requiredEquipment != null ? requiredEquipment : new ArrayList<>(),
                new ArrayList<>());
    }

    // Convenience constructor 1
    public ExplorationMission(int id, String name, int duration,
                              Skill scientificSkill, int requiredBattery,
                              List<Equipment> equipment) {
        this(id, name, duration,
                scientificSkill != null ? EnumSet.of(scientificSkill) : EnumSet.noneOf(Skill.class),
                requiredBattery, null, null, equipment);
    }

    // Convenience constructor 2
    public ExplorationMission(int id, String name, int duration) {
        this(id, name, duration, null, 30, null, null, null);
    }

    // Checks whether all required equipment is operational.
    public boolean hasOperationalRequiredEquipment() {
        return getEquipmentFailureReason() == null;
    }

    // Returns a clear explanation if equipment is not operational.
    public String getEquipmentFailureReason() {
        for (Equipment equipment : getRequiredEquipment()) {
            if (equipment == null) {
                return "Required equipment is missing.";
            }
            if (!equipment.isOperational()) {
                return "Required equipment is not operational: " + equipment.getName();
            }
        }
        return null;
    }
}