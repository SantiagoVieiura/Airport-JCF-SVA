package core.model.storage.json;

import core.model.Flight;
import core.model.Plane;
import core.model.Location;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FlightJson {
    public static ArrayList<Flight> readFlights(ArrayList<Plane> planes, ArrayList<Location> locations) throws IOException {
        String path = "json/flights.json";
        String content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        return FlightParser.parse(content, planes, locations,
                                  new PlaneSearcher(), new LocationSearcher());
    }
}

