# java-game-engine

This is a game engine written in Java that processes DOT and XML files. DOT files contain game entities including locations, objects, furniture, characters and players. XML files contain actions that a player can do in the game (beyond the basic included actions described below). The engine parses these files and builds a game world from them. The game can then be played using the terminal and supports multiplayer.

## Set-up

1. Ensure you have maven and java (minimum version 17) installed
2. Clone repo and from the main directory (cw-stag) run the following:
```bash
mvn clean package
```
Which compiles the program and creates an executable jar file. Then run:
```bash
java -jar target/STAG-game.jar
```
Which runs the program. You should see the following:
![Start-up screenshot](readme-images/start-up.png)

## Playing the game

The game is a turn based text adventure game (think zork). You interact with the game world by typing in actions to the command line.

### Basic actions

- **inventory** (or inv for short) lists all of the artefacts currently being carried by the player
- **get** picks up a specified artefact from the current location and adds it into player's inventory
- **drop** puts down an artefact from player's inventory and places it into the current location
- **goto** moves the player from the current location to the specified location (provided that there is a path to that location !)
- **look** prints details of the current location (including all entities present) and lists the paths to other locations

There are then custom actions that can be defined in the XML file.

### Multiplayer

This game supports multiplayer. Simply start an additional instance of the GameClient after launch with a new name.

### Health

There is a health feature: health starts at 3 and when it falls below 1 the player will "die", drop all their possessions in their location and restart the game at the initial location.

### Creating your own maps and actions

Build new maps and actions by creating new DOT and XML files in the config folder. Then update the files in GameServer to play new worlds.