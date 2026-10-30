
package colony;

// add humans and robots in an array in the colony so we can access or search them

import agent.human.Human;
import facilities.Facility;
import resource.ResourceType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import agent.Agent;

 //hna be manage el facilities bel functions bta3tha
public class Colony {

    private final List<Facility> facilities; // final to protect the pointer from being deleted by accident
     private final List<Agent> agents= new ArrayList<>();    // acts like a database for all agents


    public Colony() {
        facilities = new ArrayList<>();
    }

    // Add a facility to the colony

    public void addFacility(Facility facility) {
        if (facility != null) {
            facilities.add(facility);
        }
    }

    public void addAgent(Agent agent){  // in simulation or main file don't forget to add in the colony
        agents.add(agent);

    }

    // Calculate total resources in operational facilities
    public int getTotalResources(ResourceType type) {
        int total = 0;

        for (Facility f : facilities) {
            if (f.isOperational()) {
                total += f.getResourceAmount(type);
            }
        }

        return total;
    }


    public boolean consumeResources(Map<ResourceType, Integer> required) {

        // bshof law el resources enough
        for (ResourceType type : required.keySet()) { //bnrg3 kol el keys el mawgoda fel map
            int amount = required.get(type);

            if (amount < 0) {
                System.out.println("Invalid resource amount");
                return false;
            }

            if (getTotalResources(type) < amount) {
                System.out.println("Not enough resources: " + type);
                return false;
            }
        }

        // Consume resources from facilities (mokhtlfa 3an el fe facility )
        for (ResourceType type : required.keySet()) {
            int remaining = required.get(type);

            for (Facility f : facilities) {
                if (f.isOperational()) {
                    int available = f.getResourceAmount(type);
                    int take = Math.min(available, remaining); //htrg3 el rakm el asghr(3alshan mnakhodsh kemya akbr mn el mhtagnha)

                    if (take > 0) {
                        f.consumeResource(type, take);
                        remaining -= take;
                    }
                }

                if (remaining == 0) { //3amlna esthlak khalas lel kemya el matloba
                    break;
                }
            }
        }

        System.out.println("Resources consumed successfully");
        return true;
    }


}
