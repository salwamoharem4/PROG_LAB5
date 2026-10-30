
package colony;

// add humans and robots in an array in the colony so we can access or search them

import agent.human.Human;
import facilities.Facility;
import facilities.Equipment;
import mission.BaseMission;
import mission.MissionStatus;
import resource.ResourceType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import agent.Agent;
import java.util.Collections;

 //hna be manage el facilities bel functions bta3tha
public class Colony {

    private final List<Facility> facilities; // final to protect the pointer from being deleted by accident
     private final List<Agent> agents;    // acts like a database for all agents
     // Keeps track of the active normal mission.
     private BaseMission activeNormalMission;

    public Colony() {

        facilities = new ArrayList<>();
        agents=new ArrayList<>();
    }

    // Add a facility to the colony

    public void addFacility(Facility facility) {
        if (facility != null && !facilities.contains(facility)) {
            facilities.add(facility);
        }
    }

    public void addAgent(Agent agent){
        if (agent != null && !agents.contains(agent)) {
            agents.add(agent);
        }

    }

     public List<Facility> getFacilities() {
         return Collections.unmodifiableList(facilities);
     }
    public List<Agent> getAgents() {
        return Collections.unmodifiableList(agents);
    }

     // Calculate total resources in operational facilities
    public int getTotalResources(ResourceType type) {
        if(type==null) return 0;
        int total = 0;

        for (Facility f : facilities) {
            if (f.isOperational()) {
                total += f.getResourceAmount(type);
            }
        }

        return total;
    }

    //check wether the colony has enough resorces
     public boolean hasEnoughResources(Map <ResourceType,Integer> required) {
        if(required==null || required.isEmpty()) return false;
        for (Map.Entry<ResourceType, Integer> entry : required.entrySet()) {
             ResourceType type = entry.getKey();
             Integer value = entry.getValue();
             if(type==null || value==null || value<0) return false;
             if(getTotalResources(type)<value) return false;
         }
        return true;
     }
    //consume resources from mult facilties
    public boolean consumeResources(Map<ResourceType, Integer> required) {

        // bshof law el resources enough
        if(!hasEnoughResources(required)) return false;

        // Consume resources from facilities (mokhtlfa 3an el fe facility )
        for (Map.Entry<ResourceType, Integer> entry : required.entrySet()) {
            ResourceType type = entry.getKey();
            int remaining = entry.getValue();

            for (Facility facility : facilities) {

                if (!facility.isOperational()) {
                    continue;
                }

                int available = facility.getResourceAmount(type);
                int take = Math.min(available, remaining);

                if (take > 0) {
                    boolean consumed = facility.consumeResource(type, take);

                    if (!consumed) {
                        return false;
                    }
                    remaining -= take;
                }

                if (remaining == 0) {
                    break;
                }
            }
        }

        return true;
    }
     // Check that all required equipment is available
     // and operational in operational facilities.
     private boolean hasRequiredEquipment(BaseMission mission) {

         for (Equipment required : mission.getRequiredEquipment()) {

             if (required == null || !required.isOperational()) {
                 return false;
             }

             boolean found = false;

             for (Facility facility : facilities) {

                 if (!facility.isOperational()) {
                     continue;
                 }

                 for (Equipment available : facility.getEquipment()) {

                     if (available != null
                             && available.isOperational()
                             && available.getName().equalsIgnoreCase(
                             required.getName())) {

                         found = true;
                         break;
                     }
                 }

                 if (found) {
                     break;
                 }
             }

             if (!found) {
                 return false;
             }
         }

         return true;
     }

     // Check whether all selected agents belong to this colony
     // and are available and capable of performing the mission.
     private String validateAgents(BaseMission mission, List<Agent> selectedAgents) {

         if (selectedAgents == null || selectedAgents.isEmpty()) {
             return "No agents selected for the mission.";
         }

         List<Agent> checkedAgents = new ArrayList<>();

         for (Agent agent : selectedAgents) {

             if (agent == null || !agents.contains(agent)) {
                 return "Invalid agent or agent does not belong to this colony.";
             }

             if (checkedAgents.contains(agent)) {
                 return "The same agent cannot be assigned twice.";
             }

             checkedAgents.add(agent);

             if (!agent.isAvailable()) {
                 return "Agent is not available: " + agent.getName();
             }

             if (!agent.canPerform(mission)) {
                 return "Agent does not meet mission requirements: "
                         + agent.getName();
             }
         }

         if (!mission.isCoveredBy(checkedAgents)) {
             return "The selected team does not cover all required capabilities.";
         }
         return null;
     }

     // Main mission admission and starting method.
     public boolean startMission(BaseMission mission, List<Agent> selectedAgents, boolean emergency) {

         if (mission == null) {
             return false;
         }

         // A mission must be new before it can be accepted.
         if (mission.getStatus() != MissionStatus.PENDING) {
             return false;
         }

         // Only one active normal mission is allowed.
         if (!emergency && activeNormalMission != null
                 && (activeNormalMission.getStatus()
                 == MissionStatus.ASSIGNED
                 || activeNormalMission.getStatus()
                 == MissionStatus.IN_PROGRESS)) {

             mission.fail("Another normal mission is already active.");
             return false;
         }

         // Validate selected agents.
         String agentError = validateAgents(mission, selectedAgents);

         if (agentError != null) {
             mission.fail(agentError);
             return false;
         }

         // Validate equipment.
         if (!hasRequiredEquipment(mission)) {
             mission.fail(
                     "Required equipment is missing or not operational.");
             return false;
         }

         // Validate resources before consuming anything.
         if (!hasEnoughResources(mission.getRequiredResources())) {
             mission.fail("Insufficient colony resources.");
             return false;
         }

         // Assign all selected agents.
         try {
             for (Agent agent : selectedAgents) {
                 mission.assignAgent(agent);
             }

             // Check that the mission is fully covered.
             if (mission.getStatus() != MissionStatus.ASSIGNED) {
                 mission.fail(
                         "Selected agents do not satisfy mission requirements.");
                 return false;
             }

             // Consume resources only after validation and assignment.
             if (!consumeResources(mission.getRequiredResources())) {
                 mission.fail("Failed to consume required resources.");
                 return false;
             }

             // Start the mission.
             mission.start();

             // Track only normal missions here.
             if (!emergency) {
                 activeNormalMission = mission;
             }

             return true;

         } catch (IllegalArgumentException | IllegalStateException ex) {

             mission.fail("Mission assignment failed: " + ex.getMessage());
             return false;
         }
     }

     // Get the currently active normal mission.
     public BaseMission getActiveNormalMission() {
         return activeNormalMission;
     }

}
