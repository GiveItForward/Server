

# Server


##### How to read our API.
Each API call is defined by a path which may or may not consist of multiple headers. To read this API ...

### External API:
###### Requests

| Path          | HTTP Method | Headers   |Properties |
| ------------- |-----------|-----------|
| /requests     | GET |  None     | - Returns all open requests as a JSONArray of requests. |
| /requests/requestuid/open |  GET | uid : {requester_uid} | - Returns all open requests made by the user (aka requester) with the given uid. |
| /requests/requestuid | GET | uid : {requester_uid} | - Returns all open and closed requests made by the user (aka requester) with the given uid. |
| /requests/donateuid |  GET |uid : {donor_uid} | - Returns all fulfilled requests fulfilled by the user (aka donor) with the given uid. |
| /requests/create | POST | See CREATE REQUEST in json parameters below. | -Creates a new request.|
|/requests/tags|GET|None|-Returns a list of all request tags.|
\* indicates an optional header.

###### Users

| Path          | HTTP Method | Headers/Params   | Properties |  
| ------------- |------|-----------|------------|  
| /users/create |POST|  | - Returns the User, user tags, and num of donations and fulfilled requests.
| /users/login |GET|  username : {username} <br> password : {password} | - verifies a users credentials and logs a user into a session.
| /users |GET| None | - Returns a list of all users
| /users/delete |DELETE| TODO | TODO |
| /users/update |PUT| TODO | TODO |
| /users/getdonateamount/ | GET | uid:{uid} | -returns Json response of the following format for the specified user. <br> {"donateAmount":30.50}|
| /users/byuid | GET | uid:{uid} | -Returns the user with the given uid.|
|/confirm/{id} | GET | NONE | -Confirms a users email and sets signup date in the user. <br> - Returns response that's the same as login.|

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

###### JSON Response Formats
| Model | JSON |
|-------|------|
|Request|{"tagId1":5, <br>"image":"img", <br>"amount":20, <br>"thankYou":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*SEE Thank You* <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"duid":1, <br>"tagId2":8, <br>"description":"des", <br>"ruid":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*SEE User* <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"rid":0<br>}|
|User | <br>{"uid":0, <br>"image":"/img/default_profile_pic.png", <br>"bio":"bio", <br>"isAdmin":false, <br>"email":"e@email.co", <br>"orgId":0, <br>"username":"usrnme", <br>"tags": [<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"tagname":"name", <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"verifiedBy": 0, <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tid:" 0}, <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{...<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>]<br>}|
|Thank You | {"date":"year-mo-day hr:min:sec.ms", <br>"note":"note", <br>"image":"image", <br>"rid":0 <br>}|
|Organization |{"image":"img", <br>"website":"www.web.co", <br>"address":"addr", <br>"phone":"addr", <br>"name":"name", <br>"description":"words", <br>"oid":0  <br>"email":"wrc@email.co",<br>} |

###### JSON Parameter Formats
| Model | JSON |
|-------|------|
|Create Request|{"image":"img", <br>"tag1":{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tagname":&nbsp;"Request Tag1"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tid":&nbsp;1<br>},<br>"amount":20, <br>"description":"des", <br>"tag2":{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tagname":&nbsp;"Request Tag2"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tid":&nbsp;2<br>}, <br>"rUser":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"uid":&nbsp;1 <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"rid":0<br>}|
|Create User | <br>{"image":"/img/default_profile_pic.png", <br>"bio":"bio", <br>"email":"e@email.co", <br>"password":"pwd", <br>"orgId":"", <br>"username":"usrnme", <br>"tags": [ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; { <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "tagname": "nme", <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "tid": "1" <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; { <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "tagname": "otr", <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "tid": "2" <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}&nbsp;]<br>}|
| Create Thank You | {"date":"year-mo-day hr:min:sec.ms", <br>"note":"note", <br>"image":"image", <br>"rid":0 <br>}|
| Create Organization |{"image":"img", <br>"website":"www.web.co", <br>"address":"addr", <br>"phone":"addr", <br>"name":"name", <br>"description":"words", <br>"oid":0  <br>"email":"wrc@email.co",<br>} |

### Security:


### Database:
