package edu.uob;

import java.io.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;
import edu.uob.Entity.Entity;
import edu.uob.Entity.LocationEntity;

//TODO is it redundant to have gameState and parser hold like information
public class GameStateParser {

    private final String fileName;
    private final HashSet<LocationEntity> locations;
    private final HashSet<Entity> furniture;
    private final HashSet<Entity> artefacts;
    private final HashSet<Entity> characters;
    private final GameState gameState;

    public GameStateParser(String fileName) {
        this.fileName = fileName;
        this.locations = new HashSet<>();
        this.furniture = new HashSet<>();
        this.artefacts = new HashSet<>();
        this.characters = new HashSet<>();
        this.parseEntityFile();
        this.gameState = new GameState(this.locations, this.furniture, this.artefacts, this.characters);
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void parseEntityFile() {
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
            this.parseLocations(sections.get(0).getSubgraphs());
            this.parsePaths(sections.get(1).getEdges());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void parseLocations(List<Graph> locations) {
        for (Graph location : locations) {
            Node locationDetails = location.getNodes(false).get(0);
            String locationName = locationDetails.getId().getId().toLowerCase();
            String locationDescription = locationDetails.getAttribute("description").toLowerCase();
            this.locations.add(new LocationEntity(locationName, locationDescription, locationName));
            List<Graph> features = location.getSubgraphs();
            this.parseEntities(features, locationName);
        }
    }

    private void parseEntities(List<Graph> features, String locationName) {
        for (Graph feature : features) {
            String featureName = feature.getId().getId().toLowerCase();
            List<Node> entityList = feature.getNodes(false);
            for (Node entity : entityList) {
                String entityName = entity.getId().getId().toLowerCase();
                String entityDescription = entity.getAttribute("description").toLowerCase();
                switch (featureName) {
                    case "furniture":
                        this.furniture.add(new Entity(entityName, entityDescription, locationName));
                        break;
                    case "characters":
                        this.characters.add(new Entity(entityName, entityDescription, locationName));
                        break;
                    case "artefacts":
                        this.artefacts.add(new Entity(entityName, entityDescription, locationName));
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
        }
    }

    public void parsePaths(List<Edge> paths){
        Map<String, LocationEntity> locationMap = new HashMap<>();
        for (LocationEntity location : this.locations) {
            locationMap.put(location.getName(), location);
        }
        for (Edge path : paths) {
            Node fromLocation = path.getSource().getNode();
            Node toLocation = path.getTarget().getNode();
            String fromName = fromLocation.getId().getId().toLowerCase();
            String toName = toLocation.getId().getId().toLowerCase();

            LocationEntity fromLocationObj = locationMap.get(fromName);
            LocationEntity toLocationObj = locationMap.get(toName);

            if (fromLocationObj != null && toLocationObj != null) {
                fromLocationObj.addPath(toLocationObj);
            }
        }
    }
}

