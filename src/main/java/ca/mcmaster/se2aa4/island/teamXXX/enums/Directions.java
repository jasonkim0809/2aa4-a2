package ca.mcmaster.se2aa4.island.teamXXX.enums;

public enum Directions {
    N,E,W,S;
    public Directions turn_right(){
        Directions new_direction = this;
        switch (new_direction) {
            case N:
                new_direction = E;
                break;
            case E:
                new_direction = S;
                break;
            case W:
                new_direction = N;
                break;
            case S:
                new_direction = W;
                break;
        };
        return new_direction;
    }
    public Directions turn_left(){
        Directions new_direction = this;
        switch (new_direction) {
            case N:
                new_direction = W;
                break;
            case E:
                new_direction = N;
                break;
            case W:
                new_direction = S;
                break;
            case S:
                new_direction = E;
                break;
        };
        return new_direction;
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