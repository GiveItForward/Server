package giveitforward.models;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;

public abstract class Model {

    /**
     * A string representation of the Model object.
     * @return string version of the Model object.
     */
    public abstract String asString();

    /**
     * Returns a JSON object representing the model.
     */
    public abstract JSONObject asJSON();

    /**
     * Populates the calling model object with the properties from the passed JSONObject.
     * @return False if there is an issue, true otherwise.
     */
    public abstract boolean populateFromJSON(JSONObject obj);


}
