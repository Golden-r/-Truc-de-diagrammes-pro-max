package src.metier.classe;

/**
 * Représente une classe externe dans le modèle.
 * Une classe externe est une classe qui n'est pas définie dans le projet mais qui est utilisée par celui-ci.
 * Elle hérite de la classe de base Classe.
 * 
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class ClasseExterne extends Classe 
{
	/*-------------------------------*/
	/*         Constructeurs         */
	/*-------------------------------*/
	/**
	 * Constructeur de la classe ClasseExterne.
	 * 
	 * @param nom le nom de la classe externe
	 */
	public ClasseExterne(String nom) { super(nom); }

	/**
	 * Constructeur de la classe ClasseExterne avec position.
	 * 
	 * @param nom le nom de la classe externe
	 * @param x la position x de la classe externe
	 * @param y la position y de la classe externe
	 */
	public ClasseExterne(String nom, int x, int y ) 
	{
		super(nom);
		super.setPosX(x);
		super.setPosY(y);
	}
	
	/*-------------------------------*/
	/*           Méthodes            */
	/*-------------------------------*/
	/**
	 * Indique que cette classe est une classe externe.
	 * @return vrai, car c'est une classe externe
	 */
	public boolean estExterne() { return true; }
}