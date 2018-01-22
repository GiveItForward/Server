package giveitforward.models;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.sql.Timestamp;

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

//    public static String getDisplayDate(Timestamp time) {
//        String res = time.toString();
//        String pieces[] = res.split("-");
//        res = month(pieces[1]) + ", " + pieces[2] + ", " + pieces[0];
//        return res;
//    }
//
//    private String month(String month_no){
//        switch (month_no) {
//            case ""
//        }
//    }
}
