package edu.uob;

import edu.uob.Commands.*;
import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationEntity;
import edu.uob.GameEntity.PlayerEntity;

import java.util.*;

public class CommandParser {

    private static final Set<String> BASIC_ACTIONS = Set.of(
            "inventory", "inv", "get", "drop", "goto", "look"
    );

    public static String parseCommand(String command, GameState gameState) {
        LinkedList<String> tokenList = CommandParser.tokeniseString(command);
        if (tokenList.isEmpty()) {
            return ResponseList.noUserName();
        } else if (tokenList.size() < 2) {
            return ResponseList.noCommand(tokenList.get(0));
        }
        //add '(Player)' to end of playerName to avoid possibility of clash with other entity names
        String playerName = Utils.addPlayerTag(tokenList.get(0));
        tokenList.set(0, playerName);
        if (!gameState.getEntityMap("player").containsKey(playerName)) {
            gameState.addPlayer(playerName);
        }
        return CommandParser.parseAction(tokenList, gameState);
    }

    private static String parseAction(LinkedList<String> tokenList, GameState gameState) {
        //first check if there is 1 basic action - if 2+ then reject immediately
        Map<String, GameAction> customActionMap = gameState.getGameActions();
        int basicActionCount = (int) tokenList.stream().filter(BASIC_ACTIONS :: contains).count();
        if (basicActionCount == 1) {
            String action = tokenList.stream()
                    .filter(BASIC_ACTIONS::contains)
                    .findFirst()
                    .get();
            int actionIndex = tokenList.indexOf(action);
            return CommandParser.parseBasicAction(tokenList, gameState, tokenList.get(actionIndex));
        } else if (basicActionCount == 0) {

        }
        //fix below

        int customActionCount = (int) customActionMap.keySet().stream().filter(tokenList :: contains).count();
        int actionCount = customActionCount + basicActionCount;
        if (actionCount > 1) {
            return ResponseList.tooManyActions();
        } else if (actionCount == 0) {
            return ResponseList.noActionFound();
        }
        return CommandParser.parseCustomAction(tokenList, gameState);
    }

    private static String parseBasicAction(LinkedList<String> tokenList, GameState gameState, String basicAction) {
        switch (basicAction) {
            case "inventory", "inv":
                return CommandParser.parseInventory(tokenList, gameState);
            case "get":
                return CommandParser.parseGet(tokenList, gameState);
            case "drop":
                return CommandParser.parseDrop(tokenList, gameState);
            case "goto":
                return CommandParser.parseGoTo(tokenList, gameState);
            case "look":
                return CommandParser.parseLook(tokenList, gameState);
            default:
                return ResponseList.noActionFound();
        }
    }

    private static String parseLook(LinkedList<String> tokenList, GameState gameState) {
        if (tokenList.get(1).equals("look") && tokenList.size() == 2) {
            return LookCommand.execute(gameState, tokenList.get(0));
        } else {
            return ResponseList.badLookCommand();
        }
    }

    private static String parseGoTo(LinkedList<String> tokenList, GameState gameState) {
        if (!(tokenList.contains("goto") && tokenList.size() == 3)) {
            return ResponseList.badGoToCommand();
        }
        String targetLocation = getSubjectName("goto", tokenList);
        String playerLocationName =  gameState.getPlayerLocation(tokenList.get(0));
        LocationEntity playerLocation = (LocationEntity) gameState.getEntityMap("location")
                .get(playerLocationName);
        Map <String, LocationEntity> visiblePaths = playerLocation.getPaths();
        if (visiblePaths.containsKey(targetLocation)) {
            return GoToCommand.execute(gameState, tokenList.get(0), targetLocation);
        } else {
            return ResponseList.pathDoesNotExist();
        }
    }

    private static String parseDrop(LinkedList<String> tokenList, GameState gameState) {
        if (!(tokenList.contains("drop") && tokenList.size() == 3)) {
            return ResponseList.badDropCommand();
        }
        String itemToDrop = getSubjectName("drop", tokenList);
        PlayerEntity currentPlayer = (PlayerEntity) gameState.getEntityMap("player").get(tokenList.get(0));
        Map<String, GameEntity> playerInventory = currentPlayer.getInventory();
        if (playerInventory.containsKey(itemToDrop)) {
            return DropCommand.execute(gameState, currentPlayer.getName(), itemToDrop);
        } else {
            return ResponseList.noItemInventory(itemToDrop);
        }
    }

    private static String parseGet(LinkedList<String> tokenList, GameState gameState) {
        if (!(tokenList.contains("get") && tokenList.size() == 3)) {
            return ResponseList.badGetCommand();
        }
        String itemToGet = getSubjectName("get", tokenList);
        String playerLocation =  gameState.getPlayerLocation(tokenList.get(0));
        String itemType = gameState.getEntityTypeFromName(itemToGet);
        Map<String, GameEntity> nearbyEntities = gameState.getEntitiesFromLocation("all", playerLocation);
        if (nearbyEntities.containsKey(itemToGet)) {
            return parseGetResponse(tokenList, gameState, itemType, itemToGet);
        } else {
            return ResponseList.itemCannotBeFound(itemToGet);
        }
    }

    private static String getSubjectName(String keyword,LinkedList<String> tokenList) {
        String targetItem;
        if (tokenList.get(1).equals(keyword)) {
            targetItem = tokenList.get(2);
        } else {
            targetItem = tokenList.get(1);
        }
        return targetItem;
    }

    private static String parseGetResponse(LinkedList<String> tokenList, GameState gameState,
                                           String itemType, String itemToGet) {
        switch (itemType) {
            case "artefact":
                return GetCommand.execute(gameState, tokenList.get(0), itemToGet);
            case "furniture":
                return ResponseList.pickUpFurniture(itemToGet);
            case "player", "character":
                if (Utils.removePlayerTag(tokenList.get(0)).equals(itemToGet)) {
                    return ResponseList.pickUpYourself();
                }
                return ResponseList.pickUpPlayer(itemToGet);
            default:
                return ResponseList.pickUpImpossible();
        }
    }

    private static String parseInventory(LinkedList<String> tokenList, GameState gameState) {
        if (tokenList.get(1).equals("inventory") || tokenList.get(1).equals("inv")) {
            return InventoryCommand.execute(gameState, tokenList.get(0));
        } else {
            return ResponseList.badInvCommand();
        }
    }

    private static String parseCustomAction(LinkedList<String> tokenList, GameState gameState) {
        return null;

    }

    //TODO make this deal with punctuation - although apparently this isn't tested
    private static LinkedList<String> tokeniseString(String command) {
        LinkedList<String> tokenList = new LinkedList<>();
        if (!command.contains(":")) {
            return tokenList;
        }
        String userName = command.substring(0, command.indexOf(":")).trim();
        tokenList.add(userName.toLowerCase());
        command = command.substring(command.indexOf(":") + 1);
        StringTokenizer tokenizer = new StringTokenizer(command, " ");
        while (tokenizer.hasMoreTokens()) {
            String rawToken = tokenizer.nextToken().toLowerCase();
            tokenList.add(rawToken.trim().replaceAll("\\s+", " "));
        }
        return tokenList;
    }


}
