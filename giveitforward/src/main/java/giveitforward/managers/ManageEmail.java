package giveitforward.managers;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import giveitforward.models.EmailCode;
import giveitforward.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


import java.security.SecureRandom;
import java.math.BigInteger;
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
		String emailBody = "Hi there! You’ve requested to reset the password for your Give It Forward account. To continue with this process, visit the link below. " +
				"If this wasn’t you, simply ignore this email.\nwww.giveitforward.us/forgotpassword/" + hash;
		String emailSubject = "Reset your password!";
//		return sendEmail(u, emailBody, emailSubject);
		return false;
    }

    public static EmailCode getHash(User u, Character type) {

        //Create a row in EmailCode for the user.
        String hash = getRandomHash();

        EmailCode ec = new EmailCode(u.getUid(), hash, type);

        //Save the row.
        boolean successful = ManageEmail.save(ec);

        if(successful){
            return ec;
        }
        else {
            return null;
        }
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
		String htmlBody = "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"background: #f5f8fa; min-width: 350px; font-size: 1px; line-height: normal;\">\n" +
				"  <tr>\n" + "   " +
				" <td align=\"center\" valign=\"top\">\n" +
				"      <!--[if (gte mso 9)|(IE)]>\n" +
				"        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
				"          <tr>\n" +
				"            <td align=\"center\" valign=\"top\" width=\"750\">\n" +
				"            <![endif]-->\n" +
				"            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"750\" class=\"table750\"\n" +
				"            style=\"width: 100%; max-width: 750px; min-width: 350px; background: #f5f8fa;\">\n" +
				"              <tr>\n" +
				"                <td class=\"mob_pad\" width=\"25\" style=\"width: 25px; max-width: 25px; min-width: 25px;\">&nbsp;</td>\n" +
				"                <td align=\"center\" valign=\"top\" style=\"background: #ffffff;\">\n" +
				"                  <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"width: 100% !important; min-width: 100%; max-width: 100%; background: #f5f8fa;\">\n" +
				"                    <tr>\n" +
				"                      <td align=\"right\" valign=\"top\">\n" +
				"                        <div class=\"top_pad\" style=\"height: 25px; line-height: 25px; font-size: 23px;\">&nbsp;</div>\n" +
				"                      </td>\n" +
				"                    </tr>\n" +
				"                  </table>\n" +
				"                  <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"88%\" style=\"width: 88% !important; min-width: 88%; max-width: 88%;\">\n" +
				"                    <tr>\n" +
				"                      <td align=\"center\" valign=\"top\">\n" +
				"                        <div style=\"height: 40px; line-height: 40px; font-size: 38px;\">&nbsp;</div>\n" +
				"                        <a href=\"#\"\n" +
				"                        style=\"display: block; max-width: 192px;\">\n" + "                          <img src=\"https://www.giveitforward.us/img/turquoise_shadow.png\" alt=\"HireClub\" width=\"192\"\n" +
				"                          border=\"0\" style=\"display: block; width: 192px;\" />\n" +
				"                        </a>\n" +
				"                        <div class=\"top_pad2\" style=\"height: 48px; line-height: 48px; font-size: 46px;\">&nbsp;</div>\n" +
				"                      </td>\n" +
				"                    </tr>\n" +
				"                  </table>\n" +
				"                  <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"88%\" style=\"width: 88% !important; min-width: 88%; max-width: 88%;\">\n" +
				"                    <tr>\n" +
				"                      <td align=\"left\" valign=\"top\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#1a1a1a\" style=\"font-size: 52px; line-height: 54px; font-weight: 300; letter-spacing: -1.5px;\">\n" +
				"                              <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 52px; line-height: 54px; font-weight: 300; letter-spacing: -1.5px;\">Confirm Your Email</span>\n" +
				"                           </font>\n" +
				"\n" +
				"                        <div style=\"height: 21px; line-height: 21px; font-size: 19px;\">&nbsp;</div> <font face=\"'Source Sans Pro', sans-serif\" color=\"#000000\" style=\"font-size: 20px; line-height: 28px;\">\n" +
				"                              <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #000000; font-size: 20px; line-height: 28px;\">\n" +
				"                              Welcome " + name + ",\n" +
				"                              </span>\n" +
				"                           </font>\n" +
				"\n" +
				"                        <div style=\"height: 6px; line-height: 6px; font-size: 4px;\">&nbsp;</div> <font face=\"'Source Sans Pro', sans-serif\" color=\"#000000\" style=\"font-size: 20px; line-height: 28px;\">\n" +
				"                              <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #000000; font-size: 20px; line-height: 28px;\">\n" +
				"                                We received a request to set your Give It Forward email to " + email + ".\n" +
				"                                If this is correct, please confirm by clicking the button below.\n" +
				"                              </span>\n" +
				"                           </font>\n" +
				"\n" +
				"                        <div style=\"height: 30px; line-height: 30px; font-size: 28px;\">&nbsp;</div>\n" +
				"                        <table class=\"mob_btn\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\n" +
				"                        style=\"background: #6070E9; border-radius: 4px;\">\n" +
				"                          <tr>\n" +
				"                            <td align=\"center\" valign=\"top\">\n" +
				"                              <a href=\"" + link + "\"\n" +
				"                              target=\"_blank\" style=\"display: block; border: 1px solid #6070E9; border-radius: 4px; padding: 19px 27px; font-family: 'Source Sans Pro', Arial, Verdana, Tahoma, Geneva, sans-serif; color: #ffffff; font-size: 26px; line-height: 30px; text-decoration: none; white-space: nowrap; font-weight: 600;\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#ffffff\" style=\"font-size: 26px; line-height: 30px; text-decoration: none; white-space: nowrap; font-weight: 600;\">\n" +
				"               <span style=\"font-family: 'Source Sans Pro', Arial, Verdana, Tahoma, Geneva, sans-serif; color: #ffffff; font-size: 26px; line-height: 30px; text-decoration: none; white-space: nowrap; font-weight: 600;\">Confirm Email</span>\n" +
				"            </font>\n" +
				"\n" +
				"                              </a>\n" +
				"                            </td>\n" +
				"                          </tr>\n" +
				"                        </table>\n" +
				"                        <div style=\"height: 90px; line-height: 90px; font-size: 88px;\">&nbsp;</div>\n" +
				"                      </td>\n" +
				"                    </tr>\n" +
				"                  </table>\n" +
				"                  <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"90%\" style=\"width: 90% !important; min-width: 90%; max-width: 90%; border-width: 1px; border-style: solid; border-color: #e8e8e8; border-bottom: none; border-left: none; border-right: none;\">\n" +
				"                    <tr>\n" +
				"                      <td align=\"left\" valign=\"top\">\n" +
				"                        <div style=\"height: 28px; line-height: 28px; font-size: 26px;\">&nbsp;</div>\n" +
				"                      </td>\n" +
				"                    </tr>\n" +
				"                  </table>\n" +
				"                  <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"88%\" style=\"width: 88% !important; min-width: 88%; max-width: 88%;\">\n" +
				"                    <tr>\n" +
				"                      <td align=\"left\" valign=\"top\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#7f7f7f\" style=\"font-size: 17px; line-height: 23px;\">\n" +
				"                              <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #7f7f7f; font-size: 17px; line-height: 23px;\">Once you confirm, all future messages about your Give It Forward account will be sent to " + email + ".</span>\n" +
				"                           </font>\n" +
				"\n" +
				"                        <div style=\"height: 30px; line-height: 30px; font-size: 28px;\">&nbsp;</div>\n" +
				"                      </td>\n" +
				"                    </tr>\n" +
				"                  </table>\n" +
				"                  <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"width: 100% !important; min-width: 100%; max-width: 100%; background: #f5f8fa;\">\n" +
				"                    <tbody>\n" +
				"                      <tr>\n" +
				"                        <td align=\"center\" valign=\"top\">\n" +
				"                          <div style=\"height: 34px; line-height: 34px; font-size: 32px;\">&nbsp;</div>\n" +
				"                          <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"88%\" style=\"width: 88% !important; min-width: 88%; max-width: 88%;\">\n" +
				"                            <tbody>\n" +
				"                              <tr>\n" +
				"                                <td align=\"center\" valign=\"top\">\n" +
				"                                  <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"78%\" style=\"min-width: 300px;\">\n" +
				"                                    <tbody>\n" +
				"                                      <tr>\n" +
				"                                        <td align=\"center\" valign=\"top\" width=\"23%\">\n" +
				"                                          <a href=\"mailto:help@hireclub.com\" style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 14px; line-height: 20px; text-decoration: none; white-space: nowrap; font-weight: bold;\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#1a1a1a\" style=\"font-size: 14px; line-height: 20px; text-decoration: none; white-space: nowrap; font-weight: bold;\">\n" +
				"                                    <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 14px; line-height: 20px; text-decoration: none; white-space: nowrap; font-weight: bold;\">HELP</span>\n" +
				"                                 </font>\n" +
				"\n" +
				"                                          </a>\n" +
				"                                        </td>\n" +
				"                                        <td align=\"center\" valign=\"top\" width=\"10%\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#1a1a1a\" style=\"font-size: 17px; line-height: 17px; font-weight: bold;\">\n" +
				"                                 <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 17px; line-height: 17px; font-weight: bold;\">•</span>\n" +
				"                              </font>\n" +
				"\n" +
				"                                        </td>\n" +
				"                                        <td align=\"center\" valign=\"top\" width=\"23%\">\n" +
				"                                          <a href=\"#\"\n" +
				"                                          style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 14px; line-height: 20px; text-decoration: none; white-space: nowrap; font-weight: bold;\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#1a1a1a\" style=\"font-size: 14px; line-height: 20px; text-decoration: none; white-space: nowrap; font-weight: bold;\">\n" +
				"                                    <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 14px; line-height: 20px; text-decoration: none; white-space: nowrap; font-weight: bold;\">SETTINGS</span>\n" +
				"                                 </font>\n" +
				"\n" +
				"                                          </a>\n" +
				"                                        </td>\n" +
				"                                        <td align=\"center\" valign=\"top\" width=\"10%\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#1a1a1a\" style=\"font-size: 17px; line-height: 17px; font-weight: bold;\">\n" +
				"                                    <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 17px; line-height: 17px; font-weight: bold;\">•</span>\n" +
				"                                 </font>\n" +
				"\n" +
				"                                        </td>\n" +
				"                                        <td align=\"center\" valign=\"top\" width=\"23%\">\n" +
				"                                          <a href=\"#\"\n" +
				"                                          style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 14px; line-height: 20px; text-decoration: none; white-space: nowrap; font-weight: bold;\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#1a1a1a\" style=\"font-size: 14px; line-height: 20px; text-decoration: none; white-space: nowrap; font-weight: bold;\">\n" +
				"                                       <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 14px; line-height: 20px; text-decoration: none; white-space: nowrap; font-weight: bold;\">PROFILE</span>\n" +
				"                                    </font>\n" +
				"\n" +
				"                                          </a>\n" +
				"                                        </td>\n" +
				"                                      </tr>\n" + "                                    </tbody>\n" +
				"                                  </table>\n" +
				"                                  <div style=\"height: 34px; line-height: 34px; font-size: 32px;\">&nbsp;</div> <font face=\"'Source Sans Pro', sans-serif\" color=\"#868686\" style=\"font-size: 15px; line-height: 20px;\">\n" +
				"                        <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #868686; font-size: 15px; line-height: 20px;\">\n" +
				"                           Give It Forward\n" + "                           <br>\n" +
				"                           Salt Lake City · UT · 84112</span>\n" +
				"                     </font>\n" +
				"\n" +
				"                                  <div style=\"height: 4px; line-height: 4px; font-size: 2px;\">&nbsp;</div>\n" +
				"                                  <div style=\"height: 3px; line-height: 3px; font-size: 1px;\">&nbsp;</div>\n" +
				"                                  <!-- <font face=\"'Source Sans Pro', sans-serif\" color=\"#1a1a1a\" style=\"font-size:\n" +
				"                                  17px; line-height: 20px;\">\n" +
				"                        <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 17px; line-height: 20px;\"><a href=\"mailto:help@hireclub.com\" style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 17px; line-height: 20px; text-decoration: none;\">help@hireclub.com</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"#\" target=\"_blank\" style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 17px; line-height: 20px; text-decoration: none;\">1(800)232-90-26</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"#\" target=\"_blank\" style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 17px; line-height: 20px; text-decoration: none;\">Unsubscribe</a></span>\n" +
				"                     </font> \n" +
				"\n" +
				"                     <div style=\"height: 35px; line-height: 35px; font-size: 33px;\">&nbsp;</div>\n" +
				"\n" +
				"                     <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
				"                        <tbody><tr>\n" +
				"                           <td align=\"center\" valign=\"top\">\n" +
				"                              <a href=\"#\" target=\"_blank\" style=\"display: block; max-width: 19px;\">\n" +
				"                                 <img src=\"images/soc_1.png\" alt=\"img\" width=\"19\" border=\"0\" style=\"display: block; width: 19px;\">\n" +
				"                              </a>\n" +
				"                           </td>\n" +
				"                           <td width=\"45\" style=\"width: 45px; max-width: 45px; min-width: 45px;\">&nbsp;</td>\n" +
				"                           <td align=\"center\" valign=\"top\">\n" +
				"                              <a href=\"#\" target=\"_blank\" style=\"display: block; max-width: 18px;\">\n" +
				"                                 <img src=\"images/soc_2.png\" alt=\"img\" width=\"18\" border=\"0\" style=\"display: block; width: 18px;\">\n" +
				"                              </a>\n" +
				"                           </td>\n" +
				"                           <td width=\"45\" style=\"width: 45px; max-width: 45px; min-width: 45px;\">&nbsp;</td>\n" +
				"                           <td align=\"center\" valign=\"top\">\n" +
				"                              <a href=\"#\" target=\"_blank\" style=\"display: block; max-width: 21px;\">\n" +
				"                                 <img src=\"images/soc_3.png\" alt=\"img\" width=\"21\" border=\"0\" style=\"display: block; width: 21px;\">\n" +
				"                              </a>\n" +
				"                           </td>\n" +
				"                           <td width=\"45\" style=\"width: 45px; max-width: 45px; min-width: 45px;\">&nbsp;</td>\n" +
				"                           <td align=\"center\" valign=\"top\">\n" +
				"                              <a href=\"#\" target=\"_blank\" style=\"display: block; max-width: 25px;\">\n" +
				"                                 <img src=\"images/soc_4.png\" alt=\"img\" width=\"25\" border=\"0\" style=\"display: block; width: 25px;\">\n" +
				"                              </a>\n" +
				"                           </td>\n" +
				"                        </tr>\n" +
				"                     </tbody></table>\n" +
				"                     -->\n" +
				"                                  <div style=\"height: 35px; line-height: 35px; font-size: 33px;\">&nbsp;</div>\n" +
				"                                </td>\n" +
				"                              </tr>\n" +
				"                            </tbody>\n" +
				"                          </table>\n" +
				"                        </td>\n" +
				"                      </tr>\n" +
				"                    </tbody>\n" +
				"                  </table>\n" +
				"                </td>\n" +
				"                <td class=\"mob_pad\" width=\"25\" style=\"width: 25px; max-width: 25px; min-width: 25px;\">&nbsp;</td>\n" +
				"              </tr>\n" +
				"            </table>\n" +
				"            <!--[if (gte mso 9)|(IE)]>\n" +
				"            </td>\n" +
				"          </tr>\n" +
				"        </table>\n" +
				"      <![endif]-->\n" +
				"    </td>\n" +
				"  </tr>\n" +
				"</table>\n";

		String emailSubject = "Give it Forward, confirm email";
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

    public static User confirmEmail(String hash){

        EmailCode ec = confirmHash(hash);
        if(ec == null){
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
