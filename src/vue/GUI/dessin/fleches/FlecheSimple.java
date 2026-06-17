package src.vue.GUI.dessin.fleches;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import src.metier.classe.Association;
import src.vue.GUI.dessin.Fleche;

/**
 * Represente une fleche d'association simple UML. 
 * Dessine un trait plein. Si l'association est unidirectionnelle, ajoute une tete en V.
 */
public class FlecheSimple extends Fleche
{
	/*-------------------------------*/
	/* Constructeurs                 */
	/*-------------------------------*/

	/**
	 * Construit une fleche simple avec des coordonnees entieres.
	 *
	 * @param xd Coordonnee X de depart.
	 * @param yd Coordonnee Y de depart.
	 * @param xf Coordonnee X d'arrivee.
	 * @param yf Coordonnee Y d'arrivee.
	 * @param bidirectionnel Vrai si la fleche n'a pas de sens de lecture specifique (pas de tete).
	 * @param couleur La couleur du trait.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheSimple(int xd, int yd, int xf, int yf, boolean bidirectionnel, Color couleur, Association association)
	{
		super(xd, yd, xf, yf, bidirectionnel, couleur, association);
	}

	/**
	 * Construit une fleche simple avec des valeurs par defaut.
	 *
	 * @param xd Coordonnee X de depart.
	 * @param yd Coordonnee Y de depart.
	 * @param xf Coordonnee X d'arrivee.
	 * @param yf Coordonnee Y d'arrivee.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheSimple(int xd, int yd, int xf, int yf, Association association)
	{
		super(xd, yd, xf, yf, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche simple a partir de deux points.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheSimple( Point debut, Point fin, Association association )
	{
		super(debut.x, debut.y, fin.x, fin.y, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche simple avec choix de la direction.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param bidirectionnel Vrai pour ne pas afficher de tete de fleche.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheSimple( Point debut, Point fin, boolean bidirectionnel, Association association)
	{
		super(debut.x, debut.y, fin.x, fin.y, bidirectionnel, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche simple complete a partir de deux points.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param bidirectionnel Vrai pour ne pas afficher de tete de fleche.
	 * @param association L'objet metier associe a cette fleche.
	 * @param couleur La couleur specifique de la fleche.
	 */
	public FlecheSimple( Point debut, Point fin, boolean bidirectionnel, Association association, Color couleur)
	{
		super(debut.x, debut.y, fin.x, fin.y, bidirectionnel, (couleur != null) ? couleur : Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/

	/**
	 * Dessine la fleche simple sur le contexte graphique.
	 * Trace un trait plein. Si la fleche n'est pas bidirectionnelle, dessine une tete en V a l'arrivee.
	 *
	 * @param g Le contexte graphique 2D.
	 */
	@Override
	public void draw(Graphics2D g)
	{
		super.draw(g);

		g.setColor(this.getCouleur());
		g.setStroke(Fleche.LIGNE_NORMAL);

		Path2D tete = Fleche.TETE_V;
		
		// Si ce n'est pas bidirectionnel (donc unidirectionnel), on dessine la tete en V
		if ( !this.estBidirectionnel() )
		{
			g.draw(Fleche.transform(tete, this.getXDebut(), this.getYDebut(), this.getXFin(), this.getYFin()));
		}

		if ( this.getAssociation().estReflexive() )
		{
			Fleche.drawLigneAutoAsso(g, this.getXFin(), this.getYFin(), this.getXFin(), this.getYFin());
		}
		else { g.drawLine(this.getXDebut(), this.getYDebut(), this.getXFin(), this.getYFin()); }

		this.drawMultiplicite(g);
		this.drawRole(g);
	}
}