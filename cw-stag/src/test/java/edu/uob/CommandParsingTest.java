package edu.uob;

import org.junit.jupiter.api.Test;

import java.io.File;

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

}
