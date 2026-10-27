package agent;

public abstract class Agent {
    private String id;
    private String name;
    private String currentLocation;
    private boolean available;
    private boolean damaged;
    public Agent(String id, String name, String currentLocation, boolean available, boolean damaged) {
        if (id == null || id.trim().isEmpty()) {
         throw new IllegalArgumentException("Invalid ID");
       }

try {
    if (Integer.parseInt(id) <= 0) {//validate negative 
        throw new IllegalArgumentException("Invalid ID");
    }
} catch (NumberFormatException e) { //validate string like abc 
    throw new IllegalArgumentException("ID must be a number");
}

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }

        if (currentLocation == null || currentLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid location");
        }
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.available = available;
        this.damaged = damaged;
    }
    public String getID(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getCurrentLocation(){
        return currentLocation;
    }
    public boolean isAvailable(){
        return available;
    }
    public boolean isDamaged(){
        return damaged;
    }
    public void setCurrentLocation(String currentLocation){
        if (currentLocation == null || currentLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid location");
        }
        this.currentLocation=currentLocation;
    }
    public void setAvailability(boolean available){
        this.available=available;
    }
    public void setDamaged(boolean damaged){
        this.damaged=damaged;
    }
}
