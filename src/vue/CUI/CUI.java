package src.vue.CUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.ControleurCUI;
import src.metier.classe.Association;
import src.metier.classe.Attribut;
import src.metier.classe.Classe;
import src.metier.classe.Methode;
import src.metier.classe.Parametre;
import src.metier.enums.Portee;
import src.metier.enums.TypeClasse;
import src.utils.CouleurUtils;

/**
 * Interface utilisateur en mode console.
 * Gère l'affichage des classes sous différents formats (simple, UML) et l'interaction avec l'utilisateur.
 *
 * Propose plusieurs modes d'affichage :
 *   - Affichage simple des classes
 *   - UML d'une classe spécifique
 *   - UML de plusieurs classes
 *   - Affichage complet avec associations
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class CUI
{
	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	private ControleurCUI ctrl;
	private Scanner       sc;

	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/
	/**
	 * Initialise l'interface utilisateur avec le contrôleur associé.
	 * @param ctrl le contrôleur de l'application
	 */
	public CUI( ControleurCUI ctrl )
	{
		this.ctrl = ctrl;
		this.sc   = new Scanner( System.in );
	}

	/*-------------------------------*/
	/*         Méthode lancer        */
	/*-------------------------------*/
	/**
	 * Lance l'interface utilisateur et gère la boucle principale de l'application.
	 */
	public void lancer()
	{
		int choix;

		do
		{
			choix = this.menuPrincipal();
			this.ctrl.resetLstClasses();

			
			switch ( choix )
			{
				case 1 -> this.ouvertureFichierSimple ();
				case 2 -> this.ouvertureRepertoire    ();
			}
			
			if ( ! this.ctrl.getLstClasses().isEmpty() )
			{
				this.ctrl.afficherCUI( 3 );
			}

		}
		while  ( choix != 3 );

		System.out.println( CouleurUtils.rouge("Fermeture du programme.") );
		this.sc.close();
	}

	/*-------------------------------*/
	/*           Menus               */
	/*-------------------------------*/
	/**
	 * Affiche le menu principal et retourne le choix de l'utilisateur.
	 * @return le choix de l'utilisateur (1: ouverture simple, 2: répertoire, 3: quitter)
	 */
	public int menuPrincipal()
	{
		int choix;

		/*+----------------------------------------------------------------------------------------+*/
		/*|                                    Menu de lancement                                   |*/
		/*+----------------------------------------------------------------------------------------+*/
		System.out.println(                                                                         );
		System.out.println( "+-------------------------------------+"                               );
		System.out.println( "|          Menu de lancement          |"                               );
		System.out.println( "+-------------------------------------+"                               );
		System.out.println( "|  Choix :                            |"                               );
		System.out.println( "|      - " + CouleurUtils.bleu( "1" ) + " : Ouverture Fichier        |" );
		System.out.println( "|      - " + CouleurUtils.bleu( "2" ) + " : Ouverture Répertoire     |" );
		System.out.println( "|      - " + CouleurUtils.bleu( "3" ) + " : Quitter                  |" );
		System.out.println( "+-------------------------------------+"                                );
		System.out.print("\tChoix : ");

		System.out.print( CouleurUtils.bleuSeul() );
		choix = this.lireInt();
		System.out.print( CouleurUtils.reset() );

		while (choix < 1 || choix > 3)
		{
			System.out.println( CouleurUtils.rouge("Erreur de saisie") );
			System.out.print("\tChoix : ");

			System.out.print( CouleurUtils.bleuSeul() );

			choix = this.lireInt();

			System.out.print( CouleurUtils.reset() );
		}

		return choix;
	}

	/*-------------------------------*/
	/*       Actions utilisateur     */
	/*-------------------------------*/
	/**
	 * Demande à l'utilisateur le chemin d'un fichier et charge la classe correspondante.
	 */
	private void ouvertureFichierSimple()
	{
		System.out.print( "Entrez le chemin du fichier : " );
		String chemin = this.sc.nextLine();

		this.ctrl.creerClasse( chemin );
	}

	/**
	 * Demande à l'utilisateur le chemin d'un fichier et charge la classe correspondante.
	 */
	private void ouvertureRepertoire()
	{
		System.out.print( "Entrez le chemin du repertoire : " );
		String chemin = this.sc.nextLine();

		this.ctrl.ouvertureRepertoire( chemin );
	}


	/*-------------------------------*/
	/*         Utilitaires           */
	/*-------------------------------*/
	/**
	 * Lit un entier depuis l'entrée utilisateur.
	 * @return l'entier lu, ou -1 en cas d'erreur
	 */
	private int lireInt()
	{
		int valeur = -1;
		try
		{
			valeur = Integer.parseInt( this.sc.nextLine() );
		
		} catch (NumberFormatException e) { valeur = -1; }

		return valeur;
	}

	/*-------------------------------*/
	/*        ETAPE  4               */
	/*-------------------------------*/
	/**
	 * Génère une représentation UML complète d'une classe avec ses associations.
	 * 
	 * L'affichage se fait via l'appele de la méthode afficherClasse( Classe, List<Classe>, boolean ).
	 * Le boolean permet de précisier si on affiche les assiciations.
	 *  - true  = Afficher avec association
	 *  - false = Ne pas affhicher les association. 
	 * 
	 * @param c la classe à afficher
	 * @param lstClasses la liste de toutes les classes
	 * @return la chaîne représentant la classe en format UML complet
	 */
	public String toString( Classe c, List<Classe> lstClasses )
	{
		return afficherClasse(c, lstClasses, true, this.ctrl);
	}

	/**
	 * Affiche une classe au format UML.
	 * 
	 * @param c la classe à afficher
	 * @param lstClasses la liste de toutes les classes
	 * @param afficherAssociations vrai pour afficher les associations
	 * @param ctrl le contrôleur pour récupérer les associations
	 * @return la chaîne représentant la classe
	 */
	private static String afficherClasse( Classe c, List<Classe> lstClasses, boolean afficherAssociations, ControleurCUI ctrl )
	{
		// Données :
		// - - - - - - - -
		String            sRet                 = "";
		int               largeurAtt           = CUI.longueurMaxAttribut ( c.getCopieLstAttributsHorsAsso(lstClasses) );
		int               largeurMaxNomMethode = CUI.longeurMaxNomMethode( c.getCopieLstMethodes                   () );
		int               largeurMaxParams     = CUI.longeurMaxParams    ( c.getCopieLstMethodes                   () );
		ArrayList<String> attributs            = new ArrayList< String >();
		ArrayList<String> methodes             = new ArrayList< String >();
		ArrayList<String> enu                  = new ArrayList< String >();
		

		// Ajout : 
		// - - - - - - - - -
		for (Attribut attr : c.getCopieLstAttributsHorsAsso(lstClasses)) { attributs.add( CUI.formatAttribut(attr, largeurAtt                            ) ); }
		for (Methode  meth : c.getCopieLstMethodes                   ()) { methodes .add( CUI.formatMethode (meth, largeurMaxNomMethode, largeurMaxParams) ); }
		
		int            largeurMax = CUI.longueurMax     ( c.getNom(), attributs, methodes );
		String         ligne      = CUI.formatSeparateur( largeurMax                      );
		
		sRet += ligne;
		sRet += CUI.formatNom( c.getNom(), largeurMax, c.getType() ) + "\n";
		sRet += ligne;

		if ( c.getType() == TypeClasse.ENUM )
		{
			for ( String e : enu ) { sRet += e + "\n"; }
			sRet += ligne;
		}

		for ( String attr : attributs ) { sRet += attr + "\n"; }

		sRet += ligne;

		for ( String meth : methodes ) { sRet += meth + "\n"; }

		sRet += ligne;

		if ( afficherAssociations )
		{
			List<Association> lstAsso = ctrl.getLstAssoDeClasse( c );
			if ( !lstAsso.isEmpty() )
			{
				int cptAsso = 0;
				for ( Association asso : lstAsso ) 
				{ 
					sRet += String.format( "Association %2d : %s", ++cptAsso, CouleurUtils.rouge(asso + "\n") ); 
				}
				sRet += ligne;
			}

			if ( c.getExtend() != null )
			{
				sRet += "Extend : " + CouleurUtils.rouge( c.getExtend() + "\n" );
				sRet += ligne;
			}

			if ( !c.getCopieLstImplementes().isEmpty() )
			{
				int cptImpl = 0;
				for (String impl : c.getCopieLstImplementes()) 
				{ 
					sRet += String.format( "Implements %2d : %s", ++cptImpl, CouleurUtils.rouge(impl + "\n") );
				}
				sRet += ligne;
			}
		}

		return sRet;
	}

	/*-------------------------------*/
	/*       Gestions longueurs      */
	/*-------------------------------*/

	/**
	 * Calcule la largeur maximale pour l'affichage d'une classe.
	 * @param nom le nom de la classe
	 * @param attributs la liste des attributs formatés
	 * @param methodes la liste des méthodes formatées
	 * @return la largeur maximale
	 */
	private static int longueurMax( String nom, ArrayList<String> attributs, ArrayList<String> methodes )
	{
		int max = nom.length();

		for (String attr : attributs) { max = Math.max(max, attr.length()); }
		for (String meth : methodes ) { max = Math.max(max, meth.length()); }

		return max;
	}

	/**
	 * Calcule la longueur maximale des noms d'attributs.
	 * @param lstAttributs la liste des attributs
	 * @return la longueur maximale
	 */
	private static int longueurMaxAttribut( List<Attribut> lstAttributs )
	{
		int max = 0;

		for (Attribut attr : lstAttributs) { max = Math.max(max, attr.nom().length()); }

		return max;
	}

	/**
	 * Calcule la longueur maximale des noms de méthodes.
	 * @param lstMethodes la liste des méthodes
	 * @return la longueur maximale
	 */
	private static int longeurMaxNomMethode( List<Methode> lstMethodes )
	{
		int max = 0;

		for (Methode meth : lstMethodes) { max = Math.max(max, meth.nom().length()); }

		return max;
	}

	/**
	 * Calcule la longueur maximale des paramètres de méthodes.
	 * @param lstMethodes la liste des méthodes
	 * @return la longueur maximale
	 */
	private static int longeurMaxParams( List<Methode> lstMethodes )
	{
		int max = 0;

		for (Methode meth : lstMethodes)
		{
			if (!meth.estConstructeur()) { max = Math.max(max, formatParametres(meth.lstParametres()).length()); }
		}

		return max;
	}

	/*-------------------------------*/
	/*           FORMATAGE           */
	/*-------------------------------*/

	/**
	 * Crée une ligne de séparation colorée.
	 * @param largeur la largeur de la ligne
	 * @return la ligne de séparation
	 */
	private static String formatSeparateur( int largeur ) 
	{ 
		return CouleurUtils.jaune("-".repeat(largeur) + "\n"); 
	}

	/**
	 * Formate le nom d'une classe avec son type (interface, enum, etc.).
	 * @param nom le nom de la classe
	 * @param max la largeur maximale
	 * @param type le type de classe
	 * @return le nom formaté et centré
	 */
	private static String formatNom( String nom, int max, TypeClasse type )
	{
		String sRet   = "";
		int    maxNom = (max / 2) - (nom.length() / 2);

		if (type != null && type != TypeClasse.CLASS)
		{
			String typeFormat = "<<" + type.getType() + ">>";
			int    maxType    = (max / 2) - (typeFormat.length() / 2);

			sRet += " ".repeat(maxType) + CouleurUtils.rouge(typeFormat) + "\n";
		}
		return sRet + CouleurUtils.rouge(" ".repeat(maxNom) + nom);
	}

	/**
	 * Formate un attribut pour l'affichage UML.
	 * @param attribut l'attribut à formater
	 * @param largeur la largeur maximale
	 * @return l'attribut formaté
	 */
	private static String formatAttribut( Attribut attribut, int largeur )
	{

		String visibilite = CUI.getVisibilite(attribut.visibilite().getFrancais());
		String nom        = attribut.nom();
		String type       = attribut.type().isEmpty() ? "" : " : " + attribut.type();

		String valeurFinal = null;

		String signature = (largeur == 0) ? nom + type : String.format("%-" + largeur + "s%s", nom, type);
		
		if ( attribut.estFinal() )
			valeurFinal = attribut.valeur();

		signature = CUI.appliquerStyle( signature, attribut.estFinal(), attribut.portee(), valeurFinal );

		return String.format(" %s %s", visibilite, signature);
	}

	/**
	 * Formate une méthode pour l'affichage UML.
	 * @param methode la méthode à formater
	 * @param largeurMaxNomMethode la largeur maximale des noms de méthodes
	 * @param largeurMaxParams la largeur maximale des paramètres
	 * @return la méthode formatée
	 */
	private static String formatMethode( Methode methode, int largeurMaxNomMethode, int largeurMaxParams )
	{
		String visibilite = CUI.getVisibilite(methode.visibilite().getFrancais());
		String nom        = methode.nom();
		String params     = CUI.formatParametres(methode.lstParametres());
		String type       = ( methode.type().isEmpty() || methode.type().equals("void")) ? "" : methode.type();
		String estDefault = ( methode.estDefault() ? "<<default>> " : "" );

		String signature;
		String nomEtParams = nom + " (" + params + ")";

		nomEtParams = String.format( "%-" + (largeurMaxNomMethode + 3 + largeurMaxParams) + "s", nomEtParams ); // 3 pour " (" et ")"

		String typeRetour = "";
		if ( !methode.type().equals("void") ) { typeRetour = " : " + estDefault + type; }
	
		if ( methode.estConstructeur() ) { signature = nomEtParams;                    }
		else                             { signature = nomEtParams + typeRetour; }

		signature = CUI.appliquerStyle( signature.trim(), false, methode.portee(), "" );

		return String.format(" %s %s", visibilite, signature);
	}

	/**
	 * Formate la liste des paramètres d'une méthode.
	 * @param parms la liste des paramètres
	 * @return les paramètres formatés
	 */
	private static String formatParametres( List<Parametre> parms )
	{
		if (parms.isEmpty()) { return ""; }

		String parametres = "";

		for (int cptParam = 0; cptParam < parms.size(); cptParam++)
		{
			parametres += " " + parms.get(cptParam).nom() + " : " + parms.get(cptParam).type();

			if (cptParam < parms.size() - 1) { parametres += ","; }
			else                             { parametres += " "; }
		}

		return parametres;
	}

	/**
	 * Applique le style (souligné pour portee classe, gelé pour final) à une ligne.
	 * @param ligne la ligne à styler
	 * @param estFinal vrai si l'attribut est final
	 * @param porte la portée de l'élément
	 * @param valeurFinal la valeur si final
	 * @return la ligne stylée
	 */
	private static String appliquerStyle( String ligne, boolean estFinal, Portee porte, String valeurFinal )
	{
		if (estFinal                                       ) { ligne += " {gelé}";                                                   }
		if (valeurFinal != null && !valeurFinal.equals("") ) { ligne += " = " + valeurFinal;                                         }
		if (porte == Portee .CLASSE                        ) { ligne = CouleurUtils.souligneSeul() + ligne + CouleurUtils.reset();   }

		return ligne;
	}
	/**
	 * Convertit une visibilité en symbole UML coloré.
	 * @param vis la visibilité en français
	 * @return le symbole UML coloré
	 */
	private static String getVisibilite( String vis )
	{
		switch (vis)
		{
			case "publique"  : return CouleurUtils.cyan("+");
			case "privée"    : return CouleurUtils.vert("-");
			case "protégée"  : return CouleurUtils.noir("~");
			case "paquetage" : return CouleurUtils.noir("#");
			default          : return CouleurUtils.gris("#");
		}
	}
}