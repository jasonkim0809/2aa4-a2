package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
import org.json.JSONArray;

public interface DroneEchoAnalyzer{

    public void parseExtraResult(JSONObject extrasResult);
    public void parseEchoResults(JSONObject echoResult);

}