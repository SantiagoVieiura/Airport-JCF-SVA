package core.model.storage.json;

import java.util.ArrayList;

/**
 *
 * @author sviei
 */
public interface ISearcher <T> {
    T search(String id, ArrayList<T> list);
}
