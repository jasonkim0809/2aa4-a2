package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;


public class CreekFindingAlgorithm {
    private final Logger logger = LogManager.getLogger();
    private Directions direction;


    public CreekFindingAlgorithm(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String init_heading = info.getString("heading");
        direction = Directions.fromString(init_heading);
        logger.info("Initial heading: {}",direction.toString());

    }

    private class scanResults{
        Directions direction;
        public echoResults(Directions direction){
            this.direction = direction;

        }
    }

    public JSONObject findNextStep(){


    }

    public void updateScanResults(JSONObject results){


    }

}