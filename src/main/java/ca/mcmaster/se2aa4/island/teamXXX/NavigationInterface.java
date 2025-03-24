package ca.mcmaster.se2aa4.island.teamXXX;
import org.json.JSONObject;


public interface NavigationInterface{

    public JSONObject findNextStep();
    public boolean isFinished();
    public void updateResults(JSONObject results);

}
