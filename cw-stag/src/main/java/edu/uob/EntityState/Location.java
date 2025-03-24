package edu.uob.EntityState;

import java.util.HashMap;
import java.util.LinkedList;

public class Location {

    private final String locationName;
    private final HashMap<String, Location> paths;
    private final HashMap<String, Furniture> furniture;
    private HashMap<String, Character> characters;
    private HashMap<String, Artefact> artefacts;

    public Location(String locationName, HashMap<String, Location> pathList,
                    HashMap<String, Artefact> artefacts, HashMap<String, Furniture> furniture, HashMap<String, Character> characters) {
        this.locationName = locationName;
        this.paths = pathList;
        this.furniture = furniture;
        this.characters = characters;
        this.artefacts = artefacts;
    }

    public HashMap<String, Location> getPaths() {
        return this.paths;
    }

    public HashMap<String, Furniture> getFurniture() {
        return this.furniture;
    }

    public void addCharacter() {
        this.characters.put(locationName, characters.get(locationName));
    }

    public void removeCharacter() {

    }

    public HashMap<String, Character> getCharacters() {
        return this.characters;
    }

    public void addArtefact() {

    }

    public void removeArtefact() {

    }

    public HashMap<String, Artefact> getArtefacts() {
        return this.artefacts;
    }
}
