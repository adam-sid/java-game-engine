package edu.uob;

import edu.uob.Commands.*;
import edu.uob.GameAction.GameAction;
import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationEntity;
import edu.uob.GameEntity.PlayerEntity;

import java.util.*;

public class CommandParser {

    private static final Set<String> BASIC_ACTIONS = Set.of(
            "inventory", "inv", "get", "drop", "goto", "look", "health"
    );

    public static String parseCommand(String command, GameState gameState) {
        LinkedList<String> tokenList = CommandParser.tokeniseCommandString(command);
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
        Set<Integer> playerTokens = CommandParser.whichTokenPlayer(gameState, tokenList);
        for (Integer playerToken : playerTokens) {
            String taggedName = Utils.addPlayerTag(tokenList.get(playerToken));
            tokenList.set(playerToken, taggedName);
        }
        return CommandParser.parseAction(tokenList, gameState);
    }

    private static String parseAction(LinkedList<String> tokenList, GameState gameState) {
        //first check if there is 1 basic action - if 2+ then reject immediately
        int basicActionCount = (int) tokenList.stream().filter(BASIC_ACTIONS :: contains).count();
        if (basicActionCount == 1) {
            String action = tokenList.stream()
                    .filter(BASIC_ACTIONS::contains)
                    .findFirst()
                    .get();
            int actionIndex = tokenList.indexOf(action);
            return CommandParser.parseBasicAction(tokenList, gameState, tokenList.get(actionIndex));
        } else if (basicActionCount == 0) {
            Map <String, GameAction> executableActionMap = parseCustomAction(tokenList, gameState);
            boolean actionsAreSame = executableActionMap.values().stream().distinct().count() == 1;
            if (executableActionMap.size() == 1 || (executableActionMap.size() > 1 && actionsAreSame)) {
                String trigger = executableActionMap.keySet().iterator().next();
                GameAction triggeredAction = gameState.getGameActions().get(trigger);
                return CustomCommand.execute(gameState, triggeredAction, tokenList.get(0));
            } else if (!executableActionMap.isEmpty()) {
                return ResponseList.ambiguousCommand();
            }
        }
        return ResponseList.noActionFound();
    }

    //TODO work on rejecting extraneous subjects

    private static Map <String, GameAction> parseCustomAction(LinkedList<String> tokenList, GameState gameState) {
        Map<String, GameAction> customActionMap = gameState.getGameActions();
        Map <String, GameAction> executableActionMap = new HashMap<>();
        for (Map.Entry<String, GameAction> actionEntry : customActionMap.entrySet()) {
            String triggerPhrase = actionEntry.getKey();
            LinkedList<String> triggerTokens = new LinkedList<>();
            if (!triggerPhrase.contains(" ")) {
                if (tokenList.contains(triggerPhrase)) {
                    triggerTokens.add(triggerPhrase);
                    if(canActionExecute(gameState, tokenList, actionEntry.getValue())) {
                        executableActionMap.put(triggerPhrase, actionEntry.getValue());
                    }
                }
            } else {
                CommandParser.tokeniseString(triggerPhrase, triggerTokens);
                if (CommandParser.commandContainsTrigger(tokenList, triggerTokens)) {
                    if(CommandParser.canActionExecute(gameState, tokenList, actionEntry.getValue())) {
                        executableActionMap.put(triggerPhrase, actionEntry.getValue());
                    }
                }
            }

        }
        return executableActionMap;
    }

    private static boolean commandContainsTrigger(LinkedList<String> tokenList, LinkedList<String> triggerTokens) {
        if (triggerTokens.size() > tokenList.size() - 1) {
            return false;
        }
        int triggerIndex = 0;
        for (String token : tokenList) {
            if (token.equals(triggerTokens.get(triggerIndex))) {
                triggerIndex++;
                if (triggerIndex == triggerTokens.size() - 1) {
                    return true;
                }
            } else {
                triggerIndex = 0;
            }
        }
        return false;
    }

    private static Boolean canActionExecute(GameState gameState, LinkedList<String> tokenList,
                                            GameAction triggeredAction) {
        LinkedList<String> bufferList = new LinkedList<>(tokenList.subList(1, tokenList.size()));
        PlayerEntity player = gameState.getPlayer(tokenList.get(0));
        Map<String, GameEntity> actionSubjects = triggeredAction.getSubjectEntities();
        Map<String, GameEntity> subjectsInLocation = gameState.
                getEntitiesFromLocation("all", player.getLocationName());
        Map<String, GameEntity> subjectsInInventory = player.getInventory();
        Map<String, GameEntity> availableSubjects = new HashMap<>(subjectsInLocation);
        availableSubjects.putAll(subjectsInInventory);
        //check that all prerequisite subjects are available
        boolean subjectsAvailable = availableSubjects.keySet().containsAll(actionSubjects.keySet());
        //check at least one of the subjects needed for action is in command
        boolean subjectInString = actionSubjects.keySet().stream().anyMatch(tokenList::contains);
        //check if there are not any 'extraneous' subjects
        Map<String, GameEntity> extraneousSubjects = new HashMap<>(gameState.getEntityMap("all"));
        extraneousSubjects.putAll(subjectsInInventory);
        extraneousSubjects.keySet().removeAll(actionSubjects.keySet());
        boolean extraneousSubjectExists = extraneousSubjects.keySet().stream().anyMatch(bufferList::contains);
        return subjectsAvailable && subjectInString && !extraneousSubjectExists;
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
            case "health":
                return CommandParser.parseHealth(tokenList, gameState);
            default:
                return ResponseList.noActionFound();
        }
    }

    private static String parseHealth(LinkedList<String> tokenList, GameState gameState) {
        if (tokenList.get(1).equals("health") && tokenList.size() == 2) {
            return HealthCommand.execute(gameState, tokenList.get(0));
        } else {
            return ResponseList.badHealthCommand();
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
        String targetLocation = CommandParser.getSubjectName("goto", tokenList);
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
        String itemToDrop = CommandParser.getSubjectName("drop", tokenList);
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
        String itemToGet = CommandParser.getSubjectName("get", tokenList);
        String playerLocation =  gameState.getPlayerLocation(tokenList.get(0));
        String itemType = gameState.getEntityTypeFromName(itemToGet);
        Map<String, GameEntity> nearbyEntities = gameState.getEntitiesFromLocation("all", playerLocation);
        if (nearbyEntities.containsKey(itemToGet)) {
            return CommandParser.parseGetResponse(tokenList, gameState, itemType, itemToGet);
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

    private static LinkedList<String> tokeniseCommandString(String command) {
        LinkedList<String> tokenList = new LinkedList<>();
        if (!command.contains(":")) {
            return tokenList;
        }
        String userName = command.substring(0, command.indexOf(":")).trim();
        tokenList.add(userName.toLowerCase());
        command = command.substring(command.indexOf(":") + 1);
        CommandParser.tokeniseString(command, tokenList);
        return tokenList;
    }

    private static void tokeniseString(String string, LinkedList<String> tokenList) {
        StringTokenizer tokenizer = new StringTokenizer(string, " ");
        while (tokenizer.hasMoreTokens()) {
            String rawToken = tokenizer.nextToken().toLowerCase();
            tokenList.add(rawToken.trim().replaceAll("\\s+", ""));
        }
    }

    private static Set<Integer> whichTokenPlayer(GameState gameState, LinkedList<String> tokenList) {
        Set<Integer> playerIndices = new HashSet<>();
        Map<String, GameEntity> players = gameState.getEntityMap("player");
        Map<String, GameEntity> allEntitiesButPlayer = new HashMap<>(gameState.getEntityMap("all"));
        allEntitiesButPlayer.keySet().removeAll(players.keySet());
        List<String> playerNames = players.keySet().stream().toList();
        LinkedList<String> bufferList = new LinkedList<>(tokenList);
        for (int i = 0; i < bufferList.size(); i++) {
            String token = Utils.addPlayerTag(bufferList.get(i));
            if(playerNames.contains(token) && !allEntitiesButPlayer.containsKey(token)) {
                playerIndices.add(i);
            }
        }
        return playerIndices;
    }
}
