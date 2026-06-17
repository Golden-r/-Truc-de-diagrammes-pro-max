package src.vue.GUI.dessin.fleches;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import src.metier.classe.Association;
import src.vue.GUI.dessin.Fleche;

/**
 * Represente une fleche specifique pour les sous-classes (ou classes imbriquees). 
 * Dessine un trait termine par un cercle contenant une croix (+).
 */
public class FlecheSousClasse extends Fleche
{
	/*-------------------------------*/
	/* Constructeurs                 */
	/*-------------------------------*/

	/**
	 * Construit une fleche de sous-classe avec des coordonnees entieres.
	 *
	 * @param xd Coordonnee X de depart.
	 * @param yd Coordonnee Y de depart.
	 * @param xf Coordonnee X d'arrivee.
	 * @param yf Coordonnee Y d'arrivee.
	 * @param bidirectionnel Vrai si la fleche a une tete aux deux extremites.
	 * @param couleur La couleur du trait et de la forme.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheSousClasse(int xd, int yd, int xf, int yf, boolean bidirectionnel, Color couleur, Association association)
	{
		super(xd, yd, xf, yf, bidirectionnel, couleur, association);
	}

	/**
	 * Construit une fleche de sous-classe avec des valeurs par defaut.
	 *
	 * @param xd Coordonnee X de depart.
	 * @param yd Coordonnee Y de depart.
	 * @param xf Coordonnee X d'arrivee.
	 * @param yf Coordonnee Y d'arrivee.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheSousClasse(int xd, int yd, int xf, int yf, Association association)
	{
		super(xd, yd, xf, yf, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche de sous-classe a partir de deux points.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheSousClasse( Point debut, Point fin, Association association )
	{
		super(debut.x, debut.y, fin.x, fin.y, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche de sous-classe avec choix de la direction.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param bidirectionnel Vrai pour afficher le symbole aux deux bouts.
	 * @param association L'objet metier associe a cette fleche.
	 */
	public FlecheSousClasse( Point debut, Point fin, boolean bidirectionnel, Association association)
	{
		super(debut.x, debut.y, fin.x, fin.y, bidirectionnel, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche de sous-classe complete a partir de deux points.
	 *
	 * @param debut Point de depart.
	 * @param fin Point d'arrivee.
	 * @param bidirectionnel Vrai pour afficher le symbole aux deux bouts.
	 * @param association L'objet metier associe a cette fleche.
	 * @param couleur La couleur specifique de la fleche.
	 */
	public FlecheSousClasse( Point debut, Point fin, boolean bidirectionnel, Association association, Color couleur)
	{
		super(debut.x, debut.y, fin.x, fin.y, bidirectionnel, (couleur != null) ? couleur : Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/

	/**
	 * Dessine la fleche de sous-classe sur le contexte graphique.
	 * Trace la ligne, puis dessine un cercle avec une croix (+) a l'interieur a l'extremite.
	 *
	 * @param g Le contexte graphique 2D.
	 */
	@Override
	public void draw(Graphics2D g)
	{
		double angle = Math.atan2(
			this.getYFin() - this.getYDebut(),
			this.getXFin() - this.getXDebut()
		);

		int rayon = (int) Fleche.TAILLE_TETE_FLECHE;

		// Point d’arrêt de la ligne (bord du cercle)
		int xLigneFin = (int) (this.getXFin() - Math.cos(angle) * rayon);
		int yLigneFin = (int) (this.getYFin() - Math.sin(angle) * rayon);

		int xLigneFin2 = (int) (this.getXFin() - Math.cos(angle) * (rayon * 2));
		int yLigneFin2 = (int) (this.getYFin() - Math.sin(angle) * (rayon * 2));

		// Ligne principale
		g.drawLine(
			this.getXDebut(),
			this.getYDebut(),
			xLigneFin2,
			yLigneFin2
		);

		// Centre de la tête
		int cx = xLigneFin;
		int cy = yLigneFin;

		// Cercle
		g.drawArc(
			cx - rayon,
			cy - rayon,
			rayon * 2,
			rayon * 2,
			0,
			360
		);

		// Taille du +
		int taillePlus = rayon * 2;

		// Barre horizontale du +
		g.drawLine(
			cx - taillePlus / 2, cy,
			cx + taillePlus / 2, cy
		);

		// Barre verticale du +
		g.drawLine(
			cx, cy - taillePlus / 2,
			cx, cy + taillePlus / 2
		);

		super.draw(g);
	}
}