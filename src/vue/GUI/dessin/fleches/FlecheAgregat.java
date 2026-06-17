package src.vue.GUI.dessin.fleches;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import src.metier.classe.Association;
import src.vue.GUI.dessin.Fleche;

/**
 * Represente une fleche d'agregation UML.
 * Dessine un losange vide a l'extremite de la liaison.
 */
public class FlecheAgregat extends Fleche
{
	/*-------------------------------*/
	/* Constructeurs                 */
	/*-------------------------------*/

	/**
	 * Construit une fleche d'agregation avec des coordonnees entieres.
	 *
	 * @param xd Coordonnee X de depart.
	 * @param yd Coordonnee Y de depart.
	 * @param xf Coordonnee X d'arrivee.
	 * @param yf Coordonnee Y d'arrivee.
	 * @param bidirectionnel Vrai si la fleche a une tete aux deux extremites.
	 * @param couleur La couleur du trait et de la forme.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheAgregat(int xd, int yd, int xf, int yf, boolean bidirectionnel, Color couleur, Association association)
	{
		super(xd, yd, xf, yf, bidirectionnel, couleur, association);
	}

	/**
	 * Construit une fleche d'agregation avec des valeurs par defaut pour la couleur et la direction.
	 *
	 * @param xd Coordonnee X de depart.
	 * @param yd Coordonnee Y de depart.
	 * @param xf Coordonnee X d'arrivee.
	 * @param yf Coordonnee Y d'arrivee.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheAgregat(int xd, int yd, int xf, int yf, Association association)
	{
		super(xd, yd, xf, yf, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche d'agregation a partir de deux points.
	 * Utilise les valeurs par defaut pour la couleur et la direction.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheAgregat( Point debut, Point fin, Association association )
	{
		super(debut.x, debut.y, fin.x, fin.y, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche d'agregation a partir de deux points avec choix de la direction.
	 * Utilise la couleur par defaut.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param bidirectionnel Vrai pour afficher le symbole aux deux bouts.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheAgregat( Point debut, Point fin, boolean bidirectionnel, Association association)
	{
		super(debut.x, debut.y, fin.x, fin.y, bidirectionnel, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche d'agregation complete a partir de deux points.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param bidirectionnel Vrai pour afficher le symbole aux deux bouts.
	 * @param association L'objet metier associe a cette fleche.
	 * @param couleur La couleur specifique de la fleche.
	 */
	public FlecheAgregat( Point debut, Point fin, boolean bidirectionnel, Association association, Color couleur)
	{
		super(debut.x, debut.y, fin.x, fin.y, bidirectionnel, (couleur != null) ? couleur : Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/

	/**
	 * Dessine la fleche d'agregation sur le contexte graphique.
	 * Trace le trait, le losange (tete carre), gere le decalage pour ne pas chevaucher la tete,
	 * et affiche les multiplicites et les roles.
	 *
	 * @param g Le contexte graphique 2D.
	 */
	@Override
	public void draw(Graphics2D g)
	{
		// Style
		// - - - - 
		g.setColor ( this.getCouleur()  );
		g.setStroke( Fleche.LIGNE_NORMAL );

		// Données :
		// - - - - - - - 
		Path2D tete  = Fleche.TETE_CARRE;
		double angle = Math.atan2(this.getYFin() - this.getYDebut(), this.getXFin() - this.getXDebut());
		int xDeb;
		int yDeb;
		int xFin;
		int yFin;

		// Dessiner la tête à l'arrivé
		g.draw( Fleche.transform(tete, this.getXDebut(), this.getYDebut(), this.getXFin(), this.getYFin()) );

		// Eviter la supperosition tete et le trait
		xDeb = this.getXDebut();
		yDeb = this.getYDebut();
		xFin = (int)( this.getXFin() - Math.cos(angle) * (Fleche.TAILLE_TETE_FLECHE * 2) );
		yFin = (int)( this.getYFin() - Math.sin(angle) * (Fleche.TAILLE_TETE_FLECHE * 2) );

		// Gerer les fleches bidirectionnelle
		if ( this.estBidirectionnel() )
		{
			xDeb = (int)(this.getXDebut() + Math.cos(angle) * (Fleche.TAILLE_TETE_FLECHE * 2));
			yDeb = (int)(this.getYDebut() + Math.sin(angle) * (Fleche.TAILLE_TETE_FLECHE * 2));

			g.draw( Fleche.transform(tete, this.getXFin(), this.getYFin(), this.getXDebut(), this.getYDebut() ) );
		}

		// Dessiner la ligne (reflexive ou nn)
		if ( this.getAssociation().estReflexive() ) { Fleche.drawLigneAutoAsso(g, xDeb, yDeb, xFin, yFin); }
		else                                        { g.drawLine(xDeb, yDeb, xFin, yFin);                  }

		// Ajouter les multiplicités 
		this.drawMultiplicite(g);
		this.drawRole(g);
	}
}