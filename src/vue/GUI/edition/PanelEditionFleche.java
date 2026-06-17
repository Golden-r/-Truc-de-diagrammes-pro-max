package src.vue.GUI.edition;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import src.ControleurGUI;
import src.metier.classe.Association;
import src.metier.classe.Multiplicite;
import src.utils.ErrorUtils;

/**
 * Panel pour l'edition des proprietes d'une fleche (association).
 * Permet a l'utilisateur de modifier les multiplicites (min/max) et les roles
 * pour la classe source et la classe cible.
 * 
 */
public class PanelEditionFleche extends JPanel implements ActionListener
{
	private ControleurGUI ctrl;

	private Association assoc;
	
	private JTextField multiSource1;
	private JTextField multiSource2;
	private JTextField multiCible1;
	private JTextField multiCible2;
	
	private JTextField txtRoleSource;
	private JTextField txtRoleCible;

	private JButton btnValider;

	private int ms1;
	private int ms2;
	private int mc1;
	private int mc2;


	// Panels gardés en attributs
	private JPanel pnlGauche;
	private JPanel pnlDroite;

	/**
	 * Construit le panel d'edition.
	 * Initialise l'interface graphique et pre-remplit les champs avec les donnees actuelles de l'association.
	 *
	 * @param ctrl Le controleur de l'application.
	 * @param assoc L'association a modifier.
	 */
	public PanelEditionFleche(ControleurGUI ctrl, Association assoc)
	{
		this.ctrl  = ctrl;
		this.assoc = assoc;

		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(new EmptyBorder(10, 10, 10, 10));

		/* ========= HAUT ========= */
		JPanel titre     = new JPanel(new GridLayout(1, 2, 0, 0));
		JPanel pnlCentre = new JPanel(new GridLayout(1, 2, 0, 0));
		JPanel pnlGauche = new JPanel(new GridLayout(3, 1, 0, 0));
		JPanel pnlDroite = new JPanel(new GridLayout(3, 1, 0, 0));

		JPanel temp1 = new JPanel(new BorderLayout(5, 5));
		JPanel temp2 = new JPanel(new BorderLayout(5, 5));
		JPanel temp3 = new JPanel(new BorderLayout(5, 5));
		JPanel temp4 = new JPanel(new BorderLayout(5, 5));
		JPanel temp5 = new JPanel(new BorderLayout(5, 5));
		JPanel temp6 = new JPanel(new BorderLayout(5, 5));

		pnlGauche.setBorder(new TitledBorder(assoc.getClasseSource().getNom()));
		pnlDroite.setBorder(new TitledBorder(assoc.getClasseCible().getNom()));
		
		// les multiplicités sont des INT donc Integer.MAX_VALUE a un longueur max de 10
		this.multiSource1  = new JTextField(10);
		this.multiSource2  = new JTextField(10);
		this.multiCible1   = new JTextField(10);
		this.multiCible2   = new JTextField(10);
		this.txtRoleSource = new JTextField(10);
		this.txtRoleCible  = new JTextField(10);

		temp1.add(new JLabel("Min :"), BorderLayout.WEST);
		temp1.add(this.multiSource1, BorderLayout.CENTER);
		temp2.add(new JLabel("Max :"), BorderLayout.WEST);
		temp2.add(this.multiSource2, BorderLayout.CENTER);
		temp3.add(new JLabel("Role :"), BorderLayout.WEST);
		temp3.add(this.txtRoleSource, BorderLayout.CENTER);

		pnlGauche.add(temp1);
		pnlGauche.add(temp2);
		pnlGauche.add(temp3);

		Multiplicite ms = assoc.getMultipliciteSource();
		Multiplicite mc = assoc.getMultipliciteCible();

		temp4.add(new JLabel("Min : "), BorderLayout.WEST);
		temp4.add(this.multiCible1, BorderLayout.CENTER);
		temp5.add(new JLabel("Max :"), BorderLayout.WEST);
		temp5.add(this.multiCible2, BorderLayout.CENTER);
		temp6.add(new JLabel("Role :"), BorderLayout.WEST);
		temp6.add(this.txtRoleCible, BorderLayout.CENTER);

		multiSource1.setText(ms.getMinimum() == -1 ? "*" : String.valueOf(ms.getMinimum()));
		multiSource2.setText(ms.getMaximum() == -1 ? "*" : String.valueOf(ms.getMaximum()));
		multiCible1 .setText(mc.getMinimum() == -1 ? "*" : String.valueOf(mc.getMinimum()));
		multiCible2 .setText(mc.getMaximum() == -1 ? "*" : String.valueOf(mc.getMaximum()));
		txtRoleCible.setText(assoc.getRoleCible());
		txtRoleSource.setText(assoc.getRoleSource());

		pnlDroite.add(temp4);
		pnlDroite.add(temp5);
		pnlDroite.add(temp6);

		titre.add(new JLabel("Source", SwingConstants.CENTER));
		titre.add(new JLabel("Cible", SwingConstants.CENTER));

		pnlCentre.add(pnlGauche);
		pnlCentre.add(pnlDroite);

		this.btnValider = new JButton("Valider");
		this.btnValider.addActionListener(this);

		this.add(titre          , BorderLayout.NORTH );
		this.add(pnlCentre      , BorderLayout.CENTER);
		this.add(this.btnValider, BorderLayout.SOUTH );

		this.setVisible(true);
	}

	/* =========================
	   CONVERSION
	   ========================= */

	/**
	 * Recupere le contenu des champs de texte et les convertit en entiers.
	 * Gere le caractere '*' comme etant -1 (infini).
	 * En cas d'erreur de format, attribue une valeur d'erreur (-2).
	 */
	private void convertir()
	{
		try
		{
			ms1 = multiSource1.getText().equals("*") ? -1 : Integer.parseInt(multiSource1.getText());
			ms2 = multiSource2.getText().equals("*") ? -1 : Integer.parseInt(multiSource2.getText());
			mc1 = multiCible1 .getText().equals("*") ? -1 : Integer.parseInt(multiCible1 .getText());
			mc2 = multiCible2 .getText().equals("*") ? -1 : Integer.parseInt(multiCible2 .getText());
		}
		catch (NumberFormatException e)
		{
			ms1 = ms2 = mc1 = mc2 = -2;
		}
	}

	/* =========================
	   VERIFICATION
	   ========================= */

	/**
	 * Verifie la coherence des multiplicites saisies.
	 * Verifie que les valeurs sont positives (ou -1) et que le minimum est inferieur ou egal au maximum.
	 *
	 * @return Vrai si les donnees sont valides, faux sinon.
	 */
	private boolean verification()
	{
		if (ms1 < -1 || ms2 < -1 || mc1 < -1 || mc2 < -1)
			return false;

		if (ms1 != -1 && ms2 != -1 && ms1 > ms2)
			return false;

		if (mc1 != -1 && mc2 != -1 && mc1 > mc2)
			return false;

		return true;
	}

	/* =========================
	   ACTION
	   ========================= */
	   
	/**
	 * Gere l'action du bouton Valider.
	 * Convertit, verifie et applique les modifications a l'objet metier avant de fermer la fenetre.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == btnValider && assoc != null)
		{
			this.convertir();

			if (verification())
			{
				Multiplicite m1 = assoc.getMultipliciteSource();
				m1.editerMultiplicite(ms1, ms2);
				Multiplicite m2 = assoc.getMultipliciteCible();
				m2.editerMultiplicite(mc1, mc2);

				this.assoc.setRoleSource( this.txtRoleSource.getText().isEmpty() ? null : this.txtRoleSource.getText() );
				this.assoc.setRoleCible ( this.txtRoleCible .getText().isEmpty() ? null : this.txtRoleCible .getText() ); 

				this.ctrl.getFrameEditionFleche().cacherPanelEdition();
				this.ctrl.majIhm();
				ErrorUtils.showSucces("L'association à bien été modifiée.");
			}
			else
			{
				ErrorUtils.showError("Multiplicités invalides");
			}
		}
	}
}