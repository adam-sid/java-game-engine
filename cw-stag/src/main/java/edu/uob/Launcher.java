package edu.uob;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Launcher {

    public static void main(String[] args) {
        System.out.println("===============================");
        System.out.println("  Welcome to the Game! ");
        System.out.println("===============================");
        System.out.print("Please enter a name to begin: ");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String username = reader.readLine();
            if (username == null || username.trim().isEmpty()) {
                System.out.println("Username cannot be empty. Exiting.");
                return;
            }
            Thread serverThread = new Thread(() -> {
                try {
                    GameServer.main(new String[0]);
                } catch (Exception e) {
                    System.err.println("\nFATAL: Server failed to start. " + e.getMessage());
                }
            });

            serverThread.setDaemon(true);
            serverThread.start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("Welcome " +  username+ ", enter commands to interact with the world" );
            System.out.println("----------------------------------------");
            GameClient.main(new String[]{username});

        } catch (IOException e) {
            System.err.println("An error occurred while reading input: " + e.getMessage());
        }
    }
}

