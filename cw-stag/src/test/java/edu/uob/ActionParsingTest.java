package edu.uob;

import edu.uob.GameAction.GameAction;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionParsingTest {

    @Test
    void testParseAction() {
        GameStateParser stateParser = new GameStateParser("basic-entities.dot");
        GameState gameState = stateParser.getGameState();
        GameActionParser.parseActionFile("basic-actions.xml", gameState);
        //assertEquals(5+2, actionMap.size());
    }
}
