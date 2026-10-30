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
import facilities.Equipment;
import mission.BaseMission;
import mission.MissionStatus;
import resource.ResourceType;

// Represents a repair mission for a damaged facility or piece of equipment.
// Requires agents with REPAIR capability (such as Engineer or RepairRobot) and spare parts.
public class RepairMission extends BaseMission {

    // Repair missions always require REPAIR capability.
    private static final Set<Capability> DEFAULT_CAPABILITIES =
            Collections.unmodifiableSet(EnumSet.of(Capability.REPAIR));

    // Full constructor.
    public RepairMission(int id, String name, int duration,
                         Set<Skill> requiredSkills, int requiredBattery,
                         Map<ResourceType, Integer> requiredResources,
                         Location targetLocation, List<Equipment> requiredEquipment) {
        super(id,
              name,
              MissionType.REPAIR,
              MissionStatus.PENDING,
              duration,
              0,
              null,
              DEFAULT_CAPABILITIES,
              requiredSkills != null ? requiredSkills : EnumSet.noneOf(Skill.class),
              requiredBattery,
              requiredResources != null ? requiredResources : new EnumMap<>(ResourceType.class),
              targetLocation,
              requiredEquipment != null ? requiredEquipment : new ArrayList<>(),
              new ArrayList<>());
    }

    // Convenience constructor specifying target location and spare parts needed.
    public RepairMission(int id, String name, int duration,
                         Location targetLocation, int sparePartsNeeded, int requiredBattery) {
        this(id,
             name,
             duration,
             EnumSet.noneOf(Skill.class),
             requiredBattery,
             createSparePartsMap(sparePartsNeeded),
             targetLocation,
             new ArrayList<>());
    }

    // Helper method to create a resource map with spare parts.
    private static Map<ResourceType, Integer> createSparePartsMap(int sparePartsCount) {
        Map<ResourceType, Integer> resources = new EnumMap<>(ResourceType.class);
        if (sparePartsCount > 0) {
            resources.put(ResourceType.SPARE_PARTS, sparePartsCount);
        }
        return resources;
    }
    // Checks whether all required equipment is operational.
    public boolean hasOperationalRequiredEquipment() {

        for (Equipment equipment : getRequiredEquipment()) {

            if (equipment == null || !equipment.isOperational()) {
                return false;
            }
        }

        return true;
    }

    // Returns a clear equipment failure reason.
    public String getEquipmentFailureReason() {

        for (Equipment equipment : getRequiredEquipment()) {

            if (equipment == null) {
                return "Required repair equipment is missing.";
            }

            if (!equipment.isOperational()) {
                return "Required repair equipment is not operational: "
                        + equipment.getName();
            }
        }

        return null;
    }
}
