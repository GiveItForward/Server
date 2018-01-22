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

    /**
    * Returns a JSON array consisting of many object to turn into a JSONArray
    * @return
    */
    public static JSONArray asJSONCollection(List collection) {
        JSONArray jsonArray = new JSONArray();
        for (Object el : collection)
        {
            jsonArray.put(((Model)el).asJSON());
        }
        return jsonArray;
    }
}
