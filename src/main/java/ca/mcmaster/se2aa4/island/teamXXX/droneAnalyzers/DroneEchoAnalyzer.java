package ca.mcmaster.se2aa4.island.teamXXX.droneAnalyzers;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Found;

public interface DroneEchoAnalyzer extends DroneAnalyzer{

    public Found parseFoundInExtrasResult(JSONObject extrasResult);
    public int parseRangeInExtrasResult(JSONObject extrasResult);
    public JSONObject parseExtrasInResults(JSONObject echoResult);

}