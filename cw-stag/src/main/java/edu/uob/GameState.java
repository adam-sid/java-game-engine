package edu.uob;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.LocationEntity;
import edu.uob.GameEntity.PlayerEntity;

import java.util.*;

public class GameState {

    private final Map<String, GameEntity> locations;
    private final String startLocationName;
    private final Map<String, GameEntity> furniture;
    private final Map<String, GameEntity> artefacts;
    private final Map<String, GameEntity> characters;
    private final Map<String, GameEntity> players = new HashMap<>();
    private final Map<String, GameEntity> allEntities = new HashMap<>();
    private final Map<String, List<GameAction>> gameActions = new HashMap<>();

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

    public String getEntityTypeFromName(String entityName) {
        entityName = entityName.toLowerCase();
        String playerName = Utils.addPlayerTag(entityName);
        if (!(this.allEntities.containsKey(entityName) ||
                this.allEntities.containsKey(playerName))) {
            return null;
        }
        if (this.furniture.containsKey(entityName)) return "furniture";
        if (this.characters.containsKey(entityName))return "character";
        if (this.artefacts.containsKey(entityName)) return "artefact";
        if (this.locations.containsKey(entityName)) return "location";
        if (this.players.containsKey(entityName) || this.players.containsKey(playerName)) return "player";
        else {
            return null;
        }
    }

    public String getPlayerLocation(String playerName) {
        if (this.players.containsKey(playerName)) {
            return this.players.get(playerName).getLocationName();
        } else {
            throw new RuntimeException(playerName);
        }
    }

    public PlayerEntity getPlayer(String playerName) {
        if (this.players.containsKey(playerName)) {
            return (PlayerEntity) this.players.get(playerName);
        } else {
            throw new RuntimeException(playerName);
        }
    }

    public Map<String, List<GameAction>> getGameActions() {
        return gameActions;
    }

    public void addGameAction(String triggerName, GameAction gameAction) {
        if(!this.gameActions.containsKey(triggerName)) {
            List<GameAction> newAction = new LinkedList<>();
            newAction.add(gameAction);
            this.gameActions.put(triggerName, newAction);
        } else {
            this.gameActions.get(triggerName).add(gameAction);
        }
    }

    public void consumeEntity(String targetLocationName, String entityName, PlayerEntity player) {
        Map<String, GameEntity> playerInventory = player.getInventory();
        if (!(allEntities.containsKey(entityName) || playerInventory.containsKey(entityName))) {
            throw new IllegalArgumentException(entityName);
        }
        if (locations.containsKey(entityName)) {
            LocationEntity targetLocation = (LocationEntity) locations.get(targetLocationName);
            targetLocation.removePath(entityName);
        } else if (playerInventory.containsKey(entityName)) {
            this.moveFromInventory(player.getName(), entityName);
            this.moveEntity("storeroom", entityName);
        } else {
            this.moveEntity("storeroom", entityName);
        }
    }

    public void produceEntity(String targetLocationName, String entityName, PlayerEntity player) {
        Map<String, GameEntity> playerInventory = player.getInventory();
        if (!(allEntities.containsKey(entityName) || playerInventory.containsKey(entityName))) {
            throw new IllegalArgumentException(entityName);
        }
        if (locations.containsKey(entityName)) {
            LocationEntity targetLocation = (LocationEntity) locations.get(targetLocationName);
            LocationEntity producedPath = (LocationEntity) locations.get(entityName);
            targetLocation.addPath(producedPath);
        } else if (playerInventory.containsKey(entityName)) {
            this.moveFromInventory(player.getName(), entityName);
        } else {
            this.moveEntity(targetLocationName, entityName);
        }
    }

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
        this.allEntities.remove(artefactName);
    }

    public void moveFromInventory(String playerName, String artefactName) {
        PlayerEntity player = (PlayerEntity) this.players.get(playerName);
        GameEntity artefactToMove = player.getInventory().get(artefactName);
        player.removeInventory(artefactName);
        this.artefacts.put(artefactName, artefactToMove);
        this.allEntities.put(artefactName, artefactToMove);
    }

    public void resetPlayer(PlayerEntity player) {
        Map<String, GameEntity> playerInventory = player.getInventory();
        for (String entityName : playerInventory.keySet()) {
            this.moveFromInventory(player.getName(), entityName);
        }
        player.setLocationName(this.startLocationName);
        player.modifyHealth(3);
    }
}
