package edu.uob;

import java.util.LinkedList;
import java.util.StringTokenizer;

public class CommandParser {

    public static String parseCommand(String command, GameState gameState) {
        LinkedList<String> tokenList = tokeniseString(command);
        //add '(Player)' to end of playerName to avoid possibility of clash with other entity names
        StringBuilder nameBuilder = new StringBuilder(tokenList.get(0)).append("(player)");
        String playerName = nameBuilder.toString();
        if (!gameState.getEntityMap("player").containsKey(playerName)) {
            gameState.addPlayer(playerName);
        }

        return null;
    }

    private static LinkedList<String> tokeniseString(String command) {
        LinkedList<String> tokenList = new LinkedList<>();
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
