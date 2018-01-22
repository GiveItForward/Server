/* ALL USER AND ELEVATED USER TABLE QUERIES */

/* GET ALL USER INFO FROM EMAIL */
SELECT * FROM users WHERE email = '';

/* ADD NEW USER */
INSERT INTO users (email, username, password, isAdmin, oid, photo, bio, signupdate)
    VALUES ('','','',false,null,null,'',current_timestamp);

/* DEACTIVATE USER */
UPDATE users SET inactivedate=current_timestamp WHERE email='';

/* GET ALL USER INFO FROM EMAIL - INCLUDING ELEVATED */
SELECT * FROM users u, elevated_user e WHERE e.uid = u.uid AND u.email = '';

/* UPDATE A USER TO ADMIN */
UPDATE  users SET isadmin = true where email = '';
INSERT INTO elevated_user (uid, first_name, last_name, phone_num)
  SELECT uid, 'first name', 'last name', 'phone num' FROM users WHERE email = '';

/* NOTE: USER ELEVATION FOR AN ORG IS IN THE ORG SQL FILE */