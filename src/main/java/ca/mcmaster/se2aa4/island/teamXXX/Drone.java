package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

public class Drone {
    private final Logger logger = LogManager.getLogger();

    private int battery_level;
    private Directions direction;
    int phase = 0;

    private final IslandFinder islandFinder;

    private PerimeterMappingAlgorithm perimeterMapping;
    private PerimeterDimensions perimeterDimensions;

    public Drone(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        direction = Directions.fromString(info.getString("heading"));
        battery_level = info.getInt("budget");
        islandFinder = new IslandFinder(s);
        perimeterMapping = new PerimeterMappingAlgorithm(s);
    }

    public JSONObject getDecision(){ // called by explorer class
        JSONObject decision = new JSONObject();
 
        if (phase == 0) {

            decision = islandFinder.findNextStep();
            logger.info("Island Finder direction: {}",islandFinder.getDirection().toString());

        }
        else if (phase == 1) {
            //decision = perimeterMapping.findNextStep();
            logger.info("Island Finder direction: {}",islandFinder.getDirection().toString());
            logger.info("Perimeter mapper direction: {}",perimeterMapping.getDirection());
            decision.put("action","stop");
        }
        else {
            decision.put("action","stop");
            phase++;
        }

        return decision;
    }

    public void getResults(JSONObject response){
        if (phase == 0){
            islandFinder.updateEchoResults(response);    
        }
        else if (phase == 1){ 
            perimeterMapping.updateResults(response);
        }
        this.updatePhase();
    }

    private void updatePhase(){
        if (islandFinder.isFinished() && phase == 0){
            perimeterMapping.overrideDirection(direction);
            phase++;
        }
        else if (perimeterMapping.isFinished() && phase == 1){
            phase++;
        }
    }
}