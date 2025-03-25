package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Directions;

public class DronePosition {
    private final Logger logger = LogManager.getLogger();

    private int droneX, droneY;

    public DronePosition() {
        this.droneX = 0;
        this.droneY = 0;
    }

    public void updatePosition(String action, Directions droneDirection) {
        switch (action){
            case "FLY" -> updateAfterFly(droneDirection);
            case "TURN_RIGHT" -> updateAfterRT(droneDirection);
            case "TURN_LEFT" -> updateAfterLT(droneDirection);
            default -> {}
        }
    }

    private void updateAfterFly(Directions droneDirection) {
        switch (droneDirection){
            case N -> increaseDroneY();
            case E -> increaseDroneX();
            case S -> decreaseDroneY();
            case W -> decreaseDroneX();
        }
    }

    private void updateAfterRT(Directions droneDirection) {
        switch (droneDirection){
            case N -> {
                increaseDroneX();
                increaseDroneY();
            }
            case E -> {
                increaseDroneX();
                decreaseDroneY();
            }
            case S -> {
                decreaseDroneX();
                decreaseDroneY();
            }
            case W -> {
                decreaseDroneX();
                increaseDroneY();
            }
        }
    }

    private void updateAfterLT(Directions droneDirection) {
        switch (droneDirection){
            case N -> {
                decreaseDroneX();
                increaseDroneY();
            }
            case E -> {
                increaseDroneX();
                increaseDroneY();
            }
            case S -> {
                increaseDroneX();
                decreaseDroneY();
            }
            case W -> {
                decreaseDroneX();
                decreaseDroneY();
            }
        }
    }

    private void increaseDroneX() {
        this.droneX += 1;
    }

    private void decreaseDroneX() {
        this.droneX -= 1;
    }

    private void increaseDroneY() {
        this.droneY += 1;
    }

    private void decreaseDroneY() {
        this.droneY -= 1;
    }

    public int getDroneX() {
        return this.droneX;
    }

    public int getDroneY() {
        return this.droneY;
    }
}