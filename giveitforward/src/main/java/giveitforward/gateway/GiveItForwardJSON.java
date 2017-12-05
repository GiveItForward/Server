package giveitforward.gateway;

import giveitforwardtests.models.Request;
import giveitforwardtests.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class GiveItForwardJSON
{

    public static JSONObject writeUserToJSON(User user)
    {
        JSONObject object = new JSONObject();
        object.put("uid", user.getUid());
        object.put("email", user.getEmail());
        object.put("username", user.getUsername());
        object.put("password", user.getPassword());
        object.put("isAdmin", user.getIsAdmin());
        object.put("orgId", user.getOrgId());
        object.put("photo", user.getPhoto());
        object.put("bio", user.getBio());
        return object;
    }

    public static User getUserFromJSON(JSONObject object)
    {
        int uid = object.getInt("uid");
        String email = object.getString("email");
        String username = object.getString("username");
        String password = object.getString("password");
        boolean isAdmin = object.getBoolean("isAdmin");
        int orgId = object.getInt("orgId");
        String photo = object.getString("photo");
        String bio = object.getString("bio");

        User user = new User(uid, email, username, password, isAdmin, orgId, photo, bio);
        return user;
    }

    public static JSONArray getRequestJSONCollection(List<Request> requests)
    {

        JSONArray jsonArray = new JSONArray();
        for (Request req : requests)
        {
            jsonArray.put(writeRequestToJSON(req));
        }
        return jsonArray;
    }

    public static JSONObject writeRequestToJSON(Request request)
    {

        JSONObject object = new JSONObject();
        object.put("rid", request.getRid());
        object.put("description", request.getDescription());
        object.put("amount", request.getAmount());
        object.put("image", request.getImage());
        return object;
    }
}
