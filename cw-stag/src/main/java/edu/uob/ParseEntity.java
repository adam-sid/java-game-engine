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

public class ParseEntity {

    private final String fileName;
    private HashSet<Location> locations;
    private HashSet<GameEntity> furniture;
    private HashSet<GameEntity> artefacts;
    private HashSet<GameEntity> characters;
    private GameState gameState;

    public ParseEntity(String fileName) {
        this.fileName = fileName;
        this.locations = new HashSet<>();
        this.furniture = new HashSet<>();
        this.artefacts = new HashSet<>();
        this.characters = new HashSet<>();
        this.parseFile();
        this.gameState = new GameState(this.locations, this.furniture, this.artefacts, this.characters);
    }

    public GameState getGameState() {
        return this.gameState;
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
            this.parsePaths(sections.get(1).getEdges());
            this.parseLocations(sections.get(0).getSubgraphs());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void parseLocations(List<Graph> locations) {
        for (Graph location : locations) {
            Node locationDetails = location.getNodes(false).get(0);
            String locationName = locationDetails.getId().getId();
            String locationDescription = locationDetails.getAttribute("description");
            //add path here
            this.locations.add(new Location(locationName, locationDescription, locationName));
            List<Graph> features = location.getSubgraphs();
            this.parseFeatures(features, locationName);
        }
    }

    private void parseFeatures(List<Graph> features, String locationName) {
        for (Graph feature : features) {
            String featureName = feature.getId().getId();
            List<Node> entityList = feature.getNodes(false);
            for (Node entity : entityList) {
                String entityName = entity.getId().getId();
                String entityDescription = entity.getAttribute("description");
                switch (featureName) {
                    case "furniture":
                        this.furniture.add(new GameEntity(entityName, entityDescription, locationName));
                        break;
                    case "characters":
                        this.characters.add(new GameEntity(entityName, entityDescription, locationName));
                        break;
                    case "artefacts":
                        this.artefacts.add(new GameEntity(entityName, entityDescription, locationName));
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
        }
    }

    public void parsePaths(List<Edge> paths){
        for (Edge path : paths) {
            Node fromLocation = path.getSource().getNode();
            Node toLocation = path.getTarget().getNode();
            String fromName = fromLocation.getId().getId();
            String toName = toLocation.getId().getId();
        }
    }
}

