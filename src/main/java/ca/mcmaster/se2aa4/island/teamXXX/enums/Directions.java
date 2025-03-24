package ca.mcmaster.se2aa4.island.teamXXX.enums;

import org.json.JSONObject;

public enum Directions {
    N,E,W,S;
    public Directions turn_right(){
        return switch (this) {
            case N -> E;
            case E -> S;
            case S -> W;
            case W -> N;
        };
    }
    public Directions turn_left(){
        return switch (this) {
            case N -> W;
            case E -> N;
            case S -> E;
            case W -> S;
        };
    }

    @Override
    public String toString(){
        String new_str = "";
        switch (this) {
            case N:
                new_str = "N";
                break;
            case E:
                new_str = "E";
                break;
            case W:
                new_str = "W";
                break;
            case S:
                new_str = "S";
                break;
        };

        return new_str;
    }
    public static Directions fromString(String s){
        switch (s) {
            case "N":
                return Directions.N;
            case "E":
                return Directions.E;
            case "W":
                return Directions.W;
            case "S":
                return Directions.S;
        };
        return null;
    }
}
