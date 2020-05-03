package dev.glatorre.sailaway;

public class Boat {
    private String name;
    private double longitude;
    private double latitude;
    private double speed;
    private double cog;

    public Boat(String name, double latitude, double longitude, double speed, double cog){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.cog = cog;
    }


    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getSpeed() {
        return speed;
    }

    public double getCog() {
        return cog;
    }
}