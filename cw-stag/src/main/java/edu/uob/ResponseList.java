package edu.uob;

public class ResponseList {

    public static String noActionFound() {
        return "I don't understand that";
    }

    public static String noUserName() {
        return "Who are you?";
    }

    public static String noCommand(String userName) {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage.append("Welcome ").append(userName).append(", what do you want to do?");
        return buildMessage.toString();
    }

    public static String badLookCommand() {
        return "I don't understand, maybe you meant 'look'?";
    }

    public static String badInvCommand() {
        return "I don't understand, maybe you meant 'inventory' or 'inv'?";
    }

    public static String badGetCommand() {
        return "I don't understand, try saying 'get' followed by an artefact you can see";
    }

    public static String pickUpFurniture(String furnitureName) {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage.append("You try to pick up the ").append(furnitureName)
                .append(" but it is impossible to lift");
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

    public static String pickUpImpossible() {
        return "Don't be silly...";
    }

    public static String pickUpYourself() {
        return "You attempt to stuff yourself into your tiny satchel but no dice";
    }

    public static String badDropCommand() {
        return "I don't understand, try saying 'drop' followed by an item in your inventory";
    }

    public static String noItemInventory(String unknownItem) {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage.append("You search your inventory for ").append(unknownItem)
                .append(" but it is not there");
        return buildMessage.toString();
    }

    public static String badGoToCommand() {
        return "I don't understand, try saying 'goto' followed by an available location";
    }

    public static String pathDoesNotExist() {
        return "You cannot see a path to that location";
    }

    public static String ambiguousCommand() {
        return "There are many ways I can do this, be more specific";
    }

    public static String badHealthCommand() {
        return "I don't understand, maybe you meant 'health'?";
    }

    public static String playerDeath() {
        return "You died and all your items dropped on the ground, " +
                "you have respawned at the start of the game";
    }

    public static String alreadyAtLocation() {
        return "You are already there";
    }

    public static String badUserName(String userName) {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage.append(userName)
                .append(" is not a proper name");
        return buildMessage.toString();
    }
}
