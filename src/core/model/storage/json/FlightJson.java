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
        String path = "json/flights.json";
        String content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        return FlightParser.parse(content, planes, locations,
                                  new PlaneSearcher(), new LocationSearcher());
    }
}

//esto estaba aquí antes de aplicar el single respon
    
    
//    private static Plane findPlane(String id, ArrayList<Plane> planes) {
//        for (Plane p : planes) {
//            if (p.getId().equals(id)) return p;
//        }
//        return null;
//    }
//
//    private static Location findLocation(String id, ArrayList<Location> locations) {
//        for (Location l : locations) {
//            if (l.getAirportId().equals(id)) return l;
//        }
//        return null;
//    }
//}
