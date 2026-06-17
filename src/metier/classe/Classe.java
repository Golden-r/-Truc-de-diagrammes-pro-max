package src.metier.classe;

import java.util.List;
import java.util.ArrayList;
import src.metier.enums.ModifierType;
import src.metier.enums.Portee;
import src.metier.enums.TypeClasse;
import src.metier.enums.Visibilite;

/**
* La classe Classe représente une structure de classe Java, avec ses attributs, méthodes, associations, héritages et implémentations.
*
* Elle permet de stocker et manipuler les différentes composantes d'une classe, y compris les records et les enums.
*
* @author Equipe 1
* @author CHEVEAU Matéo
* @author DAMESTOY Ethan
* @author HERMILLY Joshua
* @author LAFOSSE Lucas
* @author LECLERC Jonathan
*/
public class Classe
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private String         nom         ;
	private TypeClasse     type        ; // class , interface, enum, record
	private Visibilite     visibilite  ; // public, private, protected, package
	private ModifierType   modifierType; // final, abstract, ""
	private List<Attribut> lstAttributs;
	private List<Methode>  lstMethodes ;
	private String         extend      ;
	private List<String>   implementes ;

	//Coordonnées dans la frame UML ( axe haut gauche )
	private int            posX;
	private int            posY;

	/*-------------------------------*/
	/* Constructeurs                 */
	/*-------------------------------*/
	/**
	* Constructeur minimal, crée une classe avec le nom donné et des listes vides pour les attributs et méthodes.
	* @param nom le nom de la classe
	*/
	public Classe( String nom )
	{
		this( nom, TypeClasse.CLASS, Visibilite.PACKAGE, ModifierType.NORMAL, new ArrayList<Attribut>(), new ArrayList<Methode>() );
	}

	/**
	* Constructeur intermédiaire, permet de spécifier le type, la visibilité et le modificateur de la classe.
	*
	* @param nom          le nom de la classe
	* @param type         le type de la classe (class, interface, enum, record)
	* @param visibilite   la visibilité de la classe
	* @param modifierType le modificateur (final, abstract, ...)
	*/
	public Classe( String nom, TypeClasse type, Visibilite visibilite, ModifierType modifierType )
	{
		this( nom, type, visibilite, modifierType, new ArrayList<Attribut>(), new ArrayList<Methode>() );
	}

	/**
	* Constructeur principal, permet d'initialiser tous les champs principaux de la classe.
	* @param nom          le nom de la classe
	* @param type         le type de la classe
	* @param visibilite   la visibilité
	* @param modifierType le modificateur
	* @param lstAttributs la liste des attributs
	* @param lstMethodes  la liste des méthodes
	*/
	public Classe( String       nom         , TypeClasse     type        , Visibilite    visibilite,  
				   ModifierType modifierType, List<Attribut> lstAttributs, List<Methode> lstMethodes )
	{
		this.nom          = nom;
		this.type         = type;
		this.visibilite   = visibilite;
		this.modifierType = modifierType;
		this.lstAttributs = lstAttributs;
		this.lstMethodes  = lstMethodes;
		this.extend       = null;
		this.implementes  = new ArrayList<String>();
		this.posX         = -1;
		this.posY         = -1;
	}

	/*-------------------------------*/
	/* Modificateurs                 */
	/*-------------------------------*/
	// Attributs
	/**
	* Ajoute un attribut à la classe si non déjà présent.
	* @param attribut l'attribut à ajouter
	* @return vrai si l'ajout a réussi, faux sinon
	*/
	public boolean ajouterAttribut( Attribut attribut )
	{
		if ( attribut == null                       ) return false;
		if ( this.lstAttributs.contains( attribut ) ) return false;

		this.lstAttributs.add( attribut );
		return true;
	}

	// Methodes
	/**
	* Ajoute une méthode à la classe si non déjà présente.
	* @param methode la méthode à ajouter
	* @return vrai si l'ajout a réussi, faux sinon
	*/
	public boolean ajouterMethode( Methode methode )
	{
		if ( methode == null                      ) return false;
		if ( this.lstMethodes.contains( methode ) ) return false;

		this.lstMethodes.add( methode );
		return true;
	}

	// implements
	/**
	* Ajoute une implémentation d'interface à la classe si non déjà présente.
	* @param classe le nom de l'interface à ajouter
	* @return vrai si l'ajout a réussi, faux sinon
	*/
	public boolean ajouterImplementes( String classe )
	{
		if ( classe == null                      ) return false;
		if ( this.implementes.contains( classe ) ) return false;

		this.implementes.add( classe );
		return true;
	}

	// extends
	/**
	* Définit la classe parente (extends) si non déjà définie.
	* @param classe le nom de la classe parente
	* @return vrai si l'ajout a réussi, faux sinon
	*/
	public boolean ajouterExtend( String classe )
	{
		if ( classe      == null ) return false;
		if ( this.extend != null ) return false;

		this.extend = classe;
		return true;
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	/**  @return le nom de la classe */
	public String         getNom                () { return this.nom                                          ; }

	/**  @return le type de la classe */
	public TypeClasse     getType               () { return this.type                                         ; }

	/**  @return la visibilité de la classe */
	public Visibilite     getVisibilite         () { return this.visibilite                                   ; }

	/**  @return le modificateur de la classe */
	public ModifierType   getModifierType       () { return this.modifierType                                 ; }

	/**  @return une copie de la liste des attributs */
	public List<Attribut> getCopieLstAttributs  () { return new ArrayList<Attribut>   ( this.lstAttributs    ); }

	/**  @return une copie de la liste des méthodes */
	public List<Methode>  getCopieLstMethodes   () { return new ArrayList<Methode>    ( this.lstMethodes     ); }

	/**  @return le nom de la classe parente (extends) */
	public String         getExtend             () { return this.extend                                       ; }

	/**  @return une copie de la liste des interfaces implémentées */
	public List<String>   getCopieLstImplementes() { return new ArrayList<String>     ( this.implementes     ); }

	/**  @return la position x de la classe dans la frame UML */
	public int            getPosX               () { return this.posX                                         ; }
	
	/**  @return la position y de la classe dans la frame UML */
	public int            getPosY               () { return this.posY                                         ; }

	/*-------------------------------*/
	/*            Setters            */
	/*-------------------------------*/
	public void setPosX ( int posX ) { this.posX = posX;         }
	public void setPosY ( int posY ) { this.posY = posY;         }

	/**
	* Retourne la liste des attributs qui ne sont pas des associations avec les classes données.
	*
	* @param lstClasses la liste des classes à exclure
	* @return la liste des attributs hors associations
	*/
	public List<Attribut> getCopieLstAttributsHorsAsso( List<Classe> lstClasses )
	{
		List<Attribut> lstTmpAttributs = new ArrayList<Attribut>();
		boolean        trouve          = false;

		for (Attribut attribut : this.lstAttributs)
		{
			trouve = false;

			for (Classe classe : lstClasses)
			{
				if ( classe  .getNom().equals  ( attribut.type  () ) ||
					 attribut.type  ().contains( classe  .getNom() )   )
				{
					trouve = true;
					break;
				}
			}

			if ( !trouve || this.type == TypeClasse.ENUM ) { lstTmpAttributs.add( attribut );}
		}

		return lstTmpAttributs;
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	/**
	* Génère les méthodes spécifiques à une classe de type record (constructeur et accesseurs).
	*/
	public void creerClasseRecord()
	{
		// params constructeur
		ArrayList<Parametre> paramsConstructeur = new ArrayList<Parametre>();
		Methode              methode            = null;

		for (Attribut attribut : this.lstAttributs) { paramsConstructeur.add( new Parametre( attribut ) ); }

		// constructeur
		methode = new Methode(true,
							  this.nom,
							  "",
							  Visibilite.PUBLIC,
							  ModifierType.NORMAL,
							  Portee.CLASSE,
							  paramsConstructeur,
							  false                 // false par défaut car ici on créer un record
							);

		this.ajouterMethode( methode );

		for (Attribut attribut : this.lstAttributs)
		{
			methode = new Methode( false, 
								   attribut.nom (), 
								   attribut.type(), 
								   Visibilite  .PUBLIC, 
								   ModifierType.NORMAL, 
								   Portee      .INSTANCE , 
								   new ArrayList<Parametre>(), 
								   false 
								);

			this.ajouterMethode( methode );
		}
	}

	/*-------------------------------*/
	/* toString                      */
	/*-------------------------------*/
	/**
	* Retourne une représentation textuelle de la classe, avec ses attributs et méthodes.
	* @return la chaîne représentant la classe
	*/
	public String toString()
	{
		String res = "Classe : " + this.nom + "\n";

		int cpt = 0;
		for (Attribut attribut : lstAttributs) { res += "attributs " + ++cpt + " : " + attribut.toString() + "\n"; }

		res += "\n";

		for (Methode methode : lstMethodes) { res += methode.toString() + "\n\n"; }

		return res;
	}
}