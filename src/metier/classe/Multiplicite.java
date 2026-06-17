package src.metier.classe;

/**
 * Enumération définissant les cardinalités (multiplicités) UML possibles.
 * Représente les contraintes de nombre d'instances dans une association (ex: 0..1, 1..*).
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class Multiplicite
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private int minimum;
	private int maximum;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise la multiplicité avec les valeurs minimum et maximum.
	 * Utiliser -1 pour représenter l'infini (*).
	 * @param minimum La valeur minimale (>= 0 ou -1 pour infini).
	 * @param maximum La valeur maximale (>= minimum ou -1 pour infini).
	 */
	public Multiplicite( int minimum, int maximum )
	{
		this.minimum = minimum;
		this.maximum = maximum;
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/

	/** @return La valeur minimale de la multiplicité. */
	public int getMinimum() { return minimum; }

	/** @return La valeur maximale de la multiplicité. */
	public int getMaximum() { return maximum; }
	

	/*-------------------------------*/
	/* Méthodes Utilitaires          */
	/*-------------------------------*/
	/** Modifie les valeurs minimum et maximum de la multiplicité. */	
	public void editerMultiplicite(int min, int max)
	{
		this.minimum = min;
		this.maximum = max;
	}

	/** Retourne une représentation textuelle de la multiplicité. 
	 * @return la chaîne représentant la multiplicité
	*/
	public String toString()
	{
		String min = (this.minimum == -1 ? "*" : "" + this.minimum);

		if ( this.minimum == this.maximum ) { return min; }

		String max = (this.maximum == -1 ? "*" : "" + this.maximum);

		return min + ".." + max;
	}
}