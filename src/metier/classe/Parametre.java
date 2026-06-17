package src.metier.classe;

/**
 * Représente un paramètre de méthode ou de constructeur dans une classe Java.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 *
 * @param nom le nom du paramètre
 * @param type le type du paramètre
 */
public record Parametre( String nom, String type )
{
	/**
	 * Constructeur à partir d'un attribut.
	 * @param attribut l'attribut à convertir en paramètre
	 */
	public Parametre( Attribut attribut ) { this(attribut.nom(), attribut.type()); }

	/**
	 * Retourne une représentation textuelle du paramètre.
	 * @return la chaîne représentant le paramètre
	 */
	public String toString()
	{
		return  " nom  : " + String.format("%-10s",  this.nom()  ) +
				" type : " + String.format("%-10s",  this.type() ) ;
	}
}