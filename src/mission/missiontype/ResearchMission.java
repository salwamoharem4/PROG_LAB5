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

// Represents a research mission executed by scientists inside a laboratory.
// Requires RESEARCH capability, specific science skills, equipment, and resources.
public class ResearchMission extends BaseMission {

    // Research missions require the RESEARCH capability.
    private static final Set<Capability> DEFAULT_CAPABILITIES =
            Collections.unmodifiableSet(EnumSet.of(Capability.RESEARCH));

    // Full constructor.
    public ResearchMission(int id, String name, int duration,
                           Set<Skill> requiredSkills, int requiredBattery,
                           Map<ResourceType, Integer> requiredResources,
                           Location laboratoryLocation, List<Equipment> scientificEquipment) {
        super(id,
              name,
              MissionType.RESEARCH,
              MissionStatus.PENDING,
              duration,
              0,
              null,
              DEFAULT_CAPABILITIES,
              requiredSkills != null ? requiredSkills : EnumSet.noneOf(Skill.class),
              requiredBattery,
              requiredResources != null ? requiredResources : new EnumMap<>(ResourceType.class),
              laboratoryLocation,
              scientificEquipment != null ? scientificEquipment : new ArrayList<>(),
              new ArrayList<>());
    }

    // Convenience constructor for standard lab research missions.
    public ResearchMission(int id, String name, int duration,
                           Skill requiredScienceSkill, Location laboratoryLocation,
                           List<Equipment> equipment, Map<ResourceType, Integer> resources) {
        this(id,
             name,
             duration,
             requiredScienceSkill != null ? EnumSet.of(requiredScienceSkill) : EnumSet.noneOf(Skill.class),
             0, // Research is human-centered, so 0 battery required
             resources != null ? resources : new EnumMap<>(ResourceType.class),
             laboratoryLocation,
             equipment != null ? equipment : new ArrayList<>());
    }
    // Checks whether all required scientific equipment is operational.
    public boolean hasOperationalRequiredEquipment() {

        for (Equipment equipment : getRequiredEquipment()) {

            if (equipment == null || !equipment.isOperational()) {
                return false;
            }
        }

        return true;
    }

    // Returns a clear explanation if equipment is unavailable.
    public String getEquipmentFailureReason() {

        for (Equipment equipment : getRequiredEquipment()) {

            if (equipment == null) {
                return "Required scientific equipment is missing.";
            }

            if (!equipment.isOperational()) {
                return "Scientific equipment is not operational: "
                        + equipment.getName();
            }
        }

        return null;
    }
}
