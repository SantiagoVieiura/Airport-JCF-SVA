/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model;

import java.util.ArrayList;

/**
 *
 * @author sviei
 */
public class Storage {
    private ArrayList<Passenger> passengers;
    private ArrayList<Plane> planes;
    private ArrayList<Location> locations;
    private ArrayList<Flight> flights;

    public Storage() {
        this.passengers = new ArrayList<>();
        this.planes = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.flights = new ArrayList<>();
    }
    
    

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<Passenger> passengers) {
        this.passengers = passengers;
    }

    public ArrayList<Plane> getPlanes() {
        return planes;
    }

    public void setPlanes(ArrayList<Plane> planes) {
        this.planes = planes;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }
    
    
}
