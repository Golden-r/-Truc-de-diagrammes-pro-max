package src.vue.chargement;

import javax.swing.JFrame;
import src.ControleurGUI;
import src.vue.chargement.panels.PanelChargement;

/**
 * Fenetre contenant le panel de chargement des fichiers.
 * Elle gere l'affichage initial et le comportement de fermeture de la fenetre.
 */
public class FrameChargement extends JFrame
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/

	private ControleurGUI   ctrl;
	private PanelChargement panelLancement;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise la fenetre de chargement.
	 * Definit le comportement a la fermeture (quitter ou disposer) selon l'etat du controleur.
	 *
	 * @param ctrl Le controleur principal de l'application.
	 * @param titre Le titre a afficher dans la barre de la fenetre.
	 */
	public FrameChargement(ControleurGUI ctrl, String titre)
	{
		this.ctrl = ctrl;
		this.setTitle(titre);
		this.setSize (600, 400);
		this.setLocationRelativeTo(null);

		this.panelLancement = new PanelChargement(this.ctrl, this);
		this.add(this.panelLancement);

		this.setVisible(true);
		
		// Si l'application est deja lancee (fenetre principale ouverte), on ne ferme que cette fenetre.
		// Sinon, on quitte l'application entiere.
		if ( this.ctrl.estOuvert() ) { this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); }
		else                         { this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE   ); }
	}
}