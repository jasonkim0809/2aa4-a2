package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.StringReader;

import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;


public class islandFinder {
    public islandFinder(Directions direction, String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        
    }
}
