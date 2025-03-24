package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

import java.util.*;

public class Explorer implements IExplorerRaid {
    private Drone drone;

    private final Logger logger = LogManager.getLogger();

    private Directions droneDirection;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");

        String init_heading = info.getString("heading");
        droneDirection = Directions.fromString(init_heading);

        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", droneDirection);
        logger.info("Battery level is {}", batteryLevel);
        drone = new Drone(s);
    }


    static int counter = 0;
    private final Queue<String> taskQueue = new LinkedList<>();
    @Override
    public String takeDecision() { // dependent on what phase of searching the drone is in

        JSONObject decision = new JSONObject();
        JSONObject parameter = new JSONObject();

       /*  if(counter < 1500){
            logger.info("count");
            counter++;
        } else{
            logger.info("THIS IS THE END");
            return decision.put("action","put").toString();
        }*/

        decision = drone.getDecision();
        //decision = decision.put("action","stop");
        logger.info("** Decision: {}",decision.toString());

        return decision.toString();
        

        /* 
        //decision = drone.getDecision();
        logger.info("The drone is facing {}", droneDirection);

        if (!taskQueue.isEmpty()){
            //logger.info("** Removed: {}",taskQueue.remove().toString());
            return taskQueue.remove();
        }
        
        taskQueue.add((decision.put("action", "fly")).toString());
        taskQueue.add((decision.put("action", "scan")).toString());

        parameter.put("direction", droneDirection.turn_right().toString());
        decision.put("parameters", parameter).toString();
        taskQueue.add(decision.put("action", "heading").toString());

        taskQueue.add((decision.put("action", "scan")).toString());

        droneDirection = droneDirection.turn_right();

        parameter.put("direction", droneDirection.turn_right().toString());
        decision.put("parameters", parameter).toString();
        taskQueue.add(decision.put("action", "heading").toString());
        
        droneDirection = droneDirection.turn_right();

        taskQueue.add((decision.put("action", "scan")).toString());

        parameter.put("direction", droneDirection.turn_right().toString());
        decision.put("parameters", parameter).toString();
        taskQueue.add(decision.put("action", "heading").toString());
        
        droneDirection = droneDirection.turn_right();

        taskQueue.add((decision.put("action", "scan")).toString());

        taskQueue.add((decision.put("action", "stop")).toString());

        //logger.info("** Decision: {}",decision.toString());
        //logger.info("** Removed: {}",taskQueue.remove());
        return taskQueue.remove();
        */
        ///return decision.toString();
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
        return "no creek found";
    }
}