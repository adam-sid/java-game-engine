package edu.uob;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.LocationEntity;
import edu.uob.GameEntity.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    //TODO would using a List to store these be simpler?
    private final Map<String, GameEntity> locations;
    private final String startLocationName;
    private final Map<String, GameEntity> furniture;
    private final Map<String, GameEntity> artefacts;
    private final Map<String, GameEntity> characters;
    private final Map<String, GameEntity> players = new HashMap<>();
    private final Map<String, GameEntity> allEntities = new HashMap<>();
    private final Map<String, GameAction> gameActions = new HashMap<>();


    //TODO: add multiplayer function, add execute command method that takes a player as argument and a command (will need
    //TODO-cont: parse command, person needs inventory, health and location. Make inventory a hashmap name of entity and entity

    public GameState(Map<String, GameEntity> locations, String startLocationName, Map<String, GameEntity> furniture,
                     Map<String, GameEntity> artefacts, Map<String, GameEntity> characters) {
        this.locations = locations;
        this.startLocationName = startLocationName;
        this.furniture = furniture;
        this.artefacts = artefacts;
        this.characters = characters;
        this.allEntities.putAll(locations);
        this.allEntities.putAll(furniture);
        this.allEntities.putAll(artefacts);
        this.allEntities.putAll(characters);
    }

    public void addPlayer(String playerName) {
        PlayerEntity newPlayer = new PlayerEntity(playerName, startLocationName);
        this.players.put(playerName, newPlayer);
        this.allEntities.put(playerName, newPlayer);
    }

    public Map<String, GameEntity> getEntityMap(String entityType) {
        switch (entityType) {
            case "furniture": return this.furniture;
            case "character": return this.characters;
            case "artefact": return this.artefacts;
            case "location": return this.locations;
            case "player": return this.players;
            case "all": return this.allEntities;
            default: return null;
        }
    }

    public Map<String, GameEntity> getEntitiesFromLocation(String entityType, String locationName) {
        Map<String, GameEntity> entityMap = getEntityMap(entityType);
        Map<String, GameEntity> filteredEntities = new HashMap<>();
        for (GameEntity entity : entityMap.values()) {
            if (entity.getLocationName().equals(locationName)) {
                filteredEntities.put(entity.getName(), entity);
            }
        }
        return filteredEntities;
    }

    public Map<String, GameAction> getGameActions() {
        return gameActions;
    }

    public void addGameAction(String triggerName, GameAction gameAction) {
        this.gameActions.put(triggerName, gameAction);
    }

    public void consumeEntity(String entityName) {
        moveEntity("storeroom", entityName);

    }

    //TODO exceptions may not be worth having - impossible to be thrown?
    public void produceEntity(String toLocation, String entityName) {
        if (!allEntities.containsKey(entityName)) {
            throw new IllegalArgumentException(entityName);
        }
        LocationEntity currentLocation = (LocationEntity) locations.get(entityName);
        if (locations.containsKey(entityName)) {
            LocationEntity producedPath = (LocationEntity) locations.get(entityName);
            currentLocation.addPath(producedPath);
        } else {
            moveEntity(toLocation, entityName);
        }
    }
    //TODO exceptions may not be worth having - impossible to be thrown?
    public void moveEntity(String toLocation, String entityName) {
        if (!allEntities.containsKey(entityName)) {
            throw new IllegalArgumentException(entityName);
        }
        GameEntity entityToMove = allEntities.get(entityName);
        entityToMove.setLocationName(toLocation);
    }

    public void moveToInventory(String playerName, String artefactName) {
        PlayerEntity player = (PlayerEntity) this.players.get(playerName);
        GameEntity artefactToMove = this.artefacts.get(artefactName);
        player.addInventory(artefactToMove);
        this.artefacts.remove(artefactName);
    }

    public void moveFromInventory(String playerName, String artefactName) {

    }
}
