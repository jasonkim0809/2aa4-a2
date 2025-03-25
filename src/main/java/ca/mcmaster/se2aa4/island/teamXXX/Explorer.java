package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;


public class Explorer implements IExplorerRaid {
    private Drone drone;
    private String closestCreek;

    private final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(String s) {
        drone = new Drone(s);
    }


    static int counter = 0;
    @Override
    public String takeDecision() { // dependent on what phase of searching the drone is in

        JSONObject decision = new JSONObject();

        if (counter%2 == 0 && drone.phase == 1) {
            decision.put("action", "scan");

            counter += 1;
        } else {

            //decision.put("action", "fly");
            decision = drone.getDecision();
            counter += 1;

        }
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

        drone.getResults(response);
    }

    @Override
    public String deliverFinalReport() {
        logger.info("Closest creek id: {}",drone.getClosest());
        return "no creek found";
    }
}