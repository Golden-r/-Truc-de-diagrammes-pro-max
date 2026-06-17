package src.metier.enums;

/**
 * Enumération définissant les modificateurs de statut pour les classes et méthodes (abstract, final).
 * Le type NORMAL correspond à l'absence de modificateur spécifique.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public enum ModifierType
{
	/*-------------------------------*/
	/* Constantes                    */
	/*-------------------------------*/
	ABSTRACT ( "abstract", "abstrait" ),
	FINAL    ( "final"   , "final"    ),
	NORMAL   ( "normal"  , "normal"   ); // Ni abstract Ni final

	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private final String anglais;
	private final String francais;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise le type de modificateur avec ses libellés.
	 * @param anglais Le mot-clé Java.
	 * @param francais La traduction française.
	 */
	private ModifierType( String anglais, String francais )
	{
		this.anglais  = anglais;
		this.francais = francais;
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/

	/** @return Le mot-clé anglais (ex: abstract). */
	public String getAnglais()  { return this.anglais;  }

	/** @return Le libellé français (ex: abstrait). */
	public String getFrancais() { return this.francais; }

	/*-------------------------------*/
	/* Méthodes Utilitaires          */
	/*-------------------------------*/

	/**
	 * Récupère l'enum correspondant au mot-clé anglais (insensible à la casse).
	 * @param mot Le mot-clé à rechercher.
	 * @return L'enum correspondant ou null si non trouvé.
	 */
	public static ModifierType depuisAnglais( String mot )
	{
		if ( mot == null ) { return null; }

		for ( ModifierType m : values() )
			if ( m.anglais.equalsIgnoreCase( mot ) )
				return m; 
		return null;
	}
}