/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controller;

import core.controller.utils.FlightConflictValidator;
import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Flight;
import core.model.Location;
import core.model.Passenger;
import core.model.Plane;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FlightController {

    private final StorageController storage;

    public FlightController(StorageController storage) {
        this.storage = storage;
    }

    public Response registerFlight(String id, String planeId, String departureLocationId, String arrivalLocationId, String scaleLocationId, String yearS, String monthS, String dayS, String hourS, String minutesS, String hoursDurationsArrivalS, String minutesDurationsArrivalS, String hoursDurationsScaleS, String minutesDurationsScaleS) {
        try {
            if (id.isEmpty() || planeId.isEmpty() || departureLocationId.isEmpty() || arrivalLocationId.isEmpty() || yearS.isEmpty() || monthS.isEmpty() || dayS.isEmpty() || hourS.isEmpty() || minutesS.isEmpty() || hoursDurationsArrivalS.isEmpty() || minutesDurationsArrivalS.isEmpty())
                return new Response("No text field should be empty", Status.BAD_REQUEST).clone();

            if (storage.getPlanes().isEmpty())
                return new Response("No planes available", Status.NOT_FOUND).clone();

            Plane plane;
            int day, month, year, hour, minutes, hoursDurationsArrival, minutesDurationsArrival, hoursDurationsScale, minutesDurationsScale;
            Location departure = null, arrival = null, scale = null;
            LocalDateTime departureDate;

            if (!validId(id, 1)) 
                return new Response("Flight's ID must be exactly 3 uppercase letters and 3 numbers", Status.BAD_REQUEST).clone();

            if(findFlight(id) != null)
                return new Response("That Flight ID already exists", Status.BAD_REQUEST).clone();

            if (!validId(planeId, 3))
                return new Response("Plane's ID must be exactly 2 uppercase letters and 5 numbers", Status.BAD_REQUEST).clone();

            plane = findPlane(planeId);
            if(plane == null)
                return new Response("Plane not found", Status.NOT_FOUND).clone();

            for (Location location : storage.getLocations()) {
                if (departureLocationId.equals(location.getAirportId())) 
                    departure = location;
                if (arrivalLocationId.equals(location.getAirportId())) 
                    arrival = location;
                if (scaleLocationId.equals(location.getAirportId())) 
                    scale = location;
            }

            if (scale == null || scaleLocationId.equals(departureLocationId) || scaleLocationId.equals(arrivalLocationId)) {
                scale = null;
                if (!(hoursDurationsScaleS.equals("0") || hoursDurationsScaleS.equals("Hour")) || !(minutesDurationsScaleS.equals("0") || minutesDurationsScaleS.equals("Minute"))) {
                    return new Response("Scale duration must be 00:00 when no valid scale location is selected", Status.BAD_REQUEST).clone();
                }
                hoursDurationsScale = 0;
                minutesDurationsScale = 0;
            } else {
                try {
                    hoursDurationsScale = Integer.parseInt(hoursDurationsScaleS);
                    minutesDurationsScale = Integer.parseInt(minutesDurationsScaleS);
                } catch (NumberFormatException e) {
                    return new Response("Scale duration must be numeric", Status.BAD_REQUEST).clone();
                }

                if (hoursDurationsScale == 0 && minutesDurationsScale == 0) 
                    return new Response("Scale duration must be greater than 00:00", Status.BAD_REQUEST).clone();
            }

            if (departure == null || arrival == null)
                return new Response("Departure and arrival locations must be valid", Status.NOT_FOUND).clone();

            if (departure.getAirportId().equals(arrival.getAirportId()))
                return new Response("Departure and arrival locations cannot be the same", Status.BAD_REQUEST).clone();

            try {
                hoursDurationsArrival = Integer.parseInt(hoursDurationsArrivalS);
                minutesDurationsArrival = Integer.parseInt(minutesDurationsArrivalS);

                if (hoursDurationsArrival == 0 && minutesDurationsArrival == 0)
                    return new Response("Flight duration must be greater than 00:00", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) {
                return new Response("Arrival time must be numeric", Status.BAD_REQUEST).clone();
            }

            try {
                day = Integer.parseInt(dayS);
                month = Integer.parseInt(monthS);
                year = Integer.parseInt(yearS);
                hour = Integer.parseInt(hourS);
                minutes = Integer.parseInt(minutesS);

                if (year > 2030) 
                    return new Response("Year is too far in the future", Status.BAD_REQUEST).clone();

                departureDate = LocalDateTime.of(year, month, day, hour, minutes);

                if (departureDate.isBefore(LocalDateTime.now()))
                    return new Response("Date must be today or in the future", Status.BAD_REQUEST).clone();

            } catch (Exception e) {
                return new Response("Invalid date", Status.BAD_REQUEST).clone();
            }

            
            if (FlightConflictValidator.hasConflict(storage.getFlights(),plane.getId(),departureDate,hoursDurationsArrival,minutesDurationsArrival))
                return new Response("This plane has a time conflict with another flight", Status.BAD_REQUEST).clone();
            
            Flight flight;
            if (scale == null) 
                flight = new Flight(id, plane, departure, arrival, departureDate, hoursDurationsArrival, minutesDurationsArrival);
            else 
                flight = new Flight(id, plane, departure, scale, arrival, departureDate, hoursDurationsArrival, minutesDurationsArrival, hoursDurationsScale, minutesDurationsScale);

            storage.addFlights(flight);
            return new Response("Flight registered successfully", Status.CREATED).clone();

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR).clone();
        }
    }

    public Response addToFlight(String idS, String flightId){
        try{
            long id;
            Flight flight;
            Passenger passenger = null;
            
            if (storage.getFlights() == null)
                return new Response("No flights available", Status.NOT_FOUND).clone();
            
            if (storage.getPassengers() == null)
                return new Response("No passengers available", Status.NOT_FOUND).clone();
            
            if(idS.isEmpty())
                return new Response("No text field should be empty", Status.BAD_REQUEST).clone();
            
            try {
                id = Long.parseLong(idS);
                if (!validId(idS, 2)) 
                    return new Response("Passenger's ID must be at least 0 and no more than 15 digits", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) {
                return new Response("Passenger's ID must be numeric", Status.BAD_REQUEST).clone();
            }
            
            if (!validId(flightId, 1)) 
                return new Response("Flight must be selected", Status.BAD_REQUEST).clone();
            
            flight = findFlight(flightId);
            if (flight == null)
                return new Response("That Flight doesn't exists", Status.BAD_REQUEST).clone();
            
            int pas = flight.getNumPassengers();
            
            for (Passenger p : storage.getPassengers())
                if(id == p.getId())
                    passenger = p;
            
            if (pas >= flight.getPlane().getMaxCapacity())
                return new Response("This Flight is already full", Status.BAD_REQUEST).clone();
                
            if (passenger == null)
                return new Response("That Passenger doesn't exists", Status.BAD_REQUEST).clone();
            
            for (Passenger p : flight.getPassengers())
                if (passenger == p)
                    return new Response("That Passenger has already booked this Flight", Status.BAD_REQUEST).clone();

            if (LocalDateTime.now().isAfter(flight.getDepartureDate()))
                return new Response("Too Late to log into this flight", Status.BAD_REQUEST).clone();
            storage.addToFlight(passenger, flight);
            return new Response("Flight Booked Successfully", Status.CREATED).clone();
        }catch (Exception e){
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR).clone();
        }
    }
    
    public Response delayFlight(String id, String hoursS, String minutesS){
        try{
            int hours, minutes;

            if (storage.getFlights() == null)
                return new Response("No flights available", Status.NOT_FOUND).clone();

            if (!validId(id, 1)) 
                return new Response("Flight must be selected", Status.BAD_REQUEST).clone();

            try{
                hours = Integer.parseInt(hoursS);
                minutes = Integer.parseInt(minutesS);
            }catch (NumberFormatException e){
                return new Response("Every field should be selected", Status.BAD_REQUEST).clone();
            }
            
            Flight flight = findFlight(id);
            if (flight == null)
                return new Response("That Flight doesn't exists", Status.BAD_REQUEST).clone();
            
            if (LocalDateTime.now().isAfter(flight.getDepartureDate()))
                return new Response("Too Late to delay this flight", Status.BAD_REQUEST).clone();
            
            flight.delay(hours, minutes);
            Collections.sort(storage.getFlights(), Comparator.comparing(Flight::getDepartureDate));
            
            return new Response("Flight Delayed Successfully", Status.CREATED).clone();

        }catch (Exception e){
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR).clone();
        }
    }
    
    
    private boolean validId(String id, int op) { //Op 1- Flight | Op 2- Passenger | Op 3-Plane
        switch (op) {
            case 1:
                if (id.length() != 6) 
                    return false;
                for (int i = 0; i < 3; i++)
                    if (!Character.isUpperCase(id.charAt(i)))
                        return false;
                for (int i = 3; i < 6; i++)
                    if (!Character.isDigit(id.charAt(i)))
                        return false;
                break;
            case 2:
                long idL = Long.parseLong(id);
                if (idL < 0 || String.valueOf(id).length() > 15)
                    return false;

                break;
            case 3:
                if (id.length() != 7) 
                    return false;
                for (int i = 0; i < 2; i++)
                    if (!Character.isUpperCase(id.charAt(i)))
                        return false;
                for (int i = 2; i < 7; i++)
                    if (!Character.isDigit(id.charAt(i)))
                        return false;
                break;
            default:
                break;
        }
        return true;
    }
    
    public List<Flight> getFlightsId(long passengerId) {
        Passenger passenger = null;
        for (Passenger p : storage.getPassengers()) {
            if (p.getId() == passengerId) {
                passenger = p;
                break;
            }
        }

        if (passenger == null) {
            return new ArrayList<>();
        } else {
            Collections.sort(passenger.getFlights(), Comparator.comparing(Flight::getDepartureDate));
            return passenger.getFlights();
        }
    }
    
    public List<Flight> getFlightsId2() {
        return storage.getFlights();
    }
    
    public Plane findPlane(String planeId){
        for (Plane p : storage.getPlanes()) 
            if (planeId.equals(p.getId())) 
                return p;
        return null;
    }
    
    public Flight findFlight(String id){
        for (Flight f : storage.getFlights())
            if (id.equals(f.getId()))
                return f;
        return null;
    }
}
