package core;

// Emergency kinds. Each one declares the capability a responder must report.

public enum EmergencyType {
    //Each emergency has an associated required capability.
    //When your program loads the EmergencyType enum, Java automatically creates one object for each enum constant.
    //For example when the FIRE enum constant is initialized, it passes Capability.FIRE_RESPONSE
// to the EmergencyType constructor, which stores it in the requiredCapability field.
    OXYGEN_LEAK(Capability.REPAIR),
    FIRE(Capability.FIRE_RESPONSE),
    ROVER_BREAKDOWN(Capability.REPAIR),
    MEDICAL_EMERGENCY(Capability.MEDICAL);

    private final Capability requiredCapability;

    EmergencyType(Capability requiredCapability) {
        this.requiredCapability = requiredCapability;
    }

    public Capability getRequiredCapability() {
        return requiredCapability;
    }
}
