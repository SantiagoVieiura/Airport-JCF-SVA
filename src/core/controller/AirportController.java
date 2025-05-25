package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Location;
import java.util.List;


public class AirportController {

    private final StorageController storage;

    public AirportController(StorageController storage) {
        this.storage = storage;
    }
    
    public Response registerAirport(String id, String name, String city, String country, String latitudeS, String longitudeS){
        try{
            double latitude, longitude;
            
            //Ni un solo campo debe estar vacio
            if (id.isEmpty() || name.isEmpty() || city.isEmpty() || country.isEmpty() || latitudeS.isEmpty() || longitudeS.isEmpty())
                return new Response("No text field should be empty", Status.BAD_REQUEST).clone();
            
            if(!validChars(id))  //De ninguna manera ID puede no tener minimo 3 letras mayusculas
                return new Response("Airport's ID must be exactly 3 uppercase letters", Status.BAD_REQUEST).clone();
            
            for (Location a : storage.getLocations()) { //ID tampoco puede estar repetido (la plena ojala se repita)
                if (a.getAirportId().equals(id)) 
                    return new Response("Airport ID already exists", Status.BAD_REQUEST).clone();     
            }
            
            try { //Rango de las latitudes (ojala ni uno se dañe)
                latitude = Double.parseDouble(latitudeS);
                if (latitude < -90 || latitude > 90 || !maxDec(latitudeS))
                    return new Response("Latitude must be between -90 and 90 with up to 4 decimal places", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) { 
                return new Response("Latitude must be numeric", Status.BAD_REQUEST).clone();
            }
            
            try { //Rango de las longitudes (y aqui tampoco)
                longitude = Double.parseDouble(longitudeS);
                if (longitude < -180 || longitude > 180 || !maxDec(longitudeS))  //Rango de las longitudes
                    return new Response("Longitude must be between -180 and 180 with up to 4 decimal places", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) {
                return new Response("Longitude must be numeric", Status.BAD_REQUEST).clone();
            }
            
            Location airport = new Location(id, name, city, country, latitude, longitude);
            storage.addLocations(airport);
            return new Response("Airport registered successfully", Status.CREATED).clone();
        }catch (Exception e){
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR).clone();
        }
    }
    
    public List<Location> getAllLocations() {
        return storage.getLocations(); // asumiendo que `storage` ya está disponible
    }
    
    private boolean validChars(String codigo) {
        if (codigo.length() != 3) return false;
        for (char c : codigo.toCharArray()) {
            if (!Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean maxDec(String texto) {
        int punto = texto.indexOf('.');
        if (punto == -1)
            return true;
        int decimales = texto.length() - punto - 1;
        return decimales <= 4;
    }
}


