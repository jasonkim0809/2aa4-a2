package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

public class Drone {
    private int battery_level;
    private Directions direction;
    private int phase = 0;
    private final CreekFindingAlgorithm creekFinding;

    public Drone(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        direction = Directions.fromString(info.getString("heading"));
        battery_level = info.getInt("budget");
        creekFinding = new CreekFindingAlgorithm(s);
    }

    public JSONObject getDecision(){ // called by explorer class
        JSONObject decision = new JSONObject();
 
        if (phase == 0) {

            decision = creekFinding.findNextStep();

        } else {

            decision.put("action","stop");
            phase++;
        }

        return decision;
    }

    public String getDirection(){
        return direction.toString();
    }

    public void getResults(JSONObject response){    
        creekFinding.updateResults(response);
        this.updatePhase();
    }

    private void updatePhase(){
        if (creekFinding.isFinished() && phase == 0){
            phase++;
        }
    }
}