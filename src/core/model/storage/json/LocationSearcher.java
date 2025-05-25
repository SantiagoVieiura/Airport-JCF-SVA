/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model.storage.json;

import core.model.Location;
import java.util.ArrayList;

/**
 *
 * @author sviei
 */
public class LocationSearcher implements ISearcher<Location> {
    @Override
    public  Location search(String id, ArrayList<Location> locations) {
        for (Location l : locations) {
            if (l.getAirportId().equals(id)) return l;
        }
        return null;
    }
}
