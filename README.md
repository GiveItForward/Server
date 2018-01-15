# Server


##### How to read our API.
Each API call is defined by a path which may or may not consist of multiple headers. To read this API ...

### External API:
###### Requests

| Path          | Headers   | Properties |
| ------------- |-----------|-----------|
| /requests     |  None     | - Returns all open requests as a JSONArray of requests. |
| /requests/requestuid/open | id : {requester_uid} | - Returns all open requests made by the user (aka requester) with the given uid. |
| /requests/requestuid | id : {requester_uid} | - Returns all open and closed requests made by the user (aka requester) with the given uid. |
| /requests/donateuid | id : {donor_uid} | - Returns all fulfilled requests fulfilled by the user (aka donor) with the given uid. |

\* indicates an optional header.

###### Users

| Path          | Headers   | Properties |  
| ------------- |-----------|-----------|  
| /signup     | username : {username} <br> password : {password} <br> email : {email} <br> \*isAdmin : {true/false}  <br> \*oid : {associated oid} <br> photo : {profile_pic} <br> bio : {text biography} | - isAdmin: true if the user will have admin privileges, false for normal users. <br> - oid: organization managed by the user, may be empty. <br> <br> - Returns all open requests as a JSONArray of requests. |  
| /login |  username : {username} <br> password : {password} | - verifies a users credentials and logs a user into a session.

\* indicates an optional header.

###### Organizations
| Path          | Headers   | Properties |
| ------------- |-----------|-----------|
| /organizations | None | - Returns a JSONArray of all approved organizations. |

\* indicates an optional header.

### Security:
