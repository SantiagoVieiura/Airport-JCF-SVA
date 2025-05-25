/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package core.model.storage.json;

import java.util.ArrayList;

/**
 *
 * @author sviei
 */
public interface ISearcher <T> {
    T search(String id, ArrayList<T> list);
}
