package core.controller.utils;

import core.model.Flight;
import java.time.LocalDateTime;
import java.util.List;

public class FlightConflictValidator {

    public static boolean hasConflict(List<Flight> existingFlights, String planeId, LocalDateTime newDeparture, int durationHours, int durationMinutes) {
        LocalDateTime newEnd = newDeparture.plusHours(durationHours).plusMinutes(durationMinutes);

        for (Flight f : existingFlights) {
            if (!f.getPlane().getId().equals(planeId)) continue;

            LocalDateTime existingStart = f.getDepartureDate();
            LocalDateTime existingEnd = f.calculateArrivalDate();

            // Conflicto si se solapan los tiempos
            boolean overlap = newDeparture.isBefore(existingEnd) && newEnd.isAfter(existingStart);
            if (overlap) return true;
        }
        return false;
    }
}
