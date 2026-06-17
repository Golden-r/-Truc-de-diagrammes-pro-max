package src.vue.GUI.dessin.classe;

import java.util.List;
import src.metier.classe.Parametre;
import src.metier.enums.Visibilite;

/**
 * Classe utilitaire pour le formatage des éléments UML.
 * 
 * Fournit des méthodes statiques pour :
 *   - Convertir les visibilités en symboles UML
 *   - Formater les paramètres de méthodes
 *   - Formater les types de retour
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class FormatteurUML 
{
	/**
	 * Convertit une visibilité en symbole UML
	 * @param vis La visibilité à convertir
	 * @return Le symbole UML (+, -, #, ~, ?)
	 */
	public static String getSymboleVisibilite(Visibilite vis) 
	{
		switch (vis)
		{
			case PUBLIC    : return "+";
			case PRIVATE   : return "-";
			case PROTECTED : return "#";
			case PACKAGE   : return "~";
			default        : return " ";
		}
	}

	/**
	 * Formate la liste des paramètres d'une méthode
	 * @param params Liste des paramètres
	 * @param affichageComplet true pour tout afficher, false pour limiter à 3
	 * @return La chaîne formaté
	 */
	public static String formatParametres(List<Parametre> params, boolean affichageComplet) 
	{
		String sRes = "";
		if (params == null || params.isEmpty()) 
			return sRes;

		int nbParamAMettre = ( !affichageComplet && params.size() > 2) ? 2 : params.size();

		for (int i = 0; i < nbParamAMettre; i++)
		{
			Parametre p = params.get(i);
			sRes += p.nom() + " : " + p.type();

			if ( i < nbParamAMettre - 1 ) sRes += ", "; 
		}
		
		// Si on a plus de 2 paramètre, ajouter "..."
		if ( ! affichageComplet && params.size() > 2 ) 
			sRes += " ...";
			
		return sRes;
	}

	/**
	 * Formate le type de retour d'une méthode
	 * @param typeRetour Le type de retour
	 * @param estConstructeur true si c'est un constructeur
	 * @return La chaîne formaté
	 */
	public static String formatTypeRetour(String typeRetour, boolean estConstructeur) 
	{
		if (estConstructeur) 
			return "";

		if ( typeRetour.contains("%default%") )

		// Ne pas afficher " : void" pour les méthodes void
		if (typeRetour == null || typeRetour.equals("void") || typeRetour.trim().isEmpty())
			return "";

		return " : " + typeRetour;
	}
}
