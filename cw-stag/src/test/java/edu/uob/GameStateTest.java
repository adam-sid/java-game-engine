package edu.uob;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.Location;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateTest {

    @Test
    void testEntitiesAddedToGameState() {
        ParseEntity parser = new ParseEntity("basic-entities.dot");
        GameState gameState = parser.getGameState();
        HashSet<Location> locations = gameState.locations;
        assertEquals(4, locations.size());
        HashSet<GameEntity> furniture = gameState.furniture;
        assertEquals(2, furniture.size());
        HashSet<GameEntity> characters = gameState.characters;
        assertEquals(1, characters.size());
        HashSet<GameEntity> artefacts = gameState.artefacts;
        assertEquals(4, artefacts.size());
    }

    @Test
    void testFurnitureHasLocation() {
        ParseEntity parser = new ParseEntity("basic-entities.dot");
        GameState gameState = parser.getGameState();
        HashSet<GameEntity> forestFurniture = gameState.getEntitiesFromLocation(
                "furniture", "forest");
        List<String> furnitureNames = forestFurniture.stream().map(GameEntity::getName).toList();
        assertEquals(1, forestFurniture.size());
        assertTrue(furnitureNames.contains("tree"));
    }

    void testLocationHasPaths() {
        ParseEntity parser = new ParseEntity("basic-entities.dot");
        GameState gameState = parser.getGameState();
    }

}

