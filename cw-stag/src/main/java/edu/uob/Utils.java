package edu.uob;

public class Utils {
    //function to add a (player) tag to players
    public static String removePlayerTag(String playerName) {
        if (playerName.contains("(player)")) {
            playerName = playerName.replace("(player)", "");
        }
        return playerName;
    }

    //function to remove a (player) tag on players
    public static String addPlayerTag(String playerName) {
        if (!playerName.contains("(player)")) {
            return new StringBuilder(playerName).append("(player)").toString();
        }
        return playerName;
    }
}
