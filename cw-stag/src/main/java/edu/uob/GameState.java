package edu.uob;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.Location;
import java.util.HashSet;

public class GameState {

    public final HashSet<Location> locations;
    public final HashSet<GameEntity> furniture;
    public HashSet<GameEntity> artefacts;
    public HashSet<GameEntity> characters;

    public GameState(HashSet<Location> locations, HashSet<GameEntity> furniture,
                     HashSet<GameEntity> artefacts, HashSet<GameEntity> characters ) {
        this.locations = locations;
        this.furniture = furniture;
        this.artefacts = artefacts;
        this.characters = characters;
    }

    public HashSet<GameEntity> getEntitiesFromLocation(String entityType, String locationName) {
        HashSet<GameEntity> filteredEntities = new HashSet<>();
        switch (entityType) {
            case "furniture":
                for (GameEntity gameEntity : furniture) {
                    if (gameEntity.getLocationName().equals(locationName)) {
                        filteredEntities.add(gameEntity);
                    }
                }
                break;
            case "character":
                for (GameEntity gameEntity : characters) {
                    if (gameEntity.getLocationName().equals(locationName)) {
                        filteredEntities.add(gameEntity);
                    }
                }
                break;
            case "artefact":
                for (GameEntity gameEntity : artefacts) {
                    if (gameEntity.getLocationName().equals(locationName)) {
                        filteredEntities.add(gameEntity);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return filteredEntities;
    }

    public void changeLocation(GameEntity entity, String locationName, Boolean isArtefact) {
        if (isArtefact && artefacts.contains(entity)) {
            artefacts.remove(entity);
            entity.setLocationName(locationName);
            artefacts.add(entity);
        }
        if (!isArtefact && characters.contains(entity)) {
            furniture.remove(entity);
            entity.setLocationName(locationName);
            furniture.add(entity);
        }
    }

    public void addPath(Location from, Location to) {
        locations.add(from);
    };
}
