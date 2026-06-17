package src.metier.lecture;

/**
 * La classe FormateurLigne gère le formatage de lignes de code et les commentaire.
 *
 * Elle permet de retirer :
 *   - les chaînes de caractères,
 *   - les commentaires,
 *   - et de formater les types génériques ou tableaux.
 *
 * Un attribut permet de savoir si la ligne courante est dans un commentaire étalé.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class FormateurLigne
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private boolean estCommentee;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	/**
	 * Constructeur par défaut.
	 * Initialise l'état de commentaire à faux.
	 */
	public FormateurLigne() { this.estCommentee = false; }

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	/**
	 * Indique si la ligne courante est dans un commentaire non terminé.
	 * @return vrai si la ligne est commentée, faux sinon
	 */
	public boolean estCommentee     () { return this.estCommentee;  }

	/**
	 * Réinitialise l'état de commentaire (sort d'un commentaire multi-ligne).
	 */
	public void    resetEstCommentee() { this.estCommentee = false; }

	/* ------------------------------------ */
	/* Méthodes Formatage                   */
	/* ------------------------------------ */
	/**
	 * Remplace les accolades et slashes dans les chaînes de caractères pour éviter 
	 * qu'elles ne soient interprétées comme du code Java.
	 * 
	 * Exemple : public static final String s = "// // {";
	*           devient : public static final String s = "SLASH_14100SLASH_14100 SLASH_14100** ACCO_OUVRANTE";
	*
	* @param ligne la ligne à traiter
	* @return la ligne avec les caractères spéciaux remplacés dans les chaînes
	*/
	public String remplacerAccolades(String ligne) 
	{
		ligne = ligne.trim();
		
		// partie 1
		int posDeb = ligne.indexOf("\"");
		if (posDeb == -1) return ligne;
		
		// partie 3
		int posFin = ligne.indexOf("\"", posDeb + 1);
		if (posFin == -1) return ligne;
		
		// Découper la ligne en 3 parties
		String partie1 = ligne.substring( 0         , posDeb + 1 );  // public static final String = "
		String partie2 = ligne.substring( posDeb + 1, posFin     );  // message
		String partie3 = ligne.substring( posFin                 );  // ";....
		
		partie2 = partie2.replace("{" , "ACCO_OUVRANTE")
						 .replace("}" , "ACCO_FERMANTE")
						 .replace("/" , "SLASH_14100"  );
		
		return partie1 + partie2 + partie3;
	}

	/**
	 * Supprime les commentaires (simples ou multi-lignes) de la ligne donnée.
	 *
	 * Gère les commentaires "//", "/*" ... "* /".
	 * Détermine le nouvelle état de l'attribut estCommentée
	 *
	 * @param ligne la ligne à traiter
	 * @return la ligne sans les commentaires, ou une chaîne vide si la ligne est entièrement commentée
	 */
	public String enleverCommentaireLigne( String ligne )
	{
		// Si la ligne est déjà commentée (commentaire multi-lignes en cours)
		if ( this.estCommentee )
		{
			int indexFin = ligne.indexOf("*/");
			if ( indexFin != -1 )
			{
				ligne = ligne.substring(indexFin + 2);
				this.estCommentee = false;
			}
			else { return ""; }
		}

		// Retirer les commentaire
		// - - - - - - - - - - - - -
		int indexCommentaire1 = ligne.indexOf("//");
		int indexCommentaire2 = ligne.indexOf("/*");
		int indexCommentaire3 = ligne.indexOf("*/");

		/** Entré dans un commetaire
		 *  - //
		 *  - /*
		 *
		 * Suppresion de tous les commentaires mêmes si présents plusieurs fois
		 */
		while ( indexCommentaire1 != -1 || indexCommentaire2 != -1 )
		{
			// "//" avant /*  -> suppr tt le rests
			if (indexCommentaire1 != -1 && (indexCommentaire2 == -1 || indexCommentaire1 < indexCommentaire2) )
			{
				ligne = ligne.substring(0, indexCommentaire1);
				break;
			}

			/** Présence de /* :
			 *   - si pas de fin on suppr tt et commenté = true
			 *   - fin du comm trouvé don suppr juste la partie voulue
			 */
			if (indexCommentaire2 != -1)
			{
				if (indexCommentaire3 != -1 && indexCommentaire3 > indexCommentaire2)
				{
					ligne = ligne.substring(0, indexCommentaire2) + ligne.substring(indexCommentaire3 + 2);
				}
				else
				{
					ligne             = ligne.substring(0, indexCommentaire2);
					this.estCommentee = true;
					break;
				}
			}

			// Si d'autre commentaire sur la ligne
			indexCommentaire1 = ligne.indexOf("//");
			indexCommentaire2 = ligne.indexOf("/*");
			indexCommentaire3 = ligne.indexOf("*/");
		}

		return ligne;
	}

	/**
	 * Formate une ligne contenant des types génériques (<>) ou des tableaux ([]), en supprimant les espaces inutiles.
	 *
	 * "String [ ] tab"    -> "String[] tab"
	 * "List < Type > lst" -> "List<Type> lst".
	 *
	 * @param ligne la ligne à formater
	 * @param type 'g' pour générique, 't' pour tableau
	 * @return la ligne formatée
	 */
	public String formaterLigneGeneriqueTableau( String ligne, char type )
	{
		if (ligne == null) { return null; }

		// Données :
		// - - - - - - -
		String  resultat        = "";
		int     niveau          = 0;
		boolean espaceEnAttente = false;
		char    c1;
		char    c2;

		switch ( type )
		{
			case 'g' -> { c1 = '<'; c2 = '>'; } // générqiue
			case 't' -> { c1 = '['; c2 = ']'; } // tableau
			default  -> { return ligne;       }
		}

		/** Formatter les types
		 *  - String    [   ]   tab  -> String[] tab
		 *  - List   < TYPE   > lst  -> List<String> lst
		 */
		for ( char cara : ligne.toCharArray() )
		{
			// ENTRE : nv ++ et !espace et recup
			if ( cara == c1 )
			{
				espaceEnAttente = false;
				resultat        += cara;
				niveau++;
			}

			// SORTI : nv -- et recup
			else if ( cara == c2 )
			{
				niveau--;
				resultat += cara;
			}

			// ESPACE AVANT ENTREE
			else if ( cara == ' ' )
			{
				if (niveau == 0)
					espaceEnAttente = true;
			}
			else
			{
				// AJOUT ESPACE VALIDE
				if (espaceEnAttente)
				{
					resultat       += " ";
					espaceEnAttente = false;
				}

				// AJOUT CARA
				resultat += cara;
			}
		}

		return resultat;
	}
}