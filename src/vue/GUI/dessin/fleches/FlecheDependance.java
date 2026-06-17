package src.vue.GUI.dessin.fleches;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import src.metier.classe.Association;
import src.vue.GUI.dessin.Fleche;

/**
 * Represente une fleche de dependance UML.
 * Dessine un trait pointille avec une tete ouverte (en V).
 */
public class FlecheDependance extends Fleche
{
	/*-------------------------------*/
	/* Constructeurs                 */
	/*-------------------------------*/

	/**
	 * Construit une fleche de dependance avec des coordonnees entieres.
	 *
	 * @param xd Coordonnee X de depart.
	 * @param yd Coordonnee Y de depart.
	 * @param xf Coordonnee X d'arrivee.
	 * @param yf Coordonnee Y d'arrivee.
	 * @param bidirectionnel Vrai si la fleche a une tete aux deux extremites.
	 * @param couleur La couleur du trait et de la forme.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheDependance(int xd, int yd, int xf, int yf, boolean bidirectionnel, Color couleur, Association association)
	{
		super(xd, yd, xf, yf, bidirectionnel, couleur, association);
	}

	/**
	 * Construit une fleche de dependance avec des valeurs par defaut.
	 *
	 * @param xd Coordonnee X de depart.
	 * @param yd Coordonnee Y de depart.
	 * @param xf Coordonnee X d'arrivee.
	 * @param yf Coordonnee Y d'arrivee.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheDependance(int xd, int yd, int xf, int yf, Association association)
	{
		super(xd, yd, xf, yf, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche de dependance a partir de deux points.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheDependance( Point debut, Point fin, Association association )
	{
		super(debut.x, debut.y, fin.x, fin.y, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche de dependance avec choix de la direction.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param bidirectionnel Vrai pour afficher le symbole aux deux bouts.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheDependance( Point debut, Point fin, boolean bidirectionnel, Association association)
	{
		super(debut.x, debut.y, fin.x, fin.y, bidirectionnel, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche de dependance complete a partir de deux points.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param bidirectionnel Vrai pour afficher le symbole aux deux bouts.
	 * @param association L'objet metier associe a cette fleche.
	 * @param couleur La couleur specifique de la fleche.
	 */
	public FlecheDependance( Point debut, Point fin, boolean bidirectionnel, Association association, Color couleur)
	{
		super(debut.x, debut.y, fin.x, fin.y, bidirectionnel, (couleur != null) ? couleur : Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/

	/**
	 * Dessine la fleche de dependance sur le contexte graphique.
	 * Trace un trait pointille et une tete en forme de V.
	 *
	 * @param g Le contexte graphique 2D.
	 */
	@Override
	public void draw(Graphics2D g)
	{
		g.setColor(this.getCouleur());
		g.setStroke(Fleche.LIGNE_NORMAL);

		Path2D tete = Fleche.TETE_V;

		g.draw(Fleche.transform(tete, this.getXDebut(), this.getYDebut(), this.getXFin(), this.getYFin()));

		if ( this.estBidirectionnel() )
		{
			g.draw( Fleche.transform(tete, this.getXFin(), this.getYFin(), this.getXDebut(), this.getYDebut() ) );
		}


		if ( this.getAssociation().estReflexive() )
		{
			Fleche.drawLigneAutoAsso(g, this.getXDebut(), this.getYDebut(), this.getXFin(), this.getYFin());
		}
		else { Fleche.drawLignePointille(g, this.getXDebut(), this.getYDebut(), this.getXFin(), this.getYFin()); }

		this.drawMultiplicite(g);
		this.drawRole(g);
	}
}