package core.model.storage.json;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author sviei
 */
public interface IReader<T> {
    ArrayList<T> read() throws IOException;
}
