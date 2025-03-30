package edu.uob;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

public final class GameServer {

    private static final char END_OF_TRANSMISSION = 4;
    private final GameState gameState;

    //todo make this work on any file not just basic ones
    //what is the point of this function?
    public static void main(String[] args) throws IOException {
        StringBuilder configFolderPath = new StringBuilder("config").append(File.separator);
        File entitiesFile = Paths.get(configFolderPath.toString(), "extended-entities.dot" )
                .toAbsolutePath().toFile();
        File actionsFile = Paths.get(configFolderPath.toString(), "extended-actions.xml" )
                .toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        server.blockingListenOn(8888);
    }

    /**
    * Do not change the following method signature or we won't be able to mark your submission
    * Instanciates a new server instance, specifying a game with some configuration files
    *
    * @param entitiesFile The game configuration file containing all game entities to use in your game
    * @param actionsFile The game configuration file containing all game actions to use in your game
    */
    // TODO implement server logic here
    public GameServer(File entitiesFile, File actionsFile) {
        EntityFileParser stateParser = new EntityFileParser(entitiesFile);
        this.gameState = stateParser.getGameState();
        ActionFileParser.parseActionFile(actionsFile, this.gameState);
    }

    /**
    * Do not change the following method signature or we won't be able to mark your submission
    * This method handles all incoming game commands and carries out the corresponding actions.</p>
    *
    * @param command The incoming command to be processed
    */
    // TODO implement your server logic here
    public String handleCommand(String command) {
        return CommandParser.parseCommand(command, this.gameState);
    }

    /**
    * Do not change the following method signature or we won't be able to mark your submission
    * Starts a *blocking* socket server listening for new connections.
    *
    * @param portNumber The port to listen on.
    * @throws IOException If any IO related operation fails.
    */
    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            StringBuilder buildResponse = new StringBuilder("Server listening on port ")
                    .append(portNumber);
            System.out.println(buildResponse);
            while (!Thread.interrupted()) {
                try {
                    this.blockingHandleConnection(s);
                } catch (IOException e) {
                    System.out.println("Connection closed");
                }
            }
        }
    }

    /**
    * Do not change the following method signature or we won't be able to mark your submission
    * Handles an incoming connection from the socket server.
    *
    * @param serverSocket The client socket to read/write from.
    * @throws IOException If any IO related operation fails.
    */
    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            System.out.println("Connection established");
            String incomingCommand = reader.readLine();
            if(incomingCommand != null) {
                StringBuilder buildResponse = new StringBuilder("Received message from ")
                        .append(incomingCommand);
                System.out.println(buildResponse);
                String result = this.handleCommand(incomingCommand);
                writer.write(result);
                StringBuilder buildEOT = new StringBuilder("\n")
                        .append(END_OF_TRANSMISSION)
                        .append("\n");
                writer.write(buildEOT.toString());
                writer.flush();
            }
        }
    }
}
