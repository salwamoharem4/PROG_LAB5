package facilities;
import resource.ResourceType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public abstract class Facility {
    private String name;
    private String location;
    private int capacity;
    private boolean operationalStatus;
    private Map<ResourceType,Integer> resources;
    private ArrayList<Equipment> equipment;

    public Facility(String name, String location, int capacity,boolean operational) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.operationalStatus = operational; // Fixed
        this.equipment = new ArrayList<>();
        resources = new EnumMap<>(ResourceType.class);
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOperational() {
        return operationalStatus;
    }


    public void setOperationalStatus(boolean operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public boolean setName(String name) {
        if(name==null || name.isEmpty()) return false;
        this.name = name;
        return true;
    }

    public boolean setLocation(String location) {
        if(location==null || location.isEmpty()) return false;
        this.location = location;
        return true;
    }

    public boolean setCapacity(int capacity) {
        if(capacity<=0) return  false;
        this.capacity = capacity;
        return true;
    }


    public void addEquipment(Equipment item) {
        if (item != null) {
            equipment.add(item);
        }
    }

    public boolean addResource(ResourceType type, int amount) {
        if (type == null || amount < 0) {
            return false;
        }

        int current = getResourceAmount(type);
        resources.put(type, current + amount);
        return true;
    }

    public int getResourceAmount(ResourceType type) {
        return resources.getOrDefault(type, 0);
    }
    public boolean consumeResource(ResourceType type, int amount) {

        if (amount < 0) {
            return false;
        }

        if (!isOperational()) {
            return false;
        }

        int available = getResourceAmount(type);

        if (available < amount) {
            return false;
        }

        resources.put(type, available - amount);
        return true;
    }

    public abstract String getFacility();
    public void displayInfo(){
        System.out.println("type: "+getFacility());
        System.out.println("name: "+name);
        System.out.println("location: "+location);
        System.out.println("capacity: "+capacity);
        System.out.println("operationalStatus: "+operationalStatus);
    }
}
