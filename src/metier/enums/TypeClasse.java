package src.metier.enums;

/**
 * Enumération définissant les différents types de structures Java analysables.
 * Associe chaque type à son mot-clé de déclaration.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public enum TypeClasse
{
	/*-------------------------------*/
	/* Constantes                    */
	/*-------------------------------*/
	RECORD    ( "record"    ),
	INTERFACE ( "interface" ),
	CLASS     ( "class"     ),
	ENUM      ( "enum"      );

	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private final String type;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise le type de classe avec son mot-clé Java.
	 * @param type Le mot-clé (ex: class, interface).
	 */
	private TypeClasse( String type )
	{
		this.type = type;
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/

	/** @return Le mot-clé Java associé. */
	public String getType() { return this.type; }

	
}