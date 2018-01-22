/* ALL REQUEST QUERIES (INCLUDING THANK YOUS) */

/* CREATE REQUEST */
INSERT INTO request (description, amount, image, requesttime, ruid, duid)
  VALUES ('', 0, null, current_timestamp, '', null);

/* FULFILL REQUEST */
UPDATE request SET duid = '' where rid = '';

/* UPDATE REQUEST */
/* similar to the above query with whichever things needed to update */

/* DELETE REQUEST */
DELETE FROM request WHERE rid = '';

/* GET ALL REQUESTS */
SELECT * FROM request;

/* GET ALL OPEN REQUESTS */
SELECT * FROM request WHERE duid = null;

/* GET ALL OPEN REQUESTS BY UID */
SELECT * FROM request WHERE ruid = '' and duid = null;

/* GET REQUESTS BY UID */
SELECT * FROM request WHERE ruid = '';

/* GET DONATIONS BY UID */
SELECT * FROM request WHERE duid = '';

/* GET REQUEST COUNT BY UID */
SELECT count(*) FROM request WHERE ruid = '';

/* GET DONATION COUNT BY UID */
SELECT count(*) FROM request WHERE duid = '';


/*********** FILTERING ***********/

