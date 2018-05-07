package me.metlabnews.Model.Common.Mail;

public class ResponsiveHTMLMessage
{

	// TODO: get string variables to fill the E-Mail Content
	private String m_receiverName = ""; // pseudocode: Class.getInstance().getReceiverName();
	private String m_content      = ""; // pseudocode: Class.getInstance().getContent();
	// ....
	//

	public static final String HTMLMessage =
			"<!doctype html>\n" +
					"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
					"<head>\n" +
					"<!--\n" +
					"    This template is provided for free of charge by Email on Acid, LLC.\n" +
					"\n" +
					"    Every email client is different. See exactly how your email looks in the most popular inboxes, so you can fix any problems before you hit send.\n" +
					"    https://www.emailonacid.com/\n" +
					"    -->\n" +
					"\n" +
					"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
					"\n" +
					"<title>Responsive Email Template</title>\n" +
					"\n" +
					"<style type=\"text/css\">\n" +
					"\t.ReadMsgBody {width: 100%; background-color: #ffffff;}\n" +
					"\t.ExternalClass {width: 100%; background-color: #ffffff;}\n" +
					"\tbody\t {width: 100%; background-color: #ffffff; margin:0; padding:0; -webkit-font-smoothing: antialiased;font-family: Georgia, Times, serif}\n" +
					"\ttable {border-collapse: collapse;}\n" +
					"\n" +
					"\t@media only screen and (max-width: 640px)  {\n" +
					"\t\t\t\t\t.deviceWidth {width:440px!important; padding:0;}\n" +
					"\t\t\t\t\t.center {text-align: center!important;}\n" +
					"\t\t\t}\n" +
					"\n" +
					"\t@media only screen and (max-width: 479px) {\n" +
					"\t\t\t\t\t.deviceWidth {width:280px!important; padding:0;}\n" +
					"\t\t\t\t\t.center {text-align: center!important;}\n" +
					"\t\t\t}\n" +
					"\n" +
					"</style>\n" +
					"</head>\n" +
					"\n" +
					"<body leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\" style=\"font-family: Georgia, Times, serif\">\n" +
					"\n" +
					"<!-- Wrapper -->\n" +
					"<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\n" +
					"\t<tr>\n" +
					"\t\t<td width=\"100%\" valign=\"top\" bgcolor=\"#ffffff\" style=\"padding-top:20px\">\n" +
					"\n" +
					"\t\t\t<!-- Start Header-->\n" +
					"\t\t\t<table width=\"580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"deviceWidth\" style=\"margin:0 auto;\">\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td width=\"100%\" bgcolor=\"#ffffff\">\n" +
					"\n" +
					"                            <!-- Logo -->\n" +
					"                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" class=\"deviceWidth\">\n" +
					"                                <tr>\n" +
					"                                    <td style=\"padding:10px 20px\" class=\"center\">\n" +
					"                                        <a href=\"#\"><img src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/logo2.jpg\" alt=\"\" border=\"0\" /></a>\n" +
					"                                    </td>\n" +
					"                                </tr>\n" +
					"                            </table><!-- End Logo -->\n" +
					"\n" +
					"                            <!-- Nav -->\n" +
					"                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"right\" class=\"deviceWidth\">\n" +
					"                                <tr>\n" +
					"                                    <td class=\"center\" style=\"font-size: 13px; color: #272727; font-weight: light; text-align: right; font-family: Georgia, Times, serif; line-height: 20px; vertical-align: middle; padding:10px 20px; font-style:italic\">\n" +
					"                                        <a href=\"#\" style=\"text-decoration: none; color: #3b3b3b;\">Products</a>\n" +
					"                                        &nbsp;&nbsp;&nbsp;\n" +
					"                                        <a href=\"#\" style=\"text-decoration: none; color: #3b3b3b;\">Services</a>\n" +
					"                                        &nbsp;&nbsp;&nbsp;\n" +
					"                                        <a href=\"#\" style=\"text-decoration: none; color: #3b3b3b;\">Pricing</a>\n" +
					"                                        &nbsp;&nbsp;&nbsp;\n" +
					"                                        <a href=\"#\" style=\"text-decoration: none; color: #3b3b3b;\">Contact</a>\n" +
					"                                    </td>\n" +
					"                                </tr>\n" +
					"                            </table><!-- End Nav -->\n" +
					"\n" +
					"\t\t\t\t\t</td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t</table><!-- End Header -->\n" +
					"\n" +
					"\t\t\t<!-- One Column -->\n" +
					"\t\t\t<table width=\"580\"  class=\"deviceWidth\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" bgcolor=\"#eeeeed\" style=\"margin:0 auto;\">\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td valign=\"top\" style=\"padding:0\" bgcolor=\"#ffffff\">\n" +
					"\t\t\t\t\t\t<a href=\"#\"><img  class=\"deviceWidth\" src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/free_template_1/headliner.jpg\" alt=\"\" border=\"0\" style=\"display: block; border-radius: 4px;\" /></a>\n" +
					"\t\t\t\t\t</td>\n" +
					"\t\t\t\t</tr>\n" +
					"                <tr>\n" +
					"                    <td style=\"font-size: 13px; color: #959595; font-weight: normal; text-align: left; font-family: Georgia, Times, serif; line-height: 24px; vertical-align: top; padding:10px 8px 10px 8px\" bgcolor=\"#eeeeed\">\n" +
					"\n" +
					"                        <table>\n" +
					"                            <tr>\n" +
					"                                <td valign=\"top\" style=\"padding:0 10px 10px 0\">\n" +
					"                                    <img  src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/free_template_1/1.jpg\" alt=\"\" border=\"0\" align=\"left\" />\n" +
					"                                </td>\n" +
					"                                <td valign=\"middle\" style=\"padding:0 10px 10px 0\"><a href=\"#\" style=\"text-decoration: none; color: #272727; font-size: 16px; color: #272727; font-weight: bold; font-family:Arial, sans-serif \">One Column</a>\n" +
					"                                </td>\n" +
					"                            </tr>\n" +
					"                        </table>\n" +
					"\n" +
					"\t\t\t\t\t\tLorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. <b style=\"color: #5b5b5b;\">Ut enim ad minim veniam, quis nostrud.</b> Exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
					"                    </td>\n" +
					"                </tr>\n" +
					"\t\t\t</table><!-- End One Column -->\n" +
					"\n" +
					"\n" +
					"<div style=\"height:15px;margin:0 auto;\">&nbsp;</div><!-- spacer -->\n" +
					"\n" +
					"\n" +
					"            <!-- 2 Column Images & Text Side by SIde -->\n" +
					"            <table width=\"580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"deviceWidth\" bgcolor=\"#202022\" style=\"margin:0 auto;\">\n" +
					"                <tr>\n" +
					"                    <td style=\"padding:10px 0\">\n" +
					"                            <table align=\"left\" width=\"49%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"deviceWidth\">\n" +
					"                                <tr>\n" +
					"                                    <td valign=\"top\" align=\"center\" class=\"center\" style=\"padding-top:20px\">\n" +
					"                                            <a href=\"#\"><img width=\"267\" src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/responsive4.jpg\" alt=\"\" border=\"0\" style=\"border-radius: 4px; width: 267px; display: block;\" class=\"deviceWidth\" /></a>\n" +
					"                                    </td>\n" +
					"                                </tr>\n" +
					"                            </table>\n" +
					"                            <table align=\"right\" width=\"49%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"deviceWidth\">\n" +
					"                                <tr>\n" +
					"                                    <td style=\"font-size: 12px; color: #959595; font-weight: normal; text-align: left; font-family: Georgia, Times, serif; line-height: 24px; vertical-align: top; padding:10px 8px 10px 8px\">\n" +
					"\n" +
					"                                        <table>\n" +
					"                                            <tr>\n" +
					"                                                <td valign=\"top\" style=\"padding:0 10px 10px 5px\">\n" +
					"                                                    <img  src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/free_template_1/2.jpg\" alt=\"\" border=\"0\" align=\"left\" />\n" +
					"                                                </td>\n" +
					"                                                <td valign=\"middle\" style=\"padding:0 10px 10px 0\"><a href=\"#\" style=\"text-decoration: none; font-size: 16px; color: #ccc; font-weight: bold; font-family:Arial, sans-serif \">Two column - text right</a>\n" +
					"                                                </td>\n" +
					"                                            </tr>\n" +
					"                                        </table>\n" +
					"\n" +
					"                                        <p style=\"mso-table-lspace:0;mso-table-rspace:0; margin:0\">\n" +
					"                                            Sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi.\n" +
					"                                            <br/><br/>\n" +
					"\n" +
					"                                            <table width=\"100\" align=\"right\">\n" +
					"                                                <tr>\n" +
					"                                                    <td background=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/free_template_1/blue_back.jpg\" bgcolor=\"#409ea8\" style=\"padding:5px 0;background-color:#409ea8; border-top:1px solid #77d5ea; background-repeat:repeat-x\" align=\"center\">\n" +
					"                                                        <a href=\"\"\n" +
					"                                                        style=\"\n" +
					"                                                        color:#ffffff;\n" +
					"                                                        font-size:13px;\n" +
					"                                                        font-weight:bold;\n" +
					"                                                        text-align:center;\n" +
					"                                                        text-decoration:none;\n" +
					"                                                        font-family:Arial, sans-serif;\n" +
					"                                                        -webkit-text-size-adjust:none;\">\n" +
					"                                                                Read More\n" +
					"                                                        </a>\n" +
					"\n" +
					"                                                    </td>\n" +
					"                                                </tr>\n" +
					"                                            </table>\n" +
					"\n" +
					"                                        </p>\n" +
					"                                    </td>\n" +
					"                                </tr>\n" +
					"                            </table>\n" +
					"\n" +
					"                    </td>\n" +
					"                </tr>\n" +
					"                <tr>\n" +
					"                    <td bgcolor=\"#fe7f00\"><div style=\"height:6px\">&nbsp;</div></td>\n" +
					"                </tr>\n" +
					"            </table><!-- End 2 Column Images & Text Side by SIde -->\n" +
					"\n" +
					"<div style=\"height:15px;margin:0 auto;\">&nbsp;</div><!-- spacer -->\n" +
					"\n" +
					"\n" +
					"\t\t\t<!-- Two Column (Images Stacked over Text) -->\n" +
					"\t\t\t<table width=\"580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"deviceWidth\" bgcolor=\"#eeeeed\" style=\"margin:0 auto;\">\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td class=\"center\" style=\"padding:10px 0 0 5px\">\n" +
					"\n" +
					"                        <table width=\"49%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" class=\"deviceWidth\">\n" +
					"                            <tr>\n" +
					"                                <td align=\"center\">\n" +
					"                                \t<!-- The paragraph tag is important here to ensure that this table floats properly in Outlook 2007, 2010, and 2013\n" +
					"                                    To learn more about this fix check out this link: https://www.emailonacid.com/blog/details/C13/removing_unwanted_spacing_or_gaps_between_tables_in_outlook_2007_2010\n" +
					"                                    This fix is used for all floating tables in this responsive template\n" +
					"                                    The margin set to 0 is for Gmail -->\n" +
					"                                    <p style=\"mso-table-lspace:0;mso-table-rspace:0; margin:0\"><a href=\"#\"><img width=\"267\" src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/responsive7.jpg\" alt=\"\" border=\"0\" style=\"border-radius: 4px; width: 267px\" class=\"deviceWidth\" /></a></p>\n" +
					"                                </td>\n" +
					"                            </tr>\n" +
					"                            <tr>\n" +
					"                                <td style=\"font-size: 12px; color: #959595; font-weight: normal; text-align: left; font-family: Georgia, Times, serif; line-height: 24px; vertical-align: top; padding:10px 8px 10px 8px\">\n" +
					"\n" +
					"                                        <table style=\"border-bottom: 1px solid #333\">\n" +
					"                                            <tr>\n" +
					"                                                <td valign=\"top\" style=\"padding:0 10px 10px 0\">\n" +
					"                                                    <img  src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/free_template_1/3.jpg\" alt=\"\" border=\"0\" align=\"left\" />\n" +
					"                                                </td>\n" +
					"                                                <td valign=\"middle\" style=\"padding:0 10px 10px 0\"><a href=\"#\" style=\"text-decoration: none; font-size: 16px; color: #363636; font-weight: bold; font-family:Arial, sans-serif \">Two column - text below</a>\n" +
					"                                                </td>\n" +
					"                                            </tr>\n" +
					"                                        </table>\n" +
					"                                     <p>Velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident.</p>\n" +
					"                                </td>\n" +
					"                            </tr>\n" +
					"                        </table>\n" +
					"\n" +
					"                        <table width=\"49%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" class=\"deviceWidth\">\n" +
					"                            <tr>\n" +
					"                                <td align=\"center\">\n" +
					"                                    <p style=\"mso-table-lspace:0;mso-table-rspace:0; margin:0\"><a href=\"#\"><img width=\"267\" src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/responsive5.jpg\" alt=\"\" border=\"0\" style=\"border-radius: 4px; width: 267px\" class=\"deviceWidth\" /></a></p>\n" +
					"                                </td>\n" +
					"                            </tr>\n" +
					"                            <tr>\n" +
					"                                <td style=\"font-size: 12px; color: #959595; font-weight: normal; text-align: left; font-family: Georgia, Times, serif; line-height: 24px; vertical-align: top; padding:10px 8px 10px 8px\">\n" +
					"\n" +
					"                                        <table style=\"border-bottom: 1px solid #333\">\n" +
					"                                            <tr>\n" +
					"                                                <td valign=\"top\" style=\"padding:0 10px 10px 0\">\n" +
					"                                                    <img  src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/free_template_1/4.jpg\" alt=\"\" border=\"0\" align=\"left\" />\n" +
					"                                                </td>\n" +
					"                                                <td valign=\"middle\" style=\"padding:0 10px 10px 0\"><a href=\"#\" style=\"text-decoration: none; font-size: 16px; color: #363636; font-weight: bold; font-family:Arial, sans-serif \">Two column - text below</a>\n" +
					"                                                </td>\n" +
					"                                            </tr>\n" +
					"                                        </table>\n" +
					"\n" +
					"                                     <p>Iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis.</p>\n" +
					"                                </td>\n" +
					"                            </tr>\n" +
					"                        </table>\n" +
					"\n" +
					"\t\t\t\t\t</td>\n" +
					"\t\t\t\t</tr>\n" +
					"\t\t\t</table><!-- End Two Column (Images Stacked over Text) -->\n" +
					"\n" +
					"\n" +
					"<div style=\"height:15px;margin:0 auto;\">&nbsp;</div><!-- spacer -->\n" +
					"\n" +
					"\t\t\t<!-- Dark Background, Three Column Images -->\n" +
					"\n" +
					"\n" +
					"\n" +
					"\t\t\t\t\t\t<!-- 3 Small Images -->\n" +
					"\t\t\t\t\t\t<table width=\"580\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"deviceWidth\" bgcolor=\"#202022\" style=\"margin:0 auto;\">\n" +
					"\n" +
					"                            <tr>\n" +
					"                                <td style=\"padding:10px 0 0 10px\">\n" +
					"                                        <table>\n" +
					"                                            <tr>\n" +
					"                                                <td valign=\"top\" style=\"padding:0 10px 10px 0\">\n" +
					"                                                    <img  src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/free_template_1/5.jpg\" alt=\"\" border=\"0\" align=\"left\" />\n" +
					"                                                </td>\n" +
					"                                                <td valign=\"middle\" style=\"padding:0 10px 10px 0\"><a href=\"#\" style=\"text-decoration: none; font-size: 16px; color: #eee; font-weight: bold; font-family:Arial, sans-serif \">Three column - text below</a>\n" +
					"                                                </td>\n" +
					"                                            </tr>\n" +
					"                                        </table>\n" +
					"                                 </td>\n" +
					"                            </tr>\n" +
					"\n" +
					"\t\t\t\t\t\t\t<tr>\n" +
					"\t\t\t\t\t\t\t\t<td valign=\"top\">\n" +
					"\n" +
					"                                    <table width=\"32%\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\" class=\"deviceWidth\">\n" +
					"                                        <tr>\n" +
					"                                            <td valign=\"top\" align=\"center\" style=\"padding:10px 0\">\n" +
					"                                                    <p style=\"mso-table-lspace:0;mso-table-rspace:0; margin:0\"><a href=\"#\"><img width=\"170\" src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/responsive18.jpg\" alt=\"\" border=\"0\" style=\"border-radius: 4px; width: 170px;\" class=\"deviceWidth\" /></a></p>\n" +
					"                                            </td>\n" +
					"                                        </tr>\n" +
					"                                        <tr>\n" +
					"                                            <td style=\"padding:0 10px 20px 10px\">\n" +
					"                                                <a href=\"#\" style=\"text-decoration: none; font-size: 14px; color: #eee; font-weight: bold; font-family:Arial, sans-serif \">Milk Headline</a>\n" +
					"                                                <p style=\"color:#ddd; text-align:left; font-size: 10px; line-height:17px\">Iste natus error sit volu ptatem accusa tium dolor emque lauda ntium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis.</p>\n" +
					"\n" +
					"                                                    <table width=\"90\" align=\"left\">\n" +
					"                                                        <tr>\n" +
					"                                                            <td bgcolor=\"#c8c8ca\" style=\"padding:2px 0 5px 0;background-color:#c8c8ca; border-top:1px solid #eee; background-repeat:repeat-x\" align=\"center\">\n" +
					"                                                                <a href=\"\"\n" +
					"                                                                style=\"\n" +
					"                                                                color:#333;\n" +
					"                                                                font-size:12px;\n" +
					"                                                                font-weight:bold;\n" +
					"                                                                text-align:center;\n" +
					"                                                                text-decoration:none;\n" +
					"                                                                font-family:Arial, sans-serif;\n" +
					"                                                                -webkit-text-size-adjust:none;\">\n" +
					"                                                                        Read More\n" +
					"                                                                </a>\n" +
					"\n" +
					"                                                            </td>\n" +
					"                                                        </tr>\n" +
					"                                                    </table>\n" +
					"                                            </td>\n" +
					"                                        </tr>\n" +
					"                                    </table><!-- End Image 1 -->\n" +
					"\n" +
					"\t\t\t\t\t\t\t\t\t<table width=\"32%\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\" class=\"deviceWidth\">\n" +
					"\t\t\t\t\t\t\t\t\t\t<tr>\n" +
					"\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" align=\"center\" style=\"padding:10px 0\">\n" +
					"\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"mso-table-lspace:0;mso-table-rspace:0; margin:0\"><a href=\"#\"><img width=\"170\" src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/responsive20.jpg\" alt=\"\" border=\"0\" style=\"border-radius: 4px; width: 170px;\" class=\"deviceWidth\" /></a></p>\n" +
					"\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
					"\t\t\t\t\t\t\t\t\t\t</tr>\n" +
					"                                        <tr>\n" +
					"                                            <td style=\"padding:0 10px 20px 10px\">\n" +
					"                                                <a href=\"#\" style=\"text-decoration: none; font-size: 14px; color: #eee; font-weight: bold; font-family:Arial, sans-serif \">Bon Bon Headline</a>\n" +
					"                                                <p style=\"color:#ddd; text-align:left; font-size: 10px; line-height:17px\">Iste natus error sit volu ptatem accusa tium dolor emque lauda ntium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis.</p>\n" +
					"\n" +
					"                                                    <table width=\"90\" align=\"left\">\n" +
					"                                                        <tr>\n" +
					"                                                            <td bgcolor=\"#c8c8ca\" style=\"padding:2px 0 5px 0;background-color:#c8c8ca; border-top:1px solid #eee; background-repeat:repeat-x\" align=\"center\">\n" +
					"                                                                <a href=\"\"\n" +
					"                                                                style=\"\n" +
					"                                                                color:#333;\n" +
					"                                                                font-size:12px;\n" +
					"                                                                font-weight:bold;\n" +
					"                                                                text-align:center;\n" +
					"                                                                text-decoration:none;\n" +
					"                                                                font-family:Arial, sans-serif;\n" +
					"                                                                -webkit-text-size-adjust:none;\">\n" +
					"                                                                        Read More\n" +
					"                                                                </a>\n" +
					"\n" +
					"                                                            </td>\n" +
					"                                                        </tr>\n" +
					"                                                    </table>\n" +
					"                                            </td>\n" +
					"                                        </tr>\n" +
					"\t\t\t\t\t\t\t\t\t</table><!-- End Image 2 -->\n" +
					"\n" +
					"\n" +
					"                                    <table width=\"32%\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\" class=\"deviceWidth\">\n" +
					"                                        <tr>\n" +
					"                                            <td valign=\"top\" align=\"center\" style=\"padding:10px 0\">\n" +
					"                                                    <p style=\"mso-table-lspace:0;mso-table-rspace:0; margin:0\"><a href=\"#\"><img width=\"170\" src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/free_template_1/360.jpg\" alt=\"\" border=\"0\" style=\"border-radius: 4px; width: 170px\" class=\"deviceWidth\" /></a></p>\n" +
					"                                            </td>\n" +
					"                                        </tr>\n" +
					"                                        <tr>\n" +
					"                                            <td style=\"padding:0 10px 20px 10px\">\n" +
					"                                                <a href=\"#\" style=\"text-decoration: none; font-size: 14px; color: #eee; font-weight: bold; font-family:Arial, sans-serif \">360 Headline</a>\n" +
					"                                                <p style=\"color:#ddd; text-align:left; font-size: 10px; line-height:17px\">Iste natus error sit volu ptatem accusa tium dolor emque lauda ntium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis.</p>\n" +
					"\n" +
					"                                                    <table width=\"90\" align=\"left\">\n" +
					"                                                        <tr>\n" +
					"                                                            <td bgcolor=\"#c8c8ca\" style=\"padding:2px 0 5px 0;background-color:#c8c8ca; border-top:1px solid #eee; background-repeat:repeat-x\" align=\"center\">\n" +
					"                                                                <a href=\"\"\n" +
					"                                                                style=\"\n" +
					"                                                                color:#333;\n" +
					"                                                                font-size:12px;\n" +
					"                                                                font-weight:bold;\n" +
					"                                                                text-align:center;\n" +
					"                                                                text-decoration:none;\n" +
					"                                                                font-family:Arial, sans-serif;\n" +
					"                                                                -webkit-text-size-adjust:none;\">\n" +
					"                                                                        Read More\n" +
					"                                                                </a>\n" +
					"\n" +
					"                                                            </td>\n" +
					"                                                        </tr>\n" +
					"                                                    </table>\n" +
					"                                            </td>\n" +
					"                                        </tr>\n" +
					"                                    </table><!-- End Image 3 -->\n" +
					"\n" +
					"\t\t\t\t\t\t\t\t</td>\n" +
					"\t\t\t\t\t\t\t</tr>\n" +
					"\n" +
					"                            <tr>\n" +
					"                                <td bgcolor=\"#bdcc50\"><div style=\"height:6px\">&nbsp;</div></td>\n" +
					"                            </tr>\n" +
					"\t\t\t\t\t\t</table><!-- End 3 Small Images -->\n" +
					"                <!-- Dark Background, Three Column Images -->\n" +
					"\n" +
					"<div style=\"height:15px;margin:0 auto;\">&nbsp;</div><!-- spacer -->\n" +
					"\n" +
					"\t\t\t<!-- 2 Column Images - text left -->\n" +
					"\t\t\t<table width=\"580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"deviceWidth\" bgcolor=\"#eeeeed\" style=\"margin:0 auto;\">\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td style=\"padding:10px 0\">\n" +
					"                            <table align=\"right\" width=\"49%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"deviceWidth\">\n" +
					"                                <tr>\n" +
					"                                    <td valign=\"top\" align=\"right\" class=\"center\" style=\"padding:20px 10px 0 0\">\n" +
					"\t\t\t\t\t\t\t\t\t\t<p style=\"mso-table-lspace:0;mso-table-rspace:0; margin:0\"><a href=\"#\"><img width=\"267\" src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/responsive3.jpg\" alt=\"\" border=\"0\" style=\"border-radius: 4px; width: 267px; display: block;\" class=\"deviceWidth\" /></a></p>\n" +
					"                                    </td>\n" +
					"                                </tr>\n" +
					"                            </table>\n" +
					"                            <table align=\"left\" width=\"49%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"deviceWidth\">\n" +
					"                                <tr>\n" +
					"                                    <td style=\"font-size: 13px; color: #959595; font-weight: normal; text-align: left; font-family: Georgia, Times, serif; line-height: 24px; vertical-align: top; padding:20px 0 20px 15px\">\n" +
					"\n" +
					"                                        <table>\n" +
					"                                            <tr>\n" +
					"                                                <td valign=\"top\" style=\"padding:0 10px 15px 0\">\n" +
					"                                                    <img  src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/free_template_1/6.jpg\" alt=\"\" border=\"0\" align=\"left\" />\n" +
					"                                                </td>\n" +
					"                                                <td valign=\"middle\" style=\"padding:0 10px 10px 0\"><a href=\"#\" style=\"text-decoration: none; font-size: 16px; color: #363636; font-weight: bold; font-family:Arial, sans-serif \">Two column - text left</a>\n" +
					"                                                </td>\n" +
					"                                            </tr>\n" +
					"                                        </table>\n" +
					"\n" +
					"                                        <p style=\"mso-table-lspace:0;mso-table-rspace:0; margin:0\">\n" +
					"                                            Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores.\n" +
					"                                            <br/><br/>\n" +
					"\n" +
					"                                        </p>\n" +
					"                                    </td>\n" +
					"                                </tr>\n" +
					"                            </table>\n" +
					"                    </td>\n" +
					"                </tr>\n" +
					"            </table><!-- End 2 Column Images  - text left -->\n" +
					"\n" +
					"\n" +
					"\n" +
					"<div style=\"height:25px;margin:0 auto;\">&nbsp;</div><!-- spacer -->\n" +
					"\n" +
					"            <!-- Blog Headlines -->\n" +
					"            <table width=\"580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"deviceWidth\" style=\"margin:0 auto;\">\n" +
					"\n" +
					"                <tr>\n" +
					"                    <td valign=\"top\">\n" +
					"                        <table>\n" +
					"                            <tr>\n" +
					"                                <td bgcolor=\"#3c707e\" style=\"color:#fff;font-size:12px;font-weight:bold;text-align:center;-webkit-text-size-adjust:none;padding:4px 8px;\">4.12.13</td>\n" +
					"                            </tr>\n" +
					"                        </table>\n" +
					"                    </td>\n" +
					"                    <td style=\"font-size:11px;color:#656565;text-align:left;padding-left:8px;\" valign=\"middle\">\n" +
					"                        <a href=\"https://www.emailonacid.com/blog/details/C13/emailology_vector_markup_language_and_backgrounds\" style=\"font-size:14px;font-weight:bold;color:#000000;text-align:left;text-decoration:none;\">Vector Markup Language and Backgrounds in Outlook 2007+</a><br />\n" +
					"                        Get your background to show up in flexible screen widths in Outlook.\n" +
					"                    </td>\n" +
					"                </tr>\n" +
					"                <tr><td>&nbsp;</td></tr>\n" +
					"                <tr>\n" +
					"                    <td valign=\"top\">\n" +
					"                        <table>\n" +
					"                            <tr>\n" +
					"                                <td bgcolor=\"#3c707e\" style=\"color:#fff;font-size:12px;font-weight:bold;text-align:center;-webkit-text-size-adjust:none;padding:4px 8px;\">4.02.13</td>\n" +
					"                            </tr>\n" +
					"                        </table>\n" +
					"                    </td>\n" +
					"                    <td style=\"font-size:11px;color:#656565;text-align:left;padding-left:8px;\" valign=\"middle\">\n" +
					"                        <a href=\"https://www.emailonacid.com/blog/details/C13/emailology_firing_blanks_with_outlook.com\" style=\"text-decoration:none;font-size:14px;font-weight:bold;color:#000000;text-align:left;\">Firing Blanks with Outlook.com</a><br />\n" +
					"                        Is your email showing up as a white screen in Outlook.com?  Here&#8217;s why.\n" +
					"                    </td>\n" +
					"                </tr>\n" +
					"                <tr><td>&nbsp;</td></tr>\n" +
					"                <tr>\n" +
					"                    <td valign=\"top\">\n" +
					"                        <table>\n" +
					"                            <tr>\n" +
					"                                <td bgcolor=\"#3c707e\" style=\"color:#fff;font-size:12px;font-weight:bold;text-align:center;-webkit-text-size-adjust:none;padding:4px 8px;\">3.27.13</td>\n" +
					"                            </tr>\n" +
					"                        </table>\n" +
					"                    </td>\n" +
					"                    <td style=\"font-size:11px;color:#656565;text-align:left;padding-left:8px;\" valign=\"middle\">\n" +
					"                        <a href=\"https://www.emailonacid.com/blog/details/C13/emailology_viewport_metatag_rendered_unusable\" style=\"text-decoration:none;font-size:14px;font-weight:bold;color:#000000;text-align:left;\">Viewport Meta Tag Rendered Unusable</a><br />\n" +
					"                        Is your email showing blank on the BlackBerry?  It might be your viewport tag...\n" +
					"                    </td>\n" +
					"                </tr>\n" +
					"                <tr><td>&nbsp;</td></tr>\n" +
					"                <tr>\n" +
					"                    <td valign=\"top\">\n" +
					"                        <table>\n" +
					"                            <tr>\n" +
					"                                <td bgcolor=\"#3c707e\" style=\"color:#fff;font-size:12px;font-weight:bold;text-align:center;-webkit-text-size-adjust:none;padding:4px 8px;\">3.25.13</td>\n" +
					"                            </tr>\n" +
					"                        </table>\n" +
					"                    </td>\n" +
					"                    <td style=\"font-size:11px;color:#656565;text-align:left;padding-left:8px;\" valign=\"middle\">\n" +
					"                        <a href=\"https://www.emailonacid.com/blog/details/C13/emailology_media_queries_demystified_min-width_and_max-width\" style=\"text-decoration:none;font-size:14px;font-weight:bold;color:#000000;text-align:left;\">Media Queries Demystified: Min-Width and Max-Width</a><br />\n" +
					"                        More complicated than they seem? Let&#8217;s break down MQ&#8217;s for beginners.\n" +
					"                    </td>\n" +
					"                </tr>\n" +
					"                <tr><td>&nbsp;</td></tr>\n" +
					"                <tr>\n" +
					"                    <td valign=\"top\">\n" +
					"                        <table>\n" +
					"                            <tr>\n" +
					"                                <td bgcolor=\"#3c707e\" style=\"color:#fff;font-size:12px;font-weight:bold;text-align:center;-webkit-text-size-adjust:none;padding:4px 8px;\">3.21.13</td>\n" +
					"                            </tr>\n" +
					"                        </table>\n" +
					"                    </td>\n" +
					"                    <td style=\"font-size:11px;color:#656565;text-align:left;padding-left:8px;\" valign=\"middle\">\n" +
					"                        <a href=\"https://www.emailonacid.com/blog/details/C13/emailology_media_queries_and_device_orientation\" style=\"text-decoration:none;font-size:14px;font-weight:bold;color:#000000;text-align:left;\">Media Queries and Device Orientation</a><br />\n" +
					"                        Identify your breakpoints with our research on the most popular device sizes...\n" +
					"                    </td>\n" +
					"                </tr>\n" +
					"            </table>\n" +
					"            <!-- end blog headlines -->\n" +
					"\n" +
					"\n" +
					"<div style=\"height:35px;margin:0 auto;\">&nbsp;</div><!-- spacer -->\n" +
					"\n" +
					"\n" +
					"\t\t\t<!-- 4 Columns -->\n" +
					"\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td bgcolor=\"#363636\" style=\"padding:30px 0\">\n" +
					"                        <table width=\"580\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" align=\"center\" class=\"deviceWidth\" style=\"margin:0 auto;\">\n" +
					"                            <tr>\n" +
					"                                <td>\n" +
					"                                        <table width=\"45%\" cellpadding=\"0\" cellspacing=\"0\"  border=\"0\" align=\"left\" class=\"deviceWidth\">\n" +
					"                                            <tr>\n" +
					"                                                <td valign=\"top\" style=\"font-size: 11px; color: #f1f1f1; color:#999; font-family: Arial, sans-serif; padding-bottom:20px\" class=\"center\">\n" +
					"\n" +
					"                                                    You are receiving this email because<br/>\n" +
					"                                                    1.) You're an awesome customer of \"Company Name\" or<br/>\n" +
					"                                                    2.) You subscribed via <a href=\"\" style=\"color:#999;text-decoration:underline;\">our website</a><br/>\n" +
					"\n" +
					"                                                    <br/><br/>\n" +
					"                                                    Want to be removed? No problem, <a href=\"\" style=\"color:#999;text-decoration:underline;\">click here</a> and we won't bug you again.\n" +
					"\n" +
					"                                                </td>\n" +
					"                                            </tr>\n" +
					"                                        </table>\n" +
					"\n" +
					"                                        <table width=\"40%\" cellpadding=\"0\" cellspacing=\"0\"  border=\"0\" align=\"right\" class=\"deviceWidth\">\n" +
					"                                            <tr>\n" +
					"                                                <td valign=\"top\" style=\"font-size: 11px; color: #f1f1f1; font-weight: normal; font-family: Georgia, Times, serif; line-height: 26px; vertical-align: top; text-align:right\" class=\"center\">\n" +
					"\n" +
					"                                                    <a href=\"\"><img src=\"https://www.emailonacid.com/images/emails/5_13/footer_rss.gif\" width=\"42\" height=\"42\" alt=\"RSS Feed\" title=\"RSS Feed\" border=\"0\" /></a>\n" +
					"\n" +
					"                                                    <a href=\"\"><img src=\"https://www.emailonacid.com/images/emails/5_13/footer_twitter.gif\" width=\"42\" height=\"42\" alt=\"Twitter\" title=\"Twitter\" border=\"0\" /></a>\n" +
					"\n" +
					"                                                    <a href=\"\"><img src=\"https://www.emailonacid.com/images/emails/5_13/footer_vm.gif\" width=\"42\" height=\"42\" alt=\"Vimeo\" title=\"Vimeo\" border=\"0\" /></a>\n" +
					"\n" +
					"                                                    <a href=\"\"><img src=\"https://www.emailonacid.com/images/emails/5_13/footer_fb.gif\" width=\"42\" height=\"42\" alt=\"Facebook\" title=\"Facebook\" border=\"0\" /></a>\n" +
					"                                                    <br />\n" +
					"\n" +
					"                                                    <a href=\"#\"><img src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/footer_logo2.jpg\" alt=\"\" border=\"0\" style=\"padding-top: 5px;\" /></a><br/>\n" +
					"                                                    <a href=\"#\" style=\"text-decoration: none; color: #848484; font-weight: normal;\">555-555-5555</a><br/>\n" +
					"                                                    <a href=\"#\" style=\"text-decoration: none; color: #848484; font-weight: normal;\">email@email.com</a>\n" +
					"                                                </td>\n" +
					"                                            </tr>\n" +
					"                                        </table>\n" +
					"\n" +
					"                        \t\t</td>\n" +
					"                        \t</tr>\n" +
					"                        </table>\n" +
					"                    </td>\n" +
					"                </tr>\n" +
					"            </table><!-- End 4 Columns -->\n" +
					"\n" +
					"\t\t</td>\n" +
					"\t</tr>\n" +
					"</table> <!-- End Wrapper -->\n" +
					"<div style=\"display:none; white-space:nowrap; font:15px courier; color:#ffffff;\">\n" +
					"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n" +
					"</div>\n" +
					"</body>\n" +
					"</html>\n";
}
