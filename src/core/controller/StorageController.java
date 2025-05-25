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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        Collections.sort(storage.getPassengers(), Comparator.comparingLong(p -> Long.parseLong(String.valueOf(p.getId()))));
        Collections.sort(storage.getLocations(), Comparator.comparing(Location::getAirportId));
        Collections.sort(storage.getPlanes(), Comparator.comparing(Plane::getId));
        Collections.sort(storage.getFlights(), Comparator.comparing(Flight::getDepartureDate));
        
    }

    public void addPassengers(Passenger p) {
        storage.getPassengers().add(p);
        Collections.sort(storage.getPassengers(), Comparator.comparingLong(pa -> Long.parseLong(String.valueOf(pa.getId()))));
    }
    
    public ArrayList<Passenger> getPassengers(){
        return storage.getPassengers();
    }

    public void addPlanes(Plane p){
        storage.getPlanes().add(p);
        Collections.sort(storage.getPlanes(), Comparator.comparing(Plane::getId));
    }
    
    public ArrayList<Plane> getPlanes(){
        return storage.getPlanes();
    }
    
    public void addLocations(Location l){
        storage.getLocations().add(l);
        Collections.sort(storage.getLocations(), Comparator.comparing(Location::getAirportId));
    }
    
    public ArrayList<Location> getLocations(){
        return storage.getLocations();
    }
    
    public void addFlights(Flight f){
        storage.getFlights().add(f);
        Collections.sort(storage.getFlights(), Comparator.comparing(Flight::getDepartureDate));
    }
    
    public ArrayList<Flight> getFlights(){
        return storage.getFlights();
    }
    
    public List<String> getPassengerIds() {
        return storage.getPassengers().stream().map(p -> String.valueOf(p.getId())).collect(Collectors.toList());
    }
    
    public List<String> getFlightIds() {
        return storage.getFlights().stream().map(Flight::getId).collect(Collectors.toList());
    }
    
    public List<String> getPlaneIds() {
        return storage.getPlanes().stream().map(Plane::getId).collect(Collectors.toList());
    }
    
    public List<String> getLocationIds() {
        return storage.getLocations().stream().map(Location::getAirportId).collect(Collectors.toList());
    }

}
