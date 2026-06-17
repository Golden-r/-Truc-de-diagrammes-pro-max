package src.vue.GUI.dessin.classe;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.List;
import src.metier.classe.Attribut;
import src.metier.classe.Classe;
import src.metier.classe.ClasseExterne;
import src.metier.classe.Methode;
import src.metier.enums.TypeClasse;

/**
 * Classe utilitaire pour calculer les dimensions d'une classe UML.
 * * Calcule la largeur et la hauteur de chaque section :
 * - Section nom
 * - Section attributs
 * - Section methodes
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class DimensionsCalculateur
{
	/*-------------------------------*/
	/* Constantes                    */
	/*-------------------------------*/

	private static final int PADDING           = 8;
	private static final int ESPACE_ALIGNEMENT = 5;

	private static final Font FONT_NOM    = new Font("SansSerif", Font.BOLD, 14);
	private static final Font FONT_TYPE   = new Font("SansSerif", Font.ITALIC, 11);
	private static final Font FONT_NORMAL = new Font("SansSerif", Font.PLAIN, 12);

	/*-------------------------------*/
	/* Attributs           */
	/*-------------------------------*/
	//Dimension
	private int largeur;
	private int hauteur;

	// Nom
	private int hNom;

	// Attributs
	private int hAttributs;
	private int maxLargeurNomsAttributs;
	
	// Methodes
	private int hMethodes;
	private int maxLargeurNomsMethodes;

	/*-------------------------------*/
	/* Constructeur          */
	/*-------------------------------*/

	/**
	 * Initialise toutes les dimensions a zero.
	 */
	public DimensionsCalculateur() 
	{
		this.largeur                 = 0;
		this.hauteur                 = 0;
		this.hNom                    = 0;
		this.hAttributs              = 0;
		this.hMethodes               = 0;
		this.maxLargeurNomsAttributs = 0;
		this.maxLargeurNomsMethodes  = 0;
	}

	/*-------------------------------*/
	/* Getters            */
	/*-------------------------------*/
	
	/** Retourne la largeur totale calculée de la classe. */
	public int getLargeur                () { return largeur;                 }
	/** Retourne la hauteur totale calculée de la classe. */
	public int getHauteur                () { return hauteur;                 }
	/** Retourne la hauteur de la section du nom. */
	public int getHauteurNom             () { return hNom;                    }
	/** Retourne la hauteur de la section des attributs. */
	public int getHauteurAttributs       () { return hAttributs;              }
	/** Retourne la hauteur de la section des methodes. */
	public int getHauteurMethodes        () { return hMethodes;               }
	/** Retourne la largeur maximale des noms d'attributs pour l'alignement. */
	public int getMaxLargeurNomsAttributs() { return maxLargeurNomsAttributs; }
	/** Retourne la largeur maximale des noms de methodes pour l'alignement. */
	public int getMaxLargeurNomsMethodes () { return maxLargeurNomsMethodes;  }

	/*-------------------------------*/
	/* Méthodes            */
	/*-------------------------------*/

	/**
	 * Calcule toutes les dimensions d'une classe UML (largeur et hauteurs).
	 * Gere le cas specifique des classes externes (taille fixe).
	 * * @param g2 Le contexte graphique pour les mesures de police.
	 * @param classe La classe a mesurer.
	 * @param lstClasses La liste des classes (pour filtrer les attributs d'association).
	 * @param affichageComplet true pour calculer la taille de tous les elements, false pour tronquer.
	 */
	public void calculer( Graphics2D g2, Classe classe, List<Classe> lstClasses, boolean affichageComplet ) 
	{
		FontMetrics fmNom = g2.getFontMetrics(DimensionsCalculateur.FONT_NOM);
		if (classe instanceof ClasseExterne) {
			this.largeur = fmNom.stringWidth(classe.getNom()) + DimensionsCalculateur.PADDING * 2;
			this.hauteur = 40;
			this.hNom = 40;
			this.hAttributs = 0;
			this.hMethodes = 0;
			return;
		}

		FontMetrics fmType   = g2.getFontMetrics( DimensionsCalculateur.FONT_TYPE   );
		FontMetrics fmMembre = g2.getFontMetrics( DimensionsCalculateur.FONT_NORMAL );
		
		this.calculerLargeur ( classe, lstClasses, fmNom, fmType, fmMembre, affichageComplet );
		this.calculerHauteurs( classe, lstClasses, fmNom, fmType, fmMembre, affichageComplet );

		this.hauteur = hNom + hAttributs + hMethodes;
	}

	/*-------------------------------*/
	/* LARGEUR             */
	/*-------------------------------*/

	/**
	 * Determine la largeur maximale necessaire pour la classe.
	 * Prend en compte le nom, le stereotype (<<interface>>...), les attributs et les methodes.
	 */
	private void calculerLargeur( Classe classe, List<Classe> lstClasses, FontMetrics fmNom, 
								  FontMetrics fmType, FontMetrics fmMembre, boolean affichageComplet ) 
	{
		// Largeur minimale basée sur le nom
		largeur = fmNom.stringWidth( classe.getNom() ) + DimensionsCalculateur.PADDING * 2;

		// Si la classe est pas de TYPE CLASSE 
		if ( classe.getType() != TypeClasse.CLASS ) 
		{
			String typeClasse = "<<" + classe.getType().getType() + ">>";
			largeur = Math.max( largeur, fmType.stringWidth( typeClasse ) + DimensionsCalculateur.PADDING * 2 );
		}

		this.calculerLargeurAttributs( classe, lstClasses, fmMembre        , affichageComplet );
		this.calculerLargeurMethodes ( classe, fmMembre  , affichageComplet                   );

		largeur = Math.max( 150, largeur );
	}


	/* Largeur Attributs             */
	/*-------------------------------*/

	/**
	 * Calcule la largeur requise pour afficher les attributs.
	 * Met a jour maxLargeurNomsAttributs pour l'alignement vertical des types.
	 */
	private void calculerLargeurAttributs( Classe classe, List<Classe> lstClasses, FontMetrics fmMembre, 
										   boolean affichageComplet ) 
	{
		List<Attribut> attributs = classe.getCopieLstAttributsHorsAsso( lstClasses );

		// Largeur max Attribut
		maxLargeurNomsAttributs = 0;
		for (Attribut attr : attributs) 
		{
			String visibilite = FormatteurUML.getSymboleVisibilite( attr.visibilite() );
			String partieNom  = visibilite + " " + attr.nom() + " []"+attr.multiplicite().toString();
			int    largeurNom = fmMembre.stringWidth(partieNom);

			maxLargeurNomsAttributs = Math.max(maxLargeurNomsAttributs, largeurNom);
		}  

		// Largeur max Attribut complet
		for (Attribut attr : attributs) 
		{
			String partieType = " : " + attr.type();
			String suffixe    = " " + attr.getContrainte().getValeur();
			String multiplicite = "";

			// Ajouter la multiplicité si elle n'est pas [0..1] ou [0..*]
			if ( attr.multiplicite() != null && !attr.multiplicite().toString().isEmpty() )
			{
				String mult = "[" + attr.multiplicite().toString() + "]";
				if ( !mult.equals("[0..1]") && !mult.equals("[0..*]") && !mult.equals("[1]") )
				{
					multiplicite = " " + mult;
				}
			}

			// si a une val par défaut
			if ( attr.estFinal() && attr.valeur() != null && !attr.valeur().isEmpty() ) { suffixe += " = " + attr.valeur(); }

			int largeurTotale = maxLargeurNomsAttributs + DimensionsCalculateur.ESPACE_ALIGNEMENT +  // + NomAttribut
								fmMembre.stringWidth( partieType + suffixe + multiplicite );  // : Type { gelé } ... [mult]

			largeur = Math.max( largeur, largeurTotale + DimensionsCalculateur.PADDING * 2 );
		}
	}

	/* Largeur Méthodes           */
	/*-------------------------------*/

	/**
	 * Calcule la largeur requise pour afficher les methodes.
	 * Met a jour maxLargeurNomsMethodes pour l'alignement vertical des types de retour.
	 */
	private void calculerLargeurMethodes(Classe classe, FontMetrics fmMembre, boolean affichageComplet) 
	{
		// Largeur max Non méthode + param
		// + methode ( ... )
		maxLargeurNomsMethodes = 0;
		for (Methode meth : classe.getCopieLstMethodes()) 
		{
			if ( !meth.estConstructeur() ) 
			{
				String visibilite = FormatteurUML.getSymboleVisibilite(meth.visibilite());
				String parametres = FormatteurUML.formatParametres(meth.lstParametres(), affichageComplet);

				String sDefault   = ( meth.estDefault() ? "<<default>> " : "" );
				String partieNom  = sDefault + visibilite + " " + meth.nom() + "(" + parametres + ")";
				
				int   largeurNom  = fmMembre.stringWidth(partieNom);

				maxLargeurNomsMethodes = Math.max(maxLargeurNomsMethodes, largeurNom);
			}
		}

		//  toutes les méthodes  et construc
		for (Methode meth : classe.getCopieLstMethodes()) 
		{
			/**
			 * Méthode 
			 * - visibilité
			 * - nom
			 * - paramètre
			 */
			if ( !meth.estConstructeur() ) 
			{
				String typeRetour   = FormatteurUML.formatTypeRetour(meth.type(), false);
				int   largeurTotale = maxLargeurNomsMethodes + DimensionsCalculateur.ESPACE_ALIGNEMENT + fmMembre.stringWidth( typeRetour );

				largeur = Math.max(largeur, largeurTotale + DimensionsCalculateur.PADDING * 2);
			} 

			/**
			 * Constructeur 
			 * - visibilité
			 * - nom
			 */
			else 
			{
				String visibilite = FormatteurUML.getSymboleVisibilite(meth.visibilite());
				String parametres = FormatteurUML.formatParametres(meth.lstParametres(), affichageComplet);
				String partieNom  = visibilite + " " + meth.nom() + "(" + parametres + ")";

				largeur = Math.max( largeur, fmMembre.stringWidth( partieNom ) + DimensionsCalculateur.PADDING * 2 );
			}
		}
	}


	/*-------------------------------*/
	/* HAUTEURS            */
	/*-------------------------------*/

	/**
	 * Calcule la hauteur de chaque section (nom, attributs, methodes).
	 * Si l'affichage n'est pas complet, limite la hauteur a 3 elements suivis de "...".
	 */
	private void calculerHauteurs( Classe classe, List<Classe> lstClasses, FontMetrics fmNom, 
								   FontMetrics fmType, FontMetrics fmMembre, boolean affichageComplet ) 
	{
		// Hauteur du NOM de la classe
		hNom = fmNom.getHeight() + DimensionsCalculateur.PADDING * 2;
		if ( classe.getType() != TypeClasse.CLASS )
			hNom += fmType.getHeight() + 2;

		List<Attribut> attributs = classe.getCopieLstAttributsHorsAsso( lstClasses );
		List<Methode>  methodes  = classe.getCopieLstMethodes();

		/**
		 * HAUTEUR :
		 * --> affichage complet ou pas ? 
		 * - 3 ou moins ?
		 */
		if (affichageComplet) 
		{
			hAttributs = Math.max ( 25, attributs.size() * ( fmMembre.getHeight() + 2 ) + DimensionsCalculateur.PADDING * 2 );
			hMethodes  = Math.max ( 25, methodes .size() * ( fmMembre.getHeight() + 2 ) + DimensionsCalculateur.PADDING * 2 );
		} 
		else 
		{
			//Attributs
			int nbAttributsAfficher = Math.min(3, attributs.size());
			
			if ( attributs.size() > 3 ) { nbAttributsAfficher++; }
			hAttributs = Math.max( 25, nbAttributsAfficher * (fmMembre.getHeight() + 2) + DimensionsCalculateur.PADDING * 2 );

			//Methpodes
			int nbMethodesAfficher = Math.min(3, methodes.size());

			if ( methodes.size() > 3 ) { nbMethodesAfficher++; }
			hMethodes = Math.max( 25, nbMethodesAfficher * (fmMembre.getHeight() + 2) + DimensionsCalculateur.PADDING * 2 );
		}
	}
}