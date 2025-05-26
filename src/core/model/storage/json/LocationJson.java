package core.model.storage.json;

import core.model.Location;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class LocationJson implements IReader<Location> {

    private final String path = "json/locations.json";

    @Override
    public ArrayList<Location> read() throws IOException {
        String content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        JSONArray array = new JSONArray(content);
        ArrayList<Location> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String id = obj.getString("airportId");
            String name = obj.getString("airportName");
            String city = obj.getString("airportCity");
            String country = obj.getString("airportCountry");
            double latitude = obj.getDouble("airportLatitude");
            double longitude = obj.getDouble("airportLongitude");

            list.add(new Location(id, name, city, country, latitude, longitude));
        }

        return list;
    }
}

