package edu.uob.GameEntity;

import java.util.HashMap;
import java.util.HashSet;

public class Location extends GameEntity {

    private HashSet<Location> paths;

    public Location(String name, String description, String locationName) {
        super(name, description, locationName);
    }

    public HashSet<Location> getPaths() {
        return this.paths;
    }

    public void addPath(String locationName, Location location) {
        this.paths.add(location);
    }
}
