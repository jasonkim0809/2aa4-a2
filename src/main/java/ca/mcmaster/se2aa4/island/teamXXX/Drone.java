package ca.mcmaster.se2aa4.island.teamXXX;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.enums.Actions;

public class Drone {
    private int battery_level;
    private Directions direction;
    //private EchoStatus echo = new EchoStatus();
    //private ScanStatus scanInfo;
    private Actions decision;
    private String creek_ID = "";

    public Actions getDecision(){
        return this.decision;
    }
    public String getDirection(){
        return direction.toString();
    }
    public void getResults(JSONObject in){

    }
    public void echo(String echoDirection){

        // echo logic here
    }
    public Integer getBattery() {
        return this.battery_level;
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


/* 
    public EchoResult getEchoRight() {
        return echo.echoRight;
    }

    public EchoResult getEchoLeft() {
        return echo.echoLeft;
    }

    public EchoResult getEchoAhead() {
        return echo.echoAhead;
    }

    public Integer getRangeRight() {
        return echo.rangeRight;
    }

    public Integer getRangeLeft() {
        return echo.rangeLeft;
    }

    public Integer getRangeAhead() {
        return echo.rangeAhead;
    }

    public JSONArray getScanInfoCreeks() {
        return this.scanInfo.scanCreeks;
    }

    public JSONArray getScanInfoSites() {
        return this.scanInfo.scanSites;
    }

    public JSONArray getScanInfoBiomes() {
        return this.scanInfo.scanBiomes;
    }*/
    
}
