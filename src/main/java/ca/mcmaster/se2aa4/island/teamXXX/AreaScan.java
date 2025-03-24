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
import java.lang.Math;

public class AreaScan {
    private final Logger logger = LogManager.getLogger();
    private Directions droneDirection;
    private DronePosition dronePosition;

    private final Queue<String> taskQueue = new LinkedList<>();

    private List<PointOfInterest> creeks = new LinkedList<>();
    private List<PointOfInterest> sites = new LinkedList<>();

    //gets perimeter values from Drone, which somehow gets them from CreekFind
    //Runs through a minmax heap to get max min vals for x y
    private ArrayList<Integer> checked = new ArrayList<>(); 
    private Integer[] perimeterEdgePositions = {36,14,-10,-2};

    JSONObject decision = new JSONObject();
    JSONObject parameter = new JSONObject();
    
    private boolean reverseTurn = false;
    private boolean turningRight = true; //false is left
    private boolean finished = false;
    private boolean outOfBounds = false;
    private boolean onOcean = false;
    private boolean reset = false;
    private boolean returnOutwards = false; // if false returns inwards, otherwise outwards
    private boolean onWayBack = false;

    private int lengthOfIsland = 36;

    private String creeksFound, sitesFound, biomesFound;
    private int range = 0, scannedLanes = 1;
    private int turns = 0, returns = 0;

    public AreaScan(/*Directions droneDirection,*/ String s){ // starts off facing where it originaly started
        //this.droneDirection = droneDirection;

        //determines 
        this.dronePosition = new DronePosition();

        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String init_heading = info.getString("heading");
        this.droneDirection = Directions.fromString(init_heading);
        logger.info("Initial heading: {}",droneDirection.toString());

        switch(droneDirection){
            case N,S:
                //lengthOfIsland = Math.abs(perimeterEdgePositions[1]) + Math.abs(perimeterEdgePositions[2]);
            case E,W:
                //lengthOfIsland = Math.abs(perimeterEdgePositions[0]) + Math.abs(perimeterEdgePositions[3]);
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
        

        logger.info("onWater{}",onOcean);
        logger.info("outOfBounds{}",outOfBounds);
        logger.info("range{}",range);
        logger.info("turningRight{}",turningRight);
        logger.info("droneDirection{}",droneDirection);
        logger.info("droneY{}",dronePosition.getDroneY());
        logger.info("droneX{}",dronePosition.getDroneX());
        logger.info("reverseTurn{}",reverseTurn);
        logger.info("returnOutwards{}",returnOutwards);
        logger.info("reset{}",reset);
        logger.info("onWayBack{}",onWayBack);


        if ( onOcean == false ){
            linearScan();
            if(dronePosition.getDroneY() > perimeterEdgePositions[3]+1 && dronePosition.getDroneY() < perimeterEdgePositions[1]-1 && dronePosition.getDroneX() > perimeterEdgePositions[2]+1 && dronePosition.getDroneX() < perimeterEdgePositions[0]-1){
                onWayBack = false;
            }
        } else if (outOfBounds == false){
            linearScan();
            echoForward();
        } else {
            logger.info("HELPME");
            if(onWayBack == false){
                switch(droneDirection){
                    case E:
    
                        
                        if (dronePosition.getDroneY() == perimeterEdgePositions[0]-1){ //lowest point
                            returnOutwards = false;
                            reverseTurn = true;
                            logger.info("HELLO1");
    
                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[0]){
                            returnOutwards = true;
                            reverseTurn = true;
                            logger.info("HELLO1");
                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[3]+1){ //lowest point
                            returnOutwards = true;
                            reverseTurn = true;
                            logger.info("HELLO1");
                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[3]){
                            returnOutwards = false;
                            reverseTurn = true;
                            logger.info("HELLO1");
                        }
                        break;
                    
                    case W:
                        if (dronePosition.getDroneY() == perimeterEdgePositions[0]-1){ //lowest point
                            returnOutwards = true;
                            reverseTurn = true;
                            logger.info("HELLO2A");
                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[0]){
                            returnOutwards = false;
                            reverseTurn = true;
                            logger.info("HELLO2B");
                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[3]+1){ //lowest point
                            returnOutwards = false;
                            reverseTurn = true;
                            logger.info("HELLO2C");
                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[3]){
                            returnOutwards = true;
                            reverseTurn = true;
                            logger.info("HELLO2D");
                        }
                        
                        break;
                    case N:
                        if (dronePosition.getDroneX() == perimeterEdgePositions[1]-1){ //lowest point
                            returnOutwards = true;
                            reverseTurn = true;
                            logger.info("HELLO3");
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[1]){
                            returnOutwards = false;
                            reverseTurn = true;
                            logger.info("HELLO3");
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[2]+1){ //lowest point
                            returnOutwards = false;
                            reverseTurn = true;
                            logger.info("HELLO3");
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[2]){
                            returnOutwards = true;
                            reverseTurn = true;
                            logger.info("HELLO3");
                        }   
                        
                        break;
                    case S:
                        if (dronePosition.getDroneX() == perimeterEdgePositions[2]+1){ //lowest point
                            returnOutwards = true;
                            reverseTurn = true;
                            logger.info("HELLO4");
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[2]){
                            returnOutwards = false;
                            reverseTurn = true;
                            logger.info("HELLO4");
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[1]-1){ //lowest point
                            returnOutwards = false;
                            reverseTurn = true;
                            logger.info("HELLO4");
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[1]){
                            returnOutwards = true;
                            reverseTurn = true;
                            logger.info("HELLO4");
                        }
    
                        break;
                }

            }
            
            if (reset == false && reverseTurn == false){
                range = 0;
                reset = true;
            }
            if (turningRight == false && reverseTurn == false){
                if (range <= 2){
                    echoLeft();
                    fly();
                } else{
                    turnLeft();
                    turnLeft();
                    scannedLanes++;
                    range = 0;
                    outOfBounds = false;
                    turningRight = true;
                    reset = false;

                    turns++;
                }

            } else if (turningRight == true && reverseTurn == false){
                if (range <= 2){
                    echoRight();
                    fly();
                } else{
                    turnRight();
                    turnRight();
                    scannedLanes++;
                    range = 0;
                    outOfBounds = false;
                    turningRight = false;
                    reset = false;

                    turns++;
                }
            } else{
                turnBack(returnOutwards);
                onWayBack = true;
                outOfBounds = false;
                reverseTurn = false;
                scannedLanes++;

                returns++;
            }

        }

        if(lengthOfIsland == scannedLanes){
            this.taskQueue.add(decision.put("action", "stop").toString());
            isFinished();
        }

        logger.info("SCANNEDLANES{}",scannedLanes);
        logger.info("lengthOfIsland{}",lengthOfIsland);
        logger.info("turns{}",turns);
        logger.info("returns{}",returns);

        creeksFound = "";
        sitesFound = "";
        biomesFound = "";

        return taskQueue.remove();
    }

    public void updateResults(JSONObject response){
        JSONObject extras = response.getJSONObject("extras");

        if (!extras.has("found") && !extras.has("biomes")) { // check results JSON if an echo was even used
            return; 
        } else if (!extras.has("found")){

            creeksFound = extras.getJSONArray("creeks").toString();
            sitesFound = extras.getJSONArray("sites").toString();
            biomesFound = extras.getJSONArray("biomes").toString();

            if (biomesFound.equals("[\"OCEAN\"]")){
                onOcean = true;
            } else{
                onOcean = false;
            }

        } else{
            range = extras.getInt("range"); 
            if (outOfBounds == false){
                outOfBounds = isOcean(extras.getString("found"));
            }
        }

        if (!sitesFound.equals("")){
            sites.add(new PointOfInterest(dronePosition.getDroneX(),dronePosition.getDroneY(),sitesFound));
        }

        if (!creeksFound.equals("")){
            creeks.add(new PointOfInterest(dronePosition.getDroneX(),dronePosition.getDroneY(),sitesFound));
        }

       /*  for (int i = 0; i < sites.size()-1; i++){
            logger.info("site size{}",sites.size());
            logger.info("site id{}",sites.get(i).getID());
            logger.info("site X{}",sites.get(i).getX());
            logger.info("site Y{}",sites.get(i).getY());
        }*/

    }

    private void echoForward(){
        parameter.put("direction", droneDirection.toString());
        decision.put("parameters", parameter);
        this.taskQueue.add(decision.put("action", "echo").toString());
    }

    private void echoLeft(){
        parameter.put("direction", droneDirection.turn_left().toString());
        decision.put("parameters", parameter);
        this.taskQueue.add(decision.put("action", "echo").toString());
    }

    private void echoRight(){
        parameter.put("direction", droneDirection.turn_right().toString());
        decision.put("parameters", parameter);
        this.taskQueue.add(decision.put("action", "echo").toString());
    }

    private void turnLeft(){

        dronePosition.updatePosition("TURN_LEFT",droneDirection);

        parameter.put("direction", droneDirection.turn_left().toString());
        decision.put("parameters", parameter).toString();
        taskQueue.add(decision.put("action", "heading").toString());

        droneDirection = droneDirection.turn_left();

    }

    private void turnRight(){

        logger.info("I WANT TO DIE");
        dronePosition.updatePosition("TURN_RIGHT",droneDirection);

        parameter.put("direction", droneDirection.turn_right().toString());
        decision.put("parameters", parameter).toString();
        taskQueue.add(decision.put("action", "heading").toString());

        droneDirection = droneDirection.turn_right();
        
    }

    private void linearScan(){
        this.taskQueue.add(decision.put("action","fly").toString());
        dronePosition.updatePosition("FLY",droneDirection);
        this.taskQueue.add(decision.put("action","scan").toString());

    }

    private void fly(){
        this.taskQueue.add(decision.put("action","fly").toString());
        dronePosition.updatePosition("FLY",droneDirection);
    }

    private void turnBack(boolean turningRight){

        logger.info("TURNYOUPIECEOFACTUAL");
        fly();

        if (turningRight == true){
            turnRight();

            fly();

            turnLeft();
            turnLeft();
            turnLeft();

            echoForward();
        } else{
            turnLeft();

            fly();

            turnRight();
            turnRight();
            turnRight();

            echoForward();
        }
    }

}
