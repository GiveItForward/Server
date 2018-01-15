package giveitforward.gateway;

import giveitforward.models.Request;
import giveitforward.models.Organization;
import giveitforward.models.User;
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

    public static JSONArray getOrgJSONCollection(List<Organization> organizations)
    {

        JSONArray jsonArray = new JSONArray();
        for (Organization org : organizations)
        {
            jsonArray.put(writeOrganizationToJSON(org));
        }
        return jsonArray;
    }


    public static JSONObject writeOrganizationToJSON(Organization org)
    {
        JSONObject object = new JSONObject();
        object.put("oid", org.getOid());
        object.put("email", org.getEmail());
        object.put("website", org.getWebsite());
        object.put("name", org.getName());
        return object;
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
