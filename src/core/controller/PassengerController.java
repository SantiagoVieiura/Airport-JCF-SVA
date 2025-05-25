/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Passenger;
import java.time.LocalDate;

public class PassengerController {
    private StorageController storage;

    public PassengerController(StorageController storage) {
        this.storage = storage;
    }
    
    public Response registerPassenger(String idSTR, String firstname, String lastname, String daySTR, String monthSTR, String yearSTR, String phoneCodeSTR, String phoneSTR, String country) {
        try{
            long id, phone;
            int phoneCode, year, month, day;
            
            if (firstname.isEmpty() || lastname.isEmpty() || country.isEmpty() || idSTR.isEmpty() || daySTR.isEmpty() || monthSTR.isEmpty() || yearSTR.isEmpty() || phoneCodeSTR.isEmpty() || phoneSTR.isEmpty()) 
                return new Response("No text field should be empty", Status.BAD_REQUEST).clone();
            
            try {
                id = Long.parseLong(idSTR);
                if (id < 0 || String.valueOf(id).length() > 15) 
                    return new Response("ID must be at least 0 and no more than 15 digits", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) {
                return new Response("ID must be numeric", Status.BAD_REQUEST).clone();
            }
            
            try {
                phoneCode = Integer.parseInt(phoneCodeSTR);
                if (phoneCode < 0 || String.valueOf(phoneCode).length() > 3) 
                    return new Response("Phone code must be at least 0 and no more than 3 digits", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) {
                return new Response("Phone code must be numeric", Status.BAD_REQUEST).clone();
            }

            try {
                phone = Long.parseLong(phoneSTR);
                if (phone < 0 || String.valueOf(phone).length() > 11) 
                    return new Response("Phone number must be at least 0 and les than 11 digits", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) {
                return new Response("Phone must be numeric", Status.BAD_REQUEST).clone();
            }
            
            try {
                day = Integer.parseInt(daySTR);
                month = Integer.parseInt(monthSTR);
                year = Integer.parseInt(yearSTR);
                if (year < 1910 || year > 2024) 
                    return new Response("Invalid birth year", Status.BAD_REQUEST).clone();
                LocalDate date = LocalDate.of(year, month, day);
            } catch (Exception e) {
                return new Response("Invalid birthdate", Status.BAD_REQUEST).clone();
            }
            
            for (Passenger p : storage.getPassengers()) {
                if (p.getId() == id)
                    return new Response("Passenger ID already exists", Status.BAD_REQUEST).clone();
            }
            
            Passenger passenger = new Passenger(id, firstname, lastname, LocalDate.of(year, month, day), phoneCode, phone, country);
            storage.getPassengers().add(passenger);
            return new Response("Passenger created successfully", Status.CREATED).clone();
        }catch (Exception ex){
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR).clone();
        }
    }
    
    
    public Response updatePassenger(String idSTR, String firstname, String lastname, String daySTR, String monthSTR, String yearSTR, String phoneCodeSTR, String phoneSTR, String country){     
        try{
            long id, phone;
            int phoneCode, year, month, day;
            
            if (firstname.isEmpty() || lastname.isEmpty() || country.isEmpty() || idSTR.isEmpty() || daySTR.isEmpty() || monthSTR.isEmpty() || yearSTR.isEmpty() || phoneCodeSTR.isEmpty() || phoneSTR.isEmpty()) 
                return new Response("No text field should be empty", Status.BAD_REQUEST).clone();
            
            if (storage.getPassengers() == null)
                return new Response("There are no passengers to add", Status.NOT_FOUND).clone();
            
            try {
                id = Long.parseLong(idSTR);
                if (id < 0 || String.valueOf(id).length() > 15) 
                    return new Response("ID must be at least 0 and no more than 15 digits", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) {
                return new Response("ID must be numeric", Status.BAD_REQUEST).clone();
            }
            
            try {
                phoneCode = Integer.parseInt(phoneCodeSTR);
                if (phoneCode < 0 || String.valueOf(phoneCode).length() > 3) 
                    return new Response("Phone code must be at least 0 and no more than 3 digits", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) {
                return new Response("Phone code must be numeric", Status.BAD_REQUEST).clone();
            }
            
            try {
                phone = Long.parseLong(phoneSTR);
                if (phone < 0 || String.valueOf(phone).length() > 11) 
                    return new Response("Phone number must be at least 0 and les than 11 digits", Status.BAD_REQUEST).clone();
            } catch (NumberFormatException e) {
                return new Response("Phone must be numeric", Status.BAD_REQUEST).clone();
            }
            
            try {
                day = Integer.parseInt(daySTR);
                month = Integer.parseInt(monthSTR);
                year = Integer.parseInt(yearSTR);
                if (year < 1910 || year > 2024) 
                    return new Response("Invalid birth year", Status.BAD_REQUEST).clone();
                LocalDate birthDate = LocalDate.of(year, month, day);
            } catch (Exception e) {
                return new Response("Invalid birthdate", Status.BAD_REQUEST).clone();
            }
            
            Passenger passenger = null;
            for (Passenger p : storage.getPassengers()) 
                if (p.getId() == id) 
                    passenger = p;
            
            if (passenger == null)
                return new Response("Passenger wasn't selected or it doesn't exists", Status.NOT_FOUND).clone();
            
            passenger.setFirstname(firstname);
            passenger.setLastname(lastname);
            passenger.setBirthDate(LocalDate.of(year, month, day));
            passenger.setCountryPhoneCode(phoneCode);
            passenger.setPhone(phone);
            passenger.setCountry(country);
            return new Response("Passenger updated successfully", Status.CREATED).clone();
            
        }catch (Exception e){
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR).clone();
        }
    }
}
