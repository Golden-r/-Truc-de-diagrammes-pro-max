package src.vue.GUI.edition;

import java.awt.Color;
import javax.swing.JFrame;
import src.ControleurGUI;
import src.metier.classe.Classe;

/**
 * Fenetre flottante permettant d'editer les attributs d'une classe.
 * Elle agit comme un conteneur pour le PanelEditionAttribut.
 */
public class FrameEditionAttribut extends JFrame 
{
	private ControleurGUI ctrl;
	
	private PanelEditionAttribut panelEditionAttribut;
	 
	/**
	 * Construit la fenetre d'edition.
	 * Initialise les proprietes graphiques (taille fixe, toujours au dessus, etc.).
	 * La fenetre est invisible par defaut.
	 *
	 * @param ctrl Le controleur principal de l'application.
	 */
	public FrameEditionAttribut(ControleurGUI ctrl) 
	{
		this.ctrl = ctrl;

		this.setBackground(new Color(182, 182, 182));
		this.setLocationRelativeTo(this.getParent());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop (true );
		this.setResizable   (false);
		this.setVisible     (false);
	}

	/**
	 * Initialise et affiche le panel d'edition pour une classe specifique.
	 *
	 * @param classe La classe dont on souhaite modifier les attributs.
	 */
	public void afficherPanelEdition(Classe classe)
	{
		if (this.panelEditionAttribut != null)
		{
			this.remove(this.panelEditionAttribut);
			this.panelEditionAttribut = null;
		}
		this.panelEditionAttribut = new PanelEditionAttribut(this.ctrl, this, classe);
		this.add(this.panelEditionAttribut);
		this.pack();
		this.setVisible(true);
	}   

	/**
	 * Masque la fenetre et nettoie le contenu.
	 * Retire le panel d'edition pour preparer la prochaine ouverture.
	 */
	public void cacherPanelEdition()
	{
		if (this.panelEditionAttribut != null)
		{
			this.remove(this.panelEditionAttribut);
			this.panelEditionAttribut = null;
		}
		this.setVisible(false);
		this.dispose();
	}

}