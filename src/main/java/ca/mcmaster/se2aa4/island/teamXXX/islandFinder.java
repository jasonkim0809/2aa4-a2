package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;


public class islandFinder {
    private final Logger logger = LogManager.getLogger();
    private Directions direction;

    private Directions echoDirection; // represents which direction to scan in right now, goes left, forward, right

    private echoResults leftEcho = null;
    private echoResults rightEcho = null;
    private echoResults forwardEcho = null;
    private boolean echoComplete = false;


    public islandFinder(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String init_heading = info.getString("heading");
        direction = Directions.fromString(init_heading);
        logger.info("Initial heading: {}",direction.toString());

        this.echoDirection = direction.turn_left();
    }

    private class echoResults{
        int range;
        String found; // either "GROUND" or "OUT_OF_RANGE"
        Directions direction;
        public echoResults(Directions direction, int range, String found){
            this.direction = direction;
            this.range = range;
            this.found = found;
        }
    }

    private boolean isGround(echoResults echo){
        return (echo!=null && echo.direction.toString() == "GROUND");
    }

    public JSONObject findNextStep(){
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        if (!echoComplete){
            if (echoDirection == direction.turn_left()){ // return a left echo as decision JSON
                parameters.put("direction",direction.turn_left().toString());
            }
            else if (echoDirection == direction){ // return a forward echo as decision JSON
                parameters.put("direction",direction.toString());
            }
            else if (echoDirection == direction.turn_right()){ // return a right echo as decision JSON
                parameters.put("direction",direction.turn_right().toString());
            }
            decision.put("parameters",parameters);
            decision.put("action","echo");
        }
        else{
            if (isGround(leftEcho) || isGround(forwardEcho) || isGround(rightEcho)){ // ground detected in at least ONE of these
                Directions movedir = null;
                int closestRange = 0;
                for (echoResults echo : new echoResults[]{leftEcho,forwardEcho,rightEcho}){ // for loop to initialize closestRange with SOMETHING from a valid echo (not null)
                    if(echo!=null){
                        closestRange = echo.range;
                        break;
                    }
                }

                for (echoResults echo : new echoResults[]{leftEcho,forwardEcho,rightEcho}){ // for loop to set closestRange to smallest echo found
                        if (echo.range < closestRange){
                            movedir = echo.direction;
                            closestRange = echo.range;
                        }
                }

                if (movedir != direction){ // if next move is forwards, do NOT use heading (ends game)
                    parameters.put("direction",movedir);
                    decision.put("parameters",parameters);
                    decision.put("action","heading"); // heading action also turns/moves the drone
                }
                else{
                    decision.put("action","fly");
                }
                
                
                // reset echo conditions
                echoComplete = false;
                echoDirection = direction.turn_left();
                leftEcho = null;
                forwardEcho = null;
                rightEcho = null;

                return decision;
            }
            else{ // no ground detected ANYWHERE!!!
                
            }
        }
        return decision;
    }

    public void updateEchoResults(JSONObject results){
        JSONObject extras = results.getJSONObject("extras");
        if (!extras.has("found") || !extras.has("range")) { // check results JSON if an echo was even used
            return; 
        }

        int range = extras.getInt("range");
        String found = extras.getString("found");

        if (echoDirection == direction.turn_left()){ // current echo direction is left (first step)
            leftEcho = new echoResults(echoDirection,range,found);
            echoDirection = direction;
        }
        else if (echoDirection == direction){ // current echo direction is forward
            forwardEcho = new echoResults(echoDirection,range,found);
            echoDirection = direction.turn_right();
        }
        else if (echoDirection == direction.turn_right()){ // current echo direction is right (final step)
            rightEcho = new echoResults(echoDirection,range,found);
            echoDirection = direction.turn_left();
            echoComplete = true;
        }
    }
}
