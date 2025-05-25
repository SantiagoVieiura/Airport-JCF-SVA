/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model.storage.json;

import core.model.Flight;
import core.model.Location;
import core.model.Plane;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author sviei
 */
public class FlightParser {
    public static ArrayList<Flight> parse(String json,
                                          ArrayList<Plane> planes,
                                          ArrayList<Location> locations,
                                          ISearcher<Plane> planeSearcher,
                                          ISearcher<Location> locationSearcher) {
        JSONArray array = new JSONArray(json);
        ArrayList<Flight> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String id = obj.getString("id");
            String planeId = obj.getString("plane");
            String departureId = obj.getString("departureLocation");
            String arrivalId = obj.getString("arrivalLocation");
            String scaleId = obj.optString("scaleLocation", null);
            LocalDateTime departureDate = LocalDateTime.parse(obj.getString("departureDate"));
            int hoursDurArrival = obj.getInt("hoursDurationArrival");
            int minutesDurArrival = obj.getInt("minutesDurationArrival");
            int hoursDurScale = obj.getInt("hoursDurationScale");
            int minutesDurScale = obj.getInt("minutesDurationScale");

            Plane plane = planeSearcher.search(planeId, planes);
            Location departure = locationSearcher.search(departureId, locations);
            Location arrival = locationSearcher.search(arrivalId, locations);
            Location scale = (scaleId != null && !scaleId.equals("null")) ? locationSearcher.search(scaleId, locations) : null;

            Flight flight;
            if (scale == null) {
                flight = new Flight(id, plane, departure, arrival, departureDate, hoursDurArrival, minutesDurArrival);
            } else {
                flight = new Flight(id, plane, departure, scale, arrival, departureDate, hoursDurArrival, minutesDurArrival, hoursDurScale, minutesDurScale);
            }

            list.add(flight);
        }
        return list;
    }
}

