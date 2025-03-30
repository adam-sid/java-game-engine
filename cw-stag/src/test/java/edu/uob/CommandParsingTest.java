package edu.uob;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandParsingTest {

    public File entitiesFile = new File("config" + File.separator + "basic-entities.dot");

    @Test
    void testParseCommand() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: get axe", gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: cut down tree with axe", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertFalse(response.contains("tree"));
        CommandParser.parseCommand("simon: get log", gameState);
        CommandParser.parseCommand("simon: goto riverbank", gameState);
        CommandParser.parseCommand("simon: make bridge with log", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("clearing"));
    }
}
