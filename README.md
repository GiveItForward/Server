

# Server


##### How to read our API.
Each API call is defined by a path which may or may not consist of multiple headers. To read this API ...

### External API:
###### Requests

| Path          | HTTP Method | Headers |Properties |
| ------------- |-----------|-----------|-----------|
| /requests     | GET |  None  | - Returns all open requests as a JSONArray of requests. |
| /requests/requestuid/open | GET | uid : {requester_uid} | - Returns all open requests made by the user (aka requester) with the given uid. |
|/requests/all| GET | None | -Returns all requests as a JSONArray of requests.|
| /requests/requestuid | GET | uid : {requester_uid} | - Returns all open and closed requests made by the user (aka requester) with the given uid. |
| /requests/donateuid |  GET |uid : {donor_uid} | - Returns all fulfilled requests fulfilled by the user (aka donor) with the given uid. |
| /requests/create | POST | See CREATE REQUEST in json parameters below. | -Creates a new request.|
| /requests/fulfill | POST | See CREATE REQUEST in json parameters below. | -Fulfills a request.|
| /requests/update | PUT | See CREATE REQUEST in json parameters below. | -Updates a request.|
| /requests/delete | DELETE | See CREATE REQUEST in json parameters below. | -Deletes a request.|
|/requests/tags|GET|None|-Returns a list of all request tags.|
| /requests/filters | GET | See FILTER REQUEST in json parameters below. | -Returns a list of requests filtered based on json params. |

\* indicates an optional header.

###### Users

| Path          | HTTP Method | Headers/Params   | Properties |  
| ------------- |------|-----------|------------|  
| /users/create |POST|  | - Returns the User, user tags, and num of donations and fulfilled requests.
| /users/login |GET|  username : {username} <br> password : {password} | - verifies a users credentials and logs a user into a session.
| /users |GET| None | - Returns a list of all users
| /users/delete |DELETE| See Users Json Parameters. | -Sets the user as inactive in the database. |
| /users/update |PUT| See Users Json Parameters. | -Updates the user. |
| /users/getdonateamount/ | GET | uid:{uid} | -returns Json response of the following format for the specified user. <br> {"donateAmount":30.50}|
| /users/byuid | GET | uid:{uid} | -Returns the user with the given uid.|
|/confirm/{id} | GET | NONE | -Confirms a users email and sets signup date in the user. <br> - Returns response that's the same as login.|
|/users/byuid/private | GET | NONE | ...|
|/users/gethash | GET | NONE | ...|
|/users/verifyhash | GET | NONE | ...|
|/users/deletehash | GET | NONE | ...|
|/users/promote/org | PUT | NONE | ...|
|/users/demote/org | PUT | NONE | ...|
|/users/promote/admin | PUT | NONE | ...|
|/users/demote/admin | PUT | NONE | ...|
|/users/verifytag | PUT | NONE | ...|
| /users/search | GET | search:{searchString} | - Returns list of users matching the search string |

\* indicates an optional header.

###### Organizations
| Path          | HTTP<br>Method | Headers   | Properties |
| ------------- |------|-----------|-----------|
| /organizations | GET | None | - Returns a JSONArray of all approved organizations. |
| /organizations/pending | GET | None | - Returns a JSONArray of all pending organizations. |
| /organizations/create | POST | uid:{uid} &nbsp;&nbsp;&nbsp;&nbsp; &<br> See Org Json Parameters. | - Returns the newly created org|
| /organizations/update | PUT | See Org Json Parameters. | - Returns the updated org |
| /organizations/delete | DELETE | See Org Json Parameters. | - Deactivates org by setting inactive date <br>- Returns the deactivated org |
| /organizations/approve | PUT | See Org Json Parameters. | - Set approved date in DB<br>- Returns approved org |
| /organizations/oid | GET | oid:{oid} | - Gets the org for the given oid |
| /organizations/search | GET | search:{searchString} | - Gets all orgs that match on the string in the headers |

\* indicates an optional header.


###### Tags
| Path          | HTTP<br>Method | Headers   | Properties |
| ------------- |------|-----------|-----------|
| /tags | None | GET | - Returns a JSONArray of all tags. |

\* indicates an optional header.


###### ThankYous
| Path          | HTTP<br>Method | Headers   | Properties |
| ------------- |------|-----------|-----------|
| /thankyou | None | GET | - Returns a JSONArray of all thankyous. |
| /thankyou/create | See THANKYOU in json parameters below. | POST | - Creates a new ThankYou. |
| /thankyou/update | See THANKYOU in json parameters below. | PUT | - Updates a ThankYou. |
| /thankyou/delete | See THANKYOU in json parameters below. | DELETE | - Deletes a ThankYou. |

\* indicates an optional header.

###### Notifications
| Path          | HTTP<br>Method | Headers   | Properties |
| ------------- |------|-----------|-----------|
| /notifications | GET | uid:{uid} | - Returns a JSONArray of notification for the given uid. |
| /notifications/seen | PUT | nid:{nid} | - Sets the notification with the given nid to opened in the db. <br> - Returns the opened notification. |
| /notifications/seenall | PUT | uid:{uid} | - Sets all notifications for the given uid to opened in the db. <br> - Returns the list of unopened notifications for the given user <br>(will almost always be empty except in rare cases of notifications being created between the two queries to update and then get). |

\* indicates an optional header.

Notification Types:
1. Request Fulfilled (w/ rid)
2. Thank You Received (w/ rid)
3. Tags Verified
4. Tags Unverified
5. Organization Awaiting Approval
6. Organization is Pending
7. Organization Approved
8. Organization Denied
9. Promoted to Admin

###### JSON Response Formats
| Model | JSON |
|-------|------|
|Request|{"tagId1":5, <br>"image":"img", <br>"amount":20, <br>"thankYou":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*SEE Thank You* <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"duid":1, <br>"tagId2":8, <br>"description":"des", <br>"ruid":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*SEE User* <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"rid":0<br>}|
|User | <br>{"uid":0, <br>"image":"/img/default_profile_pic.png", <br>"bio":"bio", <br>"isAdmin":false, <br>"email":"e@email.co", <br>"orgId":0, <br>"username":"usrnme", <br>"tags": [<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{"tagname":"name", <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"verifiedBy": 0, <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tid:" 0}, <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{...<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>]<br>}|
|Thank You | {"date":"year-mo-day hr:min:sec.ms", <br>"note":"note", <br>"image":"image", <br>"rid":0 <br>}|
|Organization |{"image":"img", <br>"website":"www.web.co", <br>"address":"addr", <br>"phone":"addr", <br>"name":"name", <br>"description":"words", <br>"oid":0  <br>"email":"wrc@email.co",<br>} |
| Notification | {"nid":2, <br>"message":"New Thank You Received!", <br>"uid":1, <br>"date":"year-mo-day hr:min:sec.ms", <br>"opened":false<br>} |

###### JSON Parameters Format
| Model |Uses| JSON |
|-------|----|------|
|Request|Create Request|{"image":"img", <br>"tag1":{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tagname":&nbsp;"Request Tag1"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tid":&nbsp;1<br>},<br>"amount":20, <br>"description":"des", <br>"tag2":{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tagname":&nbsp;"Request Tag2"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"tid":&nbsp;2<br>}, <br>"rUser":{ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"uid":&nbsp;1 <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}, <br>"rid":0<br>}|
|User| Create User <br> Update User <br> Delete User| <br>{"image":"/img/default_profile_pic.png", <br>"bio":"bio", <br>"email":"e@email.co", <br>"password":"pwd", <br>"orgId":"", <br>"username":"usrnme", <br>"tags": [ <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; { <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "tagname": "nme", <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "tid": "1" <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; { <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "tagname": "otr", <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "tid": "2" <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}&nbsp;]<br>}|
| Thank You | Create Thank You|{"date":"year-mo-day hr:min:sec.ms", <br>"note":"note", <br>"image":"image", <br>"rid":0 <br>}|
| Organization | Create Organization |{"image":"img", <br>"website":"www.web.co", <br>"address":"addr", <br>"phone":"addr", <br>"name":"name", <br>"description":"words", <br>"oid":0  <br>"email":"wrc@email.co",<br>} |
| Request | Filter Request | {****"age"**:"old/new",<br>****"price"**:"low/high"<br>"rtags": [&nbsp;8&nbsp;]<br>"utags": [&nbsp;0, 1 ]<br>} |
** indicates optional json

### Security:


### Database:


### Alpha:
Login:
* Password hashed. sha256
* error message for wrong password/username.
* donate count and receive count.

Signup:
* image profiles
* test

Requests/create:
* for child image
* no #other tag


Request Feed:
* Paypal
* refresh after new requests
* fix empty Tags

myRequests:

myDonations:


### Beta:


### Types of Notifications:
* Org Approved
* Org Not approved
* Request fulfilled
* Someone Said Thank You
* Tag was Approved
* Tag was Unapproved
