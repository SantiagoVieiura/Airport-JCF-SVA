package core.model.storage.json;

import core.model.Flight;
import core.model.Plane;
import core.model.Location;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class FlightJson {
    public static ArrayList<Flight> readFlights(ArrayList<Plane> planes, ArrayList<Location> locations) throws IOException {
        String path = "C:\\Users\\sebas\\Downloads\\3er Parcial\\Airport-JCF-SVA\\build\\classes\\json\\flights.json";
        String content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        JSONArray array = new JSONArray(content);
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

            Plane plane = findPlane(planeId, planes);
            Location departure = findLocation(departureId, locations);
            Location arrival = findLocation(arrivalId, locations);
            Location scale = (scaleId != null && !scaleId.equals("null")) ? findLocation(scaleId, locations) : null;

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

    private static Plane findPlane(String id, ArrayList<Plane> planes) {
        for (Plane p : planes) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    private static Location findLocation(String id, ArrayList<Location> locations) {
        for (Location l : locations) {
            if (l.getAirportId().equals(id)) return l;
        }
        return null;
    }
}
