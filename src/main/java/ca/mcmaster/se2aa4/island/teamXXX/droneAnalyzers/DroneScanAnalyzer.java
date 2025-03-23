package ca.mcmaster.se2aa4.island.teamXXX.droneAnalyzers;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Biome;
import ca.mcmaster.se2aa4.island.teamXXX.locationClasses.*;

public interface DroneScanAnalyzer extends DroneAnalyzer{

    public Boolean findBiomeInScan(Biome biome);
    public EmergencySite parseSitesInExtrasResult(JSONObject extrasResult);
    public Creek parseCreekInExtrasResult(JSONObject extrasResult);
    public Biome[] parseBiomesInExtrasResult(JSONObject extrasResult);
    public JSONObject parseExtrasInResults(JSONObject scannedResult);

}

/*
        
    @Override
    private void parseBiomesResult(JSONArray scannedBiomes){

        biomes = new Biome[scannedBiomes.length()];

        for (int i = 0; i < scannedBiomes.length(); i++) {

            biomes[i] = Biome.fromString(scannedBiomes[i]);

        }

    }

    @Override
    private void parseCreekResult(JSONArray scannedCreeks){

        creeks = new Creek[scannedCreeks.length()];

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
        */