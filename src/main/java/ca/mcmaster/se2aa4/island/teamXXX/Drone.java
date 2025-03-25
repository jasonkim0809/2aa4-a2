package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

import java.io.*;

public class Drone {
    private int battery_level;
    private Directions direction;
    private int phase = 0;
    private final PerimeterMappingAlgorithm perimeterMapping;
    private PerimeterDimensions perimeterDimensions;

    private final Logger logger = LogManager.getLogger();

    public Drone(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        direction = Directions.fromString(info.getString("heading"));
        battery_level = info.getInt("budget");
        perimeterMapping = new PerimeterMappingAlgorithm(s);
    }

    public JSONObject getDecision(){ // called by explorer class
        JSONObject decision = new JSONObject();
 
        if (phase == 0) {

            decision = perimeterMapping.findNextStep();

        } else {

            perimeterDimensions = new PerimeterDimensions(perimeterMapping.perimeterValues()[0], perimeterMapping.perimeterValues()[1], perimeterMapping.perimeterValues()[2], perimeterMapping.perimeterValues()[3]);

            decision.put("action","stop");
            phase++;


            logger.info(perimeterMapping.perimeterValues()[0] + " , " + perimeterMapping.perimeterValues()[1] + " , " + perimeterMapping.perimeterValues()[2] + " , " + perimeterMapping.perimeterValues()[3]);
        }

        return decision;
    }

    public String getDirection(){
        return direction.toString();
    }

    public void getResults(JSONObject response){    
        perimeterMapping.updateResults(response);
        this.updatePhase();
    }

    private void updatePhase(){
        if (perimeterMapping.isFinished() && phase == 0){
            phase++;
        }
    }
}