package mission;

import agent.Agent;
import core.Capability;
import core.Skill;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

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

    /** True if the given team together covers every required capability. */
    //? extends Agent means the collection can contain objects 
        // of Agent or any class implementing/extending its type.
    default boolean isCoveredBy(Collection<? extends Agent> team) {/*default: A method with an implementation inside an 
     interface. Classes implementing Mission automatically inherit this implementation unless they override it.*/
        Set<Capability> covered = EnumSet.noneOf(Capability.class);//Create an empty Set
        for (Agent a : team) {
            covered.addAll(a.getCapabilities());
            /*a.getCapabilities() gets that agent's capabilities.
               addAll() adds all those capabilities to the covered Set.*/
        }
        return covered.containsAll(getRequiredCapabilities());
    }
}
