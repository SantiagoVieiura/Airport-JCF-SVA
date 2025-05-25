package core.model.storage.json;

import core.model.Plane;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlaneJson {
    public static ArrayList<Plane> readPlanes() throws IOException {
        String path = "json/planes.json";
        String content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        JSONArray array = new JSONArray(content);
        ArrayList<Plane> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String id = obj.getString("id");
            String brand = obj.getString("brand");
            String model = obj.getString("model");
            int maxCapacity = obj.getInt("maxCapacity");
            String airline = obj.getString("airline");

            list.add(new Plane(id, brand, model, maxCapacity, airline));
        }
        return list;
    }
}
