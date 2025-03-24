package ca.mcmaster.se2aa4.island.teamXXX;
//import static org.junit.Assert.fail;

import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
//import org.json.JSONArray;

import ca.mcmaster.se2aa4.island.teamXXX.droneAnalyzers.DroneEchoAnalyzer;
import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.enums.Found;

class echoResults{
    int range;
    Found found; // either "GROUND" or "OUT_OF_RANGE"
    Directions direction;
    public echoResults(Directions direction, int range, Found found){
        this.direction = direction;
        this.range = range;
        this.found = found;
    }
}


public class CreekFindingAlgorithm implements DroneEchoAnalyzer, NavigationInterface {
    private final Logger logger = LogManager.getLogger();

    private Directions direction;
    private Directions echoDirection;
    private Directions intialDirections;
    private int returnToIntialPosPhase = 1;

    private Integer[] edgePositions = new Integer[4];
    private int edgePosCollected = 0;

    private int isUTurningPhases = 1;
    
    private int[] currentPosition = new int[2];

    private echoResults leftEcho = null;
    private echoResults rightEcho = null;
    private boolean isEchoing = false;

    private boolean finished = false;

    public CreekFindingAlgorithm(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String init_heading = info.getString("heading");
        this.direction = Directions.fromString(init_heading);

        intialDirections = direction;
        echoDirection = direction.turn_left();

        currentPosition[0] = 0;
        currentPosition[1] = 0;
    }

    @Override
    public boolean isFinished(){

        return this.finished;
    }

    @Override
    public Found parseFoundInExtrasResult(JSONObject extrasResult){

        String foundString = extrasResult.getString("found");
        return Found.fromString(foundString);

    }

    public int parseRangeInExtrasResult(JSONObject extrasResult){


        return extrasResult.getInt("range");

    }

    @Override
    public JSONObject parseExtrasInResults(JSONObject echoResult){

        return echoResult.getJSONObject("extras");

    }

    @Override
    public void updateResults(JSONObject results){
        JSONObject extras = this.parseExtrasInResults(results);
        if (!extras.has("found") || !extras.has("range")) { // check results JSON if an echo was even used
            return; 
        }

        //update this with the classes
        int range = extras.getInt("range");
        Found found = this.parseFoundInExtrasResult(extras); // either "GROUND" or "OUT_OF_RANGE" 

        if (echoDirection == direction.turn_left()){ // current echo direction is forward
            leftEcho = new echoResults(echoDirection,range,found);
        }
        else if (echoDirection == direction.turn_right()){ // current echo direction is right (final step)
            rightEcho = new echoResults(echoDirection,range,found);
        }

    }

    @Override
    public JSONObject findNextStep(){
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();

        if (edgePosCollected == 4 & currentPosition[0] == 0 & currentPosition[1] == 0) {

            finished = true;
        }

        if (edgePosCollected == 4) {

            if (returnToIntialPosPhase == 1){

                if (direction != intialDirections.turn_right().turn_right()) {

                    if ((direction == Directions.N | direction == Directions.S) & Math.abs(currentPosition[1]) == 1) {

                        parameters.put("direction",direction.turn_right().toString());
                        decision.put("parameters",parameters);
                        decision.put("action","heading");
        
                        if (direction == Directions.N) {
        
                            currentPosition[0] += 1;
                            currentPosition[1] += 1;
    
                        }else if (direction == Directions.E) {
        
                            currentPosition[0] += 1;
                            currentPosition[1] -= 1;
    
                        } else if (direction == Directions.W) {
        
                            currentPosition[0] -= 1;
                            currentPosition[1] += 1;
                        } else {
    
                            currentPosition[0] -= 1;
                            currentPosition[1] -= 1;

                        }

                        direction = direction.turn_right();

                        returnToIntialPosPhase += 1;

                        return decision;

                    } else if ((direction == Directions.N | direction == Directions.S) & Math.abs(currentPosition[1]) > 1) {

                        decision.put("action","fly");

                        if (direction == Directions.N) {
            
                            currentPosition[1] += 1;
            
                        } else if (direction == Directions.E) {
            
                            currentPosition[0] += 1;
            
                        } else if (direction == Directions.W) {
            
                            currentPosition[0] -= 1;
            
                        } else {
            
                            currentPosition[1] -= 1;
            
                        }
                        
                        return decision;

                    } else if ((direction == Directions.W | direction == Directions.E) & Math.abs(currentPosition[0]) == 1) {

                        parameters.put("direction",direction.turn_right().toString());
                        decision.put("parameters",parameters);
                        decision.put("action","heading");
        
                        if (direction == Directions.N) {
        
                            currentPosition[0] += 1;
                            currentPosition[1] += 1;
    
                        }else if (direction == Directions.E) {
        
                            currentPosition[0] += 1;
                            currentPosition[1] -= 1;
    
                        } else if (direction == Directions.W) {
        
                            currentPosition[0] -= 1;
                            currentPosition[1] += 1;
                        } else {
    
                            currentPosition[0] -= 1;
                            currentPosition[1] -= 1;

                        }

                        direction = direction.turn_right();

                        returnToIntialPosPhase += 1;

                        return decision;

                    } else if ((direction == Directions.W | direction == Directions.E) & Math.abs(currentPosition[0]) > 1) {

                        decision.put("action","fly");

                        if (direction == Directions.N) {
            
                            currentPosition[1] += 1;
            
                        } else if (direction == Directions.E) {
            
                            currentPosition[0] += 1;
            
                        } else if (direction == Directions.W) {
            
                            currentPosition[0] -= 1;
            
                        } else {
            
                            currentPosition[1] -= 1;
            
                        }
                        
                        return decision;

                    }

                } else {

                    parameters.put("direction",direction.turn_right().toString());
                    decision.put("parameters",parameters);
                    decision.put("action","heading");

                    if (direction == Directions.N) {

                        currentPosition[0] += 1;
                        currentPosition[1] += 1;

                    }else if (direction == Directions.E) {

                        currentPosition[0] += 1;
                        currentPosition[1] -= 1;

                    } else if (direction == Directions.W) {

                        currentPosition[0] -= 1;
                        currentPosition[1] += 1;


                    } else {

                        currentPosition[0] -= 1;
                        currentPosition[1] -= 1;

                    }

                    direction = direction.turn_right();
                    
                    return decision;

                }

            } else {

                decision.put("action","fly");

                if (direction == Directions.N) {
    
                    currentPosition[1] += 1;
    
                } else if (direction == Directions.E) {
    
                    currentPosition[0] += 1;
    
                } else if (direction == Directions.W) {
    
                    currentPosition[0] -= 1;
    
                } else {
    
                    currentPosition[1] -= 1;
    
                }
                
                return decision;

            }
            

        }

        if (isUTurningPhases == 2) {

            isUTurningPhases = 3;

            parameters.put("direction",direction.turn_right().toString());
            decision.put("parameters",parameters);
            decision.put("action","heading");

            if (direction == Directions.N) {

                edgePositions[0] = currentPosition[1];

                currentPosition[0] += 1;
                currentPosition[1] += 1;

            }else if (direction == Directions.E) {

                edgePositions[1] = currentPosition[0];

                currentPosition[0] += 1;
                currentPosition[1] -= 1;

            } else if (direction == Directions.W) {

                edgePositions[2] = currentPosition[0];

                currentPosition[0] -= 1;
                currentPosition[1] += 1;


            } else {

                edgePositions[3] = currentPosition[0];

                currentPosition[0] -= 1;
                currentPosition[1] -= 1;

            }

            direction = direction.turn_right();
            echoDirection = direction.turn_left();

            return decision;

        } else if (isUTurningPhases == 3) {

            isUTurningPhases = 1;

            decision.put("action","fly");

            if (direction == Directions.N) {

                currentPosition[1] += 1;

            } else if (direction == Directions.E) {

                currentPosition[0] += 1;

            } else if (direction == Directions.W) {

                currentPosition[0] -= 1;

            } else {

                currentPosition[1] -= 1;

            }

            isEchoing = true;

            return decision;

        }

        if (isEchoing == true & echoDirection == direction.turn_left()) {

            parameters.put("direction",direction.turn_left().toString());
            decision.put("parameters",parameters);
            decision.put("action","echo");

            echoDirection = direction.turn_right();

        } else if (isEchoing == true & echoDirection == direction.turn_right()) {

            parameters.put("direction",direction.turn_right().toString());
            decision.put("parameters",parameters);
            decision.put("action","echo");

            isEchoing = false;
            echoDirection = direction.turn_left();

        } else {
            if (leftEcho == null && rightEcho == null ) {

                decision.put("action","fly");

                if (direction == Directions.N) {

                    currentPosition[1] += 1;

                } else if (direction == Directions.E) {

                    currentPosition[0] += 1;

                } else if (direction == Directions.W) {

                    currentPosition[0] -= 1;

                } else {

                    currentPosition[1] -= 1;

                }

                isEchoing = true;

            } else if (leftEcho.found == Found.GROUND | rightEcho.found == Found.GROUND) {

                decision.put("action","fly");

                if (direction == Directions.N) {

                    currentPosition[1] += 1;

                } else if (direction == Directions.E) {

                    currentPosition[0] += 1;

                } else if (direction == Directions.W) {

                    currentPosition[0] -= 1;

                } else {

                    currentPosition[1] -= 1;

                }

                isEchoing = true;

            } else {

                if (edgePosCollected % 2 == 0) {

                    isUTurningPhases = 2;

                } else {

                    isEchoing = true;

                }

                edgePosCollected += 1;

                parameters.put("direction",direction.turn_right().toString());
                decision.put("parameters",parameters);
                decision.put("action","heading");

                if (direction == Directions.N) {

                    edgePositions[0] = currentPosition[1];

                    currentPosition[0] += 1;
                    currentPosition[1] += 1;

                }else if (direction == Directions.E) {

                    edgePositions[1] = currentPosition[0];

                    currentPosition[0] += 1;
                    currentPosition[1] -= 1;

                } else if (direction == Directions.W) {

                    edgePositions[2] = currentPosition[0];

                    currentPosition[0] -= 1;
                    currentPosition[1] += 1;


                } else {

                    edgePositions[3] = currentPosition[0];

                    currentPosition[0] -= 1;
                    currentPosition[1] -= 1;

                }

                direction = direction.turn_right();
                echoDirection = direction.turn_left();
            }

        }

        return decision;

    }

}