package edu.uob;

public class ResponseList {
    String response;

    public static String tooManyActions() {
        return "Invalid command";
    }

    public static String noActionFound() {
        return "No action found";
    }

    public static String noUserName() {
        return "You must specify a user to perform a command";
    }

    public static String noCommand(String userName) {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage.append("Hello ").append(userName).append(", please specify an action to perform");
        return buildMessage.toString();
    }

    public static String badLookCommand() {
        return "I don't understand that command, maybe you meant 'look'?";
    }

    public static String badInvCommand() {
        return "I don't understand that command, maybe you meant 'inventory' or 'inv' for short?";
    }

    public static String badGetCommand() {
        return "I don't understand that command, try saying 'get' followed by an artefact you can see";
    }

    public static String pickUpFurniture(String furnitureName) {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage.append("You try to pick up the ").append(furnitureName)
                .append(" but it is too heavy to lift");
        return buildMessage.toString();
    }

    public static String itemCannotBeFound(String unknownItem) {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage.append("You search for ").append(unknownItem)
                .append(" but you find nothing");
        return buildMessage.toString();
    }

    public static String pickUpPlayer(String playerName) {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage.append("You attempt to pick up ").append(Utils.removePlayerTag(playerName))
                .append(" but the pesky little scamp wriggles free!");
        return buildMessage.toString();
    }

    //TODO this will never be activated as locations cannot be within other locations
    public static String pickUpImpossible() {
        return "Don't be silly...";
    }

    public static String pickUpYourself() {
        return "You attempt to stuff yourself into your tiny satchel but no dice";
    }

    public static String badDropCommand() {
        return "I don't understand that command, try saying 'drop' followed by an artefact in your inventory";
    }

    public static String noItemInventory(String unknownItem) {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage.append("You search your inventory for ").append(unknownItem)
                .append(" but it is not there");
        return buildMessage.toString();
    }

    public static String badGoToCommand() {
        return "I don't understand that command, try saying 'goto' followed by an available location";
    }

    public static String pathDoesNotExist() {
        return "You cannot see a path to that location";
    }
}
