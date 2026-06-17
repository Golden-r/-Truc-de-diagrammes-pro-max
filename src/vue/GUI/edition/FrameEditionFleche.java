package src.vue.GUI.edition;

import java.awt.Color;
import javax.swing.JFrame;
import src.ControleurGUI;
import src.metier.classe.Association;

/**
 * Fenetre flottante permettant d'editer les proprietes d'une association (fleche).
 * Elle sert de conteneur pour le PanelEditionFleche.
 */
public class FrameEditionFleche extends JFrame 
{
	private ControleurGUI ctrl;
	
	private PanelEditionFleche panelEditionFleche;
	 
	/**
	 * Initialise la fenetre d'edition.
	 * Configure les parametres de la fenetre (taille fixe, toujours au premier plan, cachee par defaut).
	 *
	 * @param ctrl Le controleur principal de l'application.
	 */
	public FrameEditionFleche(ControleurGUI ctrl) 
	{
		this.ctrl = ctrl;

		this.setBackground(new Color(182, 182, 182));
		this.setLocationRelativeTo(this.getParent());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setVisible(false);
	}

	/**
	 * Affiche la fenetre d'edition pour une association specifique.
	 * Cree et ajoute le panel d'edition correspondant a l'association.
	 *
	 * @param assoc L'association a editer.
	 */
	public void afficherPanelEdition(Association assoc)
	{
		if (this.panelEditionFleche != null)
		{
			this.remove(this.panelEditionFleche);
			this.panelEditionFleche = null;
		}
		this.panelEditionFleche = new PanelEditionFleche(this.ctrl, assoc);
		this.add(this.panelEditionFleche);
		this.pack();
		this.setVisible(true);
	}  

	/**
	 * Masque la fenetre et retire le panel d'edition.
	 * Permet de nettoyer la fenetre avant une prochaine utilisation.
	 */
	public void cacherPanelEdition()
	{
		if (this.panelEditionFleche != null)
		{
			this.remove(this.panelEditionFleche);
			this.panelEditionFleche = null;
		}
		this.setVisible(false);
		this.dispose();
	}

}