/* ALL TAG QUERIES */

/* GET ALL TAGS */
SELECT tagname from tag;

/* GET ALL TAGS ASSOCIATED WITH A CERTAIN UID */
SELECT tagname from tag t, user_tag_pair u where t.tid = u.tagid and u.userid = '';

/* CREATE TAG */
INSERT INTO tag (tagname)
    VALUES ('');

/* UPDATE TAG */
UPDATE tag SET tagname = '' WHERE tid = '';

/* DELETE TAG */
DELETE FROM tag WHERE tid = '';

/* ADD A NEW TAG FOR A USER (this may need to be prefaced with selects to get tid/uids)*/
INSERT INTO user_tag_pair (uid, tid, time_limit, verified_by)
    VALUES ('', '', null, '');

/* VERIFY/UNVERIFY A TAG */
UPDATE user_tag_pair SET verified_by = '' WHERE tagid = '' and userid = '';

/* REMOVE A TAG FOR A USER */
DELETE FROM user_tag_pair WHERE tagid = '' and userid = '';

/* REMOVE ALL TAGS FOR A USER */
DELETE FROM user_tag_pair WHERE userid = '';

/* UPDATE TIME LIMIT TO USER'S TAG */
UPDATE user_tag_pair SET time_limit = '';