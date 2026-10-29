package core;

/**
 * Where an agent is: either a facility or the Martian surface.
 * Facility (written by the facilities owner) should implement this interface.
 */
public interface Location {
    String getName();
}
