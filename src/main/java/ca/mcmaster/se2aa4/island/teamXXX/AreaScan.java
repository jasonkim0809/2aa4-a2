package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

import ca.mcmaster.se2aa4.island.teamXXX.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.DronePosition;

import java.util.*;

public class AreaScan {
    private final Logger logger = LogManager.getLogger();
    private Directions droneDirection;
    private DronePosition dronePosition;

    private final Queue<String> taskQueue = new LinkedList<>();

    private List<PointOfInterest> creeks = new LinkedList<>();
    private List<PointOfInterest> sites = new LinkedList<>();

    //gets perimeter values from Drone, which somehow gets them from CreekFind
    //Runs through a minmax heap to get max min vals for x y
    private int[][] evenMap = {{-2,-1,0,1,2},{6,7,9,12,13}};
    private int[][] oddMap = {{75,74,73,75,74,73},{75,75,75,65,65,65}};
    private ArrayList<Integer> checked = new ArrayList<>();

    JSONObject decision = new JSONObject();
    JSONObject parameter = new JSONObject();
    
    private boolean reverseTurn = false;
    private boolean mainCoordY = false; // false is X
    private boolean turningRight = false; //false is left
    private boolean finished = false;
    private boolean outOfBounds = false;
    private boolean onWater = false;
    private boolean isEvenLength = false;

    private String creeksFound, sitesFound, biomesFound;
    private int range = 0;
    private int max = 0, min = 0;

    public AreaScan(/*Directions droneDirection,*/ String s){ // starts off facing where it originaly started
        //this.droneDirection = droneDirection;

        //determines 
        this.dronePosition = new DronePosition();

        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String init_heading = info.getString("heading");
        this.droneDirection = Directions.fromString(init_heading);
        logger.info("Initial heading: {}",droneDirection.toString());
        switch (droneDirection) {
            case E, W -> {
                mainCoordY = false;
                max = maxCoord(evenMap[0]);
                min = minCoord(evenMap[0]);
            }
            case S, N -> {
                mainCoordY = true;
                max = maxCoord(evenMap[1]);
                min = minCoord(evenMap[1]);
            }
        }

    }

    public boolean isFinished(){ //goes to next phase
        return this.finished;
    }

    public class ScanResults {
        JSONArray scanCreeks;
        JSONArray scanSites;
        JSONArray scanBiomes;

        public ScanResults(JSONObject extraInfo) {
            this.scanCreeks = extraInfo.getJSONArray("creeks");
            this.scanSites = extraInfo.getJSONArray("sites");
            this.scanBiomes = extraInfo.getJSONArray("biomes");
        }
    }

    private boolean isOcean(String echo){
        return (echo!=null && echo.equals("OUT_OF_RANGE"));
    }

    public String findNextStep(){

        //logger.info("The drone is facing this plce you know{}", droneDirection);

        if (!taskQueue.isEmpty()){
            return taskQueue.remove();
        }

        if ( onWater == false ){
            //logger.info("The drone is facing this way bum{}", droneDirection);
            linearScan();
        } else if (outOfBounds == false){
            if (range == 0){
                linearScan();
                echoForward();
            } else{
                linearScan();
            }
            
        } else {
            logger.info("START TURN");
            if(checked.size()%2 == 0){
                isEvenLength = true;
            }else{
                isEvenLength = false;
            }

            switch (droneDirection) {
                case E -> {
                    if(dronePosition.getDroneX() >= max){
                        reverseTurn = true;
                    }
                }
                case W -> {
                    if(dronePosition.getDroneX() <= min){
                        reverseTurn = true;
                    }
                }
                case N -> {
                    if(dronePosition.getDroneY() >= max){
                        reverseTurn = true;
                    }
                }
                case S -> {
                    if(dronePosition.getDroneY() <= min){
                        reverseTurn = true;
                    }
                }
            }

            int reach;
            if (turningRight == false && reverseTurn == false){
                if (mainCoordY == false){
                    reach = findIndex(evenMap[1],evenMap[0],dronePosition.getDroneY(),dronePosition.getDroneX()-1);
                    switch (droneDirection) {
                        case E -> {
                            reach = reach - 2;
                        }
                        case W -> {
                            reach = reach + 2;
                        }
                    }
                    range = evenMap[1][reach] - dronePosition.getDroneY() + 1;
                } else{
                    logger.info("YOU MADE IT!!!!!");
                    logger.info("droneY{}",dronePosition.getDroneY());
                    logger.info("droneX{}",dronePosition.getDroneX());
                    reach = findIndex(evenMap[0],evenMap[1],dronePosition.getDroneX(),dronePosition.getDroneY()-1);
                    logger.info("This is the REACH{}",reach);
                    
                    switch (droneDirection) {
                        case S -> {
                            reach = reach - 2;
                        }
                        case N -> {
                            reach = reach + 2;
                        }
                    }
                    range = evenMap[0][reach] - dronePosition.getDroneX() + 1;
                    logger.info("This is the NEW RANGE{}",range);
                }

                turningRight = true;

                if ( range > 0 ){
                    linearScan();
                }else{
                    range = 0;
                    logger.info("CONGRATULATIONS ITS ALL OVER");
                    turnLeft();
                    turnLeft();
                    outOfBounds = false;
                }
                
                checked.add(evenMap[0][reach]); //checked always +1 to account for starting line
            } else if (turningRight == true && reverseTurn == false){
                //needs fixing, doesnt always turn right on comeback
                if (mainCoordY == false){
                    reach = findIndex(evenMap[1],evenMap[0],dronePosition.getDroneY(),dronePosition.getDroneX()-1);
                    switch (droneDirection) {
                        case E -> {
                            reach = reach - 2;
                        }
                        case W -> {
                            reach = reach + 2;
                        }
                    }
                    range = evenMap[1][reach] - dronePosition.getDroneY() + 1;
                } else{
                    
                    reach = findIndex(evenMap[0],evenMap[1],dronePosition.getDroneX(),dronePosition.getDroneY()-1);

                    logger.info("THIS IS IT!!!!!!{}", reach);
                    switch (droneDirection) {
                        case S -> {
                            reach = reach + 2;
                        }
                        case N -> {
                            reach = reach - 2;
                        }
                    }
                    range = evenMap[0][reach] - dronePosition.getDroneX() + 1;
                }

                turningRight = false;

                if ( range > 0 ){
                    linearScan();
                }else{
                    range = 0;
                    turnRight();
                    turnRight();
                    outOfBounds = false;
                }

                checked.add(evenMap[0][reach]); //checked always +1 to account for starting line
            } else {
                if(isEvenLength == true){
                    turnBack(true);
                    isEvenLength = false;
                } else{
                    turnBack(false);
                    isEvenLength = false;
                }

            }
        }

        if ((evenMap[0]).length == (checked.size()+1)){
            this.taskQueue.add(decision.put("action", "stop").toString());
        }

        creeksFound = "";
        sitesFound = "";
        biomesFound = "";

        //logger.info("end{}", droneDirection);

        return taskQueue.remove();
    }

    public void updateResults(JSONObject response){
        JSONObject extras = response.getJSONObject("extras");
        logger.info("GETSHERE");

        if (!extras.has("found") && !extras.has("biomes")) { // check results JSON if an echo was even used
            return; 
        } else if (!extras.has("found")){

            creeksFound = extras.getJSONArray("creeks").toString();
            sitesFound = extras.getJSONArray("sites").toString();
            biomesFound = extras.getJSONArray("biomes").toString();

            logger.info("CHECKPOINTS");

            if (biomesFound.equals("[\"LAKE\"]") || biomesFound.equals("[\"OCEAN\"]")){
                logger.info("CHECKPOINTS2");
                onWater = true;
            } else{
                onWater = false;
            }

        } else{
            range = extras.getInt("range"); 
            outOfBounds = isOcean(extras.getString("found"));
            logger.info("range{}",range>0);
        }

        if (!sitesFound.equals("")){
            sites.add(new PointOfInterest(dronePosition.getDroneX(),dronePosition.getDroneY(),sitesFound));
        }

        if (!creeksFound.equals("")){
            creeks.add(new PointOfInterest(dronePosition.getDroneX(),dronePosition.getDroneY(),sitesFound));
        }

    }
    //if main coordY than x would be mainCoord searched for here
    public int findIndex(int[] mainCoord, int[] secondaryCoord, int mainTarget, int secondaryTarget) {
        logger.info("Why Is there more");
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < mainCoord.length; i++) {
            if (mainCoord[i] == mainTarget) {
                indexes.add(i);
            }
        }
        logger.info("we good?");
        logger.info("This is the REACH{}",indexes.get(0));
        //logger.info("This is the REACH",secondaryCoord[Integer.valueOf(indexes.get(1))]);

        if(indexes.size()==1){
            return indexes.get(0);
        }
        logger.info("This is the 2REACH{}",indexes.get(0));
        if (secondaryCoord[indexes.get(0)] > secondaryCoord[indexes.get(1)]){
            if (secondaryTarget > secondaryCoord[indexes.get(0)]){
                return indexes.get(0);
            }else{
                return indexes.get(1);
            }
        } else{
            if (secondaryTarget > secondaryCoord[indexes.get(1)]){
                return indexes.get(1);
            }else{
                return indexes.get(0);
            }
        }

    }

    public static int maxCoord(int[] arr) {

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int num : arr) {
            maxHeap.add(num);
        }
            return maxHeap.peek();
    }
        
    public static int minCoord(int[] arr) {
        
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : arr) {
            minHeap.add(num);
        }
            return minHeap.peek();
    }

    private void echoForward(){
        parameter.put("direction", droneDirection.toString());
        decision.put("parameters", parameter);
        this.taskQueue.add(decision.put("action", "echo").toString());
    }

    private void turnLeft(){
        parameter.put("direction", droneDirection.turn_left().toString());
        decision.put("parameters", parameter).toString();
        taskQueue.add(decision.put("action", "heading").toString());

        droneDirection = droneDirection.turn_left();
        dronePosition.updatePosition("TURN_LEFT",droneDirection);
    }

    private void turnRight(){
        parameter.put("direction", droneDirection.turn_right().toString());
        decision.put("parameters", parameter).toString();
        taskQueue.add(decision.put("action", "heading").toString());

        droneDirection = droneDirection.turn_right();
        dronePosition.updatePosition("TURN_RIGHT",droneDirection);
        
    }

    private void linearScan(){
        this.taskQueue.add(decision.put("action","fly").toString());
        dronePosition.updatePosition("FLY",droneDirection);
        if (range == 0){
            this.taskQueue.add(decision.put("action","scan").toString());
        } else {
            range--;
        }
    }

    private void turnBack(boolean turningRight){
        this.taskQueue.add(decision.put("action","fly").toString());
        dronePosition.updatePosition("FLY",droneDirection);

        if (turningRight == true){
            turnRight();

            this.taskQueue.add(decision.put("action","fly").toString());
            dronePosition.updatePosition("FLY",droneDirection);

            turnLeft();
            turnLeft();
            turnLeft();

            echoForward();
        } else{
            turnLeft();

            this.taskQueue.add(decision.put("action","fly").toString());
            dronePosition.updatePosition("FLY",droneDirection);

            turnRight();
            turnRight();
            turnRight();

            echoForward();
        }
    }

}
