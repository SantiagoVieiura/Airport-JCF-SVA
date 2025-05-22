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
        valid = validateDate(day , month, year) ;
        for (Passenger passenger : storage.getPassengers()){
            if(passenger.getId() == id){
                valid = false;
            
            } 
        
        
        }
        
        
        if (id <= 0 || counter > 15) {
           valid = false;
        }
        
        
        
        
        LocalDate birthDate = LocalDate.of(year, month, day);
        Passenger passenger = new Passenger(id, firstname, lastname, birthDate, phoneCode, phone, country);

    }
    
    public static boolean validateDate(int day, int month, int year) {
        boolean validDate = true;

        
        if (year < 1910 || year > 2024) {
            validDate = false;
        }

       
        else if (month < 1 || month > 12) {
            validDate = false;
        }

       
        else if (day < 1 || day > 31) {
            validDate = false;
        }

        
        else {
            if (month == 2) {
                
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    if (day > 29) {
                        validDate = false;
                    }
                } else {
                    if (day > 28) {
                        validDate = false;
                    }
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30) {
                    validDate = false;
                }
            }
            
        }

        return validDate;
    }
}
