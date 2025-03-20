package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

public class Drone {
    private int battery_level;
    private Directions direction;
    private int phase = 0;
    private IslandFinder islandFinder;

    public Drone(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        direction = Directions.fromString(info.getString("heading"));
        battery_level = info.getInt("budget");
        islandFinder = new IslandFinder(s);
    }

    public JSONObject getDecision(){ // called by explorer class
        JSONObject decision = new JSONObject();
        if (phase == 0){
            decision = islandFinder.findNextStep();
        }
        else if (phase == 1){
            decision.put("action","scan");
            phase++;
        }
        else if (phase == 2){
            decision.put("action","stop");
        }
        return decision;
    }

    public String getDirection(){
        return direction.toString();
    }

    public void getResults(JSONObject response){    
        islandFinder.updateEchoResults(response);
        this.updatePhase();
    }

    private void updatePhase(){
        if (islandFinder.isFinished() && phase == 0){
            phase++;
        }
    }
}
