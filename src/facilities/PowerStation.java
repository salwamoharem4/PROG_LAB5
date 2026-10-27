package facilities;

public class PowerStation extends Facility {
    public PowerStation(String name, String location, int capacity, boolean operational) {
        super(name, location, capacity, operational);
    }
    @Override
    public String getFacility(){
        return "PowerStation";
    }
}
