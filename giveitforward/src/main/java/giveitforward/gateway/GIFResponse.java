package giveitforward.gateway;

import giveitforward.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import javax.ws.rs.core.Response;


public class GIFResponse {

	/**
	 * Returns a 200ok of response containing the given string (should likely be created using Json.toString())
	 * @param json
	 * @return
	 */
	public static Response getSuccessObjectResponse(String json) {


		return Response.ok()
				.entity(json)
				.build();
	}

	/**
	 * Returns a server error response containing the given message.
	 * @return
	 */
	public static Response getFailueResponse(String msg){
		return Response.serverError()
				.entity(msg)
				.build();
	}
}
