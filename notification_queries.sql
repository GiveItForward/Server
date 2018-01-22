/* ALL NOTIFICATION QUERIES */

/* CREATE NEW NOTIFICATION */
INSERT INTO notification (date, message, uid, opened)
    VALUES (current_timestamp, '', '', false);

/* UPDATE ONE NOTIFICATION TO OPENED */
UPDATE notification SET opened = true WHERE nid = '';

/* UPDATE ALL NOTIFICATIONS TO OPENED */
UPDATE  notification SET opened = true WHERE uid = '';

/* DELETE NOTIFICATION FOR UID*/
DELETE FROM notification WHERE nid = '';

/* DELETE ALL NOTIFICATIONS FOR UID */
DELETE  FROM notification WHERE uid = '';