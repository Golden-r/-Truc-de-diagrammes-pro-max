package src.vue.GUI.panels.gererSouris;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import src.ControleurGUI;
import src.metier.classe.Association;
import src.metier.classe.Attribut;
import src.metier.classe.Classe;
import src.metier.classe.ClasseExterne;
import src.utils.ErrorUtils;
import src.vue.GUI.dessin.Fleche;
import src.vue.GUI.dessin.classe.ElementDiagramme;
import src.vue.GUI.panels.PanelSchema;

/**
 * Gère les événements de la souris sur le panel de schéma UML.
 * Cette classe gère la sélection, le déplacement et l'édition des classes
 * visuelles ainsi que l'interaction avec les associations (flèches).
 *
 * Fonctionnalités:
 * - Clic pour sélectionner une classe
 * - Glisser-déposer pour déplacer une classe
 * - Clic droit pour afficher la classe complète
 * - Double-clic pour éditer un attribut
 * - Survol pour afficher le curseur approprié
 */
public class GererSouris extends MouseAdapter
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private ControleurGUI ctrl;
	private PanelSchema panelSchema;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise le gestionnaire de souris.
	 *
	 * @param panelSchema Le panel de schéma UML à gérer
	 */
	public GererSouris(ControleurGUI ctrl, PanelSchema panelSchema)
	{
		this.ctrl        = ctrl;
		this.panelSchema = panelSchema;
	}

	/*-------------------------------*/
	/* Gestion des événements        */
	/*-------------------------------*/

	/**
	 * Gère le clic de la souris (appui sur un bouton).
	 * Permet de sélectionner une classe, afficher la classe complète (clic droit),
	 * ou accéder à l'édition des associations.
	 *
	 * @param e L'événement de clic
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		ElementDiagramme classeSelectionnee = panelSchema.trouverClasse(this.obtenirCoordonnees(e));
		this.panelSchema.setClasseSelectionnee(classeSelectionnee);

		Point p = obtenirCoordonnees(e);

		if (classeSelectionnee != null)
		{
			Point offset = new Point( p.x - classeSelectionnee.getX(),
									  p.y - classeSelectionnee.getY()  );
			this.panelSchema.setOffset(offset);

			// Clic droit -> affichage classe complète
			if (e.getButton() != MouseEvent.BUTTON1) 
			{  
				this.panelSchema.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				classeSelectionnee.afficherClasseComplete();
				this.panelSchema.updatePositionsFleches();

				this.panelSchema.maj();
			}
			return;
		}
		else
		{
			panelSchema.setClasseSelectionnee(null);
			panelSchema.repaint();
			for (Fleche fleche : panelSchema.getLstFleches())
			{
				Association assoc = fleche.getAssociation();

				Classe classeSource = assoc.getClasseSource();
				Classe classeCible  = assoc.getClasseCible ();

				if ( classeSource instanceof ClasseExterne || classeCible instanceof ClasseExterne) { continue; }

				Shape hitBox = fleche.getHitBox();
				if ( hitBox != null && hitBox.contains( p ) )
				{
					if ( !this.panelSchema.getCtrl().getFrameEditionFleche().isVisible() ) 
					{ 
						this.panelSchema.getCtrl().getFrameEditionFleche().afficherPanelEdition(assoc); 
					}
				}
			}
		}
	}

	/**
	 * Gère le relâchement du bouton de la souris.
	 * Restaure l'affichage court de la classe et met à jour les positions des flèches.
	 *
	 * @param e L'événement de relâchement
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		ElementDiagramme classeSelectionnee = this.panelSchema.getClasseSelectionnee();
		if (classeSelectionnee != null)
		{
			this.panelSchema.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			classeSelectionnee.afficherClasseCourte();
			this.panelSchema.updatePositionsFleches();

			this.panelSchema.maj();
		}
	}

	/**
	 * Gère le mouvement de la souris avec un bouton appuyé (glisser-déposer).
	 * Déplace la classe sélectionnée et met à jour l'affichage.
	 *
	 * @param e L'événement de mouvement avec glissage
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		ElementDiagramme classeSelectionnee = panelSchema.getClasseSelectionnee();
		if (classeSelectionnee == null) return;

		if(	classeSelectionnee.getClasse() instanceof ClasseExterne && !this.ctrl.afficherClassesExternes()) return;

		this.panelSchema.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

		Point offset = this.panelSchema.getOffset();
		Point p      = this.obtenirCoordonnees(e);
		int   newX   = p.x - offset.x;
		int   newY   = p.y - offset.y;

		if (newX < 0) newX = 0;
		if (newY < 0) newY = 0;

		classeSelectionnee.deplacer(newX, newY);

		this.panelSchema.scrollRectToVisible
		( new Rectangle( newX,
						 newY,
						 classeSelectionnee.getLargeur(),
						 classeSelectionnee.getHauteur()   )
		);

		this.panelSchema.maj();
	}

	/**
	 * Extrait les coordonnées (x, y) à partir d'un événement de souris.
	 *
	 * @param e L'événement de souris
	 * @return Les coordonnées du clic
	 */
	private Point obtenirCoordonnees(MouseEvent e) { return new Point(e.getX(), e.getY()); }

	/**
	 * Gère le mouvement de la souris sans appui de bouton.
	 * Change le curseur selon la position (sur une classe, une flèche ou vide).
	 *
	 * @param e L'événement de mouvement
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point p = obtenirCoordonnees(e);

		ElementDiagramme classe = panelSchema.trouverClasse(p);
		if (classe != null)
		{
			if (classe.getClasse() instanceof ClasseExterne
					&& !ctrl.afficherClassesExternes())
			{
				panelSchema.setCursor(Cursor.getDefaultCursor());
				return;
			}

			panelSchema.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			return;
		}

		for (Fleche fleche : panelSchema.getLstFleches())
		{
			Shape hitBox = fleche.getHitBox();
			if (hitBox == null || !hitBox.contains(p))
				continue;

			if (!ctrl.afficherClassesExternes())
			{
				Classe classeSource = fleche.getAssociation().getClasseSource();
				Classe classeCible  = fleche.getAssociation().getClasseCible ();

				if ( classeSource instanceof ClasseExterne || classeCible instanceof ClasseExterne)
				{
					panelSchema.setCursor(Cursor.getDefaultCursor());
					return;
				}
			}

			panelSchema.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			return;
		}

		panelSchema.setCursor(Cursor.getDefaultCursor());
	}


	/**
	 * Gère le double-clic de la souris.
	 * Permet d'éditer un attribut de la classe cliquée.
	 *
	 * @param e L'événement de clic
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		Point p = obtenirCoordonnees(e);
		
		// Double-clic
		if (e.getClickCount() == 2)
		{
			// Vérifier si on a cliqué sur une classe
			ElementDiagramme elemClique = panelSchema.trouverClasse(p);
			
			if (elemClique != null)
			{
				if ( elemClique.getClasse() instanceof ClasseExterne )
				{
					if (!this.ctrl.afficherClassesExternes()) return;
					ErrorUtils.showInfo("Impossible d'éditer les attributs d'une classe externe.");
					return;
				}
				
				// Récupérer la liste des attributs (hors associations)
				List<Attribut> attributs = elemClique.getClasse().getCopieLstAttributsHorsAsso(panelSchema.getCtrl().getLstClasses() );
				
				// Si la classe n'a aucun attribut, on affiche le message et on quitte
				if (attributs == null || attributs.isEmpty())
				{
					ErrorUtils.showInfo("Il n'y a pas d'attributs dans cette classe.");
					return;
				}
				

				if (!this.panelSchema.getCtrl().getFrameEditionAttribut().isVisible())
				{
					this.panelSchema.getCtrl().getFrameEditionAttribut().afficherPanelEdition( elemClique.getClasse() );
					this.panelSchema.maj();
				}
			}
		}
	}

	/**
	 * Trouve l'attribut situé sous le pointeur de la souris dans une classe.
	 * Calcule les zones de la classe pour déterminer sur quel attribut le clic a eu lieu.
	 *
	 * @param elem L'élément diagramme (classe)
	 * @param p Le point cliqué
	 * @return L'attribut cliqué, ou null si aucun
	 */
	private Attribut trouverAttributSousPointeur(ElementDiagramme elem, Point p)
	{
		// Calcul des zones
		int x = elem.getX();
		int y = elem.getY();
		
		// Zone du nom (on la saute)
		int hauteurNom = 60; // Approximation
		
		// Début de la zone des attributs
		int yDebAttr = y + hauteurNom;
		
		// Hauteur d'une ligne d'attribut
		int hauteurLigne = 20;
		
		// Si le clic n'est pas dans la zone des attributs
		if (p.y < yDebAttr || p.y > yDebAttr + elem.getClasse().getCopieLstAttributs().size() * hauteurLigne)
		{
			return null;
		}
		
		// Calculer l'index de l'attribut cliqué
		int index = (p.y - yDebAttr) / hauteurLigne;
		
		List<Attribut> attributs = elem.getClasse().getCopieLstAttributsHorsAsso(
			panelSchema.getCtrl().getLstClasses()
		);
		
		if (index >= 0 && index < attributs.size())
		{
			return attributs.get(index);
		}
		
		return null;
	}

}
