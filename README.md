# Server


##### How to read our API.
Each API call is defined by a path which may or may not consist of multiple headers. To read this API ...

### External API:
###### Requests

| Path          | Headers   |Properties |
| ------------- |-----------|
| /requests     |  None     | - Returns all open requests as a JSONArray of requests. |
| /requests/requestuid/open | uid : {requester_uid} | - Returns all open requests made by the user (aka requester) with the given uid. |
| /requests/requestuid | uid : {requester_uid} | - Returns all open and closed requests made by the user (aka requester) with the given uid. |
| /requests/donateuid | uid : {donor_uid} | - Returns all fulfilled requests fulfilled by the user (aka donor) with the given uid. |

\* indicates an optional header.

###### Users

| Path          | Headers   | Properties |  
| ------------- |-----------|-----------|  
| /signup     |  | - Returns the User, user tags, and num of donations and fulfilled requests.
| /login |  username : {username} <br> password : {password} | - verifies a users credentials and logs a user into a session.
| /users | None | - Returns a list of all users

\* indicates an optional header.

###### Organizations
| Path          | Headers   | Properties |
| ------------- |-----------|-----------|
| /organizations | None | - Returns a JSONArray of all approved organizations. |

\* indicates an optional header.


###### Tags
| Path          | Headers   | Properties |
| ------------- |-----------|-----------|
| /tags | None | - Returns a JSONArray of all tags. |

\* indicates an optional header.

###### JSON Formats
| Model | JSON |
|-------|------|
|Request|{"tag_id1":5, <br>"image":"img", <br>"amount":20, <br>"thankyou":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*SEE Thank You* <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"duid":1, <br>"tag_id2":8, <br>"description":"des", <br>"ruid":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*SEE User* <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"rid":0<br>}|
|User | <br>{"uid":0, <br>"photo":"photo", <br>"bio":"bio", <br>"isAdmin":false, <br>"email":"e@email.co", <br>"username":"usrnme"  <br>}|
|Thank You | {"date":"year-mo-day hr:min:sec.ms", <br>"note":"note", <br>"image":"image", <br>"rid":0 <br>}|

### Security:


### Database:
