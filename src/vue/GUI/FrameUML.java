package src.vue.GUI;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import src.ControleurGUI;
import src.metier.classe.Classe;
import src.vue.GUI.dessin.classe.ElementDiagramme;
import src.vue.GUI.panels.PanelUML;

public class FrameUML extends JFrame
{
	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	private ControleurGUI  ctrl;

	private PanelUML       panelUml;
	private BarreMenu      barreMenu;

	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/
	/**
	 * Initialise la frame principale de l'application UML.
	 * @param ctrl le contrôleur de l'application
	 */
	public FrameUML( ControleurGUI ctrl )
	{
		this.ctrl = ctrl;
		this.setTitle("Outils de rétroconception UML");
		this.setSize (850, 600);

		/*-------------------------------*/
		/*           Creation            */
		/*-------------------------------*/
		this.barreMenu = new BarreMenu( this.ctrl );
		this.panelUml  = new PanelUML ( this.ctrl );

		/*-------------------------------*/
		/*         Positionnement        */
		/*-------------------------------*/
		this.add( this.panelUml );
		this.add(this.barreMenu , BorderLayout.NORTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		this.setExtendedState        (JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo   (null                 );
	}

	/*-------------------------------*/
	/*        Export / Import        */
	/*-------------------------------*/
	/** Obtient la liste des elements du diagramme.
	 * @return la liste des éléments du diagramme
	 */
	public List<ElementDiagramme> getElementDiagrammes() { return this.panelUml.getLstClassesVisuelles(); }

	/*-------------------------------*/
	/*           Méthodes            */
	/*-------------------------------*/
	/** maj du panel avec le resize des classe  */
	public void majIhm ( ) { this.panelUml.majIhm(); }

	/** recharge l'interface graphique */
	public void reload                       (               ) { this.panelUml.recharger(); this.setVisible(true);     }

	/** charge les éléments du diagramme */
	public void load                         (               ) { this.panelUml.charger();                              }

	/** Ouvre le panel de configuration des couleurs des classes */
	public void ouvrirPanelCouleurFleche     (               ) { this.panelUml.ouvrirPanelCouleurFleche();          }

	/** Ouvre le panel de configuration des couleurs du fond */
	public void ouvrirPanelCouleurFond       (               ) { this.panelUml.ouvrirPanelCouleurFond();            }

	/** Indique si les classes externes sont affichées.
	 * @return vrai si les classes externes sont affichées
	 */
	public boolean isAfficherClassesExternes (               ) { return this.barreMenu.afficherClassesExternes();   }

	/** Capture l'écran de l'interface graphique. 
	 * @param chemin le chemin du fichier de capture
	 */
	public void captureEcran                 ( String chemin ) { this.panelUml.captureEcran(chemin);                }

	/** Sélectionne une classe dans l'interface graphique.
	 * @param c la classe à sélectionner
	 */
	public void selectionnerClasse          ( Classe c      ) { this.panelUml.selectClasse(c);                      }

}
