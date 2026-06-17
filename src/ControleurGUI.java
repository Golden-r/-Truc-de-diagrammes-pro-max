package src;

import java.io.File;
import java.util.List;
import src.metier.Metier;
import src.metier.classe.Association;
import src.metier.classe.Classe;
import src.metier.classe.Multiplicite;
import src.metier.export_import.Exporter;
import src.metier.export_import.Importer;
import src.vue.GUI.FrameUML;
import src.vue.GUI.dessin.classe.ElementDiagramme;
import src.vue.GUI.edition.FrameEditionFleche;
import src.vue.GUI.edition.FrameEditionAttribut;
import src.vue.chargement.FrameChargement;

/**
 * Le Controleur fait le lien entre la couche métier et la couche vue.
 * Il gère la création de classes à partir de fichiers Java et délègue l'affichage à l'IHM.
 *
 * Il permet également la sélection de fichiers via une Frame contenenant un JFileChooser.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class ControleurGUI
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	// metier
	private Metier    metier;

	// ihm
	private FrameUML  frameUml;

	// edition
	private FrameEditionFleche   frameEditionFleche;
	private FrameEditionAttribut frameEditionAttribut;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	/**
	 * Initialise le contrôleur avec la couche métier et l'interface utilisateur GUI.
	 */
	public ControleurGUI ()
	{
		this.metier               = new Metier ();
		this.frameUml             = new FrameUML( this );
		this.frameEditionFleche   = new FrameEditionFleche(this);
		this.frameEditionAttribut = new FrameEditionAttribut(this);
		
		this.frameUml.setVisible(true);
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	/** @return la liste des classes et associations du modèle */
	public List<Classe>           getLstClasses         () { return this.metier  .getLstClasses         (); }

	/** @return la liste des copies des classes du modèle */
	public List<Classe>           getLstCopieClasses    () { return this.metier  .getLstCopieClasses    (); }

	/** @return la liste des associations du modèle */
	public List<Association>      getLstAssociations    () { return this.metier  .getLstAssociations    (); }

	/** @return la liste des éléments visuels (classes) du diagramme */
	public List<ElementDiagramme> getLstClassesVisuelles() { return this.frameUml.getElementDiagrammes  (); }

	/*-------------------------------*/
	/* Modificateurs                 */
	/*-------------------------------*/
	/**
	 * Modifie la multiplicité d'une association dans le modèle.
	 * @param multiplicite
	 * @param association
	*/
	public void modifierMultiplicite( Multiplicite multiplicite, Association association ) { this.metier.modifierMultiplicite(multiplicite, association); }

	/** Reset la liste des classes */
	public void resetLstClasses() { this.metier.resetLstClasses();                             }
	/** Reset la liste des associations */
	public void resetLstAsso   () { this.metier.resetLstAsso   ();                             }
	/** Reset les listes des classes et associations */
	public void resetTout      () { this.metier.resetLstClasses(); this.metier.resetLstAsso(); } 

	/*-------------------------------*/
	/* Lecture / creation de classe  */
	/*-------------------------------*/

	/**
	 * Crée une classe à partir d'un fichier Java.
	 * @param fichier le chemin du fichier Java
	 * @return true si la création a réussi, false sinon
	 */
	public boolean creerClasse( String fichier )
	{
		this.metier.creerClasse(fichier);
		this.metier.detecterAssociations();
		return true;
	}
	
	/**
	 * Ouvre la frame de sélection de fichiers pour créer des classes.
	 */
	public void creerLstClasse() 
	{
		new FrameChargement(this, "UML Diagram Editor - Sélection des fichiers");
	}

	/**
	 * Traite un dossier pour créer des classes à partir des fichiers Java qu'il contient.
	 * 
	 * @param dossier le dossier à traiter
	 * @param recursif vrai si le traitement doit être récursif dans les sous-dossiers
	 */
	public void traiterDossier(File dossier, boolean recursif)
	{
		File[] fichiers = dossier.listFiles();
		if (fichiers != null)
		{
			for (File f : fichiers)
			{
				if ( f.isFile() && f.getName().endsWith(".java") )
				{
					this.metier.creerClasse( f.getAbsolutePath() );
				}
				else if (f.isDirectory() && recursif) { traiterDossier(f, true); }
			}
		}
	}

	/**
	 * Traite une liste de fichiers et dossiers pour créer des classes.
	 * 
	 * @param fichiers la liste des fichiers et dossiers à traiter
	 * @param recursif vrai si le traitement des dossiers doit être récursif
	 */
	public void traiterListeFichiers(List<File> fichiers, boolean recursif)
	{
		this.metier.resetLstClasses();
		this.metier.resetLstAsso   ();
		
		if (fichiers != null)
		{
			for (File f : fichiers)
			{
				if (f.isDirectory())
				{
					//System.out.println("== Dossier sélectionné : " + f.getAbsolutePath());
					this.traiterDossier(f, recursif);
				}
				else
				{
					//System.out.println("== Fichier sélectionné : " + f.getAbsolutePath());
					this.metier.creerClasse(f.getAbsolutePath());
				}
			}
		}
		this.metier.detecterEtAjouterClassesExternes();
		this.metier.detecterAssociations();
		this.reloadGUI();
	}

	/*-------------------------------*/
	/* Gestion   GUI                 */
	/*-------------------------------*/
	/** Recharge l'interface graphique.  */
	public void                reloadGUI                 () { this.frameUml.reload();                           }
	/** Affiche l'interface graphique.   */
	public void                 loadGui                  () { this.frameUml.load();                             }
	/** Ouvre le panel de configuration des couleurs des flèches. */
	public void                 ouvrirPanelCouleurFleche () { this.frameUml.ouvrirPanelCouleurFleche();         }
	/** Ouvre le panel de configuration des couleurs du fond. */
	public void                 ouvrirPanelCouleurFond   () { this.frameUml.ouvrirPanelCouleurFond();           }
	/** Indique si les classes fantome doivent être affichées. */
	public boolean              afficherClassesExternes  () { return this.frameUml.isAfficherClassesExternes(); }
	/** Indique si l'interface graphique est ouverte. */
	public boolean              estOuvert                () { return this.frameUml.isVisible();                 }
	/** @return la frame d'édition des flèches */
	public FrameEditionFleche   getFrameEditionFleche    () { return this.frameEditionFleche;                   }
	/** @return la frame d'édition des attributs */
	public FrameEditionAttribut getFrameEditionAttribut  () { return this.frameEditionAttribut;                 }
	/** Rafraîchit l'interface graphique. */
	public void                 majIhm                   () { this.frameUml.majIhm();                           }

	/** Capture l'écran de l'interface graphique. 
	 * @param chemin le chemin du fichier de capture
	 */
	public void captureEcran(String chemin ) { this.frameUml.captureEcran(chemin); }
	
	/** Exporte le modèle vers un fichier.
	 * @param chemin le chemin du fichier d'exportation
	 */
	public void exporter (String chemin ) 
	{ 
		Exporter.exporter(this.getLstClasses(), this.getLstAssociations(), chemin);
	}

	/** Importe le modèle depuis un fichier.
	 * @param chemin le chemin du fichier d'importation
	 */
	public void importer ( String chemin )
	{ 
		this.metier.resetLstClasses();
		this.metier.resetLstAsso   ();

		Importer          importer  = new Importer( chemin );
		List<Classe     > lstClasse = importer.importerListClasse     ();
		List<Association> lstAssos  = importer.importerListAssociation( lstClasse );

		this.metier .importerListes( lstClasse, lstAssos );
		
		importer = null;
		this.loadGui(); 
	}

	/** Sélectionne une classe dans l'interface graphique.
	 * @param c la classe à sélectionner
	 */
	public void selectionnerClasse( Classe c ) { this.frameUml.selectionnerClasse( c ); }
	
	/*-------------------------------*/
	/* MAIN                          */
	/*-------------------------------*/
	/**
	 * Point d'entrée de l'application.
	 * @param args les arguments de ligne de commande
	 */
	public static void main(String[] args) 
	{ 
		new ControleurGUI(); 
	}
}