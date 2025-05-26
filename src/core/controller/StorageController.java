package core.controller;

import core.controller.utils.Observer;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final List<Observer> observers = new ArrayList<>();

    public StorageController() throws IOException {
        storage = new Storage();
        passengerJson = new PassengerJson();
        flightJson = new FlightJson();
        locationJson = new LocationJson();
        planeJson = new PlaneJson();

        storage.setPassengers(passengerJson.read());
        storage.setLocations(locationJson.read());
        storage.setPlanes(planeJson.read());
        storage.setFlights(flightJson.readFlights(storage.getPlanes(), storage.getLocations()));
        Collections.sort(storage.getPassengers(), Comparator.comparingLong(Passenger::getId));
        Collections.sort(storage.getLocations(), Comparator.comparing(Location::getAirportId));
        Collections.sort(storage.getPlanes(), Comparator.comparing(Plane::getId));
        Collections.sort(storage.getFlights(), Comparator.comparing(Flight::getDepartureDate));
        
    }

    public void addPassengers(Passenger p) {
        storage.getPassengers().add(p);
        Collections.sort(storage.getPassengers(), Comparator.comparingLong(Passenger::getId));
        notifyObservers();
    }
    
    public void updatePassengerData(Passenger passenger, String firstname, String lastname, LocalDate birthDate, int phoneCode, long phone, String country) {
        passenger.setFirstname(firstname);
        passenger.setLastname(lastname);
        passenger.setBirthDate(birthDate);
        passenger.setCountryPhoneCode(phoneCode);
        passenger.setPhone(phone);
        passenger.setCountry(country);
        notifyObservers();
    }
    
    public ArrayList<Passenger> getPassengers(){
        return storage.getPassengers();
    }

    public void addPlanes(Plane p){
        storage.getPlanes().add(p);
        Collections.sort(storage.getPlanes(), Comparator.comparing(Plane::getId));
        notifyObservers();
    }
    
    public void addToFlight(Passenger passenger, Flight flight){
        passenger.addFlight(flight);
        flight.addPassenger(passenger);
        notifyObservers();
    }
    
    public ArrayList<Plane> getPlanes(){
        return storage.getPlanes();
    }
    
    public void addLocations(Location l){
        storage.getLocations().add(l);
        Collections.sort(storage.getLocations(), Comparator.comparing(Location::getAirportId));
        notifyObservers();
    }
    
    public ArrayList<Location> getLocations(){
        return storage.getLocations();
    }
    
    public void addFlights(Flight f){
        storage.getFlights().add(f);
        Collections.sort(storage.getFlights(), Comparator.comparing(Flight::getDepartureDate));
        notifyObservers();
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
    
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    private void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
    
    public Plane findPlane(String planeId) {
        for (Plane plane : getPlanes()) 
            if (plane.getId().equals(planeId)) 
                return plane;

        return null; 
    }

}
