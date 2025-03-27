package edu.uob;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.LocationGameEntity;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    //TODO would using a List to store these be simpler?
    private final Map<String, GameEntity> locations;
    private final Map<String, GameEntity> furniture;
    private final Map<String, GameEntity> artefacts;
    private final Map<String, GameEntity> characters;
    private final Map<String, GameEntity> allEntities = new HashMap<>();
    private final Map<String, GameAction> gameActions = new HashMap<>();

    //TODO: add multiplayer function, add execute command method that takes a player as argument and a command (will need
    //TODO-cont: parse command, person needs inventory, health and location. Make inventory a hashmap name of entity and entity

    public GameState(Map<String, GameEntity> locations, Map<String, GameEntity> furniture,
                     Map<String, GameEntity> artefacts, Map<String, GameEntity> characters) {
        this.locations = locations;
        this.furniture = furniture;
        this.artefacts = artefacts;
        this.characters = characters;
        this.allEntities.putAll(locations);
        this.allEntities.putAll(furniture);
        this.allEntities.putAll(artefacts);
        this.allEntities.putAll(characters);
    }
    //TODO shorten this method
    public Map<String, GameEntity> getEntitiesFromLocation(String entityType, String locationName) {
        Map<String , GameEntity> filteredEntities = new HashMap<>();
        switch (entityType) {
            case "furniture":
                for (GameEntity entity : this.furniture.values()) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.put(entity.getName(), entity);
                    }
                }
                break;
            case "character":
                for (GameEntity entity : this.characters.values()) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.put(entity.getName(), entity);
                    }
                }
                break;
            case "artefact":
                for (GameEntity entity : this.artefacts.values()) {
                    if (entity.getLocationName().equals(locationName)) {
                        filteredEntities.put(entity.getName(), entity);
                    }
                }
                break;
            case "location":
                for (GameEntity entity : this.locations.values()) {
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

    public void setGameAction(String triggerName, GameAction gameAction) {
        this.gameActions.put(triggerName, gameAction);
    }

    public Map<String, GameAction> getGameActions() {
        return gameActions;
    }

    public void consumeEntity(String entityName) {

    }

    public void produceEntity(String toLocation, String entityName) {
        if (!allEntities.containsKey(entityName)) {
            throw new IllegalArgumentException(entityName);
        }
        LocationGameEntity currentLocation = (LocationGameEntity) locations.get(entityName);
        if (locations.containsKey(entityName)) {
            LocationGameEntity producedPath = (LocationGameEntity) locations.get(entityName);
            currentLocation.addPath(producedPath);
        } else {

        }
    }

    public void moveEntity(String toLocation, String entityType) {
        switch (entityType) {
            case "furniture":
                if (furniture.contains(entity)) {
                    furniture.remove(entity);
                    entity.setLocationName(locationName);
                    furniture.add(entity);
                }
                break;
            case "character":
                if (characters.contains(entity)) {
                    characters.remove(entity);
                    entity.setLocationName(locationName);
                    characters.add(entity);
                }
                break;
            case "artefact":
                if (artefacts.contains(entity)) {
                    artefacts.remove(entity);
                    entity.setLocationName(locationName);
                    artefacts.add(entity);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Map<String, GameEntity> getAllEntities() {
        return this.allEntities;
    }


    public Map<String, GameEntity> getLocations() {
        return this.locations;
    }

    public Map<String, GameEntity> getArtefacts() {
        return this.artefacts;
    }

    public Map<String, GameEntity> getFurniture() {
        return this.furniture;
    }

    public Map<String, GameEntity> getCharacters() {
        return this.characters;
    }
}
