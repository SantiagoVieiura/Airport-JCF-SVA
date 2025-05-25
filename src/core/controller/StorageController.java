package core.controller;

import core.model.Flight;
import core.model.Location;
import core.model.Passenger;
import core.model.Plane;
import core.model.storage.Storage;
import core.model.storage.json.FlightJson;
import core.model.storage.json.LocationJson;
import java.io.IOException;
import core.model.storage.json.PassengerJson;
import core.model.storage.json.PlaneJson;
import java.util.ArrayList;

public class StorageController {
    private final Storage storage;
    private final PassengerJson passengerJson;
    private final FlightJson flightJson;
    private final LocationJson locationJson;
    private final PlaneJson planeJson;

    public StorageController() throws IOException {
        storage = new Storage();
        passengerJson = new PassengerJson();
        flightJson = new FlightJson();
        locationJson = new LocationJson();
        planeJson = new PlaneJson();

        storage.setPassengers(passengerJson.readPassengers());
        storage.setLocations(locationJson.readLocations());
        storage.setPlanes(planeJson.readPlanes());
        storage.setFlights(flightJson.readFlights(storage.getPlanes(), storage.getLocations()));
        
    }

    public void addPassengers(Passenger p) {
        storage.getPassengers().add(p);
    }
    
    public ArrayList<Passenger> getPassengers(){
        return storage.getPassengers();
    }

    public void addPlanes(Plane p){
        storage.getPlanes().add(p);
    }
    
    public ArrayList<Plane> getPlanes(){
        return storage.getPlanes();
    }
    
    public void addLocations(Location l){
        storage.getLocations().add(l);
    }
    
    public ArrayList<Location> getLocations(){
        return storage.getLocations();
    }
    
    public void addFlights(Flight f){
        storage.getFlights().add(f);
    }
    
    public ArrayList<Flight> getFlights(){
        return storage.getFlights();
    }
}
