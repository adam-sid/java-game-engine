package edu.uob;

import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationGameEntity;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateTest {

    public File entitiesFile = new File("config" + File.separator + "basic-entities.dot");

    @Test
    void testEntitiesAddedToGameState() {
        GameStateParser parser = new GameStateParser(entitiesFile);
        GameState gameState = parser.getGameState();
        Map<String, GameEntity> locations = gameState.getLocations();
        assertEquals(4, locations.size());
        Map<String, GameEntity> furniture = gameState.getFurniture();
        assertEquals(2, furniture.size());
        Map<String, GameEntity> characters = gameState.getCharacters();
        assertEquals(1, characters.size());
        Map<String, GameEntity> artefacts = gameState.getArtefacts();
        assertEquals(4, artefacts.size());
    }

    @Test
    void testFurnitureHasLocation() {
        GameStateParser parser = new GameStateParser(entitiesFile);
        GameState gameState = parser.getGameState();
        Map<String, GameEntity> forestFurniture = gameState.getEntitiesFromLocation(
                "furniture", "forest");
        GameEntity tree = forestFurniture.values().stream().findFirst().get();
        assertEquals(1, forestFurniture.size());
        assertEquals("tree", tree.getName());

    }

    @Test
    void testLocationHasPaths() {
        GameStateParser parser = new GameStateParser(entitiesFile);
        GameState gameState = parser.getGameState();
        LocationGameEntity forest = (LocationGameEntity) gameState
                .getEntitiesFromLocation("location", "forest")
                .values()
                .stream()
                .findFirst()
                .get();
        Map<String, LocationGameEntity> paths = forest.getPaths();
        assertEquals(1, paths.size());
        LocationGameEntity cabin = paths.get("cabin");
        assertEquals("cabin", cabin.getName());
    }

    @Test
    void testExtendedEntitiesFile() {
        GameStateParser parser = new GameStateParser(entitiesFile);
        GameState gameState = parser.getGameState();
    }

}

