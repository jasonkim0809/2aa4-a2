package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.File;

import ca.mcmaster.se2aa4.island.teamXXX.enums.Actions;
import static eu.ace_design.island.runner.Runner.run;


public class Runner {

    public static void main(String[] args) {
        String filename = args[0];
        
        Actions test;
        test = Actions.turn_left;
        test.enumtest();
        try {
            run(Explorer.class)
                    .exploring(new File(filename))
                    .withSeed(42L)
                    .startingAt(500, 435, "EAST")
                    .backBefore(7000)
                    .withCrew(5)
                    .collecting(1000, "WOOD")
                    .storingInto("./outputs")
                    .withName("Island")
                    .fire();
        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
