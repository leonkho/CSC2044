package concurrent_assignment;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Concurrent_Assignment {

    private static ArrayList<Airplane> planes = new ArrayList<Airplane>();
    public static ArrayList<Runway> runways = new ArrayList<Runway>();

    private static double time = 0; //Simulation time
    private static int runwayAmount = 0; //Number of runways
    private static int spawnrate = 0; //Spawn rate of airplanes

    public static void main(String[] args) {
        Input();
        Execute();
    }

    public static void Input() {
        Scanner sc = new Scanner(System.in);
        boolean timecheck = false;
        boolean runwaycheck = false;
        boolean spawnratecheck = false;
        
        do {
            if (timecheck == true && time < 0)
            {
                System.out.println("Please enter a positive value.");
            }
            
            //Gets user input for the simulation time
            System.out.println("Please enter the simulation time (Minutes): ");
            time = (sc.nextDouble() * 60);
            timecheck = true;
        } while (time < 0);

        do {
            if (runwaycheck == true && (runwayAmount < 3 || runwayAmount > 100)) {
                System.out.println("Please enter 3 or more (Maximum: 100) runways.");
            }

            //Gets user input for the number of runways needed
            System.out.println("Please enter the number of runways (Minimum: 3): ");
            runwayAmount = sc.nextInt();
            runwaycheck = true;
        } while (runwayAmount < 3 || runwayAmount > 100);

        do {
            if (spawnratecheck == true && (spawnrate > 100 || spawnrate < 0)) {
                System.out.println("Please enter a value between 0-100.");
            }
            
            //Gets user input to set the spawn rate of airplanes during the simulation
            System.out.println("Please enter the spawn rate of airplanes for every iteration (0-100%): ");
            spawnrate = sc.nextInt();
            spawnratecheck = true;
        } while (spawnrate > 100 || spawnrate < 0);
    }

    public static void Execute() {
        AirplaneGenerator ag = new AirplaneGenerator(planes, time, runways, spawnrate);
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int x = 0; x < runwayAmount; x++) {
            Runway r = new Runway("Runway " + (x + 1), planes);
            runways.add(r);
            exec.execute(r);
        }

        exec.execute(ag);

        exec.shutdown();
        while (!exec.isTerminated()) {
        }
    }
}
