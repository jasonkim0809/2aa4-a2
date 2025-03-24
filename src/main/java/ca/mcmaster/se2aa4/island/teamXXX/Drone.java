package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.enums.Actions;
import ca.mcmaster.se2aa4.island.teamXXX.AreaScan;

public class Drone {
    private int battery_level;
    private Directions direction;
    //private EchoStatus echo = new EchoStatus();
    //private ScanStatus scanInfo;
    private Actions decision;
    private int xMax;
    private int yMax;
    private int xMin;
    private int yMin;
    private final AreaScan AreaScan;

    private String creek_ID = "";

    private final Logger logger = LogManager.getLogger();

    public Drone(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        direction = Directions.fromString(info.getString("heading"));
        battery_level = info.getInt("budget");
        AreaScan = new AreaScan(s);
    }

    public JSONObject getDecision(){ // called by explorer class

        JSONObject decision = new JSONObject();

        String decisions = (AreaScan.findNextStep());

        decision = new JSONObject(decisions);
        
        //decision = decision.put("action","fly");

        return decision;
    }
    
    public String getDirection(){
        return direction.toString();
    }

    public void echo(String echoDirection){

        // echo logic here
    }
    public Integer getBattery() {
        return this.battery_level;
    }

    public Integer getXMax() {
        return this.xMax;
    }

    public Integer getYMax() {
        return this.yMax;
    }

    public Integer getXMin() {
        return this.xMin;
    }

    public Integer getYMin() {
        return this.yMin;
    }


    public void setCreekID(String creekID) {
        this.creek_ID = creekID;
    }

    public String getCreekID()
    {
        if (this.creek_ID.isEmpty()){
            return "no creek found";
        }
        return this.creek_ID;
    }

    public void getResults(JSONObject response){  
        AreaScan.updateResults(response);
    }
    
}
