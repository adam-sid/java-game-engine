package edu.uob;

import java.io.FileReader;
import java.io.File;
import java.util.HashSet;
import java.util.List;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;
import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.Location;

public class ParseEntities {

    private final String fileName;
    private HashSet<Location> locations;
    private HashSet<GameEntity> furniture;
    private HashSet<GameEntity> artefacts;
    private HashSet<GameEntity> characters;
    private GameState gameState;

    public ParseEntities(String fileName) {
        this.fileName = fileName;
        this.locations = new HashSet<>();
        this.furniture = new HashSet<>();
        this.artefacts = new HashSet<>();
        this.characters = new HashSet<>();
        parseFile();
        this.gameState = new GameState(this.locations, this.furniture, this.artefacts, this.characters);
    }

    public void parseFile() {
        String filePath = new StringBuilder("config")
                .append(File.separator)
                .append(this.fileName)
                .toString();
        try {
            FileReader reader = new FileReader(filePath);
            Parser parser = new Parser(reader);
            parser.parse(reader);
            Graph document = parser.getGraphs().get(0);
            List<Graph> sections = document.getSubgraphs();
            parseLocations(sections.get(0).getSubgraphs());
            parsePaths(sections.get(1).getEdges());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void parseLocations(List<Graph> locations) {
        for (Graph location : locations) {
            Node locationDetails = location.getNodes(false).get(0);
            String locationName = locationDetails.getId().getId();
            String locationDescription = locationDetails.getAttribute("description");
            //System.out.println(locationName);
            this.locations.add(new Location(locationName, locationDescription, locationName));
            List<Graph> features = location.getSubgraphs();
            parseFeatures(features, locationName);
        }
    }

    private void parseFeatures(List<Graph> features, String locationName) {
        for (Graph feature : features) {
            String featureName = feature.getId().getId();
            switch (featureName) {
                case "furniture":
                    parseFurniture(feature.getNodes(false), locationName);
                    break;
                case "characters":
                    parseCharacters(feature.getNodes(false), locationName);
                    break;
                case "artefacts":
                    parseArtefacts(feature.getNodes(false), locationName);
                    break;
                default:
                    throw new RuntimeException("Unknown feature: " + featureName);
            }
        }
    }

    public void parseFurniture (List<Node> furnitureList, String locationName) {
        for (Node furniture : furnitureList) {
            //set furniture name, description and location
            String furnitureName = furniture.getId().getId();
            String furnitureDescription = furniture.getAttribute("description");
            this.furniture.add(new GameEntity(furnitureName, furnitureDescription, locationName));
            //System.out.println("furniture: " + furniture.getId().getId() +
                    //"\ndescription: " + furniture.getAttribute("description"));
        }
    }

    public void parseCharacters (List<Node> characters, String locationName) {
        for (Node character : characters) {
            //set character name, description and location
            String characterName = character.getId().getId();
            String characterDescription = character.getAttribute("description");
            this.characters.add(new GameEntity(characterName, characterDescription, locationName));
            //System.out.println("character: " + character.getId().getId() +
                    //"\ndescription: " + character.getAttribute("description"));
        }
    }

    public void parseArtefacts (List<Node> artefacts, String locationName) {
        for (Node artefact : artefacts) {
            //set artefact name, description and location
            String artefactName = artefact.getId().getId();
            String artefactDescription = artefact.getAttribute("description");
            this.artefacts.add(new GameEntity(artefactName, artefactDescription, locationName));
            //System.out.println("artefact: " + artefact.getId().getId() +
                    //"\ndescription: " + artefact.getAttribute("description"));
        }
    }

    public void parsePaths(List<Edge> paths){
        for (Edge path : paths) {
            Node fromLocation = path.getSource().getNode();
            Node toLocation = path.getTarget().getNode();
            String fromName = fromLocation.getId().getId();
            String toName = toLocation.getId().getId();
            //set paths in the location
            //System.out.println(fromName + " -> " + toName);
        }
    }
}

