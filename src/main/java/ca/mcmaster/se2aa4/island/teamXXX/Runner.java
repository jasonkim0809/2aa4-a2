package ca.mcmaster.se2aa4.island.teamXXX;
import java.io.File;

import static eu.ace_design.island.runner.Runner.run;


public class Runner {

    public static void main(String[] args) {
        String filename = args[0];
        try {
            run(Explorer.class) // each tile is a 4x4 square?????
                    .exploring(new File(filename))
                    .withSeed(42L)
                    .startingAt(4, 54, "EAST")
                    .backBefore(20000)
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