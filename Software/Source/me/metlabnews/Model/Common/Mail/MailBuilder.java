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
				"\t\t\t\t\t\t\t\t\t\t\t\t<!-- Content 1 -->\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t<td data-color=\"text\" data-size=\"size text\" data-min=\"10\" data-max=\"26\" data-link-color=\"link text color\" data-link-style=\"font-weight:bold; text-decoration:underline; color:#40aceb;\" align=\"left\" style=\"font:bold 12px/20px Arial, Helvetica, sans-serif; color:#888; padding:0 0 5px;\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + c + "" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t</tr>";
	}


	private static String createClippingContent(String c)
	{
		return
				"\t\t\t\t\t<!-- CLIPPING -->\n" +
						"\t\t\t\t\t<table data-module=\"module-3\" data-thumb=\"thumbnails/03.png\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
						"\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t<td data-bgcolor=\"bg-module\" bgcolor=\"#eaeced\">\n" +
						"\t\t\t\t\t\t\t\t<table class=\"flexible\" width=\"600\" align=\"center\" style=\"margin:0 auto;\" cellpadding=\"0\" cellspacing=\"0\">\n" +
						"\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t<td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:25px 25px 25px;\" bgcolor=\"#f9f9f9\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t<td data-color=\"text\" data-size=\"size text\" data-min=\"10\" data-max=\"26\" data-link-color=\"link text color\" data-link-style=\"font-weight:bold; text-decoration:underline; color:#40aceb;\" align=\"left\" style=\"font:16px/29px Arial, Helvetica, sans-serif; color:#888; padding:0 0 10px;\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + c + "\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
						"\t\t\t\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t\t\t\t\t<tr><td height=\"28\"></td></tr>\n" +
						"\t\t\t\t\t\t\t\t</table>\n" +
						"\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t</tr>\n" +
						"\t\t\t\t\t</table>";
	}


	private static String createSalutationContent(String c)
	{
		return
				"\t\t\t\t\t\t\t\t\t\t\t\t<!-- Anrede -->\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t<td data-color=\"title\" data-size=\"size title\" data-min=\"25\" data-max=\"45\" data-link-color=\"link title color\" data-link-style=\"text-decoration:none; color:#292c34;\" class=\"title\" align=\"left\" style=\"font:25px/20px Arial, Helvetica, sans-serif; color:#292c34; padding:0 0 24px;\">\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + c + "" +
						"\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
						"\t\t\t\t\t\t\t\t\t\t\t\t</tr>";
	}

	public static String getFinalHTMLString(List<String> content, List<Article> articles)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(HTML_head);
		sb.append(HTML_module1);
		sb.append(HTML_module2_begin);
		sb.append(createSalutationContent("Sehr geehrter Abonnent, " + ""));
		sb.append(createNormalContent("nachfolgend erhalten Sie " + articles.size() + " " +
				                              "Artikel, die Sie mit Ihrem Profil erstellt haben."));
		for(String s : content)
		{
			sb.append(createNormalContent(s));
		}
		sb.append(HTML_module2_end);
		for(Article a : articles)
		{
			sb.append(createClippingContent(a.toHTMLString()));
		}
		sb.append(HTML_footer);
		return sb.toString();
	}


	private static String HTML_head =
			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
					"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
					"\t<head>\n" +
					"\t\t<title>Internal_email-29</title>\n" +
					"\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
					"\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
					"\t\t<style type=\"text/css\">\n" +
					"\t\t\t* {\n" +
					"\t\t\t\t-ms-text-size-adjust:100%;\n" +
					"\t\t\t\t-webkit-text-size-adjust:none;\n" +
					"\t\t\t\t-webkit-text-resize:100%;\n" +
					"\t\t\t\ttext-resize:100%;\n" +
					"\t\t\t}\n" +
					"\t\t\ta{\n" +
					"\t\t\t\toutline:none;\n" +
					"\t\t\t\tcolor:#40aceb;\n" +
					"\t\t\t\ttext-decoration:underline;\n" +
					"\t\t\t}\n" +
					"\t\t\ta:hover{text-decoration:none !important;}\n" +
					"\t\t\t.nav a:hover{text-decoration:underline !important;}\n" +
					"\t\t\t.title a:hover{text-decoration:underline !important;}\n" +
					"\t\t\t.title-2 a:hover{text-decoration:underline !important;}\n" +
					"\t\t\t.btn:hover{opacity:0.8;}\n" +
					"\t\t\t.btn a:hover{text-decoration:none !important;}\n" +
					"\t\t\t.btn{\n" +
					"\t\t\t\t-webkit-transition:all 0.3s ease;\n" +
					"\t\t\t\t-moz-transition:all 0.3s ease;\n" +
					"\t\t\t\t-ms-transition:all 0.3s ease;\n" +
					"\t\t\t\ttransition:all 0.3s ease;\n" +
					"\t\t\t}\n" +
					"\t\t\ttable td {border-collapse: collapse !important;}\n" +
					"\t\t\t.ExternalClass, .ExternalClass a, .ExternalClass span, .ExternalClass b, .ExternalClass br, .ExternalClass p, .ExternalClass div{line-height:inherit;}\n" +
					"\t\t\t@media only screen and (max-width:500px) {\n" +
					"\t\t\t\ttable[class=\"flexible\"]{width:100% !important;}\n" +
					"\t\t\t\ttable[class=\"center\"]{\n" +
					"\t\t\t\t\tfloat:none !important;\n" +
					"\t\t\t\t\tmargin:0 auto !important;\n" +
					"\t\t\t\t}\n" +
					"\t\t\t\t*[class=\"hide\"]{\n" +
					"\t\t\t\t\tdisplay:none !important;\n" +
					"\t\t\t\t\twidth:0 !important;\n" +
					"\t\t\t\t\theight:0 !important;\n" +
					"\t\t\t\t\tpadding:0 !important;\n" +
					"\t\t\t\t\tfont-size:0 !important;\n" +
					"\t\t\t\t\tline-height:0 !important;\n" +
					"\t\t\t\t}\n" +
					"\t\t\t\ttd[class=\"img-flex\"] img{\n" +
					"\t\t\t\t\twidth:100% !important;\n" +
					"\t\t\t\t\theight:auto !important;\n" +
					"\t\t\t\t}\n" +
					"\t\t\t\ttd[class=\"aligncenter\"]{text-align:center !important;}\n" +
					"\t\t\t\tth[class=\"flex\"]{\n" +
					"\t\t\t\t\tdisplay:block !important;\n" +
					"\t\t\t\t\twidth:100% !important;\n" +
					"\t\t\t\t}\n" +
					"\t\t\t\ttd[class=\"wrapper\"]{padding:0 !important;}\n" +
					"\t\t\t\ttd[class=\"holder\"]{padding:30px 15px 20px !important;}\n" +
					"\t\t\t\ttd[class=\"nav\"]{\n" +
					"\t\t\t\t\tpadding:20px 0 0 !important;\n" +
					"\t\t\t\t\ttext-align:center !important;\n" +
					"\t\t\t\t}\n" +
					"\t\t\t\ttd[class=\"h-auto\"]{height:auto !important;}\n" +
					"\t\t\t\ttd[class=\"description\"]{padding:30px 20px !important;}\n" +
					"\t\t\t\ttd[class=\"i-120\"] img{\n" +
					"\t\t\t\t\twidth:120px !important;\n" +
					"\t\t\t\t\theight:auto !important;\n" +
					"\t\t\t\t}\n" +
					"\t\t\t\ttd[class=\"footer\"]{padding:5px 20px 20px !important;}\n" +
					"\t\t\t\ttd[class=\"footer\"] td[class=\"aligncenter\"]{\n" +
					"\t\t\t\t\tline-height:25px !important;\n" +
					"\t\t\t\t\tpadding:20px 0 0 !important;\n" +
					"\t\t\t\t}\n" +
					"\t\t\t\ttr[class=\"table-holder\"]{\n" +
					"\t\t\t\t\tdisplay:table !important;\n" +
					"\t\t\t\t\twidth:100% !important;\n" +
					"\t\t\t\t}\n" +
					"\t\t\t\tth[class=\"thead\"]{display:table-header-group !important; width:100% !important;}\n" +
					"\t\t\t\tth[class=\"tfoot\"]{display:table-footer-group !important; width:100% !important;}\n" +
					"\t\t\t}\n" +
					"\t\t</style>\n" +
					"\t</head>\n" +
					"\t<body style=\"margin:0; padding:0;\" bgcolor=\"#eaeced\">\n" +
					"\t\t<table style=\"min-width:320px;\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#eaeced\">\n" +
					"\t\t\t<!-- fix for gmail -->\n" +
					"\t\t\t<tr>\n" +
					"\t\t\t\t<td class=\"hide\">\n" +
					"\t\t\t\t\t<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px !important;\">\n" +
					"\t\t\t\t\t\t<tr>\n" +
					"\t\t\t\t\t\t\t<td style=\"min-width:600px; font-size:0; line-height:0;\">&nbsp;</td>\n" +
					"\t\t\t\t\t\t</tr>\n" +
					"\t\t\t\t\t</table>\n" +
					"\t\t\t\t</td>\n" +
					"\t\t\t</tr>\n" +
					"\t\t\t<tr>\n" +
					"\t\t\t\t<td class=\"wrapper\" style=\"padding:0 10px;\">";


	private static String HTML_module1 =
			"\t\t\t\t\t<!-- module 1 -->\n" +
					"\t\t\t\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
					"\t\t\t\t\t\t<tr>\n" +
					"\t\t\t\t\t\t\t<td data-bgcolor=\"bg-module\" bgcolor=\"#eaeced\">\n" +
					"\t\t\t\t\t\t\t\t<table class=\"flexible\" width=\"600\" align=\"center\" style=\"margin:0 auto;\" cellpadding=\"0\" cellspacing=\"0\">\n" +
					"\t\t\t\t\t\t\t\t\t<tr>\n" +
					"\t\t\t\t\t\t\t\t\t\t<td style=\"padding:5px 0 30px;\">\n" +
					"\t\t\t\t\t\t\t\t\t\t</td>\n" +
					"\t\t\t\t\t\t\t\t\t</tr>\n" +
					"\t\t\t\t\t\t\t\t</table>\n" +
					"\t\t\t\t\t\t\t</td>\n" +
					"\t\t\t\t\t\t</tr>\n" +
					"\t\t\t\t\t</table>";


	private static String HTML_module2_begin =
			"\t\t\t\t\t<!-- module 2 -->\n" +
					"\t\t\t\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
					"\t\t\t\t\t\t<tr>\n" +
					"\t\t\t\t\t\t\t<td data-bgcolor=\"bg-module\" bgcolor=\"#eaeced\">\n" +
					"\t\t\t\t\t\t\t\t<table class=\"flexible\" width=\"600\" align=\"center\" style=\"margin:0 auto;\" cellpadding=\"0\" cellspacing=\"0\">\n" +
					"\t\t\t\t\t\t\t\t\t<tr>\n" +
					"\t\t\t\t\t\t\t\t\t\t<td class=\"img-flex\"><img src=\"http://metlabnews.me:8080/images/header.jpeg\n\" style=\"vertical-align:top;\" width=\"600\" height=\"306\" alt=\"\" /></td>\n" +
					"\t\t\t\t\t\t\t\t\t</tr>\n" +
					"\t\t\t\t\t\t\t\t\t<tr>\n" +
					"\t\t\t\t\t\t\t\t\t\t<td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:10px 10px 10px;\" bgcolor=\"#f9f9f9\">\n" +
					"\t\t\t\t\t\t\t\t\t\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">";

	private static String HTML_module2_end =
			"\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
					"\t\t\t\t\t\t\t\t\t\t</td>\n" +
					"\t\t\t\t\t\t\t\t\t</tr>\n" +
					"\t\t\t\t\t\t\t\t\t<tr><td height=\"28\"></td></tr>\n" +
					"\t\t\t\t\t\t\t\t</table>\n" +
					"\t\t\t\t\t\t\t</td>\n" +
					"\t\t\t\t\t\t</tr>\n" +
					"\t\t\t\t\t</table>";


	private static String HTML_footer =
			"\t\t\t\t</td>\n" +
					"\t\t\t</tr>\n" +
					"\t\t\t<!-- fix for gmail -->\n" +
					"\t\t\t<tr>\n" +
					"\t\t\t\t<td style=\"line-height:0;\"><div style=\"display:none; white-space:nowrap; font:15px/1px courier;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div></td>\n" +
					"\t\t\t</tr>\n" +
					"\t\t</table>\n" +
					"\t</body>\n" +
					"</html>";
}
