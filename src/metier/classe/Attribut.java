package src.metier.classe;

import src.metier.enums.Portee;
import src.metier.enums.Visibilite;
import src.metier.enums.Contraintes;

/**
 * Représente un attribut d'une classe Java, avec son nom, son type, sa visibilité, sa portée et son statut final.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class Attribut
{
	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	private String        nom;
	private String        type;
	private Visibilite    visibilite;
	private Portee        portee;
	private Contraintes   contrainte;
	private String        valeur;
	private Multiplicite  multiplicite;

	/*-------------------------------*/
	/*         Constructeurs         */
	/*-------------------------------*/
	
	/**
	 * Constructeur complet avec multiplicité
	 */
	public Attribut(String nom, String type, Visibilite visibilite, Portee portee, Contraintes contrainte, String valeur, Multiplicite multiplicite)
	{
		this.nom           = nom;
		this.type          = type;
		this.visibilite    = visibilite;
		this.portee        = portee;
		this.contrainte    = contrainte;
		this.valeur        = valeur;
		this.multiplicite  = multiplicite;
	}

	/**
	 * Constructeur sans multiplicité (pour compatibilité avec le code existant)
	 */
	public Attribut(String nom, String type, Visibilite visibilite, Portee portee, Contraintes contrainte, String valeur)
	{
		this(nom, type, visibilite, portee, contrainte, valeur, calculerMultipliciteParDefaut(type, contrainte == Contraintes.FROZEN));
	}

	/*-------------------------------*/
	/*            Getters            */
	/*-------------------------------*/
	
	public String       nom()           { return this.nom;          }
	public String       type()          { return this.type;         }
	public Visibilite   visibilite()    { return this.visibilite;   }
	public Portee       portee()        { return this.portee;       }
	public boolean      estFinal()      { return this.contrainte == Contraintes.FROZEN; }
	public String       valeur()        { return this.valeur;       }
	public Multiplicite multiplicite()  { return this.multiplicite; }
	public Contraintes  getContrainte() { return this.contrainte;   }

	/*-------------------------------*/
	/*            Setters            */
	/*-------------------------------*/
	
	public void setMultiplicite(Multiplicite multiplicite) 
	{ 
		this.multiplicite = multiplicite; 
	}
	
	public void setContrainte(Contraintes contrainte) 
	{ 
		this.contrainte = contrainte; 
	}

	/*-------------------------------*/
	/*           Méthodes            */
	/*-------------------------------*/
	
	/**
	 * Calcule la multiplicité par défaut selon le type et le caractère final
	 */
	private static Multiplicite calculerMultipliciteParDefaut(String type, boolean estFinal)
	{
		if (type == null) return new Multiplicite(0, 1);
		
		// Collection (List, Set, etc.) ou tableau
		if (type.contains("<") || type.contains("["))
		{
			if (estFinal) return new Multiplicite(1, -1); // 1..*
			else          return new Multiplicite(0, -1); // 0..*
		}
		
		// Type simple
		if (estFinal) return new Multiplicite(1, 1); // 1..1
		else          return new Multiplicite(0, 1); // 0..1
	}

	/**
	 * Retourne une représentation textuelle de l'attribut.
	 * @return la chaîne représentant l'attribut
	 */
	public String toString()
	{
		String multStr = (multiplicite != null) ? " [" + multiplicite.toString() + "]" : "";
		
		return "nom : "        + String.format("%-20s", this.nom)                      + " " +
		       "type : "       + String.format("%-15s", this.type)                     + " " +
		       "visibilité : " + String.format("%-10s", this.visibilite.getFrancais()) + " " +
		       "portée : "     + String.format("%-10s", this.portee.getFrancais())     + multStr;
	}
}