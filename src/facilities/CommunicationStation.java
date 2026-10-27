package facilities;

public class CommunicationStation extends Facility {
    public CommunicationStation(String name, String location, int capacity, boolean operational) {
        super(name, location, capacity, operational);

    }
    @Override
    public String getFacility(){
        return "CommunicationStation";
    }
}
