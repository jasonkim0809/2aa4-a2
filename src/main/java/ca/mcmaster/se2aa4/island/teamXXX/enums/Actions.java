package ca.mcmaster.se2aa4.island.teamXXX.enums;

public enum Actions {
    turn_left, turn_right, fly, echo_left, echo_right, echo_forward, scan, stop;
    public void enumtest(){
        if (this == turn_left){
            System.out.println("rueninf lwdr");
        }
        else if (this == turn_right){
            // stuff here for turning right
        }
    }
}
