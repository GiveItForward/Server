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

| Path          | HTTP Method | Headers   | Properties |  
| ------------- |------|-----------|------------|  
| /users/create |POST|  TODO | - Returns the User, user tags, and num of donations and fulfilled requests.
| /users/login |GET|  username : {username} <br> password : {password} | - verifies a users credentials and logs a user into a session.
| /users |GET| None | - Returns a list of all users
| /users/delete |DELETE| TODO | TODO |
| /users/update |PUT| TODO | TODO |

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
|Request|{"tagId1":5, <br>"image":"img", <br>"amount":20, <br>"thankYou":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*SEE Thank You* <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"duid":1, <br>"tagId2":8, <br>"description":"des", <br>"ruid":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*SEE User* <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"rid":0<br>}|
|User | <br>{"uid":0, <br>"image":"/img/default_profile_pic.png", <br>"bio":"bio", <br>"isAdmin":false, <br>"email":"e@email.co", <br>"username":"usrnme"  <br>}|
|Thank You | {"date":"year-mo-day hr:min:sec.ms", <br>"note":"note", <br>"image":"image", <br>"rid":0 <br>}|
|Organization |{"image":"img", <br>"website":"www.web.co", <br>"address":"addr", <br>"phone":"addr", <br>"name":"name", <br>"description":"words", <br>"oid":0  <br>"email":"wrc@email.co",<br>} |

### Security:


### Database:
