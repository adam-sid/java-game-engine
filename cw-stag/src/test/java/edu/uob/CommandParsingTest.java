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

    void testElfAttack() {
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
}
