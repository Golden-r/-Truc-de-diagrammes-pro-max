package src.vue.GUI.panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import src.ControleurGUI;
import src.metier.classe.Classe;
import src.metier.classe.ClasseExterne;
import src.metier.enums.TypeClasse;

/**
 * Panneau d'arborescence pour afficher l'hiérarchie des classes du projet.
 * Affiche un arbre avec toutes les classes du projet (à l'exception des classes externes).
 * Permet la sélection de classes via un TreeSelectionListener.
 * 
 */
public class PanelArboressence extends JPanel
{
	/*-------------------------------*/
	/*         Constantes            */
	/*-------------------------------*/
	/** Couleur de fond du panneau et de l'arborescence */
	private static final Color COULEUR_FOND = new Color(182, 182, 182);

	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	private ControleurGUI          ctrl;
	private JTree                  tree;
	private DefaultTreeModel       treeModel;
	private DefaultMutableTreeNode racine;

	/*-------------------------------*/
	/*         Constructeurs         */
	/*-------------------------------*/
	/**
	 * Constructeur de PanelArboressence.
	 * Initialise le panneau avec l'arborescence des classes et met en place les listeners.
	 * 
	 * @param ctrl Le contrôleur graphique
	 */
	public PanelArboressence( ControleurGUI ctrl )
	{
		this.ctrl = ctrl;
		this.setLayout( new BorderLayout() );

		/*-------------------------------*/
		/*           Creation            */
		/*-------------------------------*/
		this.racine    = new DefaultMutableTreeNode( "Projet"    );
		this.treeModel = new DefaultTreeModel      ( this.racine );
		this.tree      = new JTree( this.treeModel );
		
		this.tree.setBackground( PanelArboressence.COULEUR_FOND );

		this.tree.setShowsRootHandles( true );
		this.tree.setCellRenderer( new JavaTreeRenderer() );

		this.genererArbre();

		this.tree.getSelectionModel().addTreeSelectionListener( new GereSelection() );

		JScrollPane scrollPane = new JScrollPane( this.tree );
		this.add( scrollPane, BorderLayout.CENTER );
	}

	/*-------------------------------*/
	/*           Méthodes            */
	/*-------------------------------*/

	/**
	 * Génère l'arborescence complète des classes du projet.
	 * Parcourt toutes les classes (sauf les classes externes) et les ajoute à l'arbre.
	 */
	public void genererArbre()
	{
		this.racine.removeAllChildren();

		for ( Classe classeCourante : this.ctrl.getLstClasses() )
		{
			if ( classeCourante instanceof ClasseExterne ) continue;

			DefaultMutableTreeNode noeudClasse = new DefaultMutableTreeNode( classeCourante );
			this.racine.add( noeudClasse );
		}

		this.treeModel.reload();
		this.tree.expandRow(0);
	}

	/**
	 * Recharge l'arborescence des classes.
	 */
	public void recharger() { this.genererArbre(); }

	/*-------------------------------*/
	/*           Listener            */
	/*-------------------------------*/

	/**
	 * Classe interne pour gérer la sélection dans l'arborescence.
	 * Notifie le contrôleur quand une classe est sélectionnée.
	 */
	private class GereSelection implements TreeSelectionListener
	{
		/**
		 * Appelé lorsqu'une sélection change dans l'arborescence.
		 * 
		 * @param e L'événement de changement de sélection
		 */
		@Override
		public void valueChanged( TreeSelectionEvent e )
		{
			DefaultMutableTreeNode noeud = (DefaultMutableTreeNode) PanelArboressence.this.tree.getLastSelectedPathComponent();
			if ( noeud == null ) return;
			Object obj = noeud.getUserObject();
			if ( obj instanceof Classe )
			{
				Classe c = (Classe) obj;
				PanelArboressence.this.ctrl.selectionnerClasse( c );
			}
		}
	}

	/*-------------------------------*/
	/*           Renderer            */
	/*-------------------------------*/

	/**
	 * Classe interne pour personnaliser l'affichage des noeuds de l'arborescence.
	 * Affiche une icône colorée (avec la première lettre du type de classe) à côté du nom de la classe.
	 * Utilise des icônes différentes selon le type de classe (classe, interface, enum, record).
	 */ 
	private class JavaTreeRenderer extends DefaultTreeCellRenderer
	{
		/*-------------------------------*/
		/*           Constantes          */
		/*-------------------------------*/
		private final Icon ICON_CLASS     = new IconeSymbole(new Color( 65, 175,  70), "C");
		private final Icon ICON_INTERFACE = new IconeSymbole(new Color(180, 100, 255), "I");
		private final Icon ICON_ENUM      = new IconeSymbole(new Color(230, 140,   0), "E");
		private final Icon ICON_RECORD    = new IconeSymbole(new Color(100, 200, 220), "R");

		/**
		 * Retourne le composant de rendu personnalisé pour un noeud de l'arborescence.
		 * 
		 * @param tree L'arborescence
		 * @param valeur L'objet à afficher (DefaultMutableTreeNode)
		 * @param select Indique si le noeud est sélectionné
		 * @param ouvert Indique si le noeud est ouvert
		 * @param feuille Indique si le noeud est une feuille
		 * @param ligne L'index de la ligne
		 * @param estFocus Indique si le noeud a le focus
		 * @return Le composant de rendu pour ce noeud
		 */
		@Override
		public Component getTreeCellRendererComponent( JTree tree, Object valeur, boolean select, boolean ouvert, boolean feuille, int ligne, boolean estFocus )
		{
			super.getTreeCellRendererComponent( tree, valeur, select, ouvert, feuille, ligne, estFocus );

			this.setBackgroundNonSelectionColor( PanelArboressence.COULEUR_FOND );
			this.setOpaque( true );

			DefaultMutableTreeNode noeud  = (DefaultMutableTreeNode) valeur;
			Object                 obj    = noeud.getUserObject();


			if ( obj instanceof Classe classe )
			{
				String     nom    = classe.getNom();
				TypeClasse type   = classe.getType();

				String suffixe      = "";
				Icon   iconeChoisie = ICON_CLASS; // Par défaut

				// Utilisation des constantes déjà créées
				switch (type)
				{
					case ENUM:
						suffixe = " : enum";
						iconeChoisie = ICON_ENUM;
						break;

					case RECORD:
						suffixe = " : record";
						iconeChoisie = ICON_RECORD;
						break;

					case INTERFACE:
						suffixe = " : interface";
						iconeChoisie = ICON_INTERFACE;
						break;
					default:
						// Class par défaut
						break;
				}

				String html = "<html>" + nom + "<font color='#6e6e6eff'>" + suffixe + "</font>" + "</html>";

				this.setText( html         );
				this.setIcon( iconeChoisie );
			
			}
			else { this.setIcon(null); }

			return this;
		}
	}

	/*-------------------------------*/
	/*          Classe Interne       */
	/*-------------------------------*/
	/**
	 * Classe pour créer une icône personnalisée en forme de cercle avec une lettre au centre.
	 * Utilisée pour afficher les types de classe dans l'arborescence.
	 */
	private static class IconeSymbole implements Icon
	{
		// ATTRIBUTS
		// - - - - - - - -
		private Color  couleur;
		private String lettre;
		private int    taille  = 16;

		// METHODE
		// - - - - - - - -
		/**
		 * Constructeur d'IconeSymbole.
		 * 
		 * @param couleur La couleur du cercle
		 * @param lettre La lettre à afficher au centre
		 */
		public IconeSymbole( Color couleur, String lettre )
		{
			this.couleur = couleur;
			this.lettre  = lettre;
		}

		/**
		 * Peint l'icône à la position spécifiée.
		 * Dessine un cercle coloré avec une lettre blanche au centre.
		 * 
		 * @param c Le composant sur lequel peindre
		 * @param g Le contexte graphique
		 * @param x La coordonnée x
		 * @param y La coordonnée y
		 */
		@Override
		public void paintIcon( Component c, Graphics g, int x, int y )
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  //eviter les pixels

			// Cercle :
			// -- -- -- -- -- -- 

			g2.setColor( couleur );
			g2.fillOval( x, y + 1, taille - 2, taille - 2 );

			// Lettre :
			// -- -- -- -- -- -- 

			g2.setColor( Color.WHITE );
			g2.setFont ( new Font( "SansSerif", Font.BOLD, 10 ) );

			// centrer la lettre
			int largeurLettre = g2.getFontMetrics().stringWidth( lettre );
			g2.drawString( lettre, x + ( taille - largeurLettre ) / 2, y + 12 );
		}

		/**
		 * Retourne la largeur de l'icône.
		 * 
		 * @return La largeur en pixels
		 */
		@Override
		public int getIconWidth()  { return taille; }
		
		/**
		 * Retourne la hauteur de l'icône.
		 * 
		 * @return La hauteur en pixels
		 */
		@Override
		public int getIconHeight() { return taille; }
	}
}