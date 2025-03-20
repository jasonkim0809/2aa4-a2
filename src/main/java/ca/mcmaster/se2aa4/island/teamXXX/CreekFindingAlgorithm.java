package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

interface DroneScannerResults{

    private JSONObject initCreeks;
    private JSONObject initBiomes;
    public JSONObject extras;

    private JSONObject parseCreekResult(JSONObject scannedCreeks);
    private JSONObject parseBiomesResult(JSONObject scannedBiomes);
    public JSONObject parseScanResults(JSONObject resultExtras);

}

enum Biomes {

    OCEAN, LAKE, BEACH, GRASSLAND, MANGROVE, TROPICAL_RAIN_FOREST, TROPICAL_SEASONAL_FOREST, TEMPERATE_DECIDUOUS_FOREST, TEMPERATE_RAIN_FOREST, TEMPERATE_DESERT, TAIGA, SNOW, TUNDRA, ALPINE, GLACIER, SHRUBLAND, SUB_TOPICAL_DESERT;

}

public class CreekFindingAlgorithm {
    private final Logger logger = LogManager.getLogger();


    public CreekFindingAlgorithm(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
    }

    private class scanResults{
        Directions direction;
        public scanResults(Directions direction){
            this.direction = direction;

            initCreeks = info.getJSONArray("creeks");
            initBiomes = info.getJSONArray("biomes");

        }
    }

    public void findNextStep(){


    }

    public void updateScanResults(JSONObject results){


    }

}