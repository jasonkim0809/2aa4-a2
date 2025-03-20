package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;


public class Explorer implements IExplorerRaid {
    private Drone drone;
    private IslandFinder islandF;

    private final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        
        //test stuff for island finder
        //islandF = new islandFinder(s);
    }


    static int counter = 0;
    @Override
    public String takeDecision() { // dependent on what phase of searching the drone is in

        JSONObject decision = new JSONObject();
        JSONObject parameter = new JSONObject();

        // if (counter == 0){
        //     decision.put("action","scan");
        //     counter++;
        // }
        // else if (counter == 1){
        //     parameter.put("direction","E");
        //     decision.put("parameters",parameter);
        //     decision.put("action","heading");
        //     counter++;
        // }
        // else if (counter == 2){
        //     decision.put("action","scan");
        //     counter++;
        // }
        // else{
        //     decision.put("action","stop");
        // }

        parameter.put("direction","N");
        decision.put("action","echo");
        decision.put("parameters",parameter);
    

        //decision.put("action", "scan");

        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }


    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));

        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);

        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);

        //islandF.updateEchoResults(response);
        //drone.getResults(response);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }
}