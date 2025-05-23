/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Flight;
import core.model.Location;
import core.model.Plane;
import core.model.Storage;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FlightController {

    private final Storage storage;

    public FlightController(Storage storage) {
        this.storage = storage;
    }

    public Response registerFlight(String id, String planeId, String departureLocationId, String arrivalLocationId, String scaleLocationId, String yearS, String monthS, String dayS, String hourS, String minutesS, String hoursDurationsArrivalS, String minutesDurationsArrivalS, String hoursDurationsScaleS, String minutesDurationsScaleS) {
        try {
            if (id.isEmpty() || planeId.isEmpty() || departureLocationId.isEmpty() || arrivalLocationId.isEmpty() || yearS.isEmpty() || monthS.isEmpty() || dayS.isEmpty() || hourS.isEmpty() || minutesS.isEmpty() || hoursDurationsArrivalS.isEmpty() || minutesDurationsArrivalS.isEmpty())
                return new Response("No text field should be empty", Status.BAD_REQUEST);

            if (storage.getPlanes().isEmpty())
                return new Response("No planes available", Status.NOT_FOUND);

            Plane plane = null;
            int day, month, year, hour, minutes, hoursDurationsArrival, minutesDurationsArrival, hoursDurationsScale, minutesDurationsScale;
            Location departure = null, arrival = null, scale = null;
            LocalDateTime departureDate;
            
            if (!validId(id)) 
                return new Response("Flight's ID must be exactly 3 uppercase letters and 3 numbers", Status.BAD_REQUEST);
            
            for (Flight f : storage.getFlights())
                if (id.equals(f.getId()))
                    return new Response("That Flight ID already exists", Status.BAD_REQUEST);

            
            for (Plane p : storage.getPlanes()) 
                if (planeId.equals(p.getId())) 
                    plane = p;
            
            if(plane == null)
                return new Response("Plane not found", Status.NOT_FOUND);

            
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
                if (!hoursDurationsScaleS.equals("0") || !minutesDurationsScaleS.equals("0")) {
                    return new Response("Scale duration must be 00:00 when no valid scale location is selected", Status.BAD_REQUEST);
                }
                hoursDurationsScale = 0;
                minutesDurationsScale = 0;
            } else {
                try {
                    hoursDurationsScale = Integer.parseInt(hoursDurationsScaleS);
                    minutesDurationsScale = Integer.parseInt(minutesDurationsScaleS);
                } catch (NumberFormatException e) {
                    return new Response("Scale duration must be numeric", Status.BAD_REQUEST);
                }

                if (hoursDurationsScale == 0 && minutesDurationsScale == 0) {
                    return new Response("Scale duration must be greater than 00:00", Status.BAD_REQUEST);
                }
            }
            
            
            if (departure == null || arrival == null)
                return new Response("Departure and arrival locations must be valid", Status.NOT_FOUND);
            
            if (departure.getAirportId().equals(arrival.getAirportId()))
                return new Response("Departure and arrival locations cannot be the same", Status.BAD_REQUEST);

            try{
                hoursDurationsArrival = Integer.parseInt(hoursDurationsArrivalS);
                minutesDurationsArrival = Integer.parseInt(minutesDurationsArrivalS);

                if (hoursDurationsArrival == 0 && minutesDurationsArrival == 0)
                    return new Response("Flight duration must be greater than 00:00", Status.BAD_REQUEST);
            }catch (NumberFormatException e){
                return new Response("Arrival time must be numeric", Status.BAD_REQUEST);
            }
            
            try {
                day = Integer.parseInt(dayS);
                month = Integer.parseInt(monthS);
                year = Integer.parseInt(yearS);
                hour = Integer.parseInt(hourS);
                minutes = Integer.parseInt(minutesS);
                
                if (year > 2030) 
                    return new Response("Year is too far in the future", Status.BAD_REQUEST);
                
                departureDate = LocalDateTime.of(year, month, day, hour, minutes);
                
                if (departureDate.isBefore(LocalDateTime.now()))
                    return new Response("Date must be today or in the future", Status.BAD_REQUEST);

            } catch (Exception e) {
                return new Response("Invalid date", Status.BAD_REQUEST);
            }
            
            Flight flight;
            if (scale == null) {
                flight = new Flight(id, plane, departure, arrival, departureDate, hoursDurationsArrival, minutesDurationsArrival);
            } else {
                flight = new Flight(id, plane, departure, scale, arrival, departureDate, hoursDurationsArrival, minutesDurationsArrival, hoursDurationsScale, minutesDurationsScale);
            }
            storage.getFlights().add(flight);
            return new Response("Flight registered successfully", Status.CREATED);
        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validId(String id) {
        if (id.length() != 6) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            if (!Character.isUpperCase(id.charAt(i))) {
                return false;
            }
        }

        for (int i = 3; i < 6; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
