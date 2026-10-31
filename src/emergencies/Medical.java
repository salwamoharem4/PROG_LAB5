package emergencies;


import core.EmergencyType;
import agent.human.Human;
import agent.BaseAgent;
import facilities.Facility;
import resource.ResourceType;

import java.util.ArrayList;
import java.util.List;


// DON'T FORGET TO ADD RELEASE AFTER TIME DURATION
//Added Validation 
//modified resolve so when patient is healed it realses the doctor that is assign to it 

public class Medical extends Emergency {
private Human patient;
private Facility medicalFacility;
private int reqMedSupp=1;

    public Medical(Facility medicalFacility,Human patient) {
        super(EmergencyType.MEDICAL_EMERGENCY, patient.getLocation());
        if (medicalFacility == null) {
            throw new IllegalArgumentException("Medical facility is required");
        }
        
        this.medicalFacility=medicalFacility;
        this.patient = patient;
        System.out.println("THIS HUMAN ["+ patient.getName() +"] REQUIRES IMMEDIATE MEDICAL CARE !!  ");

    }


    @Override
    public boolean resolve(List<BaseAgent> candidateResponders) {
        List<BaseAgent> candidates = new ArrayList<>(candidateResponders); // to not Remove him from the database
        candidates.remove(patient); // the hurt person cannot heal himself

        if(!medicalFacility.isOperational()){
            markUnresolved("Medical Facility NON OPERATIONAL!!");
            return false;
        }
        if (medicalFacility.getResourceAmount(ResourceType.MEDICAL_SUPPLIES)<reqMedSupp) {
            markUnresolved("INSUFFICIENT MEDICAL SUPPLIES !!");
            return false;
        }
        BaseAgent doctor= null;
        for(BaseAgent agent : candidates){
            if(agent.canHandle(getType()) && agent.isAvailable()) {
                doctor = agent;
                break;
            }
        }

        if(doctor == null){
            markUnresolved("NO MEDICAL OFFICER FOUND !");
            return false;
        }
        dispatchResponder(doctor);
        medicalFacility.consumeResource(ResourceType.MEDICAL_SUPPLIES,reqMedSupp);
        patient.improveHealth();
        markResolved();
        System.out.println("Patient Healed By Doctor : "+doctor.getName());
        releaseResponders();
        return true;
    }
    public Human getPatient() {
        return patient;
    }
}
