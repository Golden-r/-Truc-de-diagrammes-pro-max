package src.vue.GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import src.ControleurGUI;
import src.utils.ErrorUtils;
import src.utils.FileChooserUtils;
import src.vue.GUI.edition.FrameAjouterFlags;


public class BarreMenu extends JMenuBar implements ActionListener
{
	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	private ControleurGUI     ctrl;
	
	private JMenuItem         menuiCharger;
	private JMenuItem         menuiCaptureEcran;
	private JMenuItem         menuiImporter;
	private JMenuItem         menuiExporter;
	private JMenuItem         menuiQuitter;
	private JMenuItem         menuiChangerCouleurFleche;
	private JMenuItem         menuiChangerCouleurFond;


	private JMenuItem         menuiAjouterFlags;
	private JCheckBoxMenuItem menuiAfficherClassesExternes;



	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/
	/**
	 * Initialise la barre de menu de l'application UML.
	 * @param ctrl le contrôleur de l'application
	 */
	public BarreMenu(ControleurGUI ctrl)
	{
		this.ctrl = ctrl;

		/*-------------------------------*/
		/*           Creation            */
		/*-------------------------------*/
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
		this.setBackground(new Color(182, 182, 182));

		// les JMenu avec icônes
		JMenu menuFichier   = new JMenu( "Fichier"   );
		JMenu menuAffichage = new JMenu( "Affichage" );

		// Création des éléments de menu
		this.menuiCaptureEcran            = new JMenuItem("Capture d'écran"                      );
		this.menuiImporter                = new JMenuItem("Charger (.xml )"                      );
		this.menuiCharger                 = new JMenuItem("Charger (.java)"                      );
		this.menuiExporter                = new JMenuItem("Exporter"                             );
		this.menuiQuitter                 = new JMenuItem("Quitter"                              );
		this.menuiChangerCouleurFleche    = new JMenuItem("Changer la couleur des flèches"       );
		this.menuiChangerCouleurFond      = new JMenuItem("Changer la couleur du fond"           );
		this.menuiAjouterFlags           = new JMenuItem("Ajouter flag"                         );
		this.menuiAfficherClassesExternes = new JCheckBoxMenuItem("Afficher les classes externes");


		/*-------------------------------*/
		/* positionnement des composants */
		/*-------------------------------*/
		menuFichier.add         (this.menuiCharger      );
		menuFichier.addSeparator(                       );
		menuFichier.add         (this.menuiImporter     );
		menuFichier.add         (this.menuiExporter     );
		menuFichier.addSeparator(                       );
		menuFichier.add         (this.menuiQuitter      );

		menuAffichage.add         ( this.menuiChangerCouleurFleche    );
		menuAffichage.add         ( this.menuiChangerCouleurFond      );
		menuAffichage.add         ( this.menuiAjouterFlags           );
		menuAffichage.addSeparator(                                   );
		menuAffichage.add         ( this.menuiCaptureEcran            );
		menuAffichage.addSeparator(                                   );
		menuAffichage.add         ( this.menuiAfficherClassesExternes );


		//rajout des menus
		this.add( menuFichier   );
		this.add( menuAffichage );

		/*-------------------------------*/
		/* Activation des composants     */
		/*-------------------------------*/
		this.menuiCaptureEcran           .addActionListener(this);
		this.menuiCharger                .addActionListener(this);
		this.menuiExporter               .addActionListener(this);
		this.menuiImporter	             .addActionListener(this);
		this.menuiQuitter                .addActionListener(this); 
		this.menuiChangerCouleurFleche   .addActionListener(this);
		this.menuiChangerCouleurFond	 .addActionListener(this);
		this.menuiAjouterFlags           .addActionListener(this);
		this.menuiAfficherClassesExternes.addActionListener(this);


		this.menuiAfficherClassesExternes.setSelected(false);

		// Raccourcis clavier
		this.menuiCaptureEcran    .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P , InputEvent.CTRL_DOWN_MASK));
		this.menuiCharger         .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O , InputEvent.CTRL_DOWN_MASK));
		this.menuiExporter        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S , InputEvent.CTRL_DOWN_MASK));
		this.menuiImporter        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L , InputEvent.CTRL_DOWN_MASK));
		this.menuiQuitter         .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK ));
	}

	
	/*-------------------------------*/
	/*           Getters             */
	/*-------------------------------*/
	/** Indique si les classes externes sont affichées.
	 * @return vrai si les classes externes sont affichées
	 */
	public boolean afficherClassesExternes() { return this.menuiAfficherClassesExternes.isSelected(); }

	/*-------------------------------*/
	/*      Listener                 */
	/*-------------------------------*/
	/** Gère les actions des éléments de menu.
	 * @param e l'événement d'action
	*/
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// Quitter
		if (e.getSource() == this.menuiQuitter) { System.exit(0); }

		// Charger
		if (e.getSource() == this.menuiCharger) { this.ctrl.creerLstClasse(); }

		// Capture d'écran
		if (e.getSource() == this.menuiCaptureEcran)
		{
			FileNameExtensionFilter filtre  = new FileNameExtensionFilter("Images PNG ou JPG", "png", "jpg");
			File                    fichier = FileChooserUtils.choisirPourSauvegarder(".", null, filtre, "Enregistrer l'image");
			
			if (fichier != null)
			{
				String chemin = fichier.getAbsolutePath();
				this.ctrl.captureEcran(chemin);
			}
		}

		// Changer couleur des fleches
		if (e.getSource() == this.menuiChangerCouleurFleche) { this.ctrl.ouvrirPanelCouleurFleche(); }

		// Changer couleur du fond
		if (e.getSource() == this.menuiChangerCouleurFond  ) { this.ctrl.ouvrirPanelCouleurFond  (); }

		// Ajouter flag
		if (e.getSource() == this.menuiAjouterFlags ) { new FrameAjouterFlags(this.ctrl).setVisible(true); }


		// Exporter
		if (e.getSource() == this.menuiExporter) 
		{
			File fichier = FileChooserUtils.choisirPourSauvegarder("./", "export.xml", null, "Exporter le diagramme");
			
			if (fichier == null)
			{ 
				ErrorUtils.showError("Aucun emplacement sélectionné pour l'exportation.");
				return;
			}
			
			this.ctrl.exporter(fichier.getPath());
		}
		
		// Importer
		if (e.getSource() == this.menuiImporter) 
		{
			FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers XML", "xml");
			File fichier = FileChooserUtils.choisirFichier("./", filtre);
			
			if (fichier == null)
			{ 
				ErrorUtils.showError("Aucun fichier sélectionné pour l'importation.");
				return;
			}
			
			this.ctrl.importer(fichier.getPath());
		}

		//Afficher / Cacher les classes externes
		if (e.getSource() == this.menuiAfficherClassesExternes) { this.ctrl.majIhm(); }
	}
}