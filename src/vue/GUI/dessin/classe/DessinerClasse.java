package src.vue.GUI.dessin.classe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.List;
import src.metier.classe.Attribut;
import src.metier.classe.Classe;
import src.metier.classe.ClasseExterne;
import src.metier.classe.Methode;
import src.metier.enums.Portee;
import src.metier.enums.TypeClasse;

/**
 * Classe responsable du dessin d'une classe UML.
 *
 * Dessine les 3 sections d'une classe :
 * - Section nom       ( avec le type si interface/enum/record )
 * - Section attributs ( avec visibilite et type )
 * - Section methodes  ( avec visibilite, parametres et type retour )
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class DessinerClasse
{
	/*-------------------------------*/
	/* Constantes                    */
	/*-------------------------------*/

	private static final int         EPAISSEUR_BORDURE = 2;
	private static final int         PADDING           = 8;
	private static final int         ESPACE_ALIGNEMENT = 5;

	private static final BasicStroke STROKE_BORDURE    = new BasicStroke(DessinerClasse.EPAISSEUR_BORDURE);
   
	private static final Font        FONT_NOM          = new Font("SansSerif", Font.BOLD, 14);
	private static final Font        FONT_TYPE         = new Font("SansSerif", Font.ITALIC, 11);
	private static final Font        FONT_NORMAL       = new Font("SansSerif", Font.PLAIN, 12);


	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/

	/**
	* Dessine la representation graphique complete d'une classe UML.
	* Gere l'affichage du fond, des bordures et l'appel aux methodes de dessin des sections.
	*
	* @param g2 Le contexte graphique 2D.
	* @param classe La classe a dessiner.
	* @param lstClasses La liste de toutes les classes (pour references).
	* @param dims Les dimensions calculees de la classe.
	* @param x Position X.
	* @param y Position Y.
	* @param affichageComplet Si vrai, affiche tous les membres. Si faux, tronque apres 3 elements.
	*/
	public void dessiner( Graphics2D g2, Classe classe, List<Classe> lstClasses, DimensionsCalculateur dims, 
						  int x, int y, boolean affichageComplet )
	{
		int largeur = dims.getLargeur();
		int hauteur = dims.getHauteur();
		int yActuel = y;

		if (classe instanceof ClasseExterne classeExterne) 
		{
			dessinerSectionNomClasseExterne(g2, classeExterne, dims, x, yActuel );  
			return;
		}
		
		// Fond blanc
		g2.setColor( Color.WHITE            );
		g2.fillRect( x, y, largeur, hauteur );

		// Bordure noire
		g2.setColor ( Color.BLACK                   );
		g2.setStroke( DessinerClasse.STROKE_BORDURE );
		g2.drawRect ( x, y, largeur, hauteur        );

		// Nom de la classe + Type si pas Type.CLASSE
		this.dessinerSectionNom( g2, classe, dims, x, yActuel );
		yActuel += dims.getHauteurNom();

		boolean titreOnly = (classe instanceof ClasseExterne);
		if (titreOnly)
		{
			// Affiche uniquement le titre (nom + éventuel type) : aucune section attributs/méthodes.
			g2.drawLine( x, yActuel, x + largeur, yActuel );
			return;
		}


		// Lst des Attributs ( complet ou pas ?)
		this.dessinerSectionAttributs( g2, classe, lstClasses, dims, x, yActuel, affichageComplet );
		yActuel += dims.getHauteurAttributs();

		// Lst des Méthpdes ( complet ou pas ?)
		this.dessinerSectionMethodes( g2, classe, dims, x, yActuel, affichageComplet );
	}


	/**
	 * Dessine l'entete specifique pour une classe externe (fond gris).
	 */
	private void dessinerSectionNomClasseExterne( Graphics2D g2, ClasseExterne classe, 
												DimensionsCalculateur dims, int x, int ySection ) 
	{
// Même style que la section "titre" des classes internes, mais avec une bordure visible.
		int largeur = dims.getLargeur();
		int hNom    = dims.getHauteurNom();

		// Fond
		g2.setColor( Color.WHITE );
		g2.fillRect( x, ySection, largeur, hNom );

		// Bordure (pour éviter que la bordure de classe soit masquée/peu visible sur une classe externe)
		g2.setColor( Color.BLACK );
		g2.setStroke( DessinerClasse.STROKE_BORDURE );
		g2.drawRect( x, ySection, largeur, hNom );


		// Texte
		int yTexte = ySection + DessinerClasse.PADDING;
		g2.setFont( DessinerClasse.FONT_NOM );
		FontMetrics fmNom = g2.getFontMetrics();
		int xCentreNom    = x + ( largeur - fmNom.stringWidth( classe.getNom() ) ) / 2;
		g2.setColor( Color.BLACK );
		g2.drawString( classe.getNom(), xCentreNom, yTexte + fmNom.getAscent() );

		// Légère séparation identique (une seule ligne, fin de la section titre)
		g2.setColor( Color.BLACK );
		g2.drawLine( x, ySection + hNom, x + largeur, ySection + hNom );
	}

	/* Nom + Type.CLASSE             */
	/*-------------------------------*/

	/**
	 * Dessine la section superieure contenant le nom de la classe.
	 * Ajoute le stereotype (ex: <<interface>>) si necessaire.
	 */
	private void dessinerSectionNom(Graphics2D g2, Classe classe, DimensionsCalculateur dims, int x, int ySection)
	{
		// Données :
		// - - - - - -
		int largeur = dims.getLargeur   ();
		int hNom    = dims.getHauteurNom();


		// g2.setColor(new Color(220, 220, 220));
		// g2.fillRect(x + 1, ySection + 1, largeur - 2, hNom - 1);
		int yTexte = ySection + DessinerClasse.PADDING;

		/**
		* Nom de la Classe
		* - Police
		* - Trouver le milieu
		* - Dessiner
		* - incrmenter la posY si !TypeClasse.CLASS
		*/
		g2.setFont( DessinerClasse.FONT_NOM );
		FontMetrics fmNom       = g2.getFontMetrics();
		int         xCentreNom = x + ( largeur - fmNom.stringWidth( classe.getNom() ) ) / 2;
		g2.drawString( classe.getNom(), xCentreNom, yTexte + fmNom.getAscent() );
		yTexte += fmNom.getHeight() + 2;

		/**
		* Si Type.CLASSE est faux
		* --> <<Autre Type>>
		* - Police
		* - Dessiner
		*/
		if ( classe.getType() != TypeClasse.CLASS )
		{
			g2.setFont(DessinerClasse.FONT_TYPE);
			FontMetrics fmType      = g2.getFontMetrics();
			String      typeClasse  = "<<" + classe.getType() + ">>";
			int         xCentreType = x + ( largeur - fmType.stringWidth( typeClasse ) ) / 2;

			g2.drawString( typeClasse, xCentreType, yTexte + fmType.getAscent() );
		}

		// Ligne de séparation
		g2.setColor( Color.BLACK                                      );
		g2.drawLine( x, ySection + hNom, x + largeur, ySection + hNom );
	}

	/* Attributs                     */
	/*-------------------------------*/

	/**
	 * Dessine la section centrale contenant la liste des attributs.
	 * Gere l'alignement des types, le soulignement des elements statiques et la troncature (...) si necessaire.
	 */
	private void dessinerSectionAttributs( Graphics2D g2, Classe classe, List<Classe> lstClasses, DimensionsCalculateur dims, 
									   int x, int ySection, boolean affichageComplet                                     )
	{
	// Données :
	// - - - - - -
	List<Attribut> lstAttributs  = classe.getCopieLstAttributsHorsAsso( lstClasses );

	int            largeur       = dims  .getLargeur          ();
	int            hAttributs    = dims  .getHauteurAttributs ();
	g2.setColor( Color.BLACK                );
	g2.setFont ( DessinerClasse.FONT_NORMAL );
	FontMetrics fm     = g2.getFontMetrics();
	int         yTexte = ySection + DessinerClasse.PADDING + fm.getAscent();
	int         xDebut = x + DessinerClasse.PADDING;
	
	/**
	 * Dessiner les Attributs :
	 * - Il y en a ?
	 * - Complet ?
	 * -> Non, plus de 3
	 */
	// pas d'attribut -> on draw la ligne et on coupe la méthode
	if ( lstAttributs.isEmpty() )
	{
		g2.drawLine( x, ySection + hAttributs, x + largeur, ySection + hAttributs );
		return;
	}
	
	// complet ou pas et sinon combien ?
	int nbAttributDessiner = affichageComplet ? lstAttributs.size() : Math.min(3, lstAttributs.size());
	
	/**
	 * Dessiner un Attribut :
	 * - 2 parties :
	 * -> [Visibilité] + [   Nom    ]
	 * -> [   Type   ] + [ [ estFinal ] = [ valeurDefaut ] ]
	 *
	 * - Dessin :
	 * -> Visibilité + nom
	 * |-> souligner si static
	 * -> Type + suffixe
	 * |-> alligné avec le plus long attribust
	 */
	for (int cpt = 0; cpt < nbAttributDessiner; cpt++ )
	{
		Attribut attr         = lstAttributs.get( cpt );
		
		String   visibilite   = FormatteurUML.getSymboleVisibilite( attr.visibilite() );
		String   partieNom    = visibilite + " " + attr.nom();
		String   partieType   = " : " + attr.type();
		String   suffixe      = "";
		String   multiplicite = "";
		
		if ( attr.estFinal() && attr.valeur() != null && !attr.valeur().isEmpty() ) { suffixe += " = " + attr.valeur(); }

		suffixe += " " + attr.getContrainte().getValeur();
		
		// Ajouter la multiplicité si elle n'est pas [0..1] ou [0..*]
		if ( attr.multiplicite() != null && !attr.multiplicite().toString().isEmpty() )
		{
			String mult = "[" + attr.multiplicite().toString() + "]";
			if ( !mult.equals("[0..1]") && !mult.equals("[0..*]") && !mult.equals("[1]") )
			{
				multiplicite = " " + mult;
			}
		}
		
		// Visibilité + Nom
		g2.drawString( partieNom, xDebut, yTexte );
		
		// Type et suffixe ( allignée )          [ renvoie la taille max d'un att  ]
		int xAligne = x + DessinerClasse.PADDING + dims.getMaxLargeurNomsAttributs() + DessinerClasse.ESPACE_ALIGNEMENT;
		g2.drawString( partieType + suffixe + multiplicite, xAligne, yTexte );
		
		// Static ?
		if ( attr.portee() == Portee.CLASSE )
		{
			// souligner à partir du début du nom (après le symbole de visibilité)
			int debutSouligne = xDebut  + fm.stringWidth( FormatteurUML.getSymboleVisibilite( attr.visibilite() ) + " " );
			int finSouligner  = xAligne + fm.stringWidth( partieType + suffixe + multiplicite );
			
			if ( finSouligner <= debutSouligne ) { finSouligner = xDebut + fm.stringWidth( partieNom ); }
			
			g2.drawLine( debutSouligne, yTexte + 2, finSouligner, yTexte + 2 );
		}
		
		// Type et suffixe ( allignée )          [ renvoie la taille max d'un att  ]
		xAligne = x + DessinerClasse.PADDING + dims.getMaxLargeurNomsAttributs() + DessinerClasse.ESPACE_ALIGNEMENT;
		g2.drawString( partieType + suffixe, xAligne, yTexte );

		g2.drawString(multiplicite, xAligne + fm.stringWidth(partieType + suffixe), yTexte);
		
		yTexte += fm.getHeight() + 2;
	}
	
	// Afficher "..." si nécessaire
	if ( !affichageComplet && lstAttributs.size() > 3 ) { g2.drawString("...", xDebut, yTexte); }
	
	// ligne de séparation
	g2.drawLine(x, ySection + hAttributs, x + largeur, ySection + hAttributs);
}

	/* Méthodes                      */
	/*-------------------------------*/

	/**
	 * Dessine la section inferieure contenant la liste des methodes.
	 * Gere l'affichage des signatures, le type de retour, les constructeurs et l'alignement.
	 */
	private void dessinerSectionMethodes( Graphics2D g2, Classe classe, DimensionsCalculateur dims,
										  int x, int ySection, boolean affichageComplet              )
	{
		// Données :
		// - - - - - -
		List<Methode> lstMethodes = classe.getCopieLstMethodes();

		g2.setColor(Color.BLACK               );
		g2.setFont (DessinerClasse.FONT_NORMAL);

		FontMetrics fm     = g2.getFontMetrics();
		int         yTexte = ySection + DessinerClasse.PADDING + fm.getAscent();
		int         xDebut = x + DessinerClasse.PADDING;

		// ne rien dessiner si pas de méthode
		if ( lstMethodes.isEmpty() ) { return; }

		int nbMethodeDessiner = affichageComplet ? lstMethodes.size() : Math.min(3, lstMethodes.size());

		/**
		* Dessiner une Méthode :
		* - 3 parties :
		* -> [Visibilité] + [   Nom    ] + [ Parametre ]
		* -> [   Type   ]
		* -> [ estFinal ] = [ valeurDefaut ]
		*
		* - Dessin :
		* -> Visibilité + nom + param
		* |-> souligner si static
		* -> Type + suffixe
		* |-> alligné avec le plus long attribust
		*/
		for (int cpt = 0; cpt < nbMethodeDessiner; cpt++ )
		{
			Methode meth = lstMethodes.get( cpt );

			String visibilite = FormatteurUML.getSymboleVisibilite( meth.visibilite   ()                  );
			String parametres = FormatteurUML.formatParametres    ( meth.lstParametres(), affichageComplet );
			String sDefault   = ( meth.estDefault() ? "<<default>> ": "");
			
			String partieType;
			
			partieType = FormatteurUML.formatTypeRetour(meth.type(), meth.estConstructeur());
			

			String partieNom  = sDefault + visibilite + " " + meth.nom() + "(" + parametres + ")";
			String suffixe    = meth.type().equals("final") ? " {gelé}" : "";

			// Visibilité + nom + param
			g2.drawString(partieNom, xDebut, yTexte);

			// Static ?
			if (meth.portee() == Portee.CLASSE)
			{
				int xAligne = x + DessinerClasse.PADDING + dims.getMaxLargeurNomsMethodes() + DessinerClasse.ESPACE_ALIGNEMENT;
				// début après (éventuel) <<default>> et symbole de visibilité
				int debutSouligne = xDebut + fm.stringWidth( sDefault + visibilite + " " );
				int finSouligne;
				
				if      ( !partieType.isEmpty() && !partieType.contains("void") ) { finSouligne = xAligne + fm.stringWidth(partieType + suffixe);                }
				else if ( !suffixe   .isEmpty()                                 ) { finSouligne = xDebut  + fm.stringWidth(partieNom) + fm.stringWidth(suffixe); }
				else                                                              { finSouligne = xDebut  + fm.stringWidth(partieNom);                           }

				if ( finSouligne <= debutSouligne ) { finSouligne = xDebut + fm.stringWidth(partieNom); }

				g2.drawLine(debutSouligne, yTexte + 2, finSouligne, yTexte + 2);
			}

			// Type et suffixe ( allignée )
			// Constructer pas ?
			if ( !partieType.isEmpty() )
			{                                                        // [ renvoie la taille max d'une methode  ]
				int xAligne = x + DessinerClasse.PADDING + dims.getMaxLargeurNomsMethodes() + DessinerClasse.ESPACE_ALIGNEMENT;
				if ( ! partieType.contains( "void" ) )
					g2.drawString(partieType + suffixe, xAligne, yTexte);
			}
			else if ( !suffixe.isEmpty() ) g2.drawString(suffixe, xDebut + fm.stringWidth(partieNom), yTexte);

			yTexte += fm.getHeight() + 2;
		}

		// Afficher "..." si nécessaire
		if ( !affichageComplet && lstMethodes.size() > 3 ) g2.drawString("...", xDebut, yTexte);
	}
}