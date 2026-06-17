package src;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import src.metier.Metier;
import src.metier.classe.Association;
import src.metier.classe.Classe;
import src.vue.CUI.CUI;

/**
 * Le Controleur fait le lien entre la couche métier et la couche vue.
 * Il gère la création de classes à partir de fichiers Java et délègue l'affichage à l'IHM.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class ControleurCUI
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Metier metier;
	private CUI    ihmCui;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	/**
	 * Initialise le contrôleur avec la couche métier et l'interface utilisateur CUI.
	 */
	public ControleurCUI ()
	{
		this.metier = new Metier ();
		this.ihmCui = new CUI(this);
	}

	/*-------------------------------*/
	/* Lancement                     */
	/*-------------------------------*/
	/**
	 * Lance l'application en déléguant l'affichage à l'IHM CUI
	 */
	public void lancer()
	{
		this.ihmCui.lancer();
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	/**
	 * Retourne la liste des classes chargées.
	 * @return la liste des classes
	 */
	public List<Classe> getLstClasses() { return this.metier.getLstClasses(); }

	public void resetLstClasses() { this.metier.resetLstClasses(); }

	/**
	 * Retourne la liste des associations où la classe spécifiée fait partie.
	 * @param classe la classe dont on recherche les associations
	 * @return la liste des associations contenant cette classe
	 */
	public List<Association> getLstAssoDeClasse( Classe classe )
	{
		List<Association> lstAssoClasse = new ArrayList<Association>();
		
		for ( Association asso : this.metier.getLstAssociations() )
		{
			if ( asso.getClasseSource().getNom().equals( classe.getNom() ) ||
				 asso.getClasseCible().getNom().equals( classe.getNom() ) )
			{
				lstAssoClasse.add( asso );
			}
		}
		
		return lstAssoClasse;
	}

	/*-------------------------------*/
	/* Lecture / creation de classe  */
	/*-------------------------------*/
	public boolean creerClasse( String fichier )
	{
		boolean cree = false;

		cree = this.metier.creerClasse(fichier);
		
		if ( ! cree )
		{
			System.out.println( "Erreur lors de la création du fichier : " + fichier);
		}
		
		this.metier.detecterAssociations();
		return true;
	}

	public void ouvertureRepertoire( String repertoire ) 
	{
		if ( repertoire == null ) return;
	
		File dossier = new File( repertoire );
		File[] fichiers = dossier.listFiles();
		
		if (fichiers == null) return;
		
		for ( File fichier : fichiers ) 
		{
			if ( fichier.isDirectory() ) 
				ouvertureRepertoire( fichier.getAbsolutePath() );

			else if ( fichier.isFile() && fichier.getName().endsWith(".java") ) 
				this.creerClasse( fichier.getAbsolutePath() );
		}
	}


	/*-------------------------------*/
	/* Affichcage CUI                */
	/*-------------------------------*/
	/**
	 * Affiche les classes selon le format demandé.
	 * @param exo le numéro du format d'affichage (1: simple, 2: UML d'une classe, 3: UML de plusieurs classes)
	 */
	public void afficherCUI( int exo )
	{
		List< Classe > lstClasse = this.metier.getLstCopieClasses();

		for ( Classe classe : lstClasse ) 
			System.out.println( this.ihmCui.toString( classe, lstClasse ) ); 

	}


	/*-------------------------------*/
	/* MAIN                          */
	/*-------------------------------*/
	/**
	 * Point d'entrée de l'application.
	 * @param args les arguments de ligne de commande
	 */
	public static void main(String[] args)
	{
		ControleurCUI ctrl = new ControleurCUI();
		ctrl.lancer();
	}
}
