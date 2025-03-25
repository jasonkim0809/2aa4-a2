package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

public class Drone {
    private final Logger logger = LogManager.getLogger();

    private int battery_level;
    private Directions direction;
    int phase = 0;

    private final IslandFinder islandFinder;

    private PerimeterMappingAlgorithm perimeterMapping;
    private PerimeterDimensions perimeterDimensions;

    private AreaScan areaScanner;
    
        public Drone(String s){
            JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
            direction = Directions.fromString(info.getString("heading"));
            battery_level = info.getInt("budget");
            islandFinder = new IslandFinder(s);
            perimeterMapping = new PerimeterMappingAlgorithm(direction.toString());
        }
    
        public JSONObject getDecision(){ // called by explorer class
            JSONObject decision = new JSONObject();
     
            if (phase == 0) {
                decision = islandFinder.findNextStep();
            }
            else if (phase == 1) {
                decision = perimeterMapping.findNextStep();
            } else if (phase == 2) {
                perimeterDimensions = new PerimeterDimensions(perimeterMapping.perimeterValues()[0], perimeterMapping.perimeterValues()[1], perimeterMapping.perimeterValues()[2], perimeterMapping.perimeterValues()[3]);
                areaScanner = new AreaScan(perimeterMapping.getDirection(),perimeterDimensions);
                phase++;
                decision.put("action","scan");
                return decision;
            } else if (phase == 3){
                logger.info("PHASE 3");
                String areaScanDecision = areaScanner.findNextStep();
                decision = new JSONObject(areaScanDecision);
            }
            else {
                decision.put("action","stop");
            }
    
            return decision;
        }
    
        public void getResults(JSONObject response){
            if (phase == 0){
                islandFinder.updateResults(response);    
            }
            else if (phase == 1){ 
                perimeterMapping.updateResults(response);
            }
            else if (phase == 3){
                areaScanner.updateResults(response);
            }
            this.updatePhase();
        }
    
        private void updatePhase(){
            if (islandFinder.isFinished() && phase == 0){
                perimeterMapping = new PerimeterMappingAlgorithm(islandFinder.getDirection().toString());
                phase++;
            }
            else if (perimeterMapping.isFinished() && phase == 1){
                phase++;
            }
            else if (areaScanner!=null && (areaScanner.isFinished() && phase == 3)){
                phase++;
            }
    }
}