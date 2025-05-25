/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model.storage.json;

import core.model.Location;
import core.model.Plane;
import java.util.ArrayList;


public class PlaneSearcher implements ISearcher<Plane>{
    @Override
    public Plane search(String id, ArrayList<Plane> planes) {
        for (Plane p : planes) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    
}
