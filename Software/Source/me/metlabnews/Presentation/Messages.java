package me.metlabnews.Presentation;


/**
 * Contains default messages for the front end
 */
// TODO: group and order messages
public final class Messages
{
	public static final String IllegalOperation         = "Operation kann nicht ausgeführt werden!";
	public static final String UnknownError             = "Unbekannter Fehler!";
	public static final String UnknownEmail             = "Kein Benutzer mit dieser E-Mail vorhanden!";
	public static final String WrongPassword            = "Falsches Passwort!";
	public static final String EmailAddressAlreadyInUse         = "Die angegebene E-Mail-Adresse " +
			"wird bereits von einem anderen Nutzer verwendet!";
	public static final String UnknownOrganisation              = "Die angegebene Organisation "
			+ "existiert nicht!";
	public static final String NotClientAdmin                   = "Sie benötigen Administratorrechte einer " +
			"Organisation um diese Operation ausführen zu können!";
	public static final String NotLoggedIn                      = "Sie müssen angemeldet sein, um diese" +
			"Operation ausführen zu können!";
	public static final String UserIsAlreadyVerified            = "Der Benutzer wurde bereits verfifiziert!";
	public static final String OrganisationNameAlreadyTaken     = "Dieser Name wird bereits von einer" +
			" anderen Organisation verwendet!";
	public static final String NotSystemAdmin                   = "Sie müssen als Systemadministrator angemeldet sein" +
			", um diese Operation ausführen zu können!";
	public static final String PasswordDoesNotMatchRequirements = "Das eingegebene Passwort" +
			"erfüllt die Sicherheitsanforderungen nicht!";
	public static final String InvalidEmailAddress              = "Die angegebene E-Mail-Adresse ist ungültig!";
	public static final String ObservationProfileDoesNotExist   = "Das angegebene Oberservationsprofil ist " +
			"nicht vorhanden!";
	public static final String InvalidOberservationProfile      = "Ungültiges Observationsprofil!";
	public static final String ProfileTemplateDoesNotExist      = "Die angegebene Profilvorlage ist " +
			"nicht vorhanden!";
	public static final String IncompleteObservationProfile     = "Das Observationsprofil ist unvollständig!";
	public static final String InvalidSource                    = "Ungültige Nachrichtenquelle!";
	public static final String SourceNotFound                   = "Die Nachrichtenquelle konnte nicht gefunden werden!";



	private Messages() { }
}
