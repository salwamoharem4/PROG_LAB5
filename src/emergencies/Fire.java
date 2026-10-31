package emergencies;

import agent.Agent;
import agent.human.HealthStatus;
import agent.human.Human;
import core.EmergencyType;
import facilities.Facility;
import agent.BaseAgent;
import resource.ResourceType;

import java.util.List;

// DON'T FORGET TO ADD RELEASE AFTER TIME DURATION
// DON'T FORGET TO ADD RELEASE AFTER TIME DURATION
// DON'T FORGET TO ADD RELEASE AFTER TIME DURATION

public class Fire extends Emergency {
    private Facility affectedFacility;


    public Fire( Facility affectedFacility) {
        super(EmergencyType.FIRE,affectedFacility);
        this.affectedFacility=affectedFacility;
        this.affectedFacility.setOperationalStatus(false); // Lock down Facility
        this.affectedFacility.consumeResource(ResourceType.OXYGEN, 40); // increase oxygen consuming
        System.out.println( "FIRE OCCURED IN  : " + affectedFacility.getName() +"!!");
        injureAllPersonell();
    }
    private void  injureAllPersonell(){
        for (Agent agent : BaseAgent.getAllAgents() ){

            if(agent.getLocation()!=null&& agent.getLocation().getName().equalsIgnoreCase(affectedFacility.getName()) ){

                    if (agent.isHuman()){
                        Human human= (Human) agent;
                        human.worsenHealth();
                        System.out.println("⚠️ " + human.getName() + " was injured in the fire! Health is now: " + human.getHealthStatus());

                        if(human.getHealthStatus() == HealthStatus.CRITICAL){
                            new Medical (affectedFacility , human);  // For automation
                        }
                    }
            }

            }

    }

    @Override
    public boolean resolve(List<BaseAgent> candidateResponders) {
            if(!dispatchTeam(candidateResponders)){
                return false;
            }

            markResolved();
        System.out.println("FIRE HAS BEEN PUT OUT");

        return true;
    }

}