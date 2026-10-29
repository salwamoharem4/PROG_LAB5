

import agent.Agent;
import agent.human.Engineer;
import agent.human.HealthStatus;
import agent.human.MedicalOfficer;
import agent.human.Scientist;
import agent.robot.Drone;
import agent.robot.RepairRobot;
import agent.robot.Rover;
import core.Capability;
import core.EmergencyType;
import core.Location;
import core.Skill;
import core.Surface;
import mission.Mission;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/** Smoke test. Note there is no instanceof and no cast anywhere: everything goes through Agent. */
public class Main {

    static int failures = 0;

    static void check(String label, boolean actual, boolean expected) {
        boolean ok = actual == expected;
        if (!ok) failures++;
        System.out.println((ok ? "PASS " : "FAIL ") + label);
    }

    static Mission mission(Set<Capability> caps, Set<Skill> skills, int battery) {
        return new Mission() {
            public Set<Capability> getRequiredCapabilities() { return caps; }
            public Set<Skill> getRequiredSkills() { return skills; }
            public int getRequiredBattery() { return battery; }
        };
    }

    public static void main(String[] args) {
        Location habitatA = () -> "Habitat A";

        Scientist sci = new Scientist(1, "Dr. Sara", habitatA, HealthStatus.HEALTHY,
                EnumSet.of(Skill.GEOLOGY), 10);
        Engineer eng = new Engineer(2, "Omar", habitatA, HealthStatus.HEALTHY,
                EnumSet.of(Skill.MECHANICAL), 20);
        MedicalOfficer med = new MedicalOfficer(3, "Dr. Lina", habitatA, HealthStatus.HEALTHY,
                EnumSet.of(Skill.FIRST_AID), 0);
        Rover rover = new Rover(4, "Rover-1", habitatA, 80);
        RepairRobot fixer = new RepairRobot(5, "Fixer-1", habitatA, 60);
        Drone drone = new Drone(6, "Drone-1", habitatA, 5);

        Mission explore = mission(EnumSet.of(Capability.EXPLORE), EnumSet.of(Skill.GEOLOGY), 30);
        Mission repair = mission(EnumSet.of(Capability.REPAIR), EnumSet.noneOf(Skill.class), 10);

        // Scenario 1: suitable agents accepted
        check("scientist can explore", sci.canPerform(explore), true);
        check("rover can explore (battery 80 >= 30)", rover.canPerform(explore), true);
        check("team covers exploration mission", explore.isCoveredBy(List.of(sci, rover)), true);

        // Scenario 2: unsuitable agents rejected
        check("engineer cannot explore", eng.canPerform(explore), false);
        check("drone cannot explore", drone.canPerform(explore), false);
        check("scientist cannot repair", sci.canPerform(repair), false);

        // Battery condition
        check("drone low battery fails mission needing 30", drone.canPerform(
                mission(EnumSet.of(Capability.AERIAL_RECON), EnumSet.noneOf(Skill.class), 30)), false);

        // Skill condition
        Scientist noSkill = new Scientist(7, "Newbie", habitatA, HealthStatus.HEALTHY,
                EnumSet.noneOf(Skill.class), 0);
        check("scientist without GEOLOGY cannot explore", noSkill.canPerform(explore), false);

        // Emergencies (oxygen leak: human + robot both report REPAIR)
        check("engineer handles oxygen leak", eng.canHandle(EmergencyType.OXYGEN_LEAK), true);
        check("repair robot handles oxygen leak", fixer.canHandle(EmergencyType.OXYGEN_LEAK), true);
        check("medical officer handles medical emergency", med.canHandle(EmergencyType.MEDICAL_EMERGENCY), true);
        check("rover cannot handle oxygen leak", rover.canHandle(EmergencyType.OXYGEN_LEAK), false);

        // Availability: busy agent is not available, release frees it
        eng.assign();
        check("assigned engineer not available", eng.isAvailable(), false);
        check("assigned engineer cannot handle emergency", eng.canHandle(EmergencyType.OXYGEN_LEAK), false);
        eng.release();
        check("released engineer available again", eng.isAvailable(), true);

        // Usability drives availability: broken robot / exhausted human
        fixer.breakDown();
        check("broken robot not available", fixer.isAvailable(), false);
        fixer.repair();
        check("repaired robot available", fixer.isAvailable(), true);
        sci.addFatigue(100);
        check("exhausted scientist not available", sci.isAvailable(), false);

        // Treatment raises health
        med.worsenHealth();
        med.worsenHealth();
        check("critical officer not usable", med.isUsable(), false);
        med.improveHealth();
        med.improveHealth();
        check("treated officer usable again", med.isUsable(), true);

        // Dispatch moves agent; broken rover stays on the surface
        rover.moveTo(Surface.INSTANCE);
        rover.breakDown();
        check("broken rover stays on surface", rover.getLocation() == Surface.INSTANCE, true);

        System.out.println(failures == 0 ? "\nALL CHECKS PASSED" : "\nFAILURES: " + failures);
        System.exit(failures == 0 ? 0 : 1);
    }
}
