package ca.mcmaster.se2aa4.island.teamXXX.locationClasses;

public class EmergencySite extends TargetLocations {

    protected String id;
    protected int[] position = new int[2];

    public EmergencySite(String id, int creekXPosition, int creekYPosition){

        this.id = id;
        this.position[0] = creekXPosition;
        this.position[1] = creekYPosition;

    }

}