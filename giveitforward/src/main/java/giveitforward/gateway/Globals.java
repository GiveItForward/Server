package giveitforward.gateway;

public class Globals {
	public static String emailHtml(String link, String name, String title, String body, String buttonText, String footnotes){
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
			"                              <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #1a1a1a; font-size: 52px; line-height: 54px; font-weight: 300; letter-spacing: -1.5px;\">" +
				title + "</span>\n" +
			"                           </font>\n" +
			"\n" +
			"                        <div style=\"height: 21px; line-height: 21px; font-size: 19px;\">&nbsp;</div> <font face=\"'Source Sans Pro', sans-serif\" color=\"#000000\" style=\"font-size: 20px; line-height: 28px;\">\n" +
			"                              <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #000000; font-size: 20px; line-height: 28px;\">\n" +
			"                              Welcome " +
				name + ",\n" +
			"                              </span>\n" +
			"                           </font>\n" +
			"\n" +
			"                        <div style=\"height: 6px; line-height: 6px; font-size: 4px;\">&nbsp;</div> <font face=\"'Source Sans Pro', sans-serif\" color=\"#000000\" style=\"font-size: 20px; line-height: 28px;\">\n" +
			"                              <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #000000; font-size: 20px; line-height: 28px;\">\n" +
			"                                "+
				body + "</span>\n" +
			"                           </font>\n" +
			"\n" +
			"                        <div style=\"height: 30px; line-height: 30px; font-size: 28px;\">&nbsp;</div>\n" +
			"                        <table class=\"mob_btn\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\n" +
			"                        style=\"background: #6070E9; border-radius: 4px;\">\n" +
			"                          <tr>\n" +
			"                            <td align=\"center\" valign=\"top\">\n" +
			"                              <a href=\"" +
				link + "\"\n" +
			"                              target=\"_blank\" style=\"display: block; border: 1px solid #6070E9; border-radius: 4px; padding: 19px 27px; font-family: 'Source Sans Pro', Arial, Verdana, Tahoma, Geneva, sans-serif; color: #ffffff; font-size: 26px; line-height: 30px; text-decoration: none; white-space: nowrap; font-weight: 600;\"> <font face=\"'Source Sans Pro', sans-serif\" color=\"#ffffff\" style=\"font-size: 26px; line-height: 30px; text-decoration: none; white-space: nowrap; font-weight: 600;\">\n" +
			"               <span style=\"font-family: 'Source Sans Pro', Arial, Verdana, Tahoma, Geneva, sans-serif; color: #ffffff; font-size: 26px; line-height: 30px; text-decoration: none; white-space: nowrap; font-weight: 600;\">"+
				buttonText +"</span>\n" +
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
			"                              <span style=\"font-family: 'Source Sans Pro', Arial, Tahoma, Geneva, sans-serif; color: #7f7f7f; font-size: 17px; line-height: 23px;\">" +
				footnotes + ".</span>\n" +
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
			return htmlBody;
			}
}

