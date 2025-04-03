package edu.uob;

import edu.uob.GameCommand.CustomCommand;
import edu.uob.GameCommand.LookCommand;
import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.GameEntity;
import edu.uob.GameParsing.ActionFileParser;
import edu.uob.GameParsing.CommandParser;
import edu.uob.GameParsing.EntityFileParser;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class CommandParsingTest {

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
        assertEquals(LookCommand.executeCommand(gameState, "get(player)"), response);
        response = CommandParser.parseCommand("look: look", gameState);
        assertEquals(LookCommand.executeCommand(gameState, "look(player)"), response);
        response = CommandParser.parseCommand("horn: look", gameState);
        assertEquals(LookCommand.executeCommand(gameState, "horn(player)"), response);
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
        CommandParser.parseCommand("axe: look", gameState);
        CommandParser.parseCommand("tree: look", gameState);
        CommandParser.parseCommand("simon: get axe", gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        String response = CommandParser.parseCommand("simon: tree cut down and cut and chop down axe", gameState);
        assertEquals(CustomCommand.executeCommand(gameState, cut, "simon(player)"), response);
    }

    @Test
    void testMultipleTriggersAgain() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        GameAction unlock = gameState.getGameActions().get("unlock").get(0);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: get key", gameState);
        CommandParser.parseCommand("simon: goto cabin", gameState);
        String response = CommandParser.parseCommand("simon: unlock and open trapdoor", gameState);
        assertEquals(CustomCommand.executeCommand(gameState, unlock, "simon(player)"), response);
    }

    @Test
    void testHitAndPayElfTriggers() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        assertTrue(gameState.getEntityMap("location").containsKey("storeroom"));
        CommandParser.parseCommand("coin: get axe", gameState);
        String response = CommandParser.parseCommand("coin: look", gameState);
        assertTrue(response.contains("coin"));
        response = CommandParser.parseCommand("simon: look", gameState);
        assertFalse(response.contains("axe"));
        CommandParser.parseCommand("simon: get coin", gameState);
        response = CommandParser.parseCommand("coin: look", gameState);
        assertFalse(response.contains("coin"));
        CommandParser.parseCommand("simon: drop coin", gameState);
        response = CommandParser.parseCommand("coin: look", gameState);
        assertTrue(response.contains("coin"));
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: get key", gameState);
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("simon: unlock trapdoor", gameState);
        CommandParser.parseCommand("simon: goto cellar", gameState);
        CommandParser.parseCommand("simon: hit and pay elf", gameState);
        assertEquals(2, gameState.getPlayer("simon").getHealth());
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("simon: get coin", gameState);
        CommandParser.parseCommand("simon: goto cellar", gameState);
        CommandParser.parseCommand("simon: hit and pay elf", gameState);
        assertEquals(2, gameState.getPlayer("simon").getHealth());
        assertTrue(gameState.getPlayer("simon").getInventory().containsKey("coin"));
    }

    @Test
    void listAllSubjects() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: get axe", gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: tree cut down and cut and chop down axe", gameState);
        CommandParser.parseCommand("simon: get key", gameState);
        CommandParser.parseCommand("simon: get log", gameState);
        String response = CommandParser.parseCommand("simon: inv", gameState);
        assertTrue(response.contains("log"));
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("simon: unlock and open trapdoor with key", gameState);
        CommandParser.parseCommand("simon: goto cellar", gameState);
        CommandParser.parseCommand("simon: hit and pay elf", gameState);

    }

    @Test
    void producedEntityInOtherPlayerInv() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions-produced-artefact.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: goto riverbank", gameState);
        CommandParser.parseCommand("simon: blow horn", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("potion"));
        CommandParser.parseCommand("sion: goto forest", gameState);
        CommandParser.parseCommand("sion: goto riverbank", gameState);
        CommandParser.parseCommand("sion: get potion", gameState);
        CommandParser.parseCommand("sion: goto forest", gameState);
        CommandParser.parseCommand("sion: goto cabin", gameState);
        CommandParser.parseCommand("simon: blow horn", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertFalse(response.contains("potion"));
        CommandParser.parseCommand("sion: drop potion", gameState);
        CommandParser.parseCommand("simon: blow horn", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("potion"));
    }

    @Test
    void testConsumePaths() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions-consume-path.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: goto riverbank", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("forest"));
        CommandParser.parseCommand("simon: blow horn", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertFalse(response.contains("forest"));
    }

    @Test
    void testConsumePathsPathDoesNotExist() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions-consume-path.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: get key", gameState);
        CommandParser.parseCommand("simon: goto riverbank", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("forest"));
        CommandParser.parseCommand("simon: get horn", gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("simon: unlock key", gameState);
        CommandParser.parseCommand("simon: goto cellar", gameState);
        CommandParser.parseCommand("simon: blow horn", gameState);
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("sion: blow horn", gameState);
        response = CommandParser.parseCommand("sion: look", gameState);
        assertTrue(response.contains("forest"));
        CommandParser.parseCommand("simon: blow horn", gameState);
        response = CommandParser.parseCommand("sion: look", gameState);
        assertFalse(response.contains("forest"));
    }

    @Test
    void testHealthDoesNotExceedThree() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("potion"));
        CommandParser.parseCommand("simon: drink potion", gameState);
        assertEquals(3, gameState.getPlayer("simon").getHealth());
        response = CommandParser.parseCommand("simon: look", gameState);
        assertFalse(response.contains("potion"));
    }

    @Test
    void testMultipleHealth() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions-health.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: get key", gameState);
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("simon: unlock trapdoor", gameState);
        CommandParser.parseCommand("simon: goto cellar", gameState);
        CommandParser.parseCommand("simon: attack elf", gameState);
        assertEquals(1, gameState.getPlayer("simon").getHealth());
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("simon: drink potion", gameState);
        assertEquals(3, gameState.getPlayer("simon").getHealth());
    }

    @Test
    void testEndGameAndCaseSensitivityAndWhiteSpace() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        assertTrue(gameState.getEntityMap("location").containsKey("storeroom"));
        CommandParser.parseCommand("simon: GET cOiN", gameState);
        CommandParser.parseCommand("simon: goTo     Forest", gameState);
        CommandParser.parseCommand("simon:      get    key", gameState);
        CommandParser.parseCommand("simon: gotO    CABIN", gameState);
        CommandParser.parseCommand("simon: unlock    trAPdoor", gameState);
        CommandParser.parseCommand("simon:   goto  CEllar", gameState);
        CommandParser.parseCommand("elf: goto cellar  ", gameState);
        CommandParser.parseCommand("simon: pAy Elf  ", gameState);
        CommandParser.parseCommand("simon: gEt shovel", gameState);
        CommandParser.parseCommand("simon: Goto CABin", gameState);
        CommandParser.parseCommand("simon: geT aXe", gameState);
        CommandParser.parseCommand("simon: Goto forest", gameState);
        CommandParser.parseCommand("simon: choP aXe", gameState);
        CommandParser.parseCommand("simon: get lOg", gameState);
        CommandParser.parseCommand("simon: Goto    riverbank", gameState);
        CommandParser.parseCommand("simon: make   bridge  with lOG ", gameState);
        CommandParser.parseCommand("simon: goto clEAring", gameState);
        CommandParser.parseCommand("simon: dig grOUnd", gameState);
        CommandParser.parseCommand("simon: get   GolD", gameState);
        String response = CommandParser.parseCommand("simon: INV", gameState);
        assertTrue(response.contains("gold"));
    }

    @Test
    void testUsePlayerNameInCommand() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: get axe", gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("sion: goto forest", gameState);
        CommandParser.parseCommand("simon: cut down tree with sion", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertFalse(response.contains("log"));
        CommandParser.parseCommand("axe: goto forest", gameState);
        CommandParser.parseCommand("elf: goto forest", gameState);
        CommandParser.parseCommand("tree: goto forest", gameState);
        CommandParser.parseCommand("cut down: goto forest", gameState);
        CommandParser.parseCommand("Unlock Smith: goto forest", gameState);
        CommandParser.parseCommand("simon: cut down tree with axe and John Smith and Unlock Smith", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("log"));
    }

    @Test
    void testProduceOnDeath() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions-produce-death.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: get key", gameState);
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("simon: get coin", gameState);
        CommandParser.parseCommand("sion: get axe", gameState);
        CommandParser.parseCommand("simon: unlock trapdoor", gameState);
        CommandParser.parseCommand("simon: goto cellar", gameState);
        CommandParser.parseCommand("sion: goto cellar", gameState);
        CommandParser.parseCommand("simon: attack elf", gameState);
        assertEquals("cabin", gameState.getPlayer("simon").getLocationName());
        String response = CommandParser.parseCommand("sion: look", gameState);
        assertTrue(response.contains("coin"));
        assertTrue(response.contains("lumberjack"));
        CommandParser.parseCommand("simon: goto cellar", gameState);
        CommandParser.parseCommand("sion: attack elf", gameState);
        response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("axe"));
        assertTrue(response.contains("lumberjack"));
        assertEquals("cabin", gameState.getPlayer("sion").getLocationName());
    }

    @Test
    void testSubjectIsLocation() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions-location-subject.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: get axe", gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: cut down tree", gameState);
        String response = CommandParser.parseCommand("simon: look", gameState);
        assertTrue(response.contains("log"));
    }

    @Test
    void testVeryAmbiguous() {
        File entitiesFile = new File("config" + File.separator + "extended-entities-very-ambiguous.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions-very-ambiguous.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("adam: goto forest", gameState);
        CommandParser.parseCommand("adam: get key", gameState);
        CommandParser.parseCommand("adam: goto cabin", gameState);
        CommandParser.parseCommand("adam: open sesame wizard trapdoor", gameState);
        String response = CommandParser.parseCommand("adam: look", gameState);
        assertFalse(response.contains("lumberjack"));
        assertFalse(response.contains("cellar"));
        response = CommandParser.parseCommand("adam: Unlock cabin", gameState);
        assertTrue(response.equals(ResponseList.ambiguousCommand()));
        CommandParser.parseCommand("adam: open sesame trapdoor", gameState);
        response = CommandParser.parseCommand("adam: look", gameState);
        assertTrue(response.contains("cellar"));
        //CommandParser.parseCommand("adam: open sesame wizard", gameState);
        CommandParser.parseCommand("adam: Unlock cabin wizard", gameState);
        response = CommandParser.parseCommand("adam: look", gameState);
        assertTrue(response.contains("lumberjack"));
    }

    @Test
    void testNameIsTrigger() {
        File entitiesFile = new File("config" + File.separator + "extended-entities.dot");
        File actionsFile = new File("config" + File.separator + "extended-actions-action-has-name.xml");
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        GameState gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, gameState);
        CommandParser.parseCommand("simon: goto forest", gameState);
        CommandParser.parseCommand("simon: get key", gameState);
        CommandParser.parseCommand("simon: goto cabin", gameState);
        CommandParser.parseCommand("simon: say open simon key trapdoor ", gameState);
    }
}
