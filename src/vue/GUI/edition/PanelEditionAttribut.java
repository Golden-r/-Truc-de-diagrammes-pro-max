package src.vue.GUI.edition;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import src.ControleurGUI;
import src.metier.classe.Attribut;
import src.metier.classe.Classe;
import src.metier.classe.Multiplicite;
import src.metier.enums.*;
import src.utils.ErrorUtils;

/**
 * Panel d'édition des attributs.
 * Permet de modifier la multiplicité d'un attribut.
 * 
 * @author Equipe 1
 */
public class PanelEditionAttribut extends JPanel implements ActionListener
{
	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	private ControleurGUI        ctrl;
	private FrameEditionAttribut frame;

	private Classe               classe;
	private Attribut             attributSelect;

	private JTextField           txtMin;
	private JTextField           txtMax;

	private JButton              btnValider;
	private JButton              btnAnnuler;

	private JComboBox<Attribut> lst;

	private JRadioButton rbRien;
	private JRadioButton rbFrozen;
	private JRadioButton rbAddOnly;
	private JRadioButton rbRequete;
	private ButtonGroup groupeRadio;

	private int                  min;
	private int                  max;

	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/
	public PanelEditionAttribut(ControleurGUI ctrl, FrameEditionAttribut frame, Classe classe)
	{
		this.ctrl   = ctrl;
		this.frame  = frame;
		this.classe = classe;

		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(new EmptyBorder(15, 15, 15, 15));

		/*-------------------------------*/
		/*           Création            */
		/*-------------------------------*/

		// Liste des attributs
		this.lst = new JComboBox<>();
		for (Attribut a : this.classe.getCopieLstAttributsHorsAsso( this.ctrl.getLstClasses() ))
			this.lst.addItem(a);

		// Panels
		JPanel pnlSelect = new JPanel( new BorderLayout()             );
		JPanel pnlCentre = new JPanel( new BorderLayout(10, 10       ));
		JPanel pnlRadio  = new JPanel( new GridLayout  (4, 1,  5,  5 ));
		JPanel pnlMult   = new JPanel( new GridLayout  (2, 2, 10, 10 ));
		JPanel pnlSud    = new JPanel( new GridLayout  (1, 2, 10,  0 ));

		// Radio buttons
		this.rbRien    = new JRadioButton( "Rien"      );
		this.rbFrozen  = new JRadioButton( "{gelée}"   );
		this.rbAddOnly = new JRadioButton( "{addOnly}" );
		this.rbRequete = new JRadioButton( "{Requête}" );

		// Groupe de boutons radio
		this.groupeRadio = new ButtonGroup();
		this.groupeRadio.add( rbRien    );
		this.groupeRadio.add( rbFrozen  );
		this.groupeRadio.add( rbAddOnly );
		this.groupeRadio.add( rbRequete );

		// Sélection par défaut
		this.rbRien.setSelected(true);

		// Champs de texte
		this.txtMin = new JTextField( 10 );
		this.txtMax = new JTextField( 10 );

		// Boutons
		this.btnValider = new JButton( "Valider" );
		this.btnAnnuler = new JButton( "Annuler" );

		/*-------------------------------*/
		/*             Style             */
		/*-------------------------------*/

		pnlSelect.setBorder( new TitledBorder( "Attribut"     ) );
		pnlCentre.setBorder( new TitledBorder( "Multiplicité" ) );

		/*-------------------------------*/
		/*           Listeners           */
		/*-------------------------------*/

		this.lst.addActionListener(e -> changerAttribut());
		this.btnValider.addActionListener(this);
		this.btnAnnuler.addActionListener(this);

		/*-------------------------------*/
		/*         Positionnement        */
		/*-------------------------------*/

		// Sélection attribut
		pnlSelect.add(this.lst, BorderLayout.CENTER);

		// Radio buttons
		pnlRadio.add( rbRien    );
		pnlRadio.add( rbFrozen  );
		pnlRadio.add( rbAddOnly );
		pnlRadio.add( rbRequete );

		// Multiplicité
		pnlMult.add( new JLabel("Min :", SwingConstants.RIGHT) );
		pnlMult.add( this.txtMin                               );
		pnlMult.add( new JLabel("Max :", SwingConstants.RIGHT) );
		pnlMult.add( this.txtMax                               );

		// Assemblage du panel central
		pnlCentre.add( pnlRadio, BorderLayout.WEST   );
		pnlCentre.add( pnlMult , BorderLayout.CENTER );

		// Boutons
		pnlSud.add( this.btnValider );
		pnlSud.add( this.btnAnnuler );

		// Panel principal
		this.add( pnlSelect, BorderLayout.NORTH  );
		this.add( pnlCentre, BorderLayout.CENTER );
		this.add( pnlSud   , BorderLayout.SOUTH  );

		// Sélection initiale
		if (this.lst.getItemCount() > 0)
		{
			this.lst.setSelectedIndex(0);
			this.changerAttribut();
		}

		this.setContrainteParDefaut();

		this.setVisible(true);
	}

	/*-------------------------------*/
	/*           Méthodes            */
	/*-------------------------------*/

	/**
	 * Met à jour les champs selon l'attribut sélectionné
	 */
	private void changerAttribut()
	{
		this.attributSelect = (Attribut) this.lst.getSelectedItem();

		if (this.attributSelect == null)
			return;

		Multiplicite mult = this.attributSelect.multiplicite();

		if (mult != null)
		{
			txtMin.setText(mult.getMinimum() == -1 ? "*" : String.valueOf(mult.getMinimum()));
			txtMax.setText(mult.getMaximum() == -1 ? "*" : String.valueOf(mult.getMaximum()));
		}
		else
		{
			txtMin.setText("");
			txtMax.setText("");
		}

		this.setContrainteParDefaut();
	}

	/**
	 * Convertit les valeurs saisies
	 */
	private void convertir()
	{
		try
		{
			min = txtMin.getText().equals("*") ? -1 : Integer.parseInt(txtMin.getText());
			max = txtMax.getText().equals("*") ? -1 : Integer.parseInt(txtMax.getText());
		}
		catch (NumberFormatException e)
		{
			min = max = -2;
		}
	}

	/**
	 * Vérifie la cohérence de la multiplicité
	 */
	private boolean verification()
	{
		if (min < -1 || max < -1)
			return false;

		if (min != -1 && max != -1 && min > max)
			return false;

		return true;
	}

	public void setContrainteParDefaut()
	{
		switch (this.attributSelect.getContrainte())
		{
			case ADD_ONLY -> this.rbAddOnly.setSelected(true);
			case FROZEN   -> this.rbFrozen .setSelected(true);
			case REQUETE  -> this.rbRequete.setSelected(true);
			case RIEN     -> this.rbRien   .setSelected(true);
			default -> {}
		}
	}

	/**
	 * Retourne la contrainte sélectionnée
	 */
	public Contraintes getContrainteSelectionnee()
	{
		if (rbFrozen.isSelected())
			return Contraintes.FROZEN;
		else if (rbAddOnly.isSelected())
			return Contraintes.ADD_ONLY;
		else if (rbRequete.isSelected())
			return Contraintes.REQUETE;
		else
			return Contraintes.RIEN;
	}

	/*-------------------------------*/
	/*           Listeners           */
	/*-------------------------------*/

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Annuler
		if (e.getSource() == this.btnAnnuler)
		{
			this.frame.cacherPanelEdition();
			return;
		}

		// Valider
		if (e.getSource() == this.btnValider && attributSelect != null)
		{
			this.convertir();

			if (verification())
			{
				Multiplicite nouvelleMult = new Multiplicite(min, max);
				attributSelect.setMultiplicite(nouvelleMult);
				this.attributSelect.setContrainte(this.getContrainteSelectionnee());

				this.frame.cacherPanelEdition();
				this.ctrl.majIhm();

				ErrorUtils.showSucces("La multiplicité de l'attribut a été modifiée.");
			}
			else
			{
				ErrorUtils.showError("Multiplicité invalide");
			}
		}
	}
}