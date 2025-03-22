package ca.mcmaster.se2aa4.island.teamXXX.enums;


public enum Biome {

    OCEAN, LAKE, BEACH, GRASSLAND, MANGROVE, TROPICAL_RAIN_FOREST, TROPICAL_SEASONAL_FOREST, TEMPERATE_DECIDUOUS_FOREST,TEMPERATE_RAIN_FOREST, TEMPERATE_DESERT, TAIGA, SNOW, TUNDRA, ALPINE, GLACIER, SHRUBLAND, SUB_TOPICAL_DESERT;

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
            case GLACIER:
                new_str = "GLACIER";
                break;
            case SHRUBLAND:
                new_str = "SHRUBLAND";
                break;
            case SUB_TOPICAL_DESERT:
                new_str = "SUB_TOPICAL_DESERT";
                break;
            }
            return new_str;
        }

    public static Biome fromString(String s) {
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
                return Biome.SNOW;
            case "TUNDRA":
                return Biome.TUNDRA;
            case "ALPINE":
                return Biome.ALPINE;
            case "GLACIER":
                return Biome.GLACIER;
            case "SHRUBLAND":
                return Biome.SHRUBLAND;
            case "SUB_TOPICAL_DESERT":
                return Biome.SHRUBLAND;
        }
        return null;
    }

}
