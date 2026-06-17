package src.metier.enums;

/**
 * Enumération représentant les niveaux de visibilité en Java.
 * Permet la conversion entre les mots-clés anglais (syntaxe Java) et français.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public enum Visibilite
{
	/*-------------------------------*/
	/* Constantes                    */
	/*-------------------------------*/
	PUBLIC    ( "public"    , "publique"  ),
	PRIVATE   ( "private"   , "privée"    ),
	PROTECTED ( "protected" , "protégée"  ),
	PACKAGE   ( "package"   , "paquetage" );

	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private final String anglais;
	private final String francais;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise la visibilité avec ses libellés.
	 * @param anglais Le mot-clé Java.
	 * @param francais La traduction française.
	 */
	private Visibilite( String anglais, String francais )
	{
		this.anglais  = anglais;
		this.francais = francais;
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/

	/** @return Le mot-clé anglais (ex: public). */
	public String getAnglais () { return this.anglais;  }

	/** @return Le libellé français (ex: publique). */
	public String getFrancais() { return this.francais; }

	/*-------------------------------*/
	/* Méthodes Utilitaires          */
	/*-------------------------------*/
	/**
	 * Récupère l'enum correspondant au mot-clé anglais.
	 * @param mot Le mot-clé à rechercher.
	 * @return L'enum correspondant ou null si non trouvé.
	 */
	public static Visibilite depuisAnglais( String mot )
	{
		for ( Visibilite visu : values() )
			if ( visu.anglais.equals( mot ) ) { return visu; }
		return null;
	}

	/**
	 * Récupère l'enum correspondant au libellé français.
	 * @param mot Le mot à rechercher.
	 * @return L'enum correspondant ou null si non trouvé.
	 */
	public static Visibilite depuisFrancais( String mot )
	{
		for ( Visibilite visu : values() )
			if ( visu.francais.equals( mot ) ) { return visu; }
		return null;
	}
}