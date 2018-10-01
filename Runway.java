package concurrent_assignment;

import java.text.SimpleDateFormat;
import java.util.*;
import static concurrent_assignment.Concurrent_Assignment.runways;

public class Runway implements Runnable {

    private String name = "Runway"; //Default name of runway
    private String status = "Available"; //Default status of runway
    private ArrayList<Airplane> planes = new ArrayList<Airplane>();
    private ArrayList<Airplane> servicedPlanes = new ArrayList<Airplane>();
    private Airplane currentAirplane; //Current airplane in the runway

    private int currentAirplaneDuration = 0; //Current airplane's duration
    private int departedAirplaneAmount = 0; //Number of airplanes that departed from runway
    private int landedAirplaneAmount = 0; //Number of airplanes that arrived to runway
    private int airplaneAmount = 0; //Number of airplanes serviced

    private volatile static boolean check = true; //Checker used to continue simulation

    public Runway(String _name, ArrayList<Airplane> _planes) {
        this.name = _name;
        this.planes = _planes;
    }

    public void run() {
        while (check) {
            synchronized (planes) {
                //If the ArrayList of planes is empty, the runway thread waits until there are planes to service
                while (planes.isEmpty()) {
                    try {
                        planes.wait();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

                //Once it leaves while loop, the first element in the airplane ArrayList is assigned to the currentAirplane variable
                AssignCurrentPlane();
            }

            //currentAirplane is then serviced by runway object
            ServiceCurrentPlane();

            try {
                //Next iteration
                Thread.currentThread().sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void ServiceCurrentPlane() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        for (int x = 0; x < currentAirplaneDuration; x++) {
            try {
                Thread.currentThread().sleep(1000);
                currentAirplane.decrement();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (this.status.equals("Departing") && check == true) {
            this.status = "Departed";
            currentAirplane.setStatus("Departed");

            departedAirplaneAmount++;
            airplaneAmount++;
            servicedPlanes.add(currentAirplane);
        } else if (this.status.equals("Landing") && check == true) {
            this.status = "Landed";
            currentAirplane.setStatus("Arrived");

            landedAirplaneAmount++;
            airplaneAmount++;
            servicedPlanes.add(currentAirplane);
        }
    }

    public void AssignCurrentPlane() {
        currentAirplane = planes.get(0);
        currentAirplaneDuration = currentAirplane.getDuration();

        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        if (currentAirplane.getStatus().equals("Arriving")) {
            currentAirplane.setStatus("Landing");
            this.setStatus("Landing");
        } else if (currentAirplane.getStatus().equals("Ready")) {
            if (noDeparture()) {
                currentAirplane.setDuration(currentAirplaneDuration + rand.nextInt(5));
            }
            currentAirplane.setStatus("Departing");
            this.setStatus("Departing");
        }

        planes.remove(0);
        planes.notifyAll();
    }

    public void getServicedPlanes() {
        System.out.println("\n[" + getName() + "]");

        if (servicedPlanes.isEmpty()) {
            System.out.println("1.  -");
        }

        for (int x = 0; x < servicedPlanes.size(); x++) {
            if (servicedPlanes.get(x).getStatus().equals("Arrived")) {
                System.out.format("%-3s %-16s (%-3s) %-3s[%s from %s]\n", ((x + 1) + ". "), servicedPlanes.get(x).getModel(), servicedPlanes.get(x).getID(), "", servicedPlanes.get(x).getStatus(), servicedPlanes.get(x).getDestination());
            } else if (servicedPlanes.get(x).getStatus().equals("Departed")) {
                System.out.format("%-3s %-16s (%-3s) %-3s[%s to %s]\n", ((x + 1) + ". "), servicedPlanes.get(x).getModel(), servicedPlanes.get(x).getID(), "", servicedPlanes.get(x).getStatus(), servicedPlanes.get(x).getDestination());
            }
        }
    }

    public Boolean noDeparture() {

        int count = 0;

        for (Runway r : runways) {
            if (r.isAvailable() || r.isLanding()) {
                count++;
            }
        }

        if (count == runways.size()) {
            return true;
        } else {
            return false;
        }
    }

    public void setCheck(boolean c) {
        this.check = c;
    }

    public String getName() {
        return this.name;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public boolean isAvailable() {
        if (this.status.equals("Available")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLanding() {
        if (this.status.equals("Landing")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasLanded() {
        if (this.status.equals("Landed")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDeparting() {
        if (this.status.equals("Departing")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasDeparted() {
        if (this.status.equals("Departed")) {
            return true;
        } else {
            return false;
        }
    }

    public Airplane getCurrentAirplane() {
        return this.currentAirplane;
    }

    public int getAirplaneAmount() {
        return this.airplaneAmount;
    }

    public int getDepartedAirplaneAmount() {
        return this.departedAirplaneAmount;
    }

    public int getLandedAirplaneAmount() {
        return this.landedAirplaneAmount;
    }
}
