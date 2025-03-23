package ca.mcmaster.se2aa4.island.teamXXX;
//import static org.junit.Assert.fail;

import java.io.StringReader;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
//import org.json.JSONArray;

import java.util.ArrayList; 

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
    //private final Logger logger = LogManager.getLogger();

    private Directions direction;
    private Directions echoDirection;

    private ArrayList<int[]> perimeterPositions = new ArrayList<int[]>();
    
    //establishes Origin Point
    private int[] currentPosition = new int[2];

    private echoResults forwardEcho = null;
    private echoResults rightEcho = null;

    private boolean finished = false;

    private boolean isShiftingRight = false;
    private int shiftingRightMovementCount = 1;

    private boolean isShiftingLeft = false;
    private int shiftingLeftMovementCount = 1;

    private boolean echoComplete = false;

    public CreekFindingAlgorithm(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String init_heading = info.getString("heading");
        this.direction = Directions.fromString(init_heading);

        currentPosition[0] = 0;
        currentPosition[1] = 0;

        int[] newPosition= {currentPosition[0], currentPosition[1]};
        perimeterPositions.add(newPosition);
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

        if (echoDirection == direction){ // current echo direction is forward
            forwardEcho = new echoResults(echoDirection,range,found);
            echoDirection = direction.turn_right();
        }
        else if (echoDirection == direction.turn_right()){ // current echo direction is right (final step)
            rightEcho = new echoResults(echoDirection,range,found);
            echoDirection = direction.turn_left();
        }

    }

    @Override
    public JSONObject findNextStep(){
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();


        if ((currentPosition[0] >= 0) && (currentPosition[0] <= 3) && (currentPosition[1] >= 0) && (currentPosition[1] <= 3) && (perimeterPositions.size() > 30)) {

            this.isFinished();

        }

        if ((forwardEcho == null) && (rightEcho == null) && (!echoComplete)) { // No scan result
            //Forward Scan
            parameters.put("direction",direction.toString());
            decision.put("parameters",parameters);
            decision.put("action","echo");

            echoDirection = direction;
            return decision;

        } else if ((rightEcho == null) && (!echoComplete)) { // Only have forward scan results
            //Right Scan
            parameters.put("direction",direction.turn_right().toString());
            decision.put("parameters",parameters);
            decision.put("action","echo");

            echoDirection = direction.turn_right();
            echoComplete = true;
            
            return decision;

        } else { //Have Both Scan Results

            if (isShiftingLeft == true && shiftingRightMovementCount != 2 && shiftingRightMovementCount != 5){

                parameters.put("direction",direction.turn_left().toString());
                decision.put("parameters",parameters);
                decision.put("action","heading");

                shiftingRightMovementCount += 1;

                return decision;

            } else if (isShiftingLeft == true && shiftingRightMovementCount == 2) {

                decision.put("action","fly");

                shiftingRightMovementCount += 1;

                return decision;

            } else if (isShiftingLeft == true && shiftingRightMovementCount == 5) {

                parameters.put("direction",direction.turn_left().toString());
                decision.put("parameters",parameters);
                decision.put("action","heading");

                shiftingRightMovementCount = 1;
                isShiftingLeft = false;

                echoComplete = false;
                forwardEcho = null;
                rightEcho = null;

                return decision;
            }


            if (isShiftingRight == true && shiftingLeftMovementCount != 2 && shiftingLeftMovementCount != 5){

                parameters.put("direction",direction.turn_right().toString());
                decision.put("parameters",parameters);
                decision.put("action","heading");

                shiftingLeftMovementCount += 1;

                return decision;

            } else if (isShiftingRight == true && shiftingLeftMovementCount == 2) {

                decision.put("action","fly");
                shiftingLeftMovementCount += 1;

                return decision;

            } else if (isShiftingRight == true && shiftingLeftMovementCount == 5) {

                parameters.put("direction",direction.turn_right().toString());
                decision.put("parameters",parameters);
                decision.put("action","heading");

                shiftingLeftMovementCount = 1;
                isShiftingRight = false;

                echoComplete = false;
                forwardEcho = null;
                rightEcho = null;

                return decision;
            }


            if (forwardEcho.range > 2) { 

                if (rightEcho.range > 1) {

                    parameters.put("direction",direction.turn_right().toString());
                    decision.put("parameters",parameters);
                    decision.put("action","heading");

                    if (direction == Directions.E) {

                        currentPosition[0] += 10;
                        currentPosition[1] -= 10;
    
                    } else if (direction == Directions.N) {
    
                        currentPosition[0] += 10;
                        currentPosition[1] += 10;
    
    
                    } else if (direction == Directions.S) {
    
                        currentPosition[0] -= 10;
                        currentPosition[1] -= 10;
    
                    } else {
    
                        currentPosition[0] -= 10;
                        currentPosition[1] += 10;
    
                    }

                    echoComplete = false;
                    forwardEcho = null;
                    rightEcho = null;

                    int[] newPosition= {currentPosition[0], currentPosition[1]};
                    perimeterPositions.add(newPosition);

                } else if (rightEcho.range == 0) {

                    isShiftingLeft = true;
                    shiftingLeftMovementCount += 1;

                    parameters.put("direction",direction.turn_left().toString());
                    decision.put("parameters",parameters);
                    decision.put("action","heading");

                    
                    if (direction == Directions.E) {

                        currentPosition[1] += 10;
    
                    } else if (direction == Directions.N) {
    
                        currentPosition[0] -= 10;
    
    
                    } else if (direction == Directions.S) {
    
                        currentPosition[0] += 10;
    
                    } else {
    
                        currentPosition[1] -= 10;
    
                    }
                    int[] newPosition= {currentPosition[0], currentPosition[1]};
                    perimeterPositions.add(newPosition);


                } else if (rightEcho.range == 2) {

                    isShiftingRight = true;
                    shiftingRightMovementCount += 1;

                    parameters.put("direction",direction.turn_right().toString());
                    decision.put("parameters",parameters);
                    decision.put("action","heading");


                    if (direction == Directions.E) {

                        currentPosition[1] -= 10;
    
                    } else if (direction == Directions.N) {
    
                        currentPosition[0] += 10;
    
    
                    } else if (direction == Directions.S) {
    
                        currentPosition[0] -= 10;
    
                    } else {
    
                        currentPosition[1] += 10;
    
                    }
                    int[] newPosition= {currentPosition[0], currentPosition[1]};
                    perimeterPositions.add(newPosition);

                } else {
                    decision.put("action","fly");

                    if (direction == Directions.E) {

                        currentPosition[0] += 10;
    
                    } else if (direction == Directions.N) {
    
                        currentPosition[1] += 10;
    
    
                    } else if (direction == Directions.S) {
    
                        currentPosition[1] -= 10;
    
                    } else {
    
                        currentPosition[0] -= 10;
    
                    }

                    echoComplete = false;
                    forwardEcho = null;
                    rightEcho = null;

                    int[] newPosition= {currentPosition[0], currentPosition[1]};
                    perimeterPositions.add(newPosition);
                }

            } else {

                parameters.put("direction",direction.turn_left().toString());
                decision.put("parameters",parameters);
                decision.put("action","heading");

                if (direction == Directions.E) {

                    currentPosition[0] += 10;
                    currentPosition[1] += 10;

                } else if (direction == Directions.N) {

                    currentPosition[0] -= 10;
                    currentPosition[1] += 10;


                } else if (direction == Directions.S) {

                    currentPosition[0] += 10;
                    currentPosition[1] -= 10;

                } else {

                    currentPosition[0] -= 10;
                    currentPosition[1] -= 10;

                }

                echoComplete = false;
                forwardEcho = null;
                rightEcho = null;

                int[] newPosition= {currentPosition[0], currentPosition[1]};
                    perimeterPositions.add(newPosition);

            }

            return decision;

        }

    }

}