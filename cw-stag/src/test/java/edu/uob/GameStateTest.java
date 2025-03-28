package edu.uob;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationEntity;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateTest {

    public File entitiesFile = new File("config" + File.separator + "basic-entities.dot");

    @Test
    void testEntitiesAddedToGameState() {
        EntityFileParser parser = new EntityFileParser(entitiesFile);
        GameState gameState = parser.getGameState();
        Map<String, GameEntity> locations = gameState.getEntityMap("location");
        assertEquals(4, locations.size());
        Map<String, GameEntity> furniture = gameState.getEntityMap("furniture");
        assertEquals(2, furniture.size());
        Map<String, GameEntity> characters = gameState.getEntityMap("character");
        assertEquals(1, characters.size());
        Map<String, GameEntity> artefacts = gameState.getEntityMap("artefact");
        assertEquals(4, artefacts.size());
    }

    @Test
    void testFurnitureHasLocation() {
        EntityFileParser parser = new EntityFileParser(entitiesFile);
        GameState gameState = parser.getGameState();
        Map<String, GameEntity> forestFurniture = gameState.getEntitiesFromLocation(
                "furniture", "forest");
        GameEntity tree = forestFurniture.values().stream().findFirst().get();
        assertEquals(1, forestFurniture.size());
        assertEquals("tree", tree.getName());

    }

    @Test
    void testLocationHasPaths() {
        EntityFileParser parser = new EntityFileParser(entitiesFile);
        GameState gameState = parser.getGameState();
        LocationEntity forest = (LocationEntity) gameState
                .getEntitiesFromLocation("location", "forest")
                .values()
                .stream()
                .findFirst()
                .get();
        Map<String, LocationEntity> paths = forest.getPaths();
        assertEquals(1, paths.size());
        LocationEntity cabin = paths.get("cabin");
        assertEquals("cabin", cabin.getName());
    }

    @Test
    void testExtendedEntitiesFile() {
        EntityFileParser parser = new EntityFileParser(entitiesFile);
        GameState gameState = parser.getGameState();
    }

}

