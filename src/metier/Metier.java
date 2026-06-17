package src.metier;

import java.util.List;
import java.util.ArrayList;

import src.metier.classe.Association;
import src.metier.classe.Classe;
import src.metier.classe.ClasseExterne;
import src.metier.classe.Multiplicite;
import src.metier.lecture.Lecture;

/**
 * 	Gère la partie metier de l'outil de rétroconception.
 *
 * Elle intancie la classe qui gère la lecture des fichiers. 
 * Elle contient la List<Classe>. 
 * 
 * 
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class Metier
{
	/*-------------------------------*/
	/*           Attributs           */
	/*-------------------------------*/
	private List<Classe>      lstClasses;
	private List<Association> lstAssociations;
	private Lecture           lect;

	/*-------------------------------*/
	/*         Constructeur          */
	/*-------------------------------*/
	/**
	 * Initialise la lecture, la List<Classe> et la List<Association>.
	 */
	public Metier()
	{
		this.lstClasses      = new ArrayList<Classe     >();
		this.lstAssociations = new ArrayList<Association>();
		this.lect            = new Lecture               ();
	}

	/*-------------------------------*/
	/*            Lecture            */
	/*-------------------------------*/
	/**
	 * Crée une classe à partir du fichier Java spécifié.
	 * 
	 * @param fichier le chemin absolu du fichier Java
	 * @return vrai si la classe a été créée et ajoutée avec succès
	 */
	public boolean creerClasse( String fichier )
	{
		return this.ajouterClasse( this.lect.lireFichier( fichier ) );
	}

	/*-------------------------------*/
	/*            Getters            */
	/*-------------------------------*/
	/** @return Retourne une copie de la liste des classes gérées par le métier. */
	public List<Classe     > getLstClasses          () { return  this.lstClasses;                              }
	public List<Classe     > getLstCopieClasses     () { return new ArrayList< Classe >( this.lstClasses );    }

	
	/** @return Retourne une copie de la liste des associations gérées par le métier. */
	public List<Association> getLstAssociations() { return new ArrayList<Association>( this.lstAssociations ); }

	/*-------------------------------*/
	/*         Modificateurs         */
	/*-------------------------------*/
	/**
	 * Ajoute une classe à la liste si elle n'existe pas déjà.
	 * 
	 * @param classe la classe à ajouter
	 * @return vrai si l'ajout a réussi, faux sinon
	 */
	public boolean ajouterClasse(Classe classe)
	{
		if ( classe == null                                      ) return false;
		if ( this.lstClasses.contains( classe )                  ) return false;
		if ( this.classeExiste(this.lstClasses, classe.getNom()) ) return false;

		this.lstClasses.add( classe );
		return true;
	}
	
	/**
	 * Importe une liste de classes dans la liste des classes gérées par le métier.
	 * 
	 * Cette méthode est utilisé uniquement lors de l'importation depuis un fichier XML.
	 * 
	 * @param classes la liste des classes à importer
	 * @return vrai si l'importation a réussi, faux sinon
	 */
	public boolean importerListes ( List<Classe> lstClasse, List<Association> lstAssos )
	{
		if ( lstClasse == null || lstAssos == null ) { return false; }

		this.lstClasses      = lstClasse;
		this.lstAssociations = lstAssos;
		return true;
	}

	/**
	 * Réinitialise la liste des classes en la vidant.
	 * 
	 * @return vrai si la liste est vide après réinitialisation
	 */
	public boolean resetLstClasses()
	{
		this.lstClasses = new ArrayList<Classe>();
		return this.lstClasses.isEmpty();
	}

	/**
	 * Réinitialise la liste des associations en la vidant.
	 * 
	 * @return vrai si la liste est vide après réinitialisation
	 */
	public boolean resetLstAsso()
	{
		this.lstAssociations = new ArrayList<Association>();
		return this.lstAssociations.isEmpty();
	}
		
	/*-------------------------------*/
	/*           Méthodes            */
	/*-------------------------------*/
	/**
	 * Détecte et crée les associations entre toutes les classes de la liste.
	 */
	public void detecterAssociations()
	{
		this.lstAssociations = Association.detecterAssociations( this.lstClasses );
	}

	/**
	 * Modifie la multiplicité des associations.
	 * 
	 * @param multiplicite la nouvelle multiplicité
	 * @param association  l'association à modifier
	 */
	public void modifierMultiplicite(Multiplicite multiplicite, Association association) 
	{
		if ( multiplicite == null || association == null ) return;

		for ( Association asso : this.lstAssociations ) 
		{
			if ( asso.equals( association ) ) 
			{
				asso.setMultipliciteSource( multiplicite );
				asso.setMultipliciteCible ( multiplicite );
				break;
			}
		}
	}

	/**
	 * Vérifie si une classe avec le nom donné existe dans la liste des classes donnéées.
	 * 
	 * @param nomClasse le nom de la classe à rechercher
	 * @return true si la classe existe, false sinon
	 */
	private boolean classeExiste( List<Classe> lstClasses, String nomClasse ) 
	{
		for ( Classe c : lstClasses ) 
			if ( c.getNom().equals(nomClasse) ) 
				return true; 
		return false;
	}

	/**
	 * Détecte et ajoute les classes externes ( extends/implements ) qui ne font pas partie du projet
	 * 
	 * Cette méthode parcourt toutes les classes existantes et vérifie les relations d'héritage et d'implémentation.
	 * Si une classe étendue ou implémentée n'existe pas déjà dans la liste des classes, elle est ajoutée en tant que ClasseExterne.
	 * Une classe externe est créée uniquement si elle n'est pas déjà présente dans la liste des classes.
	 */
	public void detecterEtAjouterClassesExternes() 
	{
		List<String> classesExternes = new ArrayList<>();
		
		for (Classe c : lstClasses) 
		{
			if ( c.getExtend() != null && !this.classeExiste( this.lstClasses, c.getExtend() ) )
			{
				classesExternes.add( c.getExtend() );
			}

			for (String impl : c.getCopieLstImplementes()) 
			{
				if ( !this.classeExiste( this.lstClasses, impl ) && !classesExternes.contains(impl) )
				{
					classesExternes.add( impl );
				}
			}
		}
		
		for (String nom : classesExternes)
		{
			this.ajouterClasse( new ClasseExterne( nom ) );
		}
	}
}