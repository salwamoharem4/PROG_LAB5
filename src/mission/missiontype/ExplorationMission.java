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
// Requires vehicles/agents with EXPLORE capability, scientific skill, battery, and equipment.
public class ExplorationMission extends BaseMission {

    // Exploration always requires EXPLORE capability.
    private static final Set<Capability> DEFAULT_CAPABILITIES = Collections
            .unmodifiableSet(EnumSet.of(Capability.EXPLORE));

    // Full constructor with all details.
    public ExplorationMission(int id, String name, int duration,
            Set<Skill> requiredSkills, int requiredBattery,
            Map<ResourceType, Integer> requiredResources,
            Location targetLocation, List<Equipment> requiredEquipment) {
        super(id,
                name,
                MissionType.EXPLORATION,
                MissionStatus.PENDING,
                duration,
                0,
                null,
                DEFAULT_CAPABILITIES,
                requiredSkills != null ? requiredSkills : EnumSet.noneOf(Skill.class),
                requiredBattery,
                requiredResources != null ? requiredResources : new EnumMap<>(ResourceType.class),
                targetLocation != null ? targetLocation : Surface.INSTANCE,
                requiredEquipment != null ? requiredEquipment : new ArrayList<>(),
                new ArrayList<>());
    }

    // Convenience constructor with common exploration parameters.
    public ExplorationMission(int id, String name, int duration,
            Skill scientificSkill, int requiredBattery,
            List<Equipment> equipment) {
        this(id,
                name,
                duration,
                scientificSkill != null ? EnumSet.of(scientificSkill) : EnumSet.noneOf(Skill.class),
                requiredBattery,
                new EnumMap<>(ResourceType.class),
                Surface.INSTANCE,
                equipment != null ? equipment : new ArrayList<>());
    }// It converts the single skill into a Set, supplies default resources and
     // location, then calls the full constructor.

    // Overloaded constructor for quick exploration setups.
    // This constructor only receives three arguments. It supplies defaults for
    // everything else and calls the full constructor.
    public ExplorationMission(int id, String name, int duration) {
        this(id,
                name,
                duration,
                EnumSet.noneOf(Skill.class),
                30,
                new EnumMap<>(ResourceType.class),
                Surface.INSTANCE,
                new ArrayList<>());
    }
}
