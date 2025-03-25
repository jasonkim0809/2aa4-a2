package ca.mcmaster.se2aa4.island.teamXXX;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

public class AreaScan {
    private final Logger logger = LogManager.getLogger();
    private Directions droneDirection;
    private DronePosition dronePosition;

    private final Queue<String> taskQueue = new LinkedList<>();

    private List<PointOfInterest> creeks = new LinkedList<>();
    private List<PointOfInterest> sites = new LinkedList<>();

    //gets perimeter values from Drone, which somehow gets them from CreekFind
    //Runs through a minmax heap to get max min vals for x y
    private Integer[] perimeterEdgePositions = new Integer[4];

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

    private int lengthOfIsland;

    private String creeksFound, sitesFound, biomesFound;
    private int range = 0, scannedLanes = 0;
    double smallestToSite;

    String closestCreekID;

    public boolean isFinished(){
        return finished;
    }

    public AreaScan(/*Directions droneDirection,*/ String s,PerimeterDimensions p){ // starts off facing where it originaly started
        //this.droneDirection = droneDirection;

        //determines 
        this.dronePosition = new DronePosition();

        String init_heading = s;
        this.droneDirection = Directions.fromString(init_heading);
        logger.info("Initial heading: {}",droneDirection.toString());

        this.perimeterEdgePositions[0] = p.perimeterEdgePositions[0];
        this.perimeterEdgePositions[1] = p.perimeterEdgePositions[1];
        this.perimeterEdgePositions[2] = p.perimeterEdgePositions[2];
        this.perimeterEdgePositions[3] = p.perimeterEdgePositions[3];

        switch(droneDirection){
            case N,S:
                lengthOfIsland = Math.abs(perimeterEdgePositions[1]) + Math.abs(perimeterEdgePositions[2]);
            case E,W:
                lengthOfIsland = Math.abs(perimeterEdgePositions[0]) + Math.abs(perimeterEdgePositions[3]);
        }

        smallestToSite = lengthOfIsland;

        if (dronePosition.getDroneX() == perimeterEdgePositions[1] || dronePosition.getDroneX() == perimeterEdgePositions[2] || dronePosition.getDroneY() == perimeterEdgePositions[0] || dronePosition.getDroneY() == perimeterEdgePositions[3]){
            if (turningRight == true){
                turningRight = false;
            } else{
                turningRight = true;
            }
        }

    }

    public String closestCreek(){ //goes to next phase
        for (int i = 0; i < creeks.size()-1; i++){

            int creekX = creeks.get(i).getX();
            int creekY = creeks.get(i).getY();
            int siteX = sites.get(0).getX();
            int siteY = sites.get(0).getY();
            String creekID = creeks.get(i).getID();

            double distanceToSite = Math.sqrt(Math.pow((creekY-siteY),2)+Math.pow((creekX-siteX),2));

            if(distanceToSite < smallestToSite && creekID != null){
                smallestToSite = distanceToSite;
                closestCreekID = creekID;
                logger.info("smallestToSite{}",smallestToSite);
                logger.info("closestCreekID{}",closestCreekID);
                logger.info("creekID{}",creekID);
            }
        }
        return closestCreekID;
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

        if (!taskQueue.isEmpty()){
            return taskQueue.remove();
        }

        if ( onOcean == false ){
            linearScan();
            if(dronePosition.getDroneY() > perimeterEdgePositions[3]+1 && dronePosition.getDroneY() < perimeterEdgePositions[1]-1 && dronePosition.getDroneX() > perimeterEdgePositions[2]+1 && dronePosition.getDroneX() < perimeterEdgePositions[0]-1){
                onWayBack = false;
            }
        } else if (outOfBounds == false){
            linearScan();
            echoForward();
        } else {
   
            if(onWayBack == false){
                switch(droneDirection){
                    case E:
    
                        
                        if (dronePosition.getDroneY() == perimeterEdgePositions[0]-1){ //lowest point
                            returnOutwards = false;
                            reverseTurn = true;
    
                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[0]){
                            returnOutwards = true;
                            reverseTurn = true;

                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[3]+1){ //lowest point
                            returnOutwards = true;
                            reverseTurn = true;

                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[3]){
                            returnOutwards = false;
                            reverseTurn = true;
                        }
                        break;
                    
                    case W:
                        if (dronePosition.getDroneY() == perimeterEdgePositions[0]-1){ //lowest point
                            returnOutwards = true;
                            reverseTurn = true;

                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[0]){
                            returnOutwards = false;
                            reverseTurn = true;
                         
                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[3]+1){ //lowest point
                            returnOutwards = false;
                            reverseTurn = true;
                          
                        } else if (dronePosition.getDroneY() == perimeterEdgePositions[3]){
                            returnOutwards = true;
                            reverseTurn = true;
                           
                        }
                        
                        break;
                    case N:
                        if (dronePosition.getDroneX() == perimeterEdgePositions[1]-1){ //lowest point
                            returnOutwards = true;
                            reverseTurn = true;
                           
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[1]){
                            returnOutwards = false;
                            reverseTurn = true;
                           
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[2]+1){ //lowest point
                            returnOutwards = false;
                            reverseTurn = true;
                            
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[2]){
                            returnOutwards = true;
                            reverseTurn = true;
                          
                        }   
                        
                        break;
                    case S:
                        if (dronePosition.getDroneX() == perimeterEdgePositions[2]+1){ //lowest point
                            returnOutwards = true;
                            reverseTurn = true;
                           
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[2]){
                            returnOutwards = false;
                            reverseTurn = true;
                          
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[1]-1){ //lowest point
                            returnOutwards = false;
                            reverseTurn = true;
                           
                        } else if (dronePosition.getDroneX() == perimeterEdgePositions[1]){
                            returnOutwards = true;
                            reverseTurn = true;
                           
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

                }
            } else{
                turnBack(returnOutwards);
                onWayBack = true;
                outOfBounds = false;
                reverseTurn = false;

            }

        }

        if(lengthOfIsland == scannedLanes){
            this.taskQueue.add(decision.put("action", "stop").toString());
            finished = true;
            logger.info("Found Closest Creek{}",closestCreek());
        }

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

        if (!sitesFound.equals("[]") && sitesFound != null && !sitesFound.equals("")){
            sites.add(new PointOfInterest(dronePosition.getDroneX(),dronePosition.getDroneY(),sitesFound));
        }

        if (!creeksFound.equals("[]") && creeksFound != null && !creeksFound.equals("")){
            creeks.add(new PointOfInterest(dronePosition.getDroneX(),dronePosition.getDroneY(),creeksFound));
        }

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