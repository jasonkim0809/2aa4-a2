package ca.mcmaster.se2aa4.island.teamXXX;
//import static org.junit.Assert.fail;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.droneAnalyzers.DroneEchoAnalyzer;
import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.enums.Found;

class EchoResults{
    int range;
    Found found; // either "GROUND" or "OUT_OF_RANGE"
    Directions direction;
    public EchoResults(Directions direction, int range, Found found){
        this.direction = direction;
        this.range = range;
        this.found = found;
    }
}

public class PerimeterMappingAlgorithm implements DroneEchoAnalyzer, NavigationInterface {
    private final Logger logger = LogManager.getLogger();

    private Directions direction;
    private Directions echoDirection;
    private Directions initialDirections;
    private int returnToInitialPosPhase = 1;

    private Integer[] edgePositions = new Integer[4];
    private int edgePosCollected = 0;

    private int isUTurningPhases = 1;
    
    private int[] currentPosition = new int[2];

    private EchoResults leftEcho = null;
    private EchoResults rightEcho = null;
    private boolean isEchoing = false;

    private boolean finished = false;

    public PerimeterMappingAlgorithm(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String init_heading = info.getString("heading");
        this.direction = Directions.fromString(init_heading);

        initialDirections = direction;
        echoDirection = direction.turn_left();

        currentPosition[0] = 0;
        currentPosition[1] = 0;
    }

    public Integer[] perimeterValues() {

        Integer[] perimeterValuesArray =  new Integer[4];

        perimeterValuesArray[0] = edgePositions[0];
        perimeterValuesArray[1] = edgePositions[1];
        perimeterValuesArray[2] = edgePositions[2];
        perimeterValuesArray[3] = edgePositions[3];

        return perimeterValuesArray;

    }


    @Override
    public boolean isFinished(){
        return this.finished;
    }

    public void overrideDirection(Directions d){
        initialDirections = d;
        direction = d;
        echoDirection = direction.turn_left();
    }

    public String getDirection(){
        return direction.toString();
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
            leftEcho = new EchoResults(echoDirection,range,found);
        }
        else if (echoDirection == direction.turn_right()){ // current echo direction is right (final step)
            rightEcho = new EchoResults(echoDirection,range,found);
        }

    }

    public JSONObject flyStraight() {

        JSONObject decision = new JSONObject();

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

    public JSONObject turnRight() {

        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        
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

    public JSONObject turnRight(int[] locationToMark) {

        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("direction",direction.turn_right().toString());
        decision.put("parameters",parameters);
        decision.put("action","heading");

        if (direction == Directions.N) {

            edgePositions[0] = locationToMark[1];

            currentPosition[0] += 1;
            currentPosition[1] += 1;

        }else if (direction == Directions.E) {

            edgePositions[1] = locationToMark[0];

            currentPosition[0] += 1;
            currentPosition[1] -= 1;

        } else if (direction == Directions.W) {

            edgePositions[2] = locationToMark[0];

            currentPosition[0] -= 1;
            currentPosition[1] += 1;


        } else {

            edgePositions[3] = locationToMark[0];

            currentPosition[0] -= 1;
            currentPosition[1] -= 1;

        }

        direction = direction.turn_right();
        echoDirection = direction.turn_left();

        return decision;
    }


    @Override
    public JSONObject findNextStep(){
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();

        if (edgePosCollected == 4 & currentPosition[0] == 0 & currentPosition[1] == 0) {

            finished = true;
        }

        if (edgePosCollected == 4) {

            if (returnToInitialPosPhase == 1){

                if (direction != initialDirections.turn_right().turn_right()) {

                    if ((direction == Directions.N | direction == Directions.S) & Math.abs(currentPosition[1]) == 1) {

                        returnToInitialPosPhase += 1;

                        return this.turnRight();

                    } else if ((direction == Directions.N | direction == Directions.S) & Math.abs(currentPosition[1]) > 1) {
                        
                        return this.flyStraight();

                    } else if ((direction == Directions.W | direction == Directions.E) & Math.abs(currentPosition[0]) == 1) {

                        returnToInitialPosPhase += 1;

                        return this.turnRight();

                    } else if ((direction == Directions.W | direction == Directions.E) & Math.abs(currentPosition[0]) > 1) {
                        
                        return this.flyStraight();

                    }

                } else {;
                    
                    return this.turnRight();

                }

            } else {

                return this.flyStraight();

            }


        }

        if (isUTurningPhases == 2) {

            isUTurningPhases = 3;

            decision = this.turnRight();
            echoDirection = direction.turn_left();

            return decision;

        } else if (isUTurningPhases == 3) {

            isUTurningPhases = 1;

            isEchoing = true;

            return this.flyStraight();

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

                decision = this.flyStraight();

                isEchoing = true;

            } else if (leftEcho.found == Found.GROUND | rightEcho.found == Found.GROUND) {

                decision = this.flyStraight();

                isEchoing = true;

            } else {

                if (edgePosCollected % 2 == 0) {

                    isUTurningPhases = 2;

                } else {

                    isEchoing = true;

                }

                edgePosCollected += 1;

                decision = turnRight(currentPosition);
            }

        }

        return decision;

    }

}