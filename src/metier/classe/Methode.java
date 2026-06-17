package src.metier.classe;

import java.util.List;
import src.metier.enums.ModifierType;
import src.metier.enums.Portee;
import src.metier.enums.Visibilite;

/**
 * Représente une méthode d'une classe Java, avec ses paramètres, son type de retour, sa visibilité, son modificateur et sa portée.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 *
 * @param estConstructeur vrai si la méthode est un constructeur
 * @param nom le nom de la méthode
 * @param type le type de retour
 * @param visibilite la visibilité
 * @param modifierType le modificateur
 * @param portee la portée
 * @param lstParametres la liste des paramètres
 */
public record Methode( boolean estConstructeur, String nom, String type , Visibilite visibilite, 
					   ModifierType modifierType, Portee portee, List<Parametre> lstParametres, boolean estDefault  )
{
   /**
	* Retourne une représentation textuelle de la méthode.
	* @return la chaîne représentant la méthode
	*/
	public String toString()
	{
		String res;
		String nomF        =                       String.format( "%-10s", this.nom                     () );
		String typeF       = "type de retour : " + String.format( "%-10s", this.type                    () );
		String visibiliteF = "visibilité : "     + String.format( "%-10s", this.visibilite().getFrancais() );

		if ( this.estConstructeur() ) { res = "méthode : Constructeur  " + visibiliteF;               }
		else                          { res = "méthode : " + nomF + " "  + visibiliteF + " " + typeF; }

		res += "\nparamètres : ";
		if ( this.lstParametres() == null || this.lstParametres().isEmpty() )
		{
			res += "aucun\n";
		}
		else
		{
			int cpt = 0;
			for (Parametre param : this.lstParametres())
			{
				if ( cpt != 0 ) { res+="             "; }
				String paramNom  = String.format( "p%d :  %-20s", cpt + 1 , param.nom () );
				String paramType = String.format( "type : %-20s",           param.type() );
				
				res += paramNom + " " + paramType + "\n";
				cpt++;
			}
		}
		return res;
	}
}