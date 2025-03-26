package edu.uob;

import edu.uob.GameAction.GameAction;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionParsingTest {

    @Test
    void testParseAction() {
        GameActionParser parser = new GameActionParser("basic-actions.xml");
        HashMap<String, HashSet<GameAction>> actionMap = parser.getActions();
        assertEquals(5+2, actionMap.size());
    }
}
