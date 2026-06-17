package src.metier.enums;

/**
 * Enumération définissant les différents types de relations UML possibles entre les classes.
 * Utilisée pour caractériser les liens détectés lors de l'analyse.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public enum TypeAssociation
{
	/*-------------------------------*/
	/* Constantes                    */
	/*-------------------------------*/
	DEPENDANCE              ( "dépendance"                    ),
	IMPLEMENTATION_INTERFACE( "implémentation d'interface"    ),
	GENERALISATION          ( "généralisation/spécialisation" ),
	AGREGATION              ( "agrégation"                    ),
	COMPOSITION             ( "composition"                   ),
	ASSOCIATION_SIMPLE      ( "association simple"            );

	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private final String libelle;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise le type d'association avec son libellé descriptif.
	 * @param libelle Le nom affichable de l'association.
	 */
	private TypeAssociation( String libelle )
	{
		this.libelle = libelle;
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/

	/** @return Le libellé descriptif du type d'association. */
	public String getLibelle() { return this.libelle; }

	/*-------------------------------*/
	/* Méthodes statiques            */
	/*-------------------------------*/
	/**
	 * Trouve le type d'association correspondant à un libellé.
	 * @param libelle Le libellé à rechercher
	 * @return Le TypeAssociation correspondant, ou null si non trouvé
	 */
	public static TypeAssociation fromLibelle(String libelle) 
	{
		for (TypeAssociation type : TypeAssociation.values()) 
			if (type.libelle.equals(libelle)) { return type; }
		return null;
	}
}