package src.vue.GUI.dessin.classe;

import java.awt.Graphics2D;
import java.awt.Point;
import src.ControleurGUI;
import src.metier.classe.Classe;

/**
* Représente un élément visuel d'une classe dans le diagramme UML.
*
* Cette classe gère l'affichage graphique d'une classe, incluant :
*   - Le calcul des dimensions (largeur, hauteur)
*   - Le dessin de la classe (nom, attributs, méthodes)
*   - Le positionnement et le déplacement dans le diagramme
*   - Les points de connexion pour les flèches d'association
*
* @author Equipe 1
* @author CHEVEAU Matéo
* @author DAMESTOY Ethan
* @author HERMILLY Joshua
* @author LAFOSSE Lucas
* @author LECLERC Jonathan
*/
public class ElementDiagramme 
{

	/* ------------------------------------ */
	/*			Constantes					*/
	/* ------------------------------------ */
	
	private static final int DECALAGE_CONNEXION = 30;
	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	private ControleurGUI         ctrl;
	private Classe                classe;
	private boolean               affichageComplet;

	private DimensionsCalculateur dimensionsCalc;
	private DessinerClasse        peintre;

	private boolean               dimensionsAJour;

	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/

	/**
	* Crée un élément de diagramme pour une classe donnée.
	*
	* @param lstClasses la liste de toutes les classes du diagramme (pour filtrer les attributs)
	* @param g2         le contexte graphique pour le calcul des dimensions (peut être null)
	* @param classe     la classe métier à représenter
	* @param x          la position X initiale dans le diagramme
	* @param y          la position Y initiale dans le diagramme
	*/
	public ElementDiagramme( ControleurGUI ctrl, Graphics2D g2, Classe classe, int x, int y )
	{
		this.ctrl             = ctrl;
		this.classe           = classe;
		this.affichageComplet = false;
		this.dimensionsCalc   = new DimensionsCalculateur();
		this.peintre          = new DessinerClasse();

		this.classe.setPosX(x);
		this.classe.setPosY(y);

		if (g2 != null) { this.updateLayout(g2); }
	}

	/*-------------------------------*/
	/*            Getters            */
	/*-------------------------------*/

	public int    getLargeur() { return dimensionsCalc.getLargeur(); }
	public int    getHauteur() { return dimensionsCalc.getHauteur(); }
	public Classe getClasse () { return classe;                      }
	public int    getX      () { return this.classe.getPosX();       }
	public int    getY      () { return this.classe.getPosY();       }

	/*-------------------------------*/
	/*           Méthodes            */
	/*-------------------------------*/

	/**
	* Active l'affichage complet de la classe (tous les attributs et méthodes).
	* Invalide les dimensions pour forcer un recalcul.
	*/
	public void afficherClasseComplete()
	{
		this.affichageComplet = true;
		this.dimensionsAJour  = false;
	}

	/**
	* Active l'affichage court de la classe (3 attributs et méthodes max).
	* Invalide les dimensions pour forcer un recalcul.
	*/
	public void afficherClasseCourte()
	{
		this.affichageComplet = false;
		this.dimensionsAJour  = false;
	}

	/**
	* Met à jour les dimensions de l'élément si nécessaire.
	* Le calcul n'est effectué que si les dimensions ne sont pas à jour.
	*
	* @param g2 le contexte graphique pour les calculs de FontMetrics
	*/
	public void updateLayout(Graphics2D g2)
	{
		if (this.dimensionsAJour) { return; }

		this.dimensionsCalc.calculer( g2, this.classe, this.ctrl.getLstClasses(), this.affichageComplet );
		this.dimensionsAJour = true;
	}

	/**
	* Dessine l'élément dans le contexte graphique donné.
	* Met à jour les dimensions si nécessaire avant le dessin.
	*
	* @param g2 le contexte graphique 2D
	*/
	public void dessiner(Graphics2D g2)
	{
		this.updateLayout(g2);

		peintre.dessiner( g2                       ,
						  classe                   ,
						  this.ctrl.getLstClasses(),
						  dimensionsCalc           ,
						  this.getX()              ,
						  this.getY()              ,
						  affichageComplet
						);
	}

	/**
	* Déplace l'élément vers une nouvelle position.
	*
	* @param newX la nouvelle position X
	* @param newY la nouvelle position Y
	*/
	public void deplacer( int newX, int newY )
	{
		this.classe.setPosX( newX );
		this.classe.setPosY( newY );
	}

	/**
	* Vérifie si un point est contenu dans les limites de l'élément.
	*
	* @param p le point à tester
	* @return vrai si le point est dans l'élément, faux sinon
	*/
	public boolean contient(Point p)
	{
		return p.getX() >= this.getX()                     && 
			   p.getX() <= this.getX() + this.getLargeur() &&
			   p.getY() >= this.getY()                     && 
			   p.getY() <= this.getY() + this.getHauteur();
	}


	/**
	* Version dynamique : répartit les points le long du côté selon le nombre total
	* @param autre l'élément cible
	* @param index index (0-based) parmi les connexions sur ce côté
	* @param total nombre total de connexions sur ce côté
	*/
	public Point getPointConnexion(ElementDiagramme autre, int index, int total)
	{
		int centreSrcX   = this .getX() + this .getLargeur() / 2;
		int centreSrcY   = this .getY() + this .getHauteur() / 2;
		int centreCibleX = autre.getX() + autre.getLargeur() / 2;
		int centreCibleY = autre.getY() + autre.getHauteur() / 2;

		int deltaX = centreCibleX - centreSrcX;
		int deltaY = centreCibleY - centreSrcY;

		int cote;
		if (Math.abs(deltaX) > Math.abs(deltaY)) { cote = (deltaX > 0) ? 3 : 2; } // droite ou gauche
		else                                     { cote = (deltaY > 0) ? 1 : 0; } // bas ou haut

		int margeMin = 10; // marge interne minimale par défaut
		int espacementMin = 8; // espacement minimal souhaité entre points

		if (total <= 0    ) { total = 1;         }
		if (index <  0    ) { index = 0;         }
		if (index >= total) { index = total - 1; }

		if (cote == 2 || cote == 3)
		{
			// répartir verticalement : on essaie d'assurer un espacement minimal
			int hauteurTotale            = this.getHauteur();
			int hauteurDispoAvecMargeMin = Math.max(0, hauteurTotale - 2 * margeMin);
			double pasAvecMargeMin       = (double) hauteurDispoAvecMargeMin / (double)(total + 1);

			int marge = margeMin;
			if (pasAvecMargeMin < espacementMin)
			{
				int margeTmp = (hauteurTotale - espacementMin * (total + 1)) / 2;
				if (margeTmp < 0) { margeTmp = 0; }
				marge = margeTmp;
			}

			int    hauteurDispo = Math.max(0, this.getHauteur() - 2 * marge);
			double pas          = (double)hauteurDispo / (double)(total + 1);
			int    y            = this.getY() + marge + (int)Math.round((index + 1) * pas);
			int    x            = (cote == 3) ? this.getX() + this.getLargeur() : this.getX();

			return new Point(x, y);
		}
		else
		{
			// répartir horizontalement
			int    largeurTotale            = this.getLargeur();
			int    largeurDispoAvecMargeMin = Math.max(0, largeurTotale - 2 * margeMin);
			double pasAvecMargeMin          = (double)largeurDispoAvecMargeMin / (double)(total + 1);

			int marge = margeMin;
			if (pasAvecMargeMin < espacementMin)
			{
				int computed = (largeurTotale - espacementMin * (total + 1)) / 2;
				if (computed < 0) computed = 0;
				marge = computed;
			}

			int    largeurDispo = Math.max(0, this.getLargeur() - 2 * marge);
			double pas          = (double)largeurDispo / (double)(total + 1);
			int    x            = this.getX() + marge + (int)Math.round((index + 1) * pas);
			int    y            = (cote == 1) ? this.getY() + this.getHauteur() : this.getY();
			
			return new Point(x, y);
		}
	}

	/**
	 * Détermine le côté de connexion vers une autre classe.
	 * 
	 * @param autre l'élément cible
	 * @return le côté (0=haut, 1=bas, 2=gauche, 3=droite)
	 */
	public int getCoteConnexion(ElementDiagramme autre)
	{
		int deltaX = (autre.getX() + autre.getLargeur() / 2) - (this.getX() + this.getLargeur() / 2);
		int deltaY = (autre.getY() + autre.getHauteur() / 2) - (this.getY() + this.getHauteur() / 2);
		
		// Calcul de l'angle en radians, puis conversion en degrés
		double angle = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		
		// Normalisation de l'angle entre 0 et 360
		if (angle < 0) angle += 360;
		
		// Découpage en 4 secteurs de 90° centrés sur les axes cardinaux
		// Droite: -45° à 45° (ou 315° à 45°)
		// Bas: 45° à 135°
		// Gauche: 135° à 225°
		// Haut: 225° à 315°
		
		if (angle >= 315 || angle < 45)        return 3; // droite
		else if (angle >= 45 && angle < 135)   return 1; // bas
		else if (angle >= 135 && angle < 225)  return 2; // gauche
		else                                    return 0; // haut
	}
}