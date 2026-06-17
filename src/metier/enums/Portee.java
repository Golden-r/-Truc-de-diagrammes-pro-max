package src.metier.enums;

/**
 * Enumération définissant la portée des attributs et méthodes (Instance ou Classe).
 * Associe le concept logique au mot-clé Java "static" ou sinon "instance".
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public enum Portee
{
	/*-------------------------------*/
	/* Constantes                    */
	/*-------------------------------*/
	INSTANCE ( "instance" , "instance" ),
	CLASSE   ( "static"   , "classe"   );

	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private final String anglais;
	private final String francais;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise la portée avec les termes associés.
	 * @param anglais Le mot-clé Java (ou "instance" par défaut).
	 * @param francais Le terme français correspondant.
	 */
	private Portee( String anglais, String francais )
	{
		this.anglais  = anglais;
		this.francais = francais;
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/

	/** @return Le mot-clé anglais (ex: static). */
	public String getAnglais () { return this.anglais;  }

	/** @return Le libellé français (ex: classe). */
	public String getFrancais() { return this.francais; }

	/*-------------------------------*/
	/* Méthodes Utilitaires          */
	/*-------------------------------*/

	/**
	 * Récupère l'enum correspondant au mot-clé anglais (insensible à la casse).
	 * @param mot Le mot-clé à rechercher.
	 * @return L'enum correspondant ou null si non trouvé.
	 */
	public static Portee depuisAnglais( String mot )
	{
		if ( mot == null ) { return null; }

		for ( Portee p : values() )
			if ( p.anglais.equalsIgnoreCase( mot ) ) { return p; }
		return null;
	}
}