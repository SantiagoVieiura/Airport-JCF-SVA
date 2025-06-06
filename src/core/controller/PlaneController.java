/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Plane;
import java.util.List;
public class PlaneController {
    
    private final StorageController storage;

    public PlaneController(StorageController storage) {
        this.storage = storage;
    }
    
    public Response registerPlane(String id, String brand, String model, String maxCapacityS, String airline){
        try{
            int maxCapacity;
            
            if (id.isEmpty() || brand.isEmpty() || model.isEmpty() || maxCapacityS.isEmpty() || airline.isEmpty())
                return new Response("No text field should be empty", Status.BAD_REQUEST).clone();
            
            if(!validId(id))  //De ninguna manera el ID puede tener otro formato (De lo contrario me mato)
                return new Response("Plane's ID must be exactly 2 uppercase letters and 5 numbers", Status.BAD_REQUEST).clone();
            
            for (Plane plane : storage.getPlanes())
                if (plane.getId().equals(id)) 
                    return new Response("Plane ID already exists", Status.BAD_REQUEST).clone();

            try{
                maxCapacity = Integer.parseInt(maxCapacityS);
                if (maxCapacity < 1)
                   return new Response("The Max Capacity must be greater than 0", Status.BAD_REQUEST).clone(); 
            }catch (NumberFormatException e) {
                return new Response("The Max Capacity must be numeric", Status.BAD_REQUEST).clone();
            }
            
            Plane plane = new Plane(id, brand,model, maxCapacity, airline);
            storage.addPlanes(plane);
            return new Response("Plane created successfully", Status.CREATED).clone();
        }catch (Exception e){
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR).clone();
        }
    }
    
    private boolean validId(String id) {
        if (id.length() != 7) 
            return false;

        for (int i = 0; i < 2; i++)
            if (!Character.isUpperCase(id.charAt(i))) 
                return false;
        
        for (int i = 2; i < 7; i++) 
            if (!Character.isDigit(id.charAt(i))) 
                return false;
        return true;
    }
    
    public List<Plane> getAllPlanes() {
        return storage.getPlanes();
    }
}
