package emergencies;

import core.EmergencyType;
import facilities.Facility;
import agent.BaseAgent;
import resource.ResourceType;

import java.util.List;
//made oxygenleak to consume spareparts as they asked in section 5
 // DON'T FORGET TO ADD RELEASE AFTER TIME DURATION
 public class OxygenLeak extends Emergency {

      private Facility affectedFacility;
    private int OxygenLoss;
    private int totalOxygenLost = 0;
    private int requiredSpareParts = 1;

    public OxygenLeak(Facility affectedFacility, int OxygenLoss) {
        this(affectedFacility, OxygenLoss, 1);
    }

    public OxygenLeak(Facility affectedFacility, int OxygenLoss, int requiredSpareParts) {
        super(EmergencyType.OXYGEN_LEAK, affectedFacility);
        if (affectedFacility == null) {
            throw new IllegalArgumentException("Affected facility is required");
        }
        if (OxygenLoss < 0) {
            throw new IllegalArgumentException("Oxygen loss cannot be negative");
        }
        if (requiredSpareParts < 0) {
            throw new IllegalArgumentException("Required spare parts cannot be negative");
        }
        this.affectedFacility = affectedFacility;
        this.OxygenLoss = OxygenLoss;
        this.requiredSpareParts = requiredSpareParts;

        applyOxygenLoss(OxygenLoss); // First deduction upon detection
    }

     private void applyOxygenLoss(int oxygenLossAmount) {
         if (oxygenLossAmount < 0) {
        throw new IllegalArgumentException("Oxygen loss cannot be negative");
    }
         int current = affectedFacility.getResourceAmount(ResourceType.OXYGEN);
         affectedFacility.consumeResource(ResourceType.OXYGEN, oxygenLossAmount);

         int actualLost = Math.min(oxygenLossAmount, current);   // can't lose more than exists
         this.totalOxygenLost += actualLost; // <-- 2. Add to running total!

         System.out.println("Oxygen leak in [" + affectedFacility.getName() + "]: lost "
                 + actualLost + " units. Total lost so far: " + this.totalOxygenLost);
     }

    @Override
    public boolean resolve(List<BaseAgent> candidateResponders) {
        if (!dispatchTeam(candidateResponders)) {
            applyOxygenLoss(this.OxygenLoss); // Second deduction if unresolved
            return false;
        }

        if (requiredSpareParts > 0) {
            if (affectedFacility.getResourceAmount(ResourceType.SPARE_PARTS) < requiredSpareParts) {
                markUnresolved("Insufficient spare parts to repair the oxygen leak.");
                applyOxygenLoss(this.OxygenLoss); // Second deduction
                return false;
            }
            affectedFacility.consumeResource(ResourceType.SPARE_PARTS, requiredSpareParts);
            System.out.println("Consumed " + requiredSpareParts + " spare parts to seal the leak.");
        }

        markResolved();
        System.out.println("Oxygen leak Repaired! Total oxygen lost: " + totalOxygenLost);
        return true;
    }


     // <-- 3. Public getter for status reports and tests
     public int getTotalOxygenLost() {
         return totalOxygenLost;
     }

     public int getRequiredSpareParts() {
         return requiredSpareParts;
     }
    
 }