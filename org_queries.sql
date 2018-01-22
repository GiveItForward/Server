/* ALL ORGANIZATION QUERIES */

/* GET ALL ORGS WITH ALL INFO */
SELECT * FROM organization;

/* GET ALL PENDING ORGS WITH ALL INFO */
SELECT * FROM organization WHERE approveddate IS NOT NULL;

/* ADD A NEW ORGANIZATION - THIS MUST BE ATTACHED TO AN ORG USER */
INSERT INTO organization (name, email, website, phone_number, approveddate, inactivedate, address, image, bio)
    VALUES ('', '', '', '', null, null, '', null, '');
/* then run this query with the user email & org email to attach the user to the org */
/* should this happen before or after approval??? */
UPDATE users SET oid = (SELECT oid FROM organization WHERE email = '') where email = '';

/* APPROVE ORGANIZATION */
UPDATE organization set approveddate = current_timestamp where oid = '';

/* DELETE ORGANIZATION */
DELETE FROM organization WHERE oid = '';

