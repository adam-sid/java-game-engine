package edu.uob;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.Location;

import java.util.HashMap;
import java.util.HashSet;

public class GameState {

    private final HashSet<Location> locations;
    private final HashSet<GameEntity> furniture;
    private HashSet<GameEntity> artefacts;
    private HashSet<GameEntity> characters;

    public GameState(HashSet<Location> locations, HashSet<GameEntity> furniture,
                     HashSet<GameEntity> artefacts, HashSet<GameEntity> characters ) {
        this.locations = locations;
        this.furniture = furniture;
        this.artefacts = artefacts;
        this.characters = characters;
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




}
