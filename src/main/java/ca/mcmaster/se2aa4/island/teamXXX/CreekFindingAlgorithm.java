package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.enums.Biome;
import ca.mcmaster.se2aa4.island.teamXXX.locationClasses.Creek;
import ca.mcmaster.se2aa4.island.teamXXX.locationClasses.EmergencySite;


public class CreekFindingAlgorithm {
    private final Logger logger = LogManager.getLogger();

    private int[] position = new int[2];

    public CreekFindingAlgorithm(String s, int droneXPosition, int droneYPosition){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        this.position[0] = droneXPosition;
        this.position[1] = droneYPosition;
    }

    public void updateScanResults(JSONObject results){


    }

    public void findNextStep(){


    }

}