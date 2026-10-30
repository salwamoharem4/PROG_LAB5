
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

public class Main {

    static int failures = 0;

    static void check(String label, boolean actual, boolean expected) {
        boolean ok = actual == expected;

        if (!ok) {
            failures++;
        }

        System.out.println((ok ? "PASS " : "FAIL ") + label);
    }

    static Mission mission(
            Set<Capability> caps,
            Set<Skill> skills,
            int battery) {

        return new Mission() {
            public Set<Capability> getRequiredCapabilities() {
                return caps;
            }

            public Set<Skill> getRequiredSkills() {
                return skills;
            }

            public int getRequiredBattery() {
                return battery;
            }
        };
    }

    public static void main(String[] args) {

        Location habitatA = () -> "Habitat A";

        // Create agents
        Scientist sci = new Scientist(
                1, "Dr. Sara", habitatA,
                HealthStatus.HEALTHY,
                EnumSet.of(Skill.GEOLOGY), 10);

        Engineer eng = new Engineer(
                2, "Omar", habitatA,
                HealthStatus.HEALTHY,
                EnumSet.of(Skill.MECHANICAL), 20);

        MedicalOfficer med = new MedicalOfficer(
                3, "Dr. Lina", habitatA,
                HealthStatus.HEALTHY,
                EnumSet.of(Skill.FIRST_AID), 0);

        Rover rover = new Rover(
                4, "Rover-1", habitatA,
                80);

        RepairRobot fixer = new RepairRobot(
                5, "Fixer-1", habitatA,
                60);

        Drone drone = new Drone(
                6, "Drone-1", habitatA,
                5);
        // Create missions
        Mission explore = mission(
                EnumSet.of(Capability.EXPLORE),
                EnumSet.of(Skill.GEOLOGY),
                30);

        Mission repair = mission(
                EnumSet.of(Capability.REPAIR),
                EnumSet.noneOf(Skill.class),
                10);

        Mission aerialMission = mission(
                EnumSet.of(Capability.AERIAL_RECON),
                EnumSet.noneOf(Skill.class),
                30);

        System.out.println("\\n===== 1. CAPABILITY TESTS =====");

        check("Scientist can explore",
                sci.canPerform(explore), true);

        check("Rover can explore",
                rover.canPerform(explore), true);

        check("Engineer cannot explore",
                eng.canPerform(explore), false);

        check("Drone cannot explore",
                drone.canPerform(explore), false);

        check("Scientist cannot repair",
                sci.canPerform(repair), false);


        System.out.println("\\n===== 2. SKILL TESTS =====");

        Scientist noSkill = new Scientist(
                7, "Newbie", habitatA,
                HealthStatus.HEALTHY,
                EnumSet.noneOf(Skill.class), 0);

        check("Scientist with GEOLOGY can explore",
                sci.canPerform(explore), true);

        check("Scientist without GEOLOGY cannot explore",
                noSkill.canPerform(explore), false);


        System.out.println("\\n===== 3. BATTERY TESTS =====");

        check("Rover battery 80 is enough for mission requiring 30",
                rover.canPerform(explore), true);

        check("Drone battery 5 is not enough for mission requiring 30",
                drone.canPerform(aerialMission), false);


        System.out.println("\\n===== 4. TEAM CAPABILITY TESTS =====");

        check("Scientist and Rover team covers exploration",
                explore.isCoveredBy(List.of(sci, rover)), true);

        check("Engineer alone does not cover exploration",
                explore.isCoveredBy(List.of(eng)), false);

        check("Empty team does not cover exploration",
                explore.isCoveredBy(List.of()), false);


        System.out.println("\\n===== 5. AGENT AVAILABILITY TESTS =====");

        eng.assign();

        check("Assigned engineer is not available",
                eng.isAvailable(), false);

        check("Assigned engineer cannot handle emergency",
                eng.canHandle(EmergencyType.OXYGEN_LEAK), false);

        eng.release();

        check("Released engineer is available again",
                eng.isAvailable(), true);


        System.out.println("\\n===== 6. EMERGENCY TESTS =====");

        check("Engineer handles oxygen leak",
                eng.canHandle(EmergencyType.OXYGEN_LEAK), true);

        check("Repair robot handles oxygen leak",
                fixer.canHandle(EmergencyType.OXYGEN_LEAK), true);

        check("Medical officer handles medical emergency",
                med.canHandle(EmergencyType.MEDICAL_EMERGENCY), true);

        check("Rover cannot handle oxygen leak",
                rover.canHandle(EmergencyType.OXYGEN_LEAK), false);


        System.out.println("\\n===== 7. AGENT USABILITY TESTS =====");

        fixer.breakDown();

        check("Broken robot is not available",
                fixer.isAvailable(), false);

        fixer.repair();

        check("Repaired robot is available",
                fixer.isAvailable(), true);

        sci.addFatigue(100);

        check("Exhausted scientist is not available",
                sci.isAvailable(), false);


        System.out.println("\\n===== 8. HEALTH TESTS =====");

        med.worsenHealth();
        med.worsenHealth();

        check("Critical medical officer is not usable",
                med.isUsable(), false);

        med.improveHealth();
        med.improveHealth();

        check("Treated medical officer is usable again",
                med.isUsable(), true);


        System.out.println("\\n===== 9. MOVEMENT AND BREAKDOWN TESTS =====");

        rover.moveTo(Surface.INSTANCE);
        rover.breakDown();

        check("Broken rover remains on the surface",
                rover.getLocation() == Surface.INSTANCE, true);


        System.out.println("\\n==============================");

        System.out.println(
                failures == 0
                        ? "ALL CHECKS PASSED"
                        : "FAILURES: " + failures
        );

        System.exit(failures == 0 ? 0 : 1);
    }
}
