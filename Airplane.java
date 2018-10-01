package concurrent_assignment;

public class Airplane {

    private int id = 0; //ID
    private int duration = 0; //Duration

    private String model = ""; //Model
    private String status = ""; //Status
    private String destination = ""; //Destination

    public Airplane(int _id, String _model, String _status, int _duration, String _destination) {
        this.id = _id;
        this.model = _model;
        this.status = _status;
        this.duration = _duration;
        this.destination = _destination;
    }

    public int getID() {
        return this.id;
    }

    public String getModel() {
        return this.model;
    }

    public String getStatus() {
        return this.status;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setStatus(String _status) {
        this.status = _status;
    }

    public void setDuration(int _duration) {
        this.duration = _duration;
    }

    public int incrementDuration(int _duration) {
        return this.duration += _duration;
    }

    public void decrement() {
        this.duration -= 1;
    }
}