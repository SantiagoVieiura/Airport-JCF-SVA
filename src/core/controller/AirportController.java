package core.controller;

import core.model.Passenger;
import core.model.Storage;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class AirportController {

    private Storage storage = new Storage();

    public void registerPassenger(String idSTR, String firstname, String lastname, String daySTR, String monthSTR, String yearSTR, String phoneCodeSTR, String phoneSTR, String country) {
        StringBuilder errors = new StringBuilder();
        boolean valid;

        if (noNull(idSTR, firstname, lastname, daySTR, monthSTR, yearSTR, phoneCodeSTR, phoneSTR, country)) {
            long id = Long.parseLong(idSTR);
            int year = Integer.parseInt(yearSTR);
            int month = Integer.parseInt(monthSTR);
            int day = Integer.parseInt(daySTR);
            int phoneCode = Integer.parseInt(phoneCodeSTR);
            long phone = Long.parseLong(phoneSTR);

            valid = validateDate(day, month, year);
            if (!valid){
                errors.append("Fecha inválida.\n");
            }

            for (Passenger passenger : storage.getPassengers()) {
                if (passenger.getId() == id && valid) {
                    errors.append("ID repetido.\n");
                    valid = false;
                }
            }

            if (digits(id, 15, 0)){
                errors.append("ID inválido (demasiados dígitos o negativo).\n");
                valid = false;
            }
            if (digits(phoneCode, 3, 0)){
                errors.append("Código telefónico inválido (demasiados dígitos o negativo).\n");
                valid = false;
            }
            if (digits(phone, 11, 0)){
                errors.append("Número de teléfono inválido (demasiados dígitos o negativo).\n");
                valid = false;
            }

            if (valid) {
                LocalDate birthDate = LocalDate.of(year, month, day);
                storage.getPassengers().add(new Passenger(id, firstname, lastname, birthDate, phoneCode, phone, country));
            }else{
                JOptionPane.showMessageDialog(null, errors.toString(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Hay campos por llenar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static boolean digits(long id, int dig, int min) {
        if (id < 0) 
            return true;

        long temp = id;
        int counter = 0;

        if (temp == 0) 
            counter = 1;
        else {
            while (temp != 0) {
                counter++;
                temp /= 10;
            }
        }
        return (counter > dig) || (id < min);
    }

    public static boolean noNull(String id, String firstname, String lastname, String day, String month, String year, String phoneCode, String phone, String country) {
        if (isEmpty(id) || isEmpty(firstname) || isEmpty(lastname) || isEmpty(day) ||
            isEmpty(month) || isEmpty(year) || isEmpty(phoneCode) || isEmpty(phone) || isEmpty(country)) 
            return false;

        if (!isNumeric(id) || !isNumeric(day) || !isNumeric(month) || !isNumeric(year) || !isNumeric(phoneCode) || !isNumeric(phone)) 
            return false;


        return true;
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static boolean isNumeric(String s) {
        return s.matches("\\d+");
    }

    public static boolean validateDate(int day, int month, int year) {
        boolean validDate = true;

        if (year < 1910 || year > 2024) 
            validDate = false;
        else if (month < 1 || month > 12)
            validDate = false;
        else if (day < 1 || day > 31)
            validDate = false;
        else {
            if (month == 2) {

                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    if (day > 29) 
                        validDate = false;
                } else {
                    if (day > 28) 
                        validDate = false;
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30) 
                    validDate = false;
            }
        }
        if (!validDate)
            JOptionPane.showMessageDialog(null, "Fecha Invalida", "Advertencia", JOptionPane.WARNING_MESSAGE);
        
        return validDate;
    }
}
