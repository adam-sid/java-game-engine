package edu.uob;

import org.junit.jupiter.api.Test;

import java.io.File;

public class CommandParsingTest {

    public File entitiesFile = new File("config" + File.separator + "basic-entities.dot");

    @Test
    void testParseCommand() {
        EntityFileParser parser = new EntityFileParser(entitiesFile);
        GameState gameState = parser.getGameState();
        String command = "simon: open\t  door    with tree  ";
        CommandParser.parseCommand(command, gameState);
    }
}
