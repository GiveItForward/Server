package giveitforward.managers;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import giveitforward.gateway.Globals;
import giveitforward.models.EmailCode;
import giveitforward.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


import java.security.SecureRandom;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public class ManageEmail {

    public static void main(String[] args) {
//        EmailCode ec = new EmailCode(2, "11111111111111111111111111111111", 'c');
//        boolean result = save(ec);
//        if(result == false){
//            System.err.println("result invalid");
//        }
//s
//        confirmEmail("11111111111111111111111111111111");
    }

	public static EmailCode getEmailCodeFromUser(User user, char type) {
		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;
		EmailCode ec = null;

		try
		{
			t = session.beginTransaction();

			Criteria criteria = session.createCriteria(EmailCode.class);
			criteria.add(Restrictions.eq("uid", user.getUid()));
			criteria.add(Restrictions.eq("type", type));

			ec = (EmailCode) criteria.uniqueResult();

			t.commit();
		} catch (Exception e)
		{
			if (t != null)
			{
				t.rollback();
			}
			System.out.println("ROLLBACK");
			e.printStackTrace();
			return null;
		} finally
		{
			session.close();
			return ec;
		}
	}

    public static boolean forgotPassword(String email){
        // get the user
		User u = new ManageUser().getUserFromEmail(email);

		Character type = 'f';
		EmailCode ec = getHash(u, type);
		if (ec == null){
			return false;
		}

		String hash = ec.getUhash();
		String link = "www.giveitforward.us/resetpassword/" + hash;
		String noHtmlEmailBody = "Hi there! You’ve requested to reset the password for your Give It Forward account. To continue with this process, visit the link below. " +
				"If this was not you, simply ignore this email.\n " + link;

		String subject = "Give it Forward, reset password";


		String name = u.getFirstname() + " " + u.getLastname();
		String body = "We received a request to reset your Give It Forward password. If this sounds familiar, please click below to enter a new password. If this doesn't sound familiar please ignore this email. Thanks for being a member of the Give It Forward family!";
		String footnotes = "Note: your current password is still valid until you change it via the link above.";
		String htmlBody = Globals.emailHtml(link, name, "Reset Your Password", body, "Reset Password", footnotes);

		return sendEmail(u, noHtmlEmailBody, subject, htmlBody);
    }

    public static EmailCode getHash(User u, Character type) {
		//First check if this entry already exists in the DB... if it does, return it and update the time.
		EmailCode ec = getStoredHash(u, type);
		if(ec != null){
			//Use this ec and update the time.
			ec.setCreationTime(new Timestamp(System.currentTimeMillis()));
			update(ec);
			return ec;
		}
		else {
			//Create a row in EmailCode for the user.
			String hash = getRandomHash();

			ec = new EmailCode(u.getUid(), hash, type);

			//Save the row.
			boolean successful = ManageEmail.save(ec);

			if (successful) {
				return ec;
			}
			else {
				return null;
			}
		}
    }

    public static EmailCode getStoredHash(User u, Character type){
		String query = "select e from EmailCode e where e.uid = '" + u.getUid() + "' and e.type = '" + type + "'";
		EmailCode ec = makeQuery(query);

		if(ec == null){
			System.err.println("Failled to match hash in email_codes table");
			return null;
		}
		return ec;
	}

    public static boolean deleteHash(EmailCode ec) {

        return delete(ec);
    }

    public static boolean sendEmail(User u, String emailBody, String emailSubject, String htmlBody){
		if(u == null){
			return false;
		}

		//Send the email.
		String from = "no-reply@giveitforward.us";  // Replace with your "From" address. This address must be verified.
		String to = u.getEmail();
		// Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses(new String[]{to});

		// Create the subject and body of the message.
		Content subject = new Content().withData(emailSubject);
		Content textBody = new Content().withData(emailBody);
		Body body = new Body()
				.withHtml(new Content()
								  .withCharset("UTF-8").withData(htmlBody))
				.withText(new Content()
								  .withCharset("UTF-8").withData(emailBody));

		// Create a message with the specified subject and body.
		Message message = new Message().withSubject(subject).withBody(body);

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest()
				.withSource(from)
				.withDestination(destination)
				.withMessage(message);

		try {
			System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

			/*
			 * The ProfileCredentialsProvider will return your [default]
			 * credential profile by reading from the credentials file located at
			 * (~/.aws/credentials).
			 *
			 * TransferManager manages a pool of threads, so we create a
			 * single instance and share it throughout our application.
			 */
			ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider("/.aws/credentials", "default");
			try {
				credentialsProvider.getCredentials();
			} catch (Exception e) {
				throw new AmazonClientException(
						"Cannot load the credentials from the credential profiles file. " +
								"Please make sure that your credentials file is at the correct " +
								"location (~/.aws/credentials), and is in valid format.",
						e);
			}

			// Instantiate an Amazon SES client, which will make the service call with the supplied AWS credentials.
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					.withCredentials(credentialsProvider)
					// Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your production
					// access status, sending limits, and Amazon SES identity-related settings are specific to a given
					// AWS region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using
					// the US East (N. Virginia) region. Examples of other regions that Amazon SES supports are US_WEST_2
					// and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html
					.withRegion("us-east-1")
					.build();

			// Send the email.
			client.sendEmail(request);
			System.out.println("Email sent!");

		} catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
			return false;
		}

		return true;
	}

    public static boolean sendConfirmEmail(User u) {
		Character type = 'c';
		EmailCode ec = getHash(u, type);
		if (ec == null) {
			return false;
		}
		String hash = ec.getUhash();

		String link = "www.giveitforward.us/confirm/" + hash;
		String email = u.getEmail();
		String name = u.getFirstname() + " " + u.getLastname();

		String emailBody = "Welcome to giveitforward.us! Please confirm your email by clicking the following link www.giveitforward.us/confirm/" + hash;
		String body = "We received a request to set your Give It Forward email to " + email + ".\n If this is correct, please confirm by clicking the button below.\n";
		String footnotes = "Once you confirm, all future messages about your Give It Forward account will be sent to " + email + ".";
		String emailSubject = "Give it Forward, confirm email";
		String htmlBody = Globals.emailHtml(link, name, "Confirm Your Email", body, "Confirm Email", footnotes);
		return sendEmail(u, emailBody, emailSubject, htmlBody);
    }

	public static EmailCode confirmHash(String hash){
		//Match the has to a uid in the email_codes table.
		String query = "select e from EmailCode e where e.uHash = '" + hash + "'";
		EmailCode ec = makeQuery(query);

		if(ec == null){
			System.err.println("Failled to match hash in email_codes table");
			return null;
		}
		return ec;
	}

	public static User resetPassword(String hash, String password){
		EmailCode ec = confirmHash(hash);
		if(ec == null){
			return null;
		}
		if(ec.getType() != 'f'){
			return null;
		}
		User u = new ManageUser().getUserfromUID(ec.getUid());
		u.setPassword(password);
		u =  new ManageUser().updateUser(u);
		if(u == null){
			//return error
			return null;
		}

		delete(ec);

		return u;
	}

    public static User confirmEmail(String hash){
        EmailCode ec = confirmHash(hash);
        if(ec == null){
        	return null;
		}
		if(ec.getType() != 'c'){
			return null;
		}

        //set the signup date in the user table.
        User u = new ManageUser().confirmEmail(ec.getUid());
        if(u == null) {
            System.err.println("Failled to update user");
            return null;
        }

        //delete the email_code from the table
        delete(ec);

        System.err.println("Email Confirmed");
        return u;
    }

    /**
     * Saves the email code to the db.
     * @param ec
     * @return
     */
    private static boolean save(EmailCode ec) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try
        {
            t = session.beginTransaction();
            session.save(ec);
            session.flush();
            t.commit();
        } catch (Exception e)
        {
            if (t != null)
            {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            return false;
        } finally
        {
            session.close();
        }

        System.out.println("successfully added user");
        return true;
    }

    /**
     * Removes the EmailCode from the db
     * @param ec
     * @return
     */
    private static boolean delete(EmailCode ec) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try
        {
            t = session.beginTransaction();
            session.delete(ec);
            session.flush();
            t.commit();
        } catch (Exception e)
        {
            if (t != null)
            {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            return false;
        } finally
        {
            session.close();
        }

        System.out.println("successfully added user");
        return true;
    }

    private static EmailCode update(EmailCode ec){
		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;

		try
		{
			t = session.beginTransaction();
			session.update(ec);
			session.flush();
			t.commit();
		} catch (Exception e)
		{
			if (t != null)
			{
				t.rollback();
			}
			System.out.println("ROLLBACK");
			e.printStackTrace();
			return null;
		} finally
		{
			session.close();
		}

		System.out.println("successfully added user");
		return ec;
	}

    private static String getRandomHash() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    /**
     * @param query HQL query to be performed.
     * @return a list of Requests which results from the given query.
     */
    private static EmailCode makeQuery(String query) {
        Session session = SessionFactorySingleton.getFactory().openSession();//factory.openSession();
        Transaction t = null;
        EmailCode ec = null;

        try
        {
            t = session.beginTransaction();
            ec = ((List<EmailCode>)session.createQuery(query).list()).get(0);
            t.commit();
        } catch (Exception e)
        {
            if (t != null)
            {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
        } finally
        {
            session.close();
            return ec;
        }
    }

}
