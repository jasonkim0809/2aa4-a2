package ca.mcmaster.se2aa4.island.teamXXX;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

public class Drone {
    private int battery_level;
    private Directions direction;

    public String getDecision(){ // called by explorer class
        // echo logic, change decision returned
        return "temp";
    }
    public String getDirection(){
        return direction.toString();
    }
    public void getResults(JSONObject in){

    }
}
