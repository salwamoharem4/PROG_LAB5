package mission;

import agent.Agent;
import core.Capability;
import core.Skill;
import resource.ResourceType;

import java.util.*;

/**
 * MINIMAL CONTRACT so the agent package compiles and can be tested.
 * The real Mission class (state machine, duration, resources, ...) is written by whoever owns missions
 * and should implement these three getters.
 */
public interface Mission {

    /** Capabilities the mission needs, spread over all assigned agents. */
    Set<Capability> getRequiredCapabilities();

    /** Skills a human must hold to take part (empty if none). */
    Set<Skill> getRequiredSkills();

    /** Battery percentage a robot needs to take part (0 if none). */
    int getRequiredBattery();

    default Map<ResourceType, Integer> getRequiredResources() {
        return Collections.emptyMap();
    }

    default MissionStatus getStatus() {
        return MissionStatus.PENDING;
    }

    default String getFailureReason() { //3alsjan ehna mhtageen clear reason leh el mission rejected
        return null;
    }

    default List<Agent> getAssignedAgents() {
        return Collections.emptyList();
    }

    /** True if the given team together covers every required capability. */
    //? extends Agent means the collection can contain objects (ay unkown type lazem by extend agent)
        // of Agent or any class implementing/extending its type.
    default boolean isCoveredBy(Collection<? extends Agent> team) {

        if (team == null) {
            return false;
        }

        Set<Capability> covered = EnumSet.noneOf(Capability.class);

        for (Agent agent : team) {
            if (agent == null) {
                return false;
            }

            covered.addAll(agent.getCapabilities()); //add all capabilities of agent to covered set
        }
        return covered.containsAll(getRequiredCapabilities()); //btrg3 true law andha faaln kol el capablities
    }
}
