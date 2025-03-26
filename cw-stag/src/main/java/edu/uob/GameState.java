package edu.uob;

import edu.uob.Entity.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class GameState {
    //TODO would using a List to store these be simpler?
    private final Map<String, Entity> locations;
    private final Map<String, Entity> furniture;
    private final Map<String, Entity> artefacts;
    private final Map<String, Entity> characters;
    private final Map<String, Entity> allEntities = new HashMap<>();

    /*TODO add multiplayer function, add execute command method that takes a player as argument and a command (will need
    parse command, person needs inventory, health and location. Make inventory a hashmap name of entity and entity*/

    public GameState(Map<String, Entity> locations, Map<String, Entity> furniture,
                     Map<String, Entity> artefacts, Map<String, Entity> characters) {
        this.locations = locations;
        this.furniture = furniture;
        this.artefacts = artefacts;
        this.characters = characters;
        this.allEntities.putAll(locations);
        this.allEntities.putAll(furniture);
        this.allEntities.putAll(artefacts);
        this.allEntities.putAll(characters);
    }

    public Map<String, Entity> getEntitiesFromLocation(String entityType, String locationName) {
        Map<String ,Entity> filteredEntities = new HashMap<>();
        switch (entityType) {
            case "furniture":
                for (Entity entity : this.furniture.values()) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.put(entity.getName(), entity);
                    }
                }
                break;
            case "character":
                for (Entity entity : this.characters.values()) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.put(entity.getName(), entity);
                    }
                }
                break;
            case "artefact":
                for (Entity entity : this.artefacts.values()) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.put(entity.getName(), entity);
                    }
                }
                break;
            case "location":
                for (Entity entity : this.locations.values()) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.put(entity.getName(), entity);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return filteredEntities;
    }

//    public void moveEntity(Entity entity, String locationName, String entityType) {
//        switch (entityType) {
//            case "furniture":
//                if (furniture.contains(entity)) {
//                    furniture.remove(entity);
//                    entity.setLocationName(locationName);
//                    furniture.add(entity);
//                }
//                break;
//            case "character":
//                if (characters.contains(entity)) {
//                    characters.remove(entity);
//                    entity.setLocationName(locationName);
//                    characters.add(entity);
//                }
//                break;
//            case "artefact":
//                if (artefacts.contains(entity)) {
//                    artefacts.remove(entity);
//                    entity.setLocationName(locationName);
//                    artefacts.add(entity);
//                }
//                break;
//            default:
//                throw new IllegalArgumentException();
//        }
//    }

    public Map<String, Entity> getAllEntities() {
        return this.allEntities;
    }


    public Map<String, Entity> getLocations() {
        return this.locations;
    }

    public Map<String, Entity> getArtefacts() {
        return this.artefacts;
    }

    public Map<String, Entity> getFurniture() {
        return this.furniture;
    }

    public Map<String, Entity> getCharacters() {
        return this.characters;
    }
}
