package concurrent_assignment;

import java.text.SimpleDateFormat;
import java.util.*;

public class AirplaneGenerator implements Runnable {

    private ArrayList<Airplane> planes = new ArrayList<Airplane>();
    private ArrayList<Runway> runways = new ArrayList<Runway>();
    private static ArrayList<String> logger = new ArrayList<String>();

    private int spawner; //Randomized number to determine spawnrate of airplanes
    private int spawnrate; //Used as a threshold by spawner to determine the spawn of airplanes
    private int id; //Randomized number to determine ID of airplane
    private int model; //Randomized number to determine the model of airplane
    private int destination; //Randomized number to determine the destination of airplane
    private int totalAirplaneAmount = 0; //Total number of airplanes serviced

    private boolean status; //Randomized boolean to determine status of airplane (Departing/Arriving)

    private double exectime = 0; //Simulation time
    private double percentage = 0; //Percentage of airplanes serviced by each runway

    public AirplaneGenerator(ArrayList<Airplane> _planes, double _exectime, ArrayList<Runway> _runways, int _spawnrate) {
        this.planes = _planes;
        this.exectime = _exectime;
        this.runways = _runways;
        this.spawnrate = _spawnrate;
    }

    public void run() {

        for (int x = 0; x < exectime; x++) {

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("+------------------------------------------------------------------------------+");
            System.out.format("| %-18s [%s] %-12s Time elapsed: %-18s  %-3s\n", "", sdf.format(cal.getTime()), "", (x + "s"), "|");

            PrintHeader();
            PrintRunways();

            synchronized (planes) {
                Queue();
                GenerateAirplane();
                planes.notifyAll();
            }

            try {
                Thread.currentThread().sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        for (int x = 0; x < runways.size(); x++) {
            runways.get(x).setCheck(false);
        }

        GenerateReport();
    }

    public void GenerateAirplane() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        //Randomizer object
        Random rand = new Random();

        //Generates a value from 0 - 99, for every iteration, chance of airplane being spawned based on spawnrate.
        spawner = rand.nextInt((99 - 0) + 1) + 0;

        //Generates a status for each airplane generated
        status = rand.nextBoolean();

        //Generates an ID for the airplane generated
        id = rand.nextInt((9999 - 1000) + 1) + 1000;

        //Generates a model for the airplane generated
        ArrayList<String> models = new ArrayList<String>();

        models.add("Boeing 757");
        models.add("Boeing 777");
        models.add("Boeing 787");
        models.add("Airbus A310");
        models.add("Airbus A330");
        models.add("Airbus A350");
        models.add("McDonnell DC-9");
        models.add("McDonnell DC-10");
        models.add("McDonnell MD-11");
        models.add("Tupolev Tu-104");
        models.add("Tupolev Tu-110");
        models.add("Tupolev Tu-124");

        //Assigns an airplane model to an ID
        model = rand.nextInt(models.size());

        //Generates a locations for the airplane generated
        ArrayList<String> locations = new ArrayList<String>();

        locations.add("Malaysia");
        locations.add("Singapore");
        locations.add("Japan");
        locations.add("Indonesia");
        locations.add("South Korea");
        locations.add("Spain");
        locations.add("Germany");
        locations.add("Sweden");
        locations.add("France");
        locations.add("Nepal");
        locations.add("India");
        locations.add("China");
        locations.add("Thailand");
        locations.add("Turkey");
        locations.add("Libya");
        locations.add("Lebanon");
        locations.add("Hong Kong");
        locations.add("South Africa");
        locations.add("Canada");
        locations.add("Australia");

        //Assigns a location used by airplane
        destination = rand.nextInt(locations.size());

        if (spawner <= spawnrate) {
            String planestatus = "";
            int duration = 0;

            if (status == true) {
                planestatus = "Arriving";
                duration = 10;
            } else if (status == false) {
                planestatus = "Ready";
                duration = 5;
            }

            planes.add(new Airplane(id, models.get(model), planestatus, duration, locations.get(destination)));
            System.out.println("\n" + models.get(model) + " (" + id + ")  is generated.");
            logger.add("[" + sdf.format(cal.getTime()) + "] " + models.get(model) + " (" + id + ")  is generated.");
        }
    }

    public void PrintHeader() {
        System.out.println("+-----------------+--------+-------------------+----------------+--------------+");
        System.out.format("| %-15s | %-7s| %-18s| %-15s| %-12s |\n", "Runway", "ID", "Model", "Status", "Destination");
        System.out.println("+-----------------+--------+-------------------+----------------+--------------+");
    }

    public void PrintRunways() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        for (int x = 0; x < runways.size(); x++) {
            if (runways.get(x).isAvailable()) {
                System.out.format("| %-10s[%03d] | %-7s| %-18s| %-15s| %-12s |\n", runways.get(x).getName(), runways.get(x).getAirplaneAmount(), "-", "-", runways.get(x).getStatus(), "");
                System.out.println("+-----------------+--------+-------------------+----------------+--------------+");
            } else if (runways.get(x).isLanding()) {
                System.out.format("| %-10s[%03d] | %-7s| %-18s| %-10s(%02d) | %-12s |\n", runways.get(x).getName(), runways.get(x).getAirplaneAmount(), runways.get(x).getCurrentAirplane().getID(), runways.get(x).getCurrentAirplane().getModel(), runways.get(x).getStatus(), runways.get(x).getCurrentAirplane().getDuration(), runways.get(x).getCurrentAirplane().getDestination());
                System.out.println("+-----------------+--------+-------------------+----------------+--------------+");
            } else if (runways.get(x).isDeparting()) {
                System.out.format("| %-10s[%03d] | %-7s| %-18s| %-10s(%02d) | %-12s |\n", runways.get(x).getName(), runways.get(x).getAirplaneAmount(), runways.get(x).getCurrentAirplane().getID(), runways.get(x).getCurrentAirplane().getModel(), runways.get(x).getStatus(), runways.get(x).getCurrentAirplane().getDuration(), runways.get(x).getCurrentAirplane().getDestination());
                System.out.println("+-----------------+--------+-------------------+----------------+--------------+");
            } else if (runways.get(x).hasLanded()) {
                System.out.format("| %-10s[%03d] | %-7s| %-18s| %-10s(%02d) | %-12s |\n", runways.get(x).getName(), runways.get(x).getAirplaneAmount(), runways.get(x).getCurrentAirplane().getID(), runways.get(x).getCurrentAirplane().getModel(), runways.get(x).getStatus(), runways.get(x).getCurrentAirplane().getDuration(), runways.get(x).getCurrentAirplane().getDestination());
                System.out.println("+-----------------+--------+-------------------+----------------+--------------+");
                logger.add("[" + sdf.format(cal.getTime()) + "] " + runways.get(x).getCurrentAirplane().getModel() + " (" + runways.get(x).getCurrentAirplane().getID() + ") has " + runways.get(x).getCurrentAirplane().getStatus().toLowerCase() + " from " + runways.get(x).getCurrentAirplane().getDestination() + ".");
                runways.get(x).setStatus("Available");
            } else if (runways.get(x).hasDeparted()) {
                System.out.format("| %-10s[%03d] | %-7s| %-18s| %-10s(%02d) | %-12s |\n", runways.get(x).getName(), runways.get(x).getAirplaneAmount(), runways.get(x).getCurrentAirplane().getID(), runways.get(x).getCurrentAirplane().getModel(), runways.get(x).getStatus(), runways.get(x).getCurrentAirplane().getDuration(), runways.get(x).getCurrentAirplane().getDestination());
                System.out.println("+-----------------+--------+-------------------+----------------+--------------+");
                logger.add("[" + sdf.format(cal.getTime()) + "] " + runways.get(x).getCurrentAirplane().getModel() + " (" + runways.get(x).getCurrentAirplane().getID() + ") has " + runways.get(x).getCurrentAirplane().getStatus().toLowerCase() + " to " + runways.get(x).getCurrentAirplane().getDestination() + ".");
                runways.get(x).setStatus("Available");
            }
        }
        System.out.println();
    }

    public void Queue() {
        System.out.println("Queue:");
        int x = 0;

        for (Airplane plane : planes) {
            x++;
            if (plane.getStatus().equals("Arriving")) {
                System.out.format("%-4s %-18s (%-4s) %-3s[%s from %s]\n", (x + "."), plane.getModel(), plane.getID(), "", plane.getStatus(), plane.getDestination());
            } else if (plane.getStatus().equals("Ready")) {
                System.out.format("%-4s %-18s (%-4s) %-3s[%s to depart to %s]\n", (x + "."), plane.getModel(), plane.getID(), "", plane.getStatus(), plane.getDestination());
            }
        }
    }

    public void GenerateReport() {
        while (true) {
            System.out.println("\n");
            System.out.println("Please choose one of the following options:");
            System.out.println("1. Number of airplanes per runway\n2. Airplanes assigned to runway\n3. Log report\n4. Exit");

            Scanner sc = new Scanner(System.in);
            int input = sc.nextInt();

            switch (input) {
                case 1:
                    System.out.println("\n\n[Number of airplanes per runway]\n");
                    AirplanesPerRunway();
                    break;

                case 2:
                    System.out.println("\n\n[Airplanes assigned to runway]\n");
                    AirplanesAssigned();
                    break;

                case 3:
                    System.out.println("\n\n[Log report]\n");
                    Log();
                    break;

                case 4:
                    System.exit(0);
                    break;
            }
        }
    }

    public void AirplanesPerRunway() {
        for (int x = 0; x < runways.size(); x++) {
            totalAirplaneAmount += runways.get(x).getAirplaneAmount();
        }

        for (int x = 0; x < runways.size(); x++) {
            if (totalAirplaneAmount == 0) {
                percentage = ((double) (runways.get(x).getAirplaneAmount() * 100) / 1);
            } else {
                percentage = ((double) (runways.get(x).getAirplaneAmount() * 100) / totalAirplaneAmount);
            }

            System.out.println("[" + runways.get(x).getName() + "]");
            System.out.println("Number of airplanes landed: " + runways.get(x).getLandedAirplaneAmount());
            System.out.println("Number of airplanes departed: " + runways.get(x).getDepartedAirplaneAmount());
            System.out.printf("Total number of airplanes: %d (%.2f%%)", runways.get(x).getAirplaneAmount(), percentage);
            System.out.println("\n");
        }

        System.out.println("\nAccumulative total number of airplanes: " + totalAirplaneAmount);
    }

    public void AirplanesAssigned() {
        for (int x = 0; x < runways.size(); x++) {
            runways.get(x).getServicedPlanes();
        }
    }

    public void Log() {
        for (int x = 0; x < logger.size(); x++) {
            System.out.println(logger.get(x));
        }
    }
}
