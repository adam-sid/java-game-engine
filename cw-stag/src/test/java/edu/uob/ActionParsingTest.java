package edu.uob;

import edu.uob.GameAction.GameAction;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionParsingTest {

    @Test
    void testParseAction() {
        File entitiesFile = new File("config" + File.separator + "basic-entities.dot");
        File actionsFile = new File("config" + File.separator + "basic-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        Map<String, GameAction> actionHashMap = gameState.getGameActions();
        GameAction cutAction = actionHashMap.get("cut");
    }
}
