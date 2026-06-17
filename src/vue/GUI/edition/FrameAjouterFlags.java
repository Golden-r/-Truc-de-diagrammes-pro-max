package src.vue.GUI.edition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import src.ControleurGUI;

/**
 * Frame de gestion des "flags" / tags d'affichage.
 *
 * Le but demandé :
 * - Zone en haut pour ajouter des tags (JTextField par tag)
 * - Bouton [c] pour choisir une couleur qui sera utilisée ensuite (package -> couleur)
 * - Bouton [X] pour supprimer un tag
 */
public class FrameAjouterFlags extends JFrame implements ActionListener
{
	/*-------------------------------*/
	/* Attributs                    */
	/*-------------------------------*/
	private final ControleurGUI ctrl;

	private JPanel panelTags;
	private List<JTextField> lstTags;

	private Color couleurChoisie;
	private final JPanel panelCouleur;

	/*-------------------------------*/
	/* Constructeur                 */
	/*-------------------------------*/
	public FrameAjouterFlags( ControleurGUI ctrl )
	{
		this.ctrl = ctrl;
		this.lstTags = new ArrayList<>();
		this.couleurChoisie = new Color( 255, 160, 160 );

		this.setTitle("Ajouter / supprimer des flags");
		this.setSize(520, 420);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(12, 12, 12, 12));
		this.setContentPane(content);

		// ---- Header ----
		JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
		header.add(new JLabel("Tags (un par ligne) :"));
		header.add(new JLabel(" " ));
		content.add(header, BorderLayout.NORTH);

		// ---- Centre : liste tags ----
		this.panelTags = new JPanel(new GridBagLayout());
		this.panelTags.setBackground(Color.WHITE);
		JPanel center = new JPanel(new BorderLayout());
		center.setBackground(Color.WHITE);
		center.add(this.panelTags, BorderLayout.NORTH);
		content.add(center, BorderLayout.CENTER);

		// ---- Footer : controls ----
		JPanel footer = new JPanel(new BorderLayout());
		footer.setBackground(Color.WHITE);

		// Boutons gauche (ajout / couleur)
		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
		left.setBackground(Color.WHITE);

		JButton btnAjouter = new JButton("Ajouter");
		btnAjouter.addActionListener(this);

		JButton btnCouleur = new JButton("c");
		btnCouleur.setToolTipText("Choisir la couleur du package");
		btnCouleur.addActionListener(this);

		this.panelCouleur = new JPanel();
		this.panelCouleur.setBackground(this.couleurChoisie);
		this.panelCouleur.setPreferredSize(new java.awt.Dimension(24, 24));
		this.panelCouleur.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK));

		left.add(btnAjouter);
		left.add(new JLabel("Couleur:"));
		left.add(btnCouleur);
		left.add(this.panelCouleur);

		footer.add(left, BorderLayout.WEST);

		// Boutons droite (valider / annuler)
		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		right.setBackground(Color.WHITE);

		JButton btnValider = new JButton("Valider");
		btnValider.addActionListener(this);

		JButton btnAnnuler = new JButton("Annuler");
		btnAnnuler.addActionListener(this);

		right.add(btnValider);
		right.add(btnAnnuler);
		footer.add(right, BorderLayout.EAST);

		content.add(footer, BorderLayout.SOUTH);

		// Ajout d'une ligne par défaut
		this.ajouterTag("ihm");
		this.ajouterTag("metier");
	}

	/*-------------------------------*/
	/* API publique (tags / couleur) */
	/*-------------------------------*/
	public List<String> getTags()
	{
		List<String> res = new ArrayList<>();
		for (JTextField tf : this.lstTags)
		{
			String s = (tf.getText() != null) ? tf.getText().trim() : "";
			if (!s.isEmpty()) res.add(s);
		}
		return res;
	}

	public Color getCouleurChoisie()
	{
		return this.couleurChoisie;
	}

	/*-------------------------------*/
	/* Implémentation ActionListener */
	/*-------------------------------*/
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();

		// Ajouter
		if (src instanceof JButton btn && "Ajouter".equals(btn.getText()))
		{
			this.ajouterTag("");
			return;
		}

		// Couleur
		if (src instanceof JButton btn && "c".equals(btn.getText()))
		{
			Color c = JColorChooser.showDialog(this, "Choisissez une couleur", this.couleurChoisie);
			if (c != null)
			{
				this.couleurChoisie = c;
				this.panelCouleur.setBackground(this.couleurChoisie);
				this.panelCouleur.repaint();
			}
			return;
		}

		// Valider
		if (src instanceof JButton btn && "Valider".equals(btn.getText()))
		{
			List<String> tags = this.getTags();
			if (tags.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "Ajoute au moins un tag (nom de package/flag).", "Info", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// TODO: À connecter au dessin (package -> couleur) une fois la structure de stockage définie.
			// Pour l'instant, on ferme.
			this.dispose();
			return;
		}

		// Annuler
		if (src instanceof JButton btn && "Annuler".equals(btn.getText()))
		{
			this.dispose();
			return;
		}
	}

	/*-------------------------------*/
	/* Gestion UI tags              */
	/*-------------------------------*/
	private void ajouterTag( String valeur )
	{
		int row = this.lstTags.size();

		JTextField tf = new JTextField(20);
		tf.setText((valeur != null) ? valeur : "");

		JButton btnSuppr = new JButton("X");
		btnSuppr.setMargin(new java.awt.Insets(2, 8, 2, 8));
		btnSuppr.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Supprimer visuellement et depuis la liste
				panelTags.remove(tf);
				// Ici on fait simple : on reconstruit toute la grille
				lstTags.remove(tf);
				reconstruireGrille();
				panelTags.revalidate();
				panelTags.repaint();
			}
		});

		this.lstTags.add(tf);

		reconstruireGrille();
		this.panelTags.revalidate();
		this.panelTags.repaint();
	}

	private void reconstruireGrille()
	{
		this.panelTags.removeAll();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;

		for (int i = 0; i < this.lstTags.size(); i++)
		{
			JTextField tf = this.lstTags.get(i);
			JButton btnSuppr = new JButton("X");
			btnSuppr.setMargin(new java.awt.Insets(2, 8, 2, 8));
			final JTextField tfRef = tf;
			btnSuppr.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					lstTags.remove(tfRef);
					reconstruireGrille();
					panelTags.revalidate();
					panelTags.repaint();
				}
			});

			// Champ texte
			gbc.gridx = 0;
			gbc.gridy = i;
			this.panelTags.add(tf, gbc);

			// Bouton X
			gbc.gridx = 1;
			this.panelTags.add(btnSuppr, gbc);
		}
	}
}

