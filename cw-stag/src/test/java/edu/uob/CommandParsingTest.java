package edu.uob;

import edu.uob.Commands.CustomCommand;
import edu.uob.Commands.LookCommand;
import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationEntity;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CommandParsingTest {

    public File entitiesFile = new File("config" + File.separator + "basic-entities.dot");

    @Test
    void testMakeBridge() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: get axe", gameState);
        String response = CommandParser.parseCommand("simon: inv", gameState);
        assertTrue(response.contains("axe"));
        CommandParser.parseCommand("simon: goto forest", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("tree"));
        response = CommandParser.parseCommand("simon: cut down tree with axe", gameState);
        assertEquals("you cut down the tree with the axe", response);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertFalse(response.contains("tree"));
        CommandParser.parseCommand("simon: get log", gameState);
        response = CommandParser.parseCommand("simon: inv", gameState);
        assertTrue(response.contains("log"));
        CommandParser.parseCommand("simon: goto riverbank", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("river"));
        CommandParser.parseCommand("simon: make bridge with log", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("clearing"));
    }

    @Test
    //there are two actions with the same trigger but the program should still run
    void testSemiAmbiguousActions() {
        File entitiesFile = new File("config" + File.separator + "semi-ambiguous-entities.dot");
        File actionsFile = new File("config" + File.separator + "semi-ambiguous-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);

        CommandParser.parseCommand("simon: goto forest", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("tree"));
        CommandParser.parseCommand("simon: get key", gameState);
        CommandParser.parseCommand("simon: get ladder", gameState);
        response = CommandParser.parseCommand("simon: inv", gameState);
        assertTrue(response.contains("key"));
        assertTrue(response.contains("ladder"));
        CommandParser.parseCommand("simon: goto cabin", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("attic"));
        response = CommandParser.parseCommand("simon: open hatch with ladder", gameState);
        assertTrue(response.contains("secure"));
        CommandParser.parseCommand("simon: goto attic", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("painting"));
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("simon: open trapdoor with key", gameState);
        CommandParser.parseCommand("simon: goto cellar", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("elf"));
    }

    @Test
    //there are two actions with the same trigger and same subject so should fail
    void testAmbiguousActions() {
        File entitiesFile = new File("config" + File.separator + "semi-ambiguous-entities.dot");
        File actionsFile = new File("config" + File.separator + "ambiguous-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);

        CommandParser.parseCommand("simon: goto forest", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("tree"));
        CommandParser.parseCommand("simon: get key", gameState);
        response = CommandParser.parseCommand("simon: inv", gameState);
        assertTrue(response.contains("key"));
        CommandParser.parseCommand("simon: goto cabin", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("attic"));
        CommandParser.parseCommand("simon: open key", gameState);
        response = CommandParser.parseCommand("simon: inv", gameState);
        assertTrue(response.contains("key"));
        response = CommandParser.parseCommand("simon: open hatch with key", gameState);
        assertTrue(response.equals("you open the hatch and see a dark attic above"));
        CommandParser.parseCommand("simon: goto attic", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("painting"));
    }

    @Test
    //test ambiguous commands that also have whitespace
    void testAmbiguousAndCommandHasWhitespace() {
        File entitiesFile = new File("config" + File.separator + "semi-ambiguous-entities.dot");
        File actionsFile = new File("config" + File.separator + "ambiguous-actions-whitespace.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("tree"));
        CommandParser.parseCommand("simon: get key", gameState);
        response = CommandParser.parseCommand("simon: inv", gameState);
        assertTrue(response.contains("key"));
        CommandParser.parseCommand("simon: goto cabin", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("attic"));
        CommandParser.parseCommand("simon: open the portal key", gameState);
        response = CommandParser.parseCommand("simon: inv", gameState);
        assertTrue(response.contains("key"));
        response = CommandParser.parseCommand("simon: open the portal hatch", gameState);
        assertTrue(response.equals("you open the hatch and see a dark attic above"));
    }

    @Test
        //test that when one player dies they respawn at the start
    void testMultiPlayerDeath() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: get axe", gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("tree"));
        CommandParser.parseCommand("simon: get key", gameState);
        response = CommandParser.parseCommand("simon: inv", gameState);
        assertTrue(response.contains("key"));
        assertTrue(response.contains("axe"));
        CommandParser.parseCommand("simon: goto cabin", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("trapdoor"));
        CommandParser.parseCommand("simon: open trapdoor", gameState);
        response = CommandParser.parseCommand("simon: goto cellar", gameState);
        assertTrue(response.contains("elf"));
        response = CommandParser.parseCommand("sion: look", gameState);
        assertTrue(response.contains("trapdoor"));
        assertFalse(response.contains("simon"));
        CommandParser.parseCommand("simon: hit elf", gameState);
        CommandParser.parseCommand("simon: hit elf", gameState);
        CommandParser.parseCommand("simon: hit elf", gameState);
        response = CommandParser.parseCommand("sion: look", gameState);
        assertTrue(response.contains("trapdoor"));
        assertTrue(response.contains("simon"));
        CommandParser.parseCommand("sion: goto cellar", gameState);
        response = CommandParser.parseCommand("sion: look", gameState);
        assertTrue(response.contains("elf"));
        assertTrue(response.contains("axe"));
        CommandParser.parseCommand("sion: get axe", gameState);
        response = CommandParser.parseCommand("sion: inv", gameState);
        assertTrue(response.contains("axe"));
    }

    @Test
    void testNoPlayerNameWithColon() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        assertDoesNotThrow(() -> CommandParser.parseCommand(": look", gameState));
        String response = CommandParser.parseCommand(": look", gameState);
        assertEquals(ResponseList.noUserName(), response);
    }

    @Test
    void testNoPlayerNameWithOutColon() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        assertDoesNotThrow(() -> CommandParser.parseCommand("look", gameState));
        String response = CommandParser.parseCommand("look", gameState);
        assertEquals(ResponseList.noUserName(), response);
    }

    @Test
    void testMultipleColon() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        assertDoesNotThrow(() -> CommandParser.parseCommand(":look :", gameState));
    }

    @Test
    void testInvalidPlayerName() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        assertDoesNotThrow(() -> CommandParser.parseCommand("!!!&*(: look", gameState));
        String response = CommandParser.parseCommand("!!!&*(: look", gameState);
        assertEquals(ResponseList.badUserName("!!!&*("), response);
    }

    @Test
    void testStoreRoomCreated() {
        File entitiesFile = new File("config" + File.separator + "extended-entities-no-storeroom.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        assertTrue(gameState.getEntityMap("location").containsKey("storeroom"));
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: get key", gameState);
        CommandParser.parseCommand("simon: goto cabin ", gameState);
        CommandParser.parseCommand("simon: open trapdoor", gameState);
        GameEntity key = gameState.getEntityMap("artefact").get("key");
        assertEquals("storeroom", key.getLocationName());
    }

    @Test
    void testPlayerHasReservedName() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        assertTrue(gameState.getEntityMap("location").containsKey("storeroom"));
        CommandParser.parseCommand("get: get axe", gameState);
        CommandParser.parseCommand("get: goto forest", gameState);
        String response = CommandParser.parseCommand("get: look", gameState);
        assertEquals(LookCommand.execute(gameState, "get(player)"), response);
        response = CommandParser.parseCommand("look: look", gameState);
        assertEquals(LookCommand.execute(gameState, "look(player)"), response);
        response = CommandParser.parseCommand("horn: look", gameState);
        assertEquals(LookCommand.execute(gameState, "horn(player)"), response);
        CommandParser.parseCommand("get: goto forest", gameState);
        CommandParser.parseCommand("get: goto riverbank", gameState);
        CommandParser.parseCommand("get: blow horn", gameState);
        response = CommandParser.parseCommand("get: look", gameState);
        assertTrue(response.contains("lumberjack"));
        CommandParser.parseCommand("lumberjack: look", gameState);
    }

    @Test
    void testMultipleTriggers() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
       GameAction cut = gameState.getGameActions().get("cut").get(0);
        assertTrue(gameState.getEntityMap("location").containsKey("storeroom"));
        CommandParser.parseCommand("simon: goto forest", gameState);
        String response = CommandParser.parseCommand("simon: tree cut down and cut and chop down", gameState);
        assertEquals(CustomCommand.execute(gameState, cut, "simon(player)"), response);

    }

}
