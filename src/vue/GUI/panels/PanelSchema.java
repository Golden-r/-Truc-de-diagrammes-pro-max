package src.vue.GUI.panels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import src.ControleurGUI;
import src.metier.classe.Association;
import src.metier.classe.Classe;
import src.metier.classe.ClasseExterne;
import src.metier.enums.TypeAssociation;
import src.vue.GUI.dessin.Fleche;
import src.vue.GUI.dessin.classe.ElementDiagramme;
import src.vue.GUI.dessin.fleches.*;
import src.vue.GUI.panels.gererSouris.GererSouris;

/**
 * Panel d'affichage du diagramme UML.
 * Gère l'affichage des classes, des associations (flèches) et l'interaction
 * avec l'utilisateur pour éditer le diagramme.
 *
 * Responsabilités:
 * - Positionner et afficher les classes visuelles
 * - Gérer les flèches d'association
 * - Écouter les événements de souris (sélection, déplacement)
 * - Mettre à jour les positions des flèches lors des modifications
 *
 */
public class PanelSchema extends JPanel
{
	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	/** Le contrôleur de l'application */
	private ControleurGUI             ctrl;

	/** Liste des classes visuelles affichées */
	private List<ElementDiagramme> lstElementDiagrammes;
	
	/** Liste des flèches d'association affichées */
	private List<Fleche>           lstFleches;
	
	/** La classe actuellement sélectionnée */
	private ElementDiagramme       classeSelectionnee;

	/** Offset de position pour le déplacement */
	private Point                  offset;
	
	/** Couleur des flèches */
	private Color                  flecheCouleur;

	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/
	/**
	 * Initialise le panel de schéma UML.
	 * Configure l'affichage, positionne les classes initiales et met en place
	 * les écouteurs de souris.
	 *
	 * @param ctrl Le contrôleur de l'application
	 */
	public PanelSchema(ControleurGUI ctrl)
	{
		this.ctrl = ctrl;
		this.setLayout(null);
		this.setBackground(Color.WHITE);

		this.lstElementDiagrammes = new ArrayList<ElementDiagramme>();
		this.lstFleches           = new ArrayList<Fleche>          ();

		this.classeSelectionnee = null;
		this.flecheCouleur      = Color.BLACK;

		// On place nos classe et met à jour l'affichage
		this.placerClassesInitiales();
		this.majTaillePanel        ();

		GererSouris sml = new GererSouris(this.ctrl, this);
		this.addMouseListener      ( sml );
		this.addMouseMotionListener( sml );
	}

	/*-------------------------------*/
	/*           Getters             */
	/*-------------------------------*/

	/**
	 * Retourne une copie de la liste des classes visuelles.
	 *
	 * @return Liste des éléments du diagramme
	 */
	public List<ElementDiagramme> getLstClassesVisuelles() { return new ArrayList<>(this.lstElementDiagrammes); }

	/**
	 * Retourne le contrôleur de l'application.
	 *
	 * @return Le contrôleur
	 */
	public ControleurGUI getCtrl() { return this.ctrl; }

	/**
	 * Retourne la liste des flèches d'association.
	 *
	 * @return Liste des flèches
	 */
	public List<Fleche> getLstFleches() { return this.lstFleches; }

	/**
	 * Retourne la classe actuellement sélectionnée.
	 *
	 * @return La classe sélectionnée ou null
	 */
	public ElementDiagramme getClasseSelectionnee() { return this.classeSelectionnee; }

	/**
	 * Retourne l'offset de position pour le déplacement.
	 *
	 * @return Le point offset
	 */
	public Point getOffset() { return this.offset; }

	/**
	 * Retourne la dimension du panel.
	 *
	 * @return Dimension du panel
	 */
	public Dimension getPreferredSize()
	{
		Dimension dim = super.getPreferredSize();
		return new Dimension( (int)(dim.width), (int)(dim.height) );
	}

	/*-------------------------------*/
	/*             Setters           */
	/*-------------------------------*/

	/**
	 * Définit la liste des classes visuelles et met à jour l'affichage.
	 *
	 * @param lst La nouvelle liste des éléments du diagramme
	 */
	public void setLstClassesVisuelles( List<ElementDiagramme> lst )
	{
		this.lstElementDiagrammes = lst;
		this.maj();
	}

	/**
	 * Définit la classe actuellement sélectionnée.
	 *
	 * @param elem L'élément du diagramme sélectionné
	 */
	public void setClasseSelectionnee(ElementDiagramme elem) { this.classeSelectionnee = elem; }

	/**
	 * Définit l'offset de position pour le déplacement.
	 *
	 * @param offset Le point offset
	 */
	public void setOffset(Point offset) { this.offset = offset; }


	/*-------------------------------*/
	/*    Placement par défaut       */
	/*-------------------------------*/
	/**
	* Place les classes initiales avec un algorithme de grille adaptative
	* qui évite les chevauchements en tenant compte des dimensions réelles
	*/
	private void placerClassesInitiales()
	{
		List<Classe> classes = ctrl.getLstClasses();
		if ( classes.isEmpty() ) return;

		// Créer un Graphics2D temporaire pour les calculs de dimensions
		BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D    g2Temp    = tempImage.createGraphics();
		g2Temp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Créer toutes les classes visuelles avec leurs dimensions réelles
		List<ElementDiagramme> tempClasses = new ArrayList<>();
		for (Classe classe : classes)
		{
			ElementDiagramme cv = new ElementDiagramme( this.ctrl, g2Temp, classe, 0, 0 );
			tempClasses.add(cv);
		}

		g2Temp.dispose();

		// Espacement entre les classes
		int espacementH = 50;
		int espacementV = 60;

		// Marge initiale
		int margeX = 40;
		int margeY = 40;

		// Calculer le nombre de colonnes optimal
		// On privilégie l'étalement horizontal
		int nbClasses  = tempClasses.size();
		int nbColonnes;

		if      (nbClasses <= 3)  nbColonnes = nbClasses; // Toutes sur une ligne
		else if (nbClasses <= 8)  nbColonnes = 4;         // 4 colonnes pour 4-8 classes
		else if (nbClasses <= 15) nbColonnes = 5;         // 5 colonnes pour 9-15 classes
		else                      nbColonnes = 6;         // 6 colonnes pour plus de 15 classes

		// Placer les classes selon une grille adaptative avec hauteurs variables
		int colonne  = 0;
		int xCourant = margeX;
		int yCourant = margeY;

		// Pour suivre les hauteurs maximales par ligne
		List<Integer> xPositions    = new ArrayList<>();
		List<Integer> yPositions    = new ArrayList<>();
		List<Integer> hauteurLignes = new ArrayList<>();

		int hauteurLigneCourante = 0;
		int indexLigneCourante   = 0;

		for (int i = 0; i < tempClasses.size(); i++)
		{
			ElementDiagramme cv = tempClasses.get(i);

			// Vérifier si on doit passer à la ligne suivante
			if (colonne >= nbColonnes)
			{
				// Sauvegarder la hauteur de la ligne terminée
				hauteurLignes.add(hauteurLigneCourante);

				colonne   = 0;
				xCourant  = margeX;
				yCourant += hauteurLigneCourante + espacementV;

				hauteurLigneCourante = 0;
				indexLigneCourante++;
			}

			// Sauvegarder la position
			xPositions.add(xCourant);
			yPositions.add(yCourant);

			// Mettre à jour la hauteur maximale de la ligne courante
			hauteurLigneCourante = Math.max(hauteurLigneCourante, cv.getHauteur());

			// Préparer la position X pour la prochaine classe
			xCourant += cv.getLargeur() + espacementH;
			colonne++;
		}

		// Ajouter la dernière ligne
		if (colonne > 0) { hauteurLignes.add(hauteurLigneCourante); }

		// Maintenant, positionner réellement les classes avec les bonnes positions
		for (int i = 0; i < tempClasses.size(); i++)
		{
			ElementDiagramme cv = tempClasses.get(i);
			cv.deplacer(xPositions.get(i), yPositions.get(i));
			this.lstElementDiagrammes.add(cv);
		}

		// Créer les flèches d'association
		this.initFleche();
	}

	/**
	 * Initialise les flèches d'association.
	 * Crée une flèche pour chaque association du modèle et met à jour leurs positions.
	 */
	private void initFleche()
	{
		this.lstFleches.clear();
		for (Association assoc : this.ctrl.getLstAssociations())
		{
			Fleche fleche = this.determinerFleche(new Point(0,0), new Point(0,0), assoc);
			this.lstFleches.add(fleche);
		}
		this.updatePositionsFleches();
	}

	/**
	 * Met à jour les positions des flèches d'association.
	 * Recalcule l'empilement des flèches pour éviter les chevauchements.
	 */
	public void updatePositionsFleches()
	{
		// Première passe : compter le nombre de connexions par classe+côté
		Map<String, Integer> totalParCote = new HashMap<String, Integer>();
		for (Fleche fleche : this.lstFleches)
		{
			Association assoc = fleche.getAssociation();
			ElementDiagramme src = this.trouverClasse(assoc.getClasseSource());
			ElementDiagramme tgt = this.trouverClasse(assoc.getClasseCible());
			if (src == null || tgt == null) continue;

			int coteSrc = src.getCoteConnexion(tgt);
			int coteTgt = tgt.getCoteConnexion(src);

			String cleSrc = src.getClasse().getNom() + "_" + coteSrc;
			String cleTgt = tgt.getClasse().getNom() + "_" + coteTgt;

			totalParCote.put(cleSrc, totalParCote.getOrDefault(cleSrc, 0) + 1);
			totalParCote.put(cleTgt, totalParCote.getOrDefault(cleTgt, 0) + 1);
		}

		// Deuxième passe : assigner index par clé et positionner dynamiquement
		Map<String, Integer> compteurs = new HashMap<String, Integer>();
		for (Fleche fleche : this.lstFleches)
		{
			Association      assoc = fleche.getAssociation();
			ElementDiagramme src   = this.trouverClasse(assoc.getClasseSource());
			ElementDiagramme tgt   = this.trouverClasse(assoc.getClasseCible());
			if (src == null || tgt == null) continue;

			int coteSrc = src.getCoteConnexion(tgt);
			int coteTgt = tgt.getCoteConnexion(src);

			String cleSrc = src.getClasse().getNom() + "_" + coteSrc;
			String cleTgt = tgt.getClasse().getNom() + "_" + coteTgt;

			int indexSrc = compteurs.getOrDefault(cleSrc, 0);
			int indexTgt = compteurs.getOrDefault(cleTgt, 0);

			int totalSrc = totalParCote.getOrDefault(cleSrc, 1);
			int totalTgt = totalParCote.getOrDefault(cleTgt, 1);

			Point pDeb = src.getPointConnexion(tgt, indexSrc, totalSrc);
			Point pFin = tgt.getPointConnexion(src, indexTgt, totalTgt);

			fleche.setXDebut(pDeb.x);
			fleche.setYDebut(pDeb.y);
			fleche.setXFin(pFin.x);
			fleche.setYFin(pFin.y);

			compteurs.put(cleSrc, indexSrc + 1);
			compteurs.put(cleTgt, indexTgt + 1);
		}
	}

	private void placerClassesDepuisModele()
	{
		List<Classe> classes = ctrl.getLstClasses();
		if (classes.isEmpty()) return;

		// Graphics temporaire pour les dimensions
		BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2Temp = tempImage.createGraphics();
		g2Temp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (Classe classe : classes)
		{
			ElementDiagramme cv = new ElementDiagramme(
				this.ctrl,
				g2Temp,
				classe,
				classe.getPosX(),   //coordonnées métier
				classe.getPosY()
			);
			this.lstElementDiagrammes.add(cv);
		}

		g2Temp.dispose();

		this.initFleche();
	}

	/**
	 * Détermine le type de flèche à utiliser pour une association donnée.
	 * @param p1    le point de départ de la flèche
	 * @param p2    le point de fin de la flèche
	 * @param assoc l'association liée à la flèche
	 * @return la flèche appropriée pour l'association
	 */
	private Fleche determinerFleche(Point p1, Point p2, Association assoc)
	{
		TypeAssociation type  = assoc.getTypeAssociation();
		boolean         bidir = assoc.estBidirectionnelle();
		switch ( type )
		{
			case DEPENDANCE               : return new FlecheDependance     ( p1, p2, bidir, assoc, this.flecheCouleur );
			case GENERALISATION           : return new FlecheGeneralisation ( p1, p2, bidir, assoc, this.flecheCouleur );
			case IMPLEMENTATION_INTERFACE : return new FlecheImplementation ( p1, p2, bidir, assoc, this.flecheCouleur );
			case AGREGATION               : return new FlecheAgregat        ( p1, p2, bidir, assoc, this.flecheCouleur );
			//case INTERNE                  : return new FlecheSousClasse(p1, p2, bidir, assoc, this.flecheCouleur);
			case COMPOSITION              : return new FlecheComposition    ( p1, p2, bidir, assoc, this.flecheCouleur );
			default                       : return new FlecheSimple         ( p1, p2, bidir, assoc, this.flecheCouleur );
		}
	}


	/*-------------------------------*/
	/*            DESSINER           */
	/*-------------------------------*/
	@Override
	/** Dessine le panneau de schéma UML.
	 * @param g le contexte graphique
	 */
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if ( this.classeSelectionnee != null && 
			 this.classeSelectionnee.getClasse() instanceof ClasseExterne && 
			!this.ctrl.afficherClassesExternes())
		{
			this.classeSelectionnee = null;
		}

		for (ElementDiagramme elem : lstElementDiagrammes) { elem.updateLayout(g2); }

		this.updatePositionsFleches();

		this.dessinerAssociations(g2);

		for (ElementDiagramme cv : lstElementDiagrammes) 
		{
			Classe classe = cv.getClasse();
			if (classe instanceof ClasseExterne && !ctrl.afficherClassesExternes()) { continue; }
			if (cv == this.classeSelectionnee && this.classeSelectionnee != null  ) { continue; } // on la skip, affich en dernière

			cv.dessiner(g2);
		}
		if (this.classeSelectionnee != null) // ici
		{
			// dessiner la classe sélectionnée en dernier, puis ajouter un contour épais
			this.classeSelectionnee.dessiner(g2);

			int sx = this.classeSelectionnee.getX();
			int sy = this.classeSelectionnee.getY();
			int sw = this.classeSelectionnee.getLargeur();
			int sh = this.classeSelectionnee.getHauteur();

			g2.drawRect(sx - 4, sy - 4, sw + 8, sh + 8);
		}
	}
	
	/**
	 * Dessine les associations entre les classes.
	 * @param g2 le contexte graphique 2D
	 */
	private void dessinerAssociations(Graphics2D g2)
	{
		for (Fleche fleche : this.lstFleches)
		{
			Association assoc        = fleche.getAssociation();
			Classe      classeSource = assoc .getClasseSource();
			Classe      classeCible  = assoc .getClasseCible();
			
			if ((classeSource instanceof ClasseExterne || classeCible instanceof ClasseExterne))
			{
				if (ctrl.afficherClassesExternes()) { fleche.setCouleur(Color.GRAY); }
				else                                { continue;                      }
			}
			else { fleche.setCouleur(this.flecheCouleur); }

			fleche.draw(g2);
		}
	}

	
	/*-------------------------------*/
	/*          UTLITAIRES           */
	/*-------------------------------*/
	/**maj la taille de panel */
	public void majTaillePanel()
	{
		Dimension dimension = calculerTaillePanneau();
		this.setPreferredSize(dimension);
		this.revalidate();
	}

	/**
	 *  Retourne nouvelle dimension du panel après le deplacer des classes
	 * 
	 * @return Dimensio
	 */
	private Dimension calculerTaillePanneau()
	{
		int maxX = 0;
		int maxY = 0;
		for (ElementDiagramme classe : this.lstElementDiagrammes)
		{
			maxX = Math.max(maxX, classe.getX() + classe.getLargeur());
			maxY = Math.max(maxY, classe.getY() + classe.getHauteur());
		}

		maxX += 50 * 2;
		maxY += 50 * 2;

		maxX = Math.max(maxX, 1000);
		maxY = Math.max(maxY, 800);

		return new Dimension(maxX, maxY);
	}


	/**
	 * Trouve l'élément du diagramme situé à une position donnée.
	 * Recherche en ordre inverse pour trouver l'élément le plus visible (au-dessus).
	 *
	 * @param p La position du point à vérifier
	 * @return L'élément du diagramme contenant ce point, ou null
	 */
	public ElementDiagramme trouverClasse(Point p)
	{
		for (int cptClasse = lstElementDiagrammes.size() - 1; cptClasse >= 0; cptClasse--)
		{
			Classe classe = lstElementDiagrammes.get(cptClasse).getClasse();
			if ( classe instanceof ClasseExterne && !ctrl.afficherClassesExternes() ) { continue;                                  }
			if ( lstElementDiagrammes.get(cptClasse).contient(p)                    ) { return lstElementDiagrammes.get(cptClasse);}
		}

		return null;
	}

	/**
	 * Trouve l'élément visuel correspondant à une classe du modèle.
	 *
	 * @param classe La classe du modèle
	 * @return L'élément du diagramme visuel, ou null
	 */
	private ElementDiagramme trouverClasse(Classe classe)
	{
		for (ElementDiagramme cv : lstElementDiagrammes) if (cv.getClasse() == classe)
		return cv;

		return null;
	}

	/**
	 * Capture l'écran actuel du diagramme et l'enregistre en image PNG ou JPG.
	 * Désélectionne temporairement la classe sélectionnée pendant la capture.
	 *
	 * @param chemin Le chemin d'enregistrement de l'image
	 */
	public void captureEcran( String chemin )
	{
		if (!chemin.endsWith(".png") && !chemin.endsWith(".jpg")) 
			chemin += ".png";

		ElementDiagramme sauvegarde = this.classeSelectionnee;
		boolean avaitSelection = (sauvegarde != null);
		if (avaitSelection)
		{
			this.classeSelectionnee = null;
			this.repaint();
		}

		BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics g = image.getGraphics();
		this.paint(g);
		g.dispose();

		try { ImageIO.write(image, "png", new File(chemin)); }
		catch (IOException e) { e.printStackTrace(); }
		finally
		{
			if (avaitSelection)
			{
				this.classeSelectionnee = sauvegarde;
				this.repaint();
			}
		}
	}


	/**
	 * Sélectionne visuellement la classe fournie :
	 * - met la `classeSelectionnee`
	 * - centre / rend visible la zone dans le viewport
	 * - repaint
	 */
	public void selectClasse(Classe classe)
	{
		if (classe == null) { return; }
		ElementDiagramme ed = this.trouverClasse(classe);
		if (ed     == null) { return; }

		this.classeSelectionnee = ed;
		int x = ed.getX();
		int y = ed.getY();
		int w = ed.getLargeur();
		int h = ed.getHauteur();

		// rendre visible dans le scrollpane parent
		this.scrollRectToVisible(new Rectangle(x - 20, y - 20, w + 40, h + 40));

		this.repaint();
	}
	
	/*-------------------------------*/
	/*           RECHARGER           */
	/*-------------------------------*/

	/**methode pour mettre à jour l'affichage */
	public void maj()
	{
		this.majTaillePanel();
		this.revalidate    ();
		this.repaint       ();
	}

	/**methode pour recharger l'affichage avec pos initiliale */
	public void recharger()
	{
		this.lstElementDiagrammes.clear();
		this.lstFleches          .clear();
		this.classeSelectionnee = null;

		this.placerClassesInitiales();
		this.maj();
	}

	/**methode pour charger l'affichage sans pos initiliale */
	public void charger()
	{
		this.classeSelectionnee = null;
		this.lstElementDiagrammes.clear();
		this.lstFleches          .clear();

		this.placerClassesDepuisModele();
		this.maj();
	}

	/**
	 * Change la couleur de toutes les flèches d'association.
	 *
	 * @param couleur La nouvelle couleur
	 */
	public void changerCouleurFleche(Color couleur)
	{
		this.flecheCouleur = couleur;
		this.repaint();
	}

	/**
	 * Change la couleur de fond du panel de schéma.
	 *
	 * @param couleur La nouvelle couleur de fond
	 */
	public void changerCouleurFond(Color couleur)
	{
		this.setBackground(couleur);
		this.repaint();
	}
}