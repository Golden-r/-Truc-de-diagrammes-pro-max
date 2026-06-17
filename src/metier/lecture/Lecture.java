package src.metier.lecture;

import java.io.FileInputStream;
import java.util.Scanner;
import src.metier.classe.Classe;
import src.metier.enums.*;

/**
 * Lis un fichier source Java qui compile.
 * Cette classe gère la lecture, le formatage des lignes et l'instanciation
 * des éléments (Classe, Attributs, Méthodes) via la FabriqueJava.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class Lecture
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private boolean        bFinEnum;
	private boolean        bIgnorerCorps;
	private int            niveau;
	private String         instructionEnCours;
	private FormateurLigne formateurLigne;
	private Fabrique       fabriqueJava;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise les composants de formatage et de fabrication.
	 */
	public Lecture()
	{
		this.formateurLigne = new FormateurLigne();
		this.fabriqueJava   = new Fabrique      ();
	}

	/*-------------------------------*/
	/* Lecture                       */
	/*-------------------------------*/

	/**
	 * Analyse un fichier Java et construit la représentation objet de la classe.
	 * Parcourt le fichier ligne par ligne, nettoie le code et interprète les instructions.
	 *
	 * @param fichier Le chemin absolu du fichier à analyser.
	 * @return L'objet Classe construit, ou null en cas d'erreur.
	 */
	public Classe lireFichier( String fichier )
	{
		// Données
		int     niveauPrc;
		String  ligne;

		Classe  classe          = null;

		this.niveau             = 0;
		this.bIgnorerCorps      = false;
		this.bFinEnum           = false;
		this.instructionEnCours = "";

		this.formateurLigne.resetEstCommentee();

		// Lecture
		try
		{
			Scanner scFichier = new Scanner( new FileInputStream( fichier ) , "UTF-8" );

			while ( scFichier.hasNextLine() )
			{
				ligne = scFichier.nextLine();
				
				/** 1
				 * Formatter si on a du final et un chaine instancié
				 * 
				 *    - final String EXEMPLE = "test{ }"; 
				 *    -> final String EXEMPLE = "testACCOLADE_OUVRANTE ACCOLADE_FERMANTE"; 
				 * 
				 * -- Permet de ne pas biaiser la gestion des niveaux.
				 */
				if ( ligne.contains("final") && ligne.contains("\"") )
				{
					ligne = this.formateurLigne.remplacerAccolades( ligne );
				}

				/** 2
				 * Calculer le niveaux.
				 * 
				 *    - Gerer la calcul des niveaux d'acolade.
				*/
				niveauPrc = this.niveau;                                // se placer au nouveau niveau 
				if ( this.doitIgnorerLigne( niveauPrc ) ) { continue; } // dans méthode
				this.determinerNiveau( ligne );                         // calculer le niveau
				

				/** 3
				 * Créer l'instruction en cours : 
				 * 
				 *     - niveau <= 1 ( hors méthodes )
				 *     - fomattage
				 *     - incrémente l'instruction
				 *     - analyse la ligne en cours d'instruction
				*/
				if ( niveauPrc <= 1 )
				{
					ligne = this.formaterLigne( ligne );
					//if ( ! ligne.trim().isEmpty() ) System.out.println( "\t"+ ligne); // pas enlever

					// pas traiter les lignes vide
					if ( !ligne.isEmpty() )
					{
						this.instructionEnCours += ligne;
						classe = this.construireInstruction( classe );
					}
				}
			}
			scFichier.close();

		} catch (Exception e) { e.printStackTrace(); System.out.println( "Erreur lors de la tentative de lecture de : " + fichier + ".\nLe lien est-il le bon ?\nLa classe est bien indenté ?"); }

		return classe;
	}

	/**
	 * Met à jour le niveau d'imbrication en fonction des accolades.
	 * @param ligne La ligne contenant potentiellement des { ou }.
	 */
	private void determinerNiveau( String ligne )
	{
		char cara;

		for ( int cptChar = 0; cptChar < ligne.length(); cptChar++ )
		{
			cara = ligne.charAt( cptChar );

			if      ( cara == '{' ) { this.niveau++; }
			else if ( cara == '}' ) { this.niveau--; }
		}
	}

	/**
	 * Gère l'ignorance du contenu des corps de méthodes (niveau > 1).
	 * Retourne true si la ligne doit être ignorée, false sinon.
	 * 
	 * @param niveauPrecedent Le niveau avant la lecture de la ligne actuelle.
	 * @return true si la ligne doit être ignorée (corps de méthode).
	 */
	private boolean doitIgnorerLigne(int niveauPrc )
	{
		/** On ignore la ligne si le niveau est :
		 *   - on est dans la class ( donc on est pas dans une méthode )
		 *      -> donc nvPrc = 1 
		 * 
		 *   - le nv > 1
		 * 
		 * On saute la ligne
		*/
		if ( niveauPrc == 1     && this.niveau >  1 ) { this.bIgnorerCorps = true;  }
		if ( this.bIgnorerCorps && this.niveau <= 1 ) { this.bIgnorerCorps = false; }
		
		return this.bIgnorerCorps;
	}

	/**
	 * Nettoie et formate une ligne brute du fichier source.
	 * Retire les commentaires, gère les accolades et normalise les espacements
	 * pour les génériques et tableaux.
	 *
	 * @param ligne La ligne brute.
	 * @return La ligne formatée prête pour l'analyse, ou une chaîne vide si ignorée.
	 */
	private String formaterLigne( String ligne )
	{		
		/**
		 * Commentaire :
		 *  -- //
		 *  -- /*
		 *  -- * /
		 *  -- deja commenté 
		 */
		if ( ligne.contains("//") || ligne.contains("/*") || ligne.contains("*/") )
		{
			ligne = this.formateurLigne.enleverCommentaireLigne(ligne);
		}
		if ( this.formateurLigne.estCommentee() ) { return ""; }

		/**
		 * Supprimer les accolades
		 */
		if ( ligne.contains("{") && ligne.contains( "}" ) )
		{
			int indexAcoOuvrante = ligne.indexOf( "{" );
			int indexAcoFermante = ligne.indexOf( "}" );
			ligne = ligne.substring(0, indexAcoOuvrante) + ligne.substring(indexAcoFermante+1);
		}
		
		if ( ligne.trim().startsWith( "import" ) || ligne.trim().startsWith( "package" ) || ligne.trim().startsWith( "@" ) )
		{
			return "";
		}

		if ( ligne.contains( "[" ) ) { ligne = this.formateurLigne.formaterLigneGeneriqueTableau(ligne, 't'); }
		if ( ligne.contains( "<" ) ) { ligne = this.formateurLigne.formaterLigneGeneriqueTableau(ligne, 'g'); }


		// forcer le formatage
		ligne = ligne.trim().replace("{", ""   ).replace("}", ""   )
							.replace(")", " ) ").replace("(", " ( ")
							.replace(";", " ; ")
							.replace("=", " = ");

		return ligne.trim();
	}

	/**
	 * Interprète l'instruction en cours pour construire les éléments de la classe.
	 * Identifie la déclaration de la classe, les énumérations, les méthodes et les attributs.
	 *
	 * @param ligne L'instruction accumulée en cours de traitement.
	 * @param classe L'objet Classe en cours de construction.
	 * @return L'objet Classe mis à jour ou nouvellement créé.
	 */
	private Classe construireInstruction( Classe classe )
	{
		// CLASSE / INTERFACE / ENUM / RECORD
		if ( classe == null )
		{
			for ( TypeClasse typeClasse : TypeClasse.values() )
			{
				if ( this.instructionEnCours.contains( typeClasse.getType() ) )
				{
					// Si le record est sur plusieurs lignes
					if ( typeClasse == TypeClasse.RECORD && ! this.instructionEnCours.contains( ")" ) ) { break; }

					classe                  = this.fabriqueJava.creerClasse( this.instructionEnCours );
					this.instructionEnCours = "";
					return classe;
				}
			}
		}

		// Dans une classe
		if ( classe != null )
		{
			/** ENUM
			 *  - INSTANCE ( "instance" , "instance" ),  -> contient ','
			 *  - CLASSE   ( "static"   , "classe"   );  -> contient ';'
			 * est forcement AVANT les attributs.
			 */
			if ( classe.getType() == TypeClasse.ENUM &&
			   ( this.instructionEnCours.contains( ",") || this.instructionEnCours.contains( ";") ) &&
				!this.bFinEnum )
			{
				if ( this.instructionEnCours.contains( ";") ) { bFinEnum = true; }

				classe.ajouterAttribut( this.fabriqueJava.creerEnumAttribut( new Scanner( this.instructionEnCours ), classe.getNom() ) );
				this.instructionEnCours = "";
			}

			/** METHODE
			 *   - contient '(' et ')'
			 *   - pas de '='
			 */
			else if ( this.instructionEnCours.contains("(") && this.instructionEnCours.contains(")") && !this.instructionEnCours.contains("=") )
			{
				classe.ajouterMethode( this.fabriqueJava.creerMethode( this.instructionEnCours, classe.getNom()) );
				this.instructionEnCours = "";
			}

			/** ATTRIBUT
			 *  - contient ';'
			*/
			else if ( this.instructionEnCours.contains( ";") )
			{
				classe.ajouterAttribut( this.fabriqueJava.creeAttribut(this.instructionEnCours) );
				this.instructionEnCours = "";
			}
		}

		return classe;
	}
}