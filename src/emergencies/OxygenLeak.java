package emergencies;

import core.EmergencyType;
import facilities.Facility;
import agent.BaseAgent;
import resource.ResourceType;

import java.util.List;

 // DON'T FORGET TO ADD RELEASE AFTER TIME DURATION
 public class OxygenLeak extends emergency {

     private Facility affectedFacility;
     private int OxygenLoss;
     private int totalOxygenLost = 0; // <-- 1. Track the running total!

     public OxygenLeak( Facility affectedFacility, int OxygenLoss) {
         super(EmergencyType.OXYGEN_LEAK, affectedFacility);
         this.affectedFacility = affectedFacility;
         this.OxygenLoss = OxygenLoss;

         applyOxygenLoss(OxygenLoss); // First deduction
     }

     private void applyOxygenLoss(int oxygenLossAmount) {
         int current = affectedFacility.getResourceAmount(ResourceType.OXYGEN);
         affectedFacility.consumeResource(ResourceType.OXYGEN, oxygenLossAmount);

         int actualLost = current - affectedFacility.getResourceAmount(ResourceType.OXYGEN);
         this.totalOxygenLost += actualLost; // <-- 2. Add to running total!

         System.out.println("Oxygen leak in [" + affectedFacility.getName() + "]: lost "
                 + actualLost + " units. Total lost so far: " + this.totalOxygenLost);
     }

     @Override
     public boolean resolve(List<BaseAgent> candidateResponders) {
         if (!dispatchTeam(candidateResponders)) {
             applyOxygenLoss(this.OxygenLoss); // Second deduction
             return false;
         }

         markResolved();
         System.out.println("Oxygen leak Repaired! Total oxygen lost: " + totalOxygenLost);
         return true;
     }

     // <-- 3. Public getter for status reports and tests
     public int getTotalOxygenLost() {
         return totalOxygenLost;
     }
 }