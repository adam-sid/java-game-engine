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

    private static final Set<Character> VALID_CHARS = Set.of(
            ' ', '\'', '-'
    );

    public static String parseCommand(String command, GameState gameState) {
        LinkedList<String> tokenList = CommandParser.tokeniseCommandString(command);
        if (tokenList.isEmpty()) {
            return ResponseList.noUserName();
        } else if (tokenList.size() < 2) {
            return ResponseList.noCommand(tokenList.get(0));
        }
        //add '(Player)' to end of playerName to avoid possibility of clash with entity
        if (CommandParser.invalidUserName(tokenList.get(0))) {
            return ResponseList.badUserName(tokenList.get(0));
        }
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

    private static boolean invalidUserName(String userName) {
        for (char stringChar : userName.toCharArray()) {
            if (!(Character.isLetter(stringChar) || VALID_CHARS.contains(stringChar))) {
                return true;
            }
        }
        return false;
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
            Map <String, List<GameAction>> executableActionMap = parseCustomAction(tokenList, gameState);
            List<GameAction> executableActionList = executableActionMap.values().stream().toList()
                    .stream().flatMap(List :: stream).toList();
            boolean actionsAreSame = executableActionList.stream().distinct().count() == 1;
            if (actionsAreSame) {
                GameAction triggeredAction = executableActionList.get(0);
                return CustomCommand.execute(gameState, triggeredAction, tokenList.get(0));
            } else if (!executableActionList.isEmpty()) {
                return ResponseList.ambiguousCommand();
            }
        }
        return ResponseList.noActionFound();
    }

    private static Map<String, List<GameAction>> parseCustomAction(LinkedList<String> tokenList, GameState gameState) {
        Map<String, List<GameAction>> customActionMap = gameState.getGameActions();
        Map <String, List<GameAction>> executableActionMap = new HashMap<>();
        for (Map.Entry<String, List<GameAction>> actionEntry : customActionMap.entrySet()) {
            String triggerPhrase = actionEntry.getKey();
            LinkedList<String> triggerTokens = new LinkedList<>();
            if (!triggerPhrase.contains(" ")) {
                if (tokenList.contains(triggerPhrase)) {
                    triggerTokens.add(triggerPhrase);
                    List<GameAction> validActions = CommandParser
                            .parseActionList(gameState, tokenList, actionEntry.getValue());
                    executableActionMap.put(triggerPhrase, validActions);
                }
            } else {
                CommandParser.tokeniseString(triggerPhrase, triggerTokens);
                if (CommandParser.commandContainsTrigger(tokenList, triggerTokens)) {
                    triggerTokens.add(triggerPhrase);
                    List<GameAction> validActions = CommandParser
                            .parseActionList(gameState, tokenList, actionEntry.getValue());
                    executableActionMap.put(triggerPhrase, validActions);
                }
            }
        }
        return executableActionMap;
    }

    private static List<GameAction> parseActionList(GameState gameState, LinkedList<String> tokenList,
                                                    List<GameAction> possibleActions) {
        List<GameAction> validActionList = new LinkedList<>();
        for (GameAction action : possibleActions) {
            if(canActionExecute(gameState, tokenList, action)) {
                validActionList.add(action);
            }
        }
        return validActionList;
    }

    private static boolean commandContainsTrigger(LinkedList<String> tokenList, LinkedList<String> triggerTokens) {
        if (triggerTokens.size() > tokenList.size() - 1) {
            return false;
        }
        int triggerIndex = 0;
        for (String token : tokenList) {
            if (token.equals(triggerTokens.get(triggerIndex))) {
                triggerIndex++;
                if (triggerIndex == triggerTokens.size()) {
                    return true;
                }
            } else {
                triggerIndex = 0;
            }
        }
        return false;
    }

    //TODO this doesnt work on multiple triggers - use debug to work out why
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
        String playerLocationName =  gameState.getPlayerLocation(tokenList.get(0)).getLocationName();
        LocationEntity playerLocation = (LocationEntity) gameState.getEntityMap("location")
                .get(playerLocationName);
        Map <String, LocationEntity> visiblePaths = playerLocation.getPaths();
        if (visiblePaths.containsKey(targetLocation)) {
            return GoToCommand.execute(gameState, tokenList.get(0), targetLocation);
        } else {
            if (Objects.equals(playerLocationName, targetLocation)) {
                return ResponseList.alreadyAtLocation();
            }
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
        String playerLocation =  gameState.getPlayerLocation(tokenList.get(0)).getLocationName();
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
                if (tokenList.get(0).equals(itemToGet)) {
                    return ResponseList.pickUpYourself();
                }
                return ResponseList.pickUpPlayer(itemToGet);
            default:
                return ResponseList.pickUpImpossible();
        }
    }

    private static String parseInventory(LinkedList<String> tokenList, GameState gameState) {
        if ((tokenList.get(1).equals("inventory") || tokenList.get(1).equals("inv")) && tokenList.size() == 2) {
            return InventoryCommand.execute(gameState, tokenList.get(0));
        } else {
            return ResponseList.badInvCommand();
        }
    }

    private static LinkedList<String> tokeniseCommandString(String command) {
        LinkedList<String> tokenList = new LinkedList<>();
        if (!command.contains(":")) {
            return new LinkedList<>();
        }
        String userName = command.substring(0, command.indexOf(":")).trim();
        if (userName.isEmpty()) {
            return new LinkedList<>();
        }
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
        Set<String> customActionTriggers = gameState.getGameActions().keySet();
        Map<String, GameEntity> players = gameState.getEntityMap("player");
        Map<String, GameEntity> allEntitiesButPlayer = new HashMap<>(gameState.getEntityMap("all"));
        allEntitiesButPlayer.keySet().removeAll(players.keySet());
        List<String> playerNames = players.keySet().stream().toList();
        LinkedList<String> bufferList = new LinkedList<>(tokenList);
        for (int i = 0; i < bufferList.size(); i++) {
            String tokenNoTag = (bufferList.get(i));
            String tokenTag = Utils.addPlayerTag(bufferList.get(i));
            boolean a = playerNames.contains(tokenTag);
            boolean b = allEntitiesButPlayer.containsKey(tokenNoTag);
            boolean c = BASIC_ACTIONS.contains(tokenNoTag);
            boolean d = customActionTriggers.contains(tokenNoTag);
            if(playerNames.contains(tokenTag) && !allEntitiesButPlayer.containsKey(tokenNoTag) &&
                !BASIC_ACTIONS.contains(tokenNoTag) && !customActionTriggers.contains(tokenNoTag)) {
                playerIndices.add(i);
            }
        }
        return playerIndices;
    }
}
