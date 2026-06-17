package src.utils;

/**
 * Classe utilitaire pour la gestion des couleurs ANSI dans le terminal.
 * Permet d'appliquer des styles (gras, italique, souligné) et des couleurs au texte.
 *
 * La classe détecte automatiquement si le terminal supporte les couleurs et s'adapte en conséquence.
 * La gestion des couleurs peut être désactivée via la variable d'environnement CHAT_COLOR.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public final class CouleurUtils
{
	/*-------------------------------*/
	/*           Code ANSI           */
	/*-------------------------------*/
	public static final String RESET    = "\u001B[0m";
	public static final String GRAS     = "\u001B[1m";
	public static final String SOULIGNE = "\u001B[4m";
	public static final String ROUGE    = "\u001B[31m";
	public static final String VERT     = "\u001B[32m";
	public static final String JAUNE    = "\u001B[33m";
	public static final String BLEU     = "\u001B[34m";
	public static final String CYAN     = "\u001B[36m";
	public static final String GRIS     = "\u001B[37m";
	public static final String NOIR     = "\u001B[30m";

	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/

	// Détecte si le terminal supporte les couleur ANSI sinon, on affichera pas de couleur
	private static boolean active = CouleurUtils.detect();

	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/

	/**
	 * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
	 */
	private CouleurUtils() {}

	/*-------------------------------*/
	/*            Getters            */
	/*-------------------------------*/

	/**
	 * Indique si les couleurs sont activées.
	 * @return vrai si les couleurs sont activées
	 */
	public static boolean estActive() { return CouleurUtils.active; }

	/*-------------------------------*/
	/*           Méthodes            */
	/*-------------------------------*/

	/**
	 * Applique le style souligné à une chaîne et réinitialise le style.
	 * @param s la chaîne à formater
	 * @return la chaîne formatée
	 */
	public static String souligne( String s ) { return CouleurUtils.active && s != null ? CouleurUtils.SOULIGNE + s + CouleurUtils.RESET : safe(s); }
	/**
	 * Applique la couleur rouge à une chaîne et réinitialise le style.
	 * @param s la chaîne à formater
	 * @return la chaîne formatée
	 */
	public static String rouge   ( String s ) { return CouleurUtils.active && s != null ? CouleurUtils.ROUGE    + s + CouleurUtils.RESET : safe(s); }
	/**
	 * Applique la couleur verte à une chaîne et réinitialise le style.
	 * @param s la chaîne à formater
	 * @return la chaîne formatée
	 */
	public static String vert    ( String s ) { return CouleurUtils.active && s != null ? CouleurUtils.VERT     + s + CouleurUtils.RESET : safe(s); }
	/**
	 * Applique la couleur jaune à une chaîne et réinitialise le style.
	 * @param s la chaîne à formater
	 * @return la chaîne formatée
	 */
	public static String jaune   ( String s ) { return CouleurUtils.active && s != null ? CouleurUtils.JAUNE    + s + CouleurUtils.RESET : safe(s); }
	/**
	 * Applique la couleur bleue à une chaîne et réinitialise le style.
	 * @param s la chaîne à formater
	 * @return la chaîne formatée
	 */
	public static String bleu    ( String s ) { return CouleurUtils.active && s != null ? CouleurUtils.BLEU     + s + CouleurUtils.RESET : safe(s); }
	/**
	 * Applique la couleur cyan à une chaîne et réinitialise le style.
	 * @param s la chaîne à formater
	 * @return la chaîne formatée
	 */
	public static String cyan    ( String s ) { return CouleurUtils.active && s != null ? CouleurUtils.CYAN     + s + CouleurUtils.RESET : safe(s); }
	/**
	 * Applique la couleur grise à une chaîne et réinitialise le style.
	 * @param s la chaîne à formater
	 * @return la chaîne formatée
	 */
	public static String gris    ( String s ) { return CouleurUtils.active && s != null ? CouleurUtils.GRIS     + s + CouleurUtils.RESET : safe(s); }
	/**
	 * Applique la couleur noire à une chaîne et réinitialise le style.
	 * @param s la chaîne à formater
	 * @return la chaîne formatée
	 */
	public static String noir    ( String s ) { return CouleurUtils.active && s != null ? CouleurUtils.NOIR     + s + CouleurUtils.RESET : safe(s); }

	/**
	 * Retourne les codes ANSI seuls, sans texte.
	 * Utile pour appliquer le style puis ajouter du texte manuellement.
	 */
	public static String bleuSeul    () { return CouleurUtils.active ? CouleurUtils.BLEU     : ""; }
	public static String souligneSeul() { return CouleurUtils.active ? CouleurUtils.SOULIGNE : ""; }
	public static String reset       () { return CouleurUtils.active ? CouleurUtils.RESET    : ""; }

	/*-------------------------------*/
	/*            Setters            */
	/*-------------------------------*/

	/**
	 * Active ou désactive l'utilisation des couleurs.
	 * @param active vrai pour activer les couleurs
	 */
	public static void setActive(boolean active ) { CouleurUtils.active = active; }

	/*-------------------------------*/
	/*        Méthode privées        */
	/*-------------------------------*/

	/**
	 * Retourne une chaîne vide si s est null, sinon retourne s.
	 * @param s la chaîne à vérifier
	 * @return la chaîne sûre
	 */
	private static String safe(String s) { return s == null ? "" : s; }

	/**
	 * Détecte si le terminal supporte les couleurs ANSI.
	 * Vérifie la variable d'environnement CHAT_COLOR et la présence d'une console interactive.
	 * @return vrai si les couleurs sont supportées
	 */
	private static boolean detect()
	{
		String env = System.getenv("CHAT_COLOR");
		if ("0".equals(env) || "false".equalsIgnoreCase(env)) return false;
		if ("1".equals(env) || "true" .equalsIgnoreCase(env)) return true;

		// Si on a une console interactive --> activer
		return System.console() != null;
	}
}