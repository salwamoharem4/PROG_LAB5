package mission;

import agent.Agent;
import core.Capability;
import core.Skill;
import resource.ResourceType;

import java.util.*;

public interface Mission {
    Set<Capability> getRequiredCapabilities();

    Set<Skill> getRequiredSkills();

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
