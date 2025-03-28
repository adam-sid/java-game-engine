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
        StringBuilder message = new StringBuilder();
        message.append("Hello ").append(userName).append(", please specify an action to perform");
        return message.toString();
    }
}
