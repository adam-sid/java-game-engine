package edu.uob;

import edu.uob.Commands.LookCommand;
import edu.uob.GameAction.GameAction;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class CommandParser {

    private static final Set<String> BASIC_ACTIONS = Set.of(
            "inventory", "inv", "get", "drop", "goto", "look"
    );

    public static String parseCommand(String command, GameState gameState) {
        LinkedList<String> tokenList = tokeniseString(command);
        if (tokenList.isEmpty()) {
            return ResponseList.noUserName();
        } else if (tokenList.size() < 2) {
            return ResponseList.noCommand(tokenList.get(0));
        }
        //add '(Player)' to end of playerName to avoid possibility of clash with other entity names
        StringBuilder nameBuilder = new StringBuilder(tokenList.get(0)).append("(player)");
        String playerName = nameBuilder.toString();
        tokenList.set(0, playerName);
        if (!gameState.getEntityMap("player").containsKey(playerName)) {
            gameState.addPlayer(playerName);
        }
        return parseAction(tokenList, gameState);
    }

    private static String parseAction(LinkedList<String> tokenList, GameState gameState) {
        //first check if there is 1 basic action - if 2+ then reject immediately
        int basicActionCount = (int) tokenList.stream().filter(BASIC_ACTIONS :: contains).count();
        Map<String, GameAction> customActionMap = gameState.getGameActions();
        int customActionCount = (int) customActionMap.keySet().stream().filter(tokenList :: contains).count();
        int actionCount = customActionCount + basicActionCount;
        if (actionCount > 1) {
            return ResponseList.tooManyActions();
        } else if (actionCount == 0) {
            return ResponseList.noActionFound();
        }
        if (basicActionCount == 1) {
            String action = tokenList.stream()
                    .filter(BASIC_ACTIONS::contains)
                    .findFirst()
                    .get();
            int actionIndex = tokenList.indexOf(action);
            return parseBasicAction(tokenList, gameState, tokenList.get(actionIndex));
        }
        return parseCustomAction(tokenList, gameState);
    }

    private static String parseBasicAction(LinkedList<String> tokenList, GameState gameState, String basicAction) {
        switch (basicAction) {
            case "inventory":
                return parseInventory(tokenList, gameState);
            case "inv":
                return parseInventory(tokenList, gameState);
            case "get":
                return parseGet(tokenList, gameState);
            case "drop":
                return parseDrop(tokenList, gameState);
            case "goto":
                return parseGoTo(tokenList, gameState);
            case "look":
                return LookCommand.execute(gameState, tokenList.get(0));
            default:
                return ResponseList.noActionFound();
        }
    }

    private static String parseGoTo(LinkedList<String> tokenList, GameState gameState) {
        return null;
    }

    private static String parseDrop(LinkedList<String> tokenList, GameState gameState) {
        return null;
    }

    private static String parseGet(LinkedList<String> tokenList, GameState gameState) {
        return null;
    }

    private static String parseInventory(LinkedList<String> tokenList, GameState gameState) {
        return null;
    }

    private static String parseCustomAction(LinkedList<String> tokenList, GameState gameState) {
        return null;

    }


    //TODO make this deal with punctuation
    private static LinkedList<String> tokeniseString(String command) {
        LinkedList<String> tokenList = new LinkedList<>();
        if (!command.contains(":")) {
            return tokenList;
        }
        String userName = command.substring(0, command.indexOf(":")).trim();
        tokenList.add(userName);
        command = command.substring(command.indexOf(":") + 1);
        StringTokenizer tokenizer = new StringTokenizer(command, " ");
        while (tokenizer.hasMoreTokens()) {
            String rawToken = tokenizer.nextToken();
            tokenList.add(rawToken.trim().replaceAll("\\s+", " "));
        }
        return tokenList;
    }


}
