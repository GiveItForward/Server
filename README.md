# Server


##### How to read our API.
Each API call is defined by a path which may or may not consist of multiple headers. To read this API ...

### External API:
###### Requests

| Path          | Headers   | Properties |
| ------------- |-----------||-----------|
| /requests     |  None     | - Returns all open requests as a JSONArray of requests. |
| /requests/requestuid/open | id : {requester_uid} | - Returns all open requests made by the user (aka requester) with the given uid. |
| /requests/requestuid/all | id : {requester_uid} | - Returns all open and closed requests made by the user (aka requester) with the given uid. |
| /requests/donateuid | id : {donor_uid} | - Returns all fulfilled requests fulfilled by the user (aka donor) with the given uid. |
\* indicates an optional header.

###### Users

| Path          | Headers   | Properties |
| ------------- |-----------||-----------|
| /signup     | username : {username} <br> password : {password} <br> email : {email} <br> \*isAdmin : {true/false}  <br> \*oid : {associated oid} <br> photo : {profile_pic} <br> bio : {text biography} | - isAdmin: true if the user will have admin privileges, false for normal users. <br> - oid: organization managed by the user, may be empty. <br> <br> - Returns all open requests as a JSONArray of requests. |
| /login |  username : {username} <br> password : {password} | 
\* indicates an optional header.


String username = headers.getRequestHeader("username").get(0);
        String password = headers.getRequestHeader("password").get(0);
        String email = headers.getRequestHeader("email").get(0);
//        String isAdminString = headers.getRequestHeader("isAdmin").get(0);
//        String oidString = headers.getRequestHeader("oid").get(0);
//        String photo = headers.getRequestHeader("photo").get(0);
        String bio = headers.getRequestHeader("bio").get(0);



Text form:
/requests  
(No headers)  
Returns all requests from every user as a JSONArray of requests.  
Returns:
