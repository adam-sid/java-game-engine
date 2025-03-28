package edu.uob;

import java.io.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;
import edu.uob.GameEntity.GameEntity;
import edu.uob.GameEntity.LocationEntity;

//TODO is it redundant to have gameState and parser hold like information
public class EntityFileParser {

    private final File file;
    private final Map<String, GameEntity> locations;
    private String startLocationName;
    private final Map<String, GameEntity> furniture;
    private final Map<String, GameEntity> artefacts;
    private final Map<String, GameEntity> characters;
    private final GameState gameState;

    public EntityFileParser(File file) {
        this.file = file;
        this.locations = new HashMap<String, GameEntity>();
        this.startLocationName = null;
        this.furniture = new HashMap<String, GameEntity>();
        this.artefacts = new HashMap<String, GameEntity>();
        this.characters = new HashMap<String, GameEntity>();
        this.parseEntityFile();
        this.gameState = new GameState(this.locations, this.startLocationName,
                this.furniture, this.artefacts, this.characters);
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void parseEntityFile() {
        try {
            FileReader reader = new FileReader(this.file);
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
            this.locations.put(locationName, new LocationEntity(locationName, locationDescription));
            if (this.startLocationName == null) {
                this.startLocationName = locationName;
            }
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
                        this.furniture.put(entityName, new GameEntity(entityName, entityDescription, locationName));
                        break;
                    case "characters":
                        this.characters.put(entityName, new GameEntity(entityName, entityDescription, locationName));
                        break;
                    case "artefacts":
                        this.artefacts.put(entityName, new GameEntity(entityName, entityDescription, locationName));
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
            String fromName = fromLocation.getId().getId().toLowerCase();
            String toName = toLocation.getId().getId().toLowerCase();

            LocationEntity fromLocationObj = (LocationEntity) locations.get(fromName);
            LocationEntity toLocationObj = (LocationEntity) locations.get(toName);

            if (fromLocationObj != null && toLocationObj != null) {
                fromLocationObj.addPath(toLocationObj);
            }
        }
    }
}

