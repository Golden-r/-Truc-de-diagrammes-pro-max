package src.vue.chargement.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import src.ControleurGUI;
import src.utils.ErrorUtils;
import src.utils.FileChooserUtils;
import src.vue.chargement.FrameChargement;

/**
 * Panel de chargement permettant de selectionner les fichiers ou dossiers a analyser.
 * Gere la liste des fichiers, les boutons d'action et l'option de recursivite.
 */
public class PanelChargement extends JPanel implements ListDataListener, ActionListener
{
	/*-------------------------------*/
	/* Constantes                    */
	/*-------------------------------*/

	private static final Color COLOR_BTN_FOND = Color.WHITE;

	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/

	private DefaultListModel<File> listModel;
	private JList<File>            lstFichiers;

	private JButton                btnAjouter;
	private JButton                btnSupprimer;
	private JButton                btnAfficher;
			 
	private JCheckBox              chkRecursif;

	private ControleurGUI          ctrl;
	private FrameChargement        frame;


	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Initialise le panel, les composants graphiques et les ecouteurs.
	 *
	 * @param ctrl Le controleur principal de l'application.
	 * @param frame La fenetre parente contenant ce panel.
	 */
	public PanelChargement( ControleurGUI ctrl, FrameChargement frame )
	{
		this.ctrl        = ctrl;
		this.frame       = frame;
		this.listModel   = new DefaultListModel<>();
		this.lstFichiers = new JList<>(listModel);

		/*-------------------------------*/
		/* Creation                      */
		/*-------------------------------*/

		// panels
		JPanel      panelCentre          = new JPanel(new BorderLayout(10, 0)                  );
		JPanel      panelBoutonsEdit     = new JPanel(new GridLayout(3, 1, 0, 10)              );
		JPanel      panelBoutonPlacement = new JPanel(new BorderLayout()                       );
		JScrollPane scrollPane           = new JScrollPane(this.lstFichiers                    );
		JPanel      panelSud             = new JPanel(new FlowLayout(FlowLayout.CENTER)        );

		// boutons
		this.btnAjouter   = new JButton  ( "Ajouter"               );
		this.btnSupprimer = new JButton  ( "Supprimer"             );
		this.btnAfficher  = new JButton  ( "Afficher"              );
		this.chkRecursif  = new JCheckBox( "Ouverture Récursive"   );
		

		this.btnAjouter  .setMnemonic(KeyEvent.VK_A); // Alt + A
		this.btnSupprimer.setMnemonic(KeyEvent.VK_S); // Alt + S
		this.btnAfficher .setMnemonic(KeyEvent.VK_F); // Alt + F
		this.chkRecursif .setMnemonic(KeyEvent.VK_R); // Alt + R

		// label
		JLabel lblTitre = new JLabel("Sélectionnez les fichiers (.java) ou dossiers à analyser", JLabel.CENTER);

		/*-------------------------------*/
		/* Style             */
		/*-------------------------------*/

		// panel
		this.setLayout    ( new BorderLayout(10, 10)                        );
		this.setBorder    ( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
		this.setBackground( new Color(245, 245, 245)                        );

		scrollPane          .setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		panelCentre         .setOpaque(false);
		panelBoutonsEdit    .setOpaque(false);
		panelBoutonPlacement.setOpaque(false);
		panelSud            .setOpaque(false);
		
		// boutons
		styleBouton(this.btnAjouter  , PanelChargement.COLOR_BTN_FOND );
		styleBouton(this.btnSupprimer, PanelChargement.COLOR_BTN_FOND );
		styleBouton(this.btnAfficher , PanelChargement.COLOR_BTN_FOND );
		
		this.btnAfficher.setPreferredSize(new Dimension(150, 40));
		this.btnAfficher.setFont(new Font("Segoe UI", Font.BOLD, 14));

		// checkbox
		this.chkRecursif.setOpaque(false);
		this.chkRecursif.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		this.chkRecursif.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		// label
		lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));

		// liste
		this.lstFichiers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		this.lstFichiers.setFixedCellHeight(24);
		
		/*-------------------------------*/
		/* Listeners           */
		/*-------------------------------*/

		this.btnAjouter  .addActionListener  ( this );
		this.btnSupprimer.addActionListener  ( this );
		this.btnAfficher .addActionListener  ( this );
		this.listModel   .addListDataListener( this );
		
		this.majEtatBoutons();

		/*-------------------------------*/
		/* Positionnement         */
		/*-------------------------------*/
		// Nord
		this                .add(lblTitre             , BorderLayout.NORTH  );
		// Centre    
		panelBoutonsEdit    .add( this.btnAjouter                           );
		panelBoutonsEdit    .add( this.btnSupprimer                         );
		panelBoutonsEdit    .add( this.chkRecursif                          );
		  
		panelBoutonPlacement.add( panelBoutonsEdit    , BorderLayout.NORTH  );
		  
		panelCentre         .add( scrollPane          , BorderLayout.CENTER );
		panelCentre         .add( panelBoutonPlacement, BorderLayout.EAST   );
 
		this                .add( panelCentre         , BorderLayout.CENTER );
		// Sud
		panelSud            .add( this.btnAfficher                          );
		this                .add( panelSud            , BorderLayout.SOUTH  );
	}
	
	/**
	 * Applique un style aux boutons (police, couleur, curseur).
	 *
	 * @param btn Le bouton a styliser.
	 * @param bg La couleur de fond.
	 */
	private void styleBouton(JButton btn, Color bg)
	{
		btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btn.setBackground(bg);
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Active ou desactive les boutons selon si la liste contient des fichiers.
	 */
	private void majEtatBoutons()
	{
		boolean aDesFichiers = !this.listModel.isEmpty();

		this.btnSupprimer.setEnabled(aDesFichiers);
		this.btnAfficher .setEnabled(aDesFichiers);
	}

	/*-------------------------------*/
	/* ListDataListener        */
	/*-------------------------------*/

	/**
	 * Appele quand un element est ajoute a la liste. Met a jour les boutons.
	 */
	@Override
	public void intervalAdded(ListDataEvent e) { this.majEtatBoutons(); }

	/**
	 * Appele quand un element est supprime de la liste. Met a jour les boutons.
	 */
	@Override
	public void intervalRemoved(ListDataEvent e) { this.majEtatBoutons(); }

	/**
	 * Appele quand le contenu change. Met a jour les boutons.
	 */
	@Override
	public void contentsChanged(ListDataEvent e) { this.majEtatBoutons(); }

	/*-------------------------------*/
	/* Listeners           */
	/*-------------------------------*/

	/**
	 * Gere les clics sur les boutons Ajouter, Supprimer et Afficher.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Ajouter
		if (e.getSource() == this.btnAjouter)
			{
				FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers Java", "java");
				File[] fichiers = FileChooserUtils.choisir(
					System.getProperty("user.dir"), 
					JFileChooser.FILES_AND_DIRECTORIES, 
					true, 
					filtre
				);
	
				for (File f : fichiers)
				{
					if (!this.listModel.contains(f)) { this.listModel.addElement(f); }
				}
			}

		// Supprimer
		if (e.getSource() == this.btnSupprimer)
		{
			List<File> selection = this.lstFichiers.getSelectedValuesList();
			for (File f : selection) listModel.removeElement(f);
		}

		// Afficher
		if (e.getSource() == this.btnAfficher)
		{
			if (this.listModel.isEmpty())
			{
				ErrorUtils.showWarning("Veuillez sélectionner au moins un fichier ou un dossier à analyser.");
				return;
			}

			List<File> fichiers = new ArrayList<>();
			for (int cptFichier = 0; cptFichier < listModel.size(); cptFichier++) fichiers.add(listModel.get(cptFichier));
			
			this.ctrl.traiterListeFichiers(fichiers, this.chkRecursif.isSelected());
			this.ctrl.reloadGUI();

			this.frame.dispose();
		}
	}
}