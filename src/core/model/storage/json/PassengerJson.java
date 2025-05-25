package core.model.storage.json;

import core.model.Passenger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class PassengerJson {
    public PassengerJson() {
    }

    public static ArrayList<Passenger> readPassengers() throws IOException {
        // Ruta relativa desde la carpeta de ejecución
        String path = "json/passengers.json";

        String content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);

        JSONArray array = new JSONArray(content);
        ArrayList<Passenger> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            long id = obj.getLong("id");
            String firstname = obj.getString("firstname");
            String lastname = obj.getString("lastname");
            LocalDate birthDate = LocalDate.parse(obj.getString("birthDate"));
            int countryPhoneCode = obj.getInt("countryPhoneCode");
            long phone = obj.getLong("phone");  
            String country = obj.getString("country");
            list.add(new Passenger(id, firstname, lastname, birthDate, countryPhoneCode, phone, country));
        }
        return list;
    }
}


