package src.vue.GUI.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JColorChooser;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import src.ControleurGUI;
import src.metier.classe.Classe;
import src.vue.GUI.dessin.classe.ElementDiagramme;

/**
 * Panneau principal d'affichage du diagramme UML.
 * Combine un arborescence des classes à gauche et un schéma UML à droite.
 * Gère l'affichage, le chargement et l'export du diagramme.
 * 
 */
public class PanelUML extends JSplitPane
{
	/*-------------------------------*/
	/*          Constantes           */
	/*-------------------------------*/
	private static final int VITESSE_SCROLL = 24; // A modifier si pas assez ou trop rapide

	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	private ControleurGUI         ctrl;

	private PanelSchema        panelSchema;
	private PanelArboressence  panelClasses;
	private JScrollPane        scrollPanel;

	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/
	/**
	 * Constructeur de PanelUML.
	 * Initialise le panneau principal avec l'arborescence des classes et le schéma UML.
	 * Positionne les deux panneaux dans un JSplitPane horizontal.
	 * 
	 * @param ctrl Le contrôleur de l'application GUI
	 */
	public PanelUML(ControleurGUI ctrl)
	{
		this.ctrl = ctrl;

		/*-------------------------------*/
		/*           Creation            */
		/*-------------------------------*/
		this.panelSchema  = new PanelSchema       ( this.ctrl          );
		this.panelClasses = new PanelArboressence ( this.ctrl          );
		
		this.scrollPanel  = new JScrollPane( this.panelSchema );
		this.scrollPanel.getVerticalScrollBar  ().setUnitIncrement( PanelUML.VITESSE_SCROLL );
		this.scrollPanel.getHorizontalScrollBar().setUnitIncrement( PanelUML.VITESSE_SCROLL );


		/*-------------------------------*/
		/* positionnement des composants */
		/*-------------------------------*/
		this.scrollPanel.setPreferredSize            ( new Dimension(850, 600)                 );
		this.scrollPanel.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
		this.scrollPanel.setVerticalScrollBarPolicy  ( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS   );

		// JSplitPane, permet l'affichage de toutes les classes sur la gauche
		this.setDividerLocation   ( JSplitPane.HORIZONTAL_SPLIT );
		this.setDividerLocation   ( 200                         );

		/*-------------------------------*/
		/* positionnement des composants */
		/*-------------------------------*/
		this.setLeftComponent ( this.panelClasses );
		this.setRightComponent( this.scrollPanel );
		this.setVisible       ( true             );
	}

	/*-------------------------------*/
	/*           Méthodes            */
	/*-------------------------------*/
	
	/**
	 * Capture une image du diagramme UML et l'enregistre au chemin spécifié.
	 * 
	 * @param chemin Le chemin où enregistrer l'image
	 */
	public void captureEcran(String chemin) 
	{ 
		this.panelSchema.captureEcran(chemin); 
	}

	/**
	 * Met à jour l'interface graphique après une modification.
	 * Ajuste la taille du panneau schéma et recharge les éléments.
	 */
	public void majIhm()
	{
		this.panelSchema.majTaillePanel();
		this.charger();
	}

	/**
	 * Recharge complètement l'interface graphique.
	 * Rafraîchit le schéma UML et l'arborescence des classes.
	 */
	public void recharger()
	{
		this.panelSchema .recharger();
		this.panelClasses.recharger();
	}

	/**
	 * Charge les éléments du diagramme UML et l'arborescence.
	 */
	public void charger() 
	{ 
		this.panelSchema .charger  (); 
		this.panelClasses.recharger();
	}
	
	/**
	 * Ouvre un sélecteur de couleur pour changer la couleur des flèches du diagramme.
	 * Applique la couleur choisie si l'utilisateur valide.
	 */
	public void ouvrirPanelCouleurFleche() 
	{
		Color couleur = JColorChooser.showDialog( this,
												  "Choisissez une couleur pour la flèche",
												  Color.BLUE
												);
		
		if (couleur != null) this.panelSchema.changerCouleurFleche(couleur);
		
	}

	/**
	 * Ouvre un sélecteur de couleur pour changer la couleur de fond du diagramme.
	 * Applique la couleur choisie si l'utilisateur valide.
	 */
	public void ouvrirPanelCouleurFond() 
	{
		Color couleur = JColorChooser.showDialog( this,
												  "Choisissez une couleur pour le fond",
												  Color.BLUE
												);
		
		if (couleur != null) this.panelSchema.changerCouleurFond(couleur);
		
	}

	/**
	 * Sélectionne visuellement une classe dans le schéma UML.
	 * Centre la vue sur la classe et la met en évidence.
	 * 
	 * @param classe La classe à sélectionner (peut être null)
	 */
	public void selectClasse(Classe classe)
	{
		if (classe == null) return;
		this.panelSchema.selectClasse(classe);
	}

	/*-------------------------------*/
	/*        Export / Import        */
	/*-------------------------------*/
	
	/**
	 * Obtient la liste des éléments visuels (classes dessinées) du diagramme.
	 * 
	 * @return La liste des éléments du diagramme
	 */
	public List<ElementDiagramme> getLstClassesVisuelles() { return this.panelSchema.getLstClassesVisuelles();}

	/**
	 * Définit la liste des éléments visuels du diagramme.
	 * Cette méthode est utilisée lors du chargement depuis un fichier.
	 * 
	 * @param lst La liste des éléments visuels du diagramme
	 */
	public void setLstClassesVisuelles(List<ElementDiagramme> lst)
	{
		this.panelSchema.setLstClassesVisuelles(lst);
	}
}
