package ca.mcmaster.se2aa4.island.teamXXX.enums;

public enum Found {

    GROUND, OUT_OF_RANGE;

    @Override
    public String toString(){
        String new_str = "";
        switch (this) {
            case GROUND:
                new_str = "GROUND";
                break;
            case OUT_OF_RANGE:
                new_str = "OUT_OF_RANGE";
                break;
        }
        return new_str;
    }


    public static Found fromString(String s){
        switch (s) {
            case "GROUND":
                return Found.GROUND;
            case "OUT_OF_RANGE":
                return Found.OUT_OF_RANGE;
        };
        return null;
    }

}