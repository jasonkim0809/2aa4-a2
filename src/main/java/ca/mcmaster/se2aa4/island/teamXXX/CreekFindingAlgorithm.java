package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

interface NavigationInterface{

    public JSONObject findNextStep();
    public void updateEchoResults(JSONObject results);


}


interface DroneScanner{

    private Biomes[] biomes;
    private Creeks[] creeks;
    private Sites[] sites;

    private Boolean findBiomeInScan(Biome biome);

    private void parseSitesResult(JSONArray scannedSites);
    private void parseCreekResult(JSONArray scannedCreeks);
    private void parseBiomesResult(JSONArray scannedBiomes);
    private void parseExtraResult(JSONObject resultExtras);
    public void parseScanResults(JSONObject scannedResult);

}

enum Biome {

    OCEAN, LAKE, BEACH, GRASSLAND, MANGROVE, TROPICAL_RAIN_FOREST, TROPICAL_SEASONAL_FOREST, TEMPERATE_DECIDUOUS_FOREST, TEMPERATE_RAIN_FOREST, TEMPERATE_DESERT, TAIGA, SNOW, TUNDRA, ALPINE, GLACIER, SHRUBLAND, SUB_TOPICAL_DESERT;

    @Override
    public String toString(){
        String new_str = "";
        switch (this) {
            case OCEAN:
                new_str = "OCEAN";
                break;
            case LAKE:
                new_str = "LAKE";
                break;
            case BEACH:
                new_str = "BEACH";
                break;
            case GRASSLAND:
                new_str = "GRASSLAND";
                break;
            case MANGROVE:
                new_str = "MANGROVE";
                break;
            case TROPICAL_RAIN_FOREST:
                new_str = "TROPICAL_RAIN_FOREST";
                break;
            case TROPICAL_SEASONAL_FOREST:
                new_str = "TROPICAL_SEASONAL_FOREST";
                break;
            case TEMPERATE_DECIDUOUS_FOREST:
                new_str = "TEMPERATE_DECIDUOUS_FOREST";
                break;
            case TEMPERATE_RAIN_FOREST:
                new_str = "TEMPERATE_RAIN_FOREST";
                break;
            case TEMPERATE_DESERT:
                new_str = "TEMPERATE_DESERT";
                break;
            case TAIGA:
                new_str = "TAIGA";
                break;
            case SNOW:
                new_str = "SNOW";
                break;
            case TUNDRA:
                new_str = "TUNDRA";
                break;
            case ALPINE:
                new_str = "ALPINE";
                break;
            case SHRUBLAND:
                new_str = "SHRUBLAND";
                break;
            case SUB_TOPICAL_DESERT:
                new_str = "SUB_TOPICAL_DESERT";
                break;
        };

        public static Biome fromString(String s){
            switch (s) {
                case "OCEAN":
                    return Biome.OCEAN;
                case "LAKE":
                    return Biome.LAKE;
                case "BEACH":
                    return Biome.BEACH;
                case "GRASSLAND":
                    return Biome.GRASSLAND;
                case "MANGROVE":
                    return Biome.MANGROVE;
                case "TROPICAL_RAIN_FOREST":
                    return Biome.TROPICAL_RAIN_FOREST;
                case "TROPICAL_SEASONAL_FOREST":
                    return Biome.TROPICAL_SEASONAL_FOREST;
                case "TEMPERATE_DECIDUOUS_FOREST":
                    return Biome.TEMPERATE_DECIDUOUS_FOREST;
                case "TEMPERATE_RAIN_FOREST":
                    return Biome.TEMPERATE_RAIN_FOREST;
                case "TEMPERATE_DESERT":
                    return Biome.TEMPERATE_RAIN_FOREST;
                case "TAIGA":
                    return Biome.TAIGA;
                case "SNOW":
                    return Biome.SNOW
                case "TUNDRA":
                    return Biome.TUNDRA;
                case "ALPINE":
                    return Biome.ALPINE;
                case "SHRUBLAND":
                    return Biome.SHRUBLAND;
                case "SUB_TOPICAL_DESERT":
                    return Biome.SHRUBLAND;
            };

    }

}

interface LocationsOfInterest {

    String id;
    int[] position;

}

class Creek implements LocationsOfInterest {

    String id;
    int[] position;

    public Creek(String id, int creekXPosition, int creekYPosition){

        this.id = id;
        this.position = {creekXPosition,creekYPosition};

    }

}

public class CreekFindingAlgorithm implements DroneScanner {
    private final Logger logger = LogManager.getLogger();

    private Biome[] biomes;
    private Creek[] creeks;

    private int[] position;

    public CreekFindingAlgorithm(String s, int creekXPosition, int creekYPosition){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        this.position = {creekXPosition,creekYPosition};
    }

    @Override
    private void parseBiomesResult(JSONArray scannedBiomes){

        biomes = new Creeks[scannedBiomes.length()];

        for (int i = 0; i < scannedBiomes.length(); i++) {

            biomes[i] = Biome.fromString(scannedBiomes[i]);

        }

    }

    @Override
    private void parseCreekResult(JSONArray scannedCreeks){

        creeks = new Creeks[scannedCreeks.length()];

        for (int i = 0; i < scannedCreeks.length(); i++) {

            creeks[i] = new Creek(scannedCreeks[i], position[0], position[1]);

        }

    }

    @Override
    private void parseExtraResult(JSONObject resultExtras){
        
        JSONArray biomes = resultExtras.getJSONArray("biomes");
        parseBiomesResult(biomes)

        JSONArray creeks = resultExtras.getJSONArray("creeks");
        parseCreekResult(creeks)


    }

    public void parseScanResults(JSONObject scannedResult){

        JSONObject extras = scannedResult.getJSONObject("extras");
        parseExtraResult(extras);

    }

    public void updateScanResults(JSONObject results){


    }

    public void findNextStep(){


    }

}