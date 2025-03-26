package edu.uob;

import edu.uob.Entity.Entity;
import edu.uob.Entity.LocationEntity;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateTest {

    @Test
    void testEntitiesAddedToGameState() {
        GameStateParser parser = new GameStateParser("basic-entities.dot");
        GameState gameState = parser.getGameState();
        Map<String, Entity> locations = gameState.getLocations();
        assertEquals(4, locations.size());
        Map<String, Entity> furniture = gameState.getFurniture();
        assertEquals(2, furniture.size());
        Map<String, Entity> characters = gameState.getCharacters();
        assertEquals(1, characters.size());
        Map<String, Entity> artefacts = gameState.getArtefacts();
        assertEquals(4, artefacts.size());
    }

    @Test
    void testFurnitureHasLocation() {
        GameStateParser parser = new GameStateParser("basic-entities.dot");
        GameState gameState = parser.getGameState();
        Map<String, Entity> forestFurniture = gameState.getEntitiesFromLocation(
                "furniture", "forest");
        Entity tree = forestFurniture.values().stream().findFirst().get();
        assertEquals(1, forestFurniture.size());
        assertEquals("tree", tree.getName());

    }

    @Test
    void testLocationHasPaths() {
        GameStateParser parser = new GameStateParser("basic-entities.dot");
        GameState gameState = parser.getGameState();
        LocationEntity forest = (LocationEntity) gameState
                .getEntitiesFromLocation("location", "forest")
                .values()
                .stream()
                .findFirst()
                .get();
        List<LocationEntity> paths = forest.getPaths().stream().toList();
        assertEquals(1, paths.size());
        assertEquals("cabin", paths.get(0).getName());
    }

    @Test
    void testExtendedEntitiesFile() {
        GameStateParser parser = new GameStateParser("extended-entities.dot");
        GameState gameState = parser.getGameState();
    }

}

