package edu.uob;

public class Utils {
    public static String removePlayerTag(String playerName) {
        if (playerName.contains("(player)")) {
            playerName = playerName.replace("(player)", "");
        }
        return playerName;
    }

    public static String addPlayerTag(String playerName) {
        if (!playerName.contains("(player)")) {
            return new StringBuilder(playerName).append("(player)").toString();
        }
        return playerName;
    }
}
