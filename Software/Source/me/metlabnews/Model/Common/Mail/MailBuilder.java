package me.metlabnews.Model.Common.Mail;

import me.metlabnews.Model.Entities.Article;

import java.util.List;



/***
 * This class is needed to build a responsive HTML Mail.
 * You can only call the getFinalHTMLString - method.
 */
public class MailBuilder
{

	private static String createNormalContent(String c)
	{
		return
				"<table>\n" +
						"   <tr>\n" +
						"       <td valign=\"middle\" style=\"padding:0 10px 10px 0\"><p href=\"#\" style=\"text-decoration: none; color: #272727; font-size: 12px; color: #272727; font-weight: bold; font-family:Arial, sans-serif \">" + c + "</p></td>\n" +
						"   </tr>\n" +
						"</table>";
	}


	private static String createClippingContent(String c)
	{
		return
				"<table border=\"1\">\n" +
						"   <tr>\n" +
						"       <td valign=\"middle\" style=\"padding:0 10px 10px 0\"><p href=\"#\" style=\"text-decoration: none; color: #272727; font-size: 12px; color: #272727; font-weight: bold; font-family:Arial, sans-serif \">" + c + "</p></td>\n" +
						"   </tr>\n" +
						"</table>";
	}


	private static String createSalutationContent(String c)
	{
		return
				"<table>\n" +
						"   <tr>\n" +
						"       <td valign=\"middle\" style=\"padding:0 10px 10px 0\"><p href=\"#\" style=\"text-decoration: none; color: #272727; font-size: 16px; color: #272727; font-weight: bold; font-family:Arial, sans-serif \">" + c + "</p></td>\n" +
						"   </tr>\n" +
						"</table>";
	}

	public static String getFinalHTMLString(List<String> content, List<Article> articles)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(HTML_header);
		sb.append(createSalutationContent("Sehr geehrter Abonnent, " + "\n"));
		sb.append(createNormalContent("nachfolgend erhalten Sie " + articles.size() + " " +
				                              "Artikel, die Sie mit Ihrem Profil erstellt haben"));
		for(String s : content)
		{
			sb.append(createNormalContent(s)).append("\n");
		}
		for(Article a : articles)
		{
			sb.append(createClippingContent(a.toHTMLString())).append("\n");
		}
		sb.append(HTML_footer);
		return sb.toString();
	}


	private static String HTML_header =
			"<!doctype html>\n" +
					"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
					"<head>\n" +
					"\n" +
					"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
					"\n" +
					"<title>METLAB News Clipping</title>\n" +
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
					"\n" +
					"\n" +
					"\t\t\t<!-- One Column -->\n" +
					"\t\t\t<table width=\"580\" border=\"0\" class=\"deviceWidth\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" bgcolor=\"#eeeeed\" style=\"margin:0 auto;\">\n" +
					"\t\t\t\t<tr>\n" +
					"\t\t\t\t\t<td valign=\"top\" style=\"padding:0\" bgcolor=\"#ffffff\">\n" +
					"\t\t\t\t\t\t<p href=\"#\"><img  class=\"deviceWidth\" src=\"file:/// http://metlabnews.me:8080/images/header.jpeg\" alt=\"\" border=\"0\" style=\"display: block; border-radius: 4px;\" /></p>\n" +
					"\t\t\t\t\t</td>\n" +
					"\t\t\t\t</tr>\n" +
					"                <tr>\n" +
					"                    <td style=\"font-size: 13px; color: #959595; font-weight: normal; text-align: left; font-family: Georgia, Times, serif; line-height: 24px; vertical-align: top; padding:10px 8px 10px 8px\" bgcolor=\"#eeeeed\">";


	private static String HTML_footer =
			"</td>\n" +
					"                </tr>\n" +
					"\t\t\t</table><!-- End One Column -->\n" +
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
					"                                                    Sie erhalten diese E-Mail, weil<br/>\n" +
					"                                                    1.) Sie ein Abonnent unseres Dienstes sind, oder<br/>\n" +
					"                                                    2.) Sie <a href=\"\" style=\"color:#999;text-decoration:underline;\">unseren Newsletter</a> abonniert haben.\n" +
					"\n" +
					"                                                    <br/><br/>\n" +
					"                                                    Sie wollen diese E-Mail nicht länger erhalten? Kein Problem, <a href=\"\" style=\"color:#999;text-decoration:underline;\">hier klicken</a> und sie werden nicht länger benachrichtigt.\n" +
					"\n" +
					"                                                </td>\n" +
					"                                            </tr>\n" +
					"                                        </table>\n" +
					"\n" +
					"                                        <table width=\"40%\" cellpadding=\"0\" cellspacing=\"0\"  border=\"0\" align=\"right\" class=\"deviceWidth\">\n" +
					"                                            <tr>\n" +
					"                                                <td valign=\"top\" style=\"font-size: 11px; color: #f1f1f1; font-weight: normal; font-family: Georgia, Times, serif; line-height: 26px; vertical-align: top; text-align:right\" class=\"center\">\n" +
					"\n" +
					"                                                    <a href=\"https://metlabnews.wixsite.com/metlab-news\"><img src=\"https://www.emailonacid.com/images/blog_images/Emailology/2013/footer_logo2.jpg\" alt=\"\" border=\"0\" style=\"padding-top: 5px;\" /></a><br/>\n" +
					"                                                    <a href=\"mailto:metlabnews@gmail.com\" style=\"text-decoration: none; color: #848484; font-weight: normal;\">Metlabnews@gmail.com</a>\n" +
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
					"</html>";
}
