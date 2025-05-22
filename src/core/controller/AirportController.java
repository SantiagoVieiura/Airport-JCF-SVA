package core.controller;

import core.model.Passenger;
import core.model.Storage;
import java.time.LocalDate;


public class AirportController {
    private Storage storage = new Storage();
    public void registerPassenger(long id, String firstname, String lastname, int day , int month ,int year, int phoneCode, long phone, String country) {
        long aux = id;
        int counter = 0;
        boolean valid = true;
        while (aux != 0) {

            counter += 1;
            aux /= id;
        }
        for (Passenger passenger : storage.getPassengers()){
            if(passenger.getId() == id){
                valid = false;
            
            } 
        
        
        }
        
        
        if (id <= 0 || counter > 15) {
           valid = false;
        }
        boolean validDate = true;
        if(2025<year || year<1909){
            validDate = false;
        
         
        }
        if (month<1 || 12<month ){
            validDate = false;
        }
        
        
        
        LocalDate birthDate = LocalDate.of(year, month, day);
        Passenger passenger = new Passenger(id, firstname, lastname, birthDate, phoneCode, phone, country);

    }
}
