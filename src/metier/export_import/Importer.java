package src.metier.export_import;

import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import src.metier.classe.*;
import src.metier.enums.*;
import src.utils.ErrorUtils;

public class Importer
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	static Document document;
	static Element  racine;
	
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	/**
	 * Constructeur de l'importer.
	 * 
	 * @param lienFichier : Le lien vers le fichier XML à importer.
	 */
	public Importer( String lienFichier ) 
	{
		SAXBuilder sxb = new SAXBuilder();
		try 
		{
			// On crée un nouveau document JDOM avec en argument le fichier XML
			// Le parsing est terminé
			document = sxb.build( new File( lienFichier ) );
			racine = document.getRootElement();
		}
		catch (Exception e){ ErrorUtils.showError("Impossible d'ouvrir le ficher"); }

		//Pour notre exemple, l'élément racine sera la balise <region> de notre fichier XML.
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	/**
	 * Importer la liste des classes.
	 * 
	 * La liste des classes est composée des classes et des classes fantômes.
	 * 
	 * @return
	 */
	public List< Classe > importerListClasse()
	{
		// Données :
		// - - - - - 
		Element elemClasses         = racine     .getChild   ( "CLASSES"        );
		List    lstClasseXML        = elemClasses.getChildren( "CLASSE"         );
		List    lstClasseFantomeXML = elemClasses.getChildren( "CLASSE_FANTOME" );
		List    lstAttributXML      = null;
		List    lstMethodeXML       = null;
		
		Classe       classeCourante = null;
		List<Classe> lstClasse      = new ArrayList<Classe>();

		//Parcours :
		// - - - - - 
		// CLASSE 
		Iterator itLstClasse = lstClasseXML.iterator();
		while ( itLstClasse.hasNext() )
		{
			//Nous stockons l'ensemble de la balise <CLASSE> dans un Element.
			Element basliseCourante = (Element) itLstClasse.next();

			//Classe
			classeCourante = this.creerClasse( basliseCourante );
			lstClasse.add( classeCourante );

			//Attributs
			lstAttributXML         = basliseCourante.getChildren( "ATTRIBUTS" );
			Iterator itLstAttribut = lstAttributXML.iterator();
			while ( itLstAttribut.hasNext() )
			{
				//Nous stockons l'ensemble de la balise <ATTRIBUTS> dans un Element.
				Element baliseAttributs = (Element) itLstAttribut.next();

				// On boucle sur les balises <attribut>
				List<Element> lstAttributXML2 = baliseAttributs.getChildren( "attribut" );
				for ( Element baliseAttribut : lstAttributXML2 ) 
				{
					Attribut attribut = this.creerAttribut( baliseAttribut );
					classeCourante.ajouterAttribut( attribut );
				}
			}

			//Méthodes
			lstMethodeXML           = basliseCourante.getChildren( "METHODES" );
			Iterator ilstMethodeXML = lstMethodeXML.iterator();
			while ( ilstMethodeXML.hasNext() )
			{
				//Nous stockons l'ensemble de la balise <METHODES> dans un Element.
				Element baliseAttributs = (Element) ilstMethodeXML.next();

				// On boucle sur les balises <constructeur>
				List<Element> lstMethodeXML2 = baliseAttributs.getChildren( "constructeur" );
				for ( Element baliseConstructeur : lstMethodeXML2 ) 
				{
					Methode methode = this.creerMethode( baliseConstructeur, true );
					classeCourante.ajouterMethode( methode );
				}

				// On boucle sur les balises <methode>
				List<Element> lstMethodeXML3 = baliseAttributs.getChildren( "methode" );
				for ( Element baliseConstructeur : lstMethodeXML3 ) 
				{
					Methode methode = this.creerMethode( baliseConstructeur, false );
					classeCourante.ajouterMethode( methode );
				}
			}

		}

		// CLASSE FANTOME
		Iterator itLstClasseFantome = lstClasseFantomeXML.iterator();
		while ( itLstClasseFantome.hasNext() )
		{
			//Nous stockons l'ensemble de la balise <CLASSE_FANTOME> dans un Element.
			Element basliseCourante = (Element) itLstClasseFantome.next();

			//Classe Fantome
			ClasseExterne classeFantome = this.creerClasseFantome( basliseCourante );
			lstClasse.add( classeFantome );			
		}

		return lstClasse;
	}

	/**
	 * Importer la liste des associations.
	 *  
	 * @return la list d'assocations créer à partir du .xml
	 */
	public List<Association> importerListAssociation( List<Classe> lstClasse )
	{
		// Données :
		// - - - - - - 
		Element elemAssos  = racine   .getChild   ( "ASSOCIATIONS" );
		List    lstAssoXML = elemAssos.getChildren( "ASSOCIATION"  );

		Association          asso    = null;
		List < Association > lstAsso = new ArrayList< Association >();

		//Parcours :
		// - - - - - 
		// Association
		Iterator itLstAssoXML = lstAssoXML.iterator();
		while ( itLstAssoXML.hasNext() ) 
		{
			//Nous stockons l'ensemble de la balise <ASSOCIATION> dans un Element.
			Element basliseCourante = (Element) itLstAssoXML.next();

			//asso
			asso = this.creerAssociation( basliseCourante, lstClasse );
			lstAsso.add( asso );
		}

		return lstAsso;
	}

	/*-------------------------------*/
	/* Méthodes privées              */
	/*-------------------------------*/
	/**
	 * Créer une classe.
	 * 
	 * La création de la classe se fait à partir de la balise "CLASSE".
	 * Les informations sont stockées dans les balises "visibilite", "type", "modifierType", "nom", "posX" et "posY".
	 * 
	 * @param balise
	 * @return
	 */
	private Classe creerClasse( Element balise )
	{
		// Données :
		// - - - - - - 
		Visibilite   visibilite    = Visibilite  .depuisAnglais( this.recupEnfantFormatter( balise, "visibilite" ).toLowerCase() );
		TypeClasse   type          = TypeClasse  .valueOf      ( this.recupEnfantFormatter( balise, "type"       ).toUpperCase() );
		String       nom           =                             this.recupEnfantFormatter( balise, "nom"                        );
		int          posX          = Integer     .parseInt     ( this.recupEnfantFormatter( balise, "posX"       )               );
		int          posY          = Integer     .parseInt     ( this.recupEnfantFormatter( balise, "posY"       )               );
		String       extend        =                             this.recupEnfantFormatter( balise, "extend"                     );
		List<String> lstImplements = this.creerListeImplements(  balise.getChild( "IMPLEMENTS" )                                 );
		Element      modifierElem  =                             balise.getChild( "modifierType"                                 );
		ModifierType modifierType  = modifierElem != null ? ModifierType.depuisAnglais( modifierElem.getText().trim() ) : ModifierType.NORMAL;


		// Création :
		// - - - - - - 
		Classe classe = new Classe( nom, type, visibilite, modifierType );
		classe.setPosX( posX );
		classe.setPosY( posY );
		
		classe.ajouterExtend(extend);
		for (String string : lstImplements) 
			classe.ajouterImplementes( string );
		
		return classe;
	}

	/**
	 * Créer une association.
	 * 
	 * La création de l'association se fait à partir de la balise "ASSOCIATION".
	 * Les informations sont stockées dans les balises "classeSource", "classeCible", "MULTIPLICITE_CIBLE", "MULTIPLICITE_SOURCE", "rSource", "rCible", "typeAssociation" et "estBidirectionnel".
	 * 
	 * @param balise
	 * @param lstClasse
	 * @return
	 */
	private Association creerAssociation( Element balise, List<Classe> lstClasse )
	{
		// Données :
		// - - - - - - 
		Classe          classeSource       = this.trouverClasse         ( this.recupEnfantFormatter( balise, "classeSource"   ), lstClasse  );
		Classe          classeCible        = this.trouverClasse         ( this.recupEnfantFormatter( balise, "classeCible"    ), lstClasse  );
		Multiplicite    multipliciteCible  = this.creerMultiplicite     ( balise.getChild          ( "MULTIPLICITE_CIBLE"     )             );
		Multiplicite    multipliciteSource = this.creerMultiplicite     ( balise.getChild          ( "MULTIPLICITE_SOURCE"    )             );
		String          roleSource         =                              this.recupEnfantFormatter( balise, "rSource"                     );
		String          roleCible          =                              this.recupEnfantFormatter( balise, "rCible"                      );
		TypeAssociation typeAssociation    = TypeAssociation.fromLibelle( this.recupEnfantFormatter( balise, "typeAssociation"       )     );
		boolean         bidirectionnelle   = Boolean        .valueOf    ( this.recupEnfantFormatter( balise, "estBidirectionnel"     )     );

		// Création :
		// - - - - - - 
		Association asso = new Association(classeSource, classeCible, multipliciteCible, multipliciteSource, roleSource, roleCible, typeAssociation, bidirectionnelle);

		return asso;
	}
	
	/**
	 * Trouver une classe par son nom dans la liste des classes.
	 * 
	 * @param nomClasse le nom de la classe à trouver
	 * @param lstClasses la liste des classes à parcourir
	 * @return la classe trouvée, ou null si non trouvée
	 */
	private Classe trouverClasse( String nomClasse, List<Classe> lstClasses )
	{
		for (Classe classe : lstClasses) 
		{
			if ( classe.getNom().equals( nomClasse ) ) { return classe; }	
		}
		return null;
	}
	
	/**
	 * Créer une multiplicité.
	 * 
	 * La création de la multiplicité se fait à partir de la balise "MULTIPLICITE".
	 * Les informations sont stockées dans les balises "mult_min" et "mult_max".
	 * 
	 * @param balise
	 * @return
	 */
	private Multiplicite creerMultiplicite( Element balise )
	{
		// Données :
		// - - - - - 
		int minimum = Integer.parseInt( this.recupEnfantFormatter(balise, "mult_min") );
		int maximum = Integer.parseInt( this.recupEnfantFormatter(balise, "mult_max") );

		return new Multiplicite(minimum, maximum);
	}

	/**
	 * Créer une classe fantôme.
	 * 
	 * La création de la classe fantôme se fait à partir de la balise "CLASSE_FANTOME".
	 * Les informations sont stockées dans les balises "nom", "posX" et "posY".
	 * 
	 * @param balise
	 * @return
	 */
	private ClasseExterne creerClasseFantome( Element balise )
	{
		// Données :
		// - - - - - - 
		String nom  =                    this.recupEnfantFormatter( balise,"nom"     );
		int    posX = Integer.parseInt(  this.recupEnfantFormatter( balise, "posX" ) );
		int    posY = Integer.parseInt(  this.recupEnfantFormatter( balise, "posY" ) );

		// Création :
		// - - - - - - 
		ClasseExterne classeFantome = new ClasseExterne( nom, posX, posY );
		return classeFantome;
	}

	/**
	 * Créer un attribut.
	 * 
	 * La création de l'attribut se fait à partir de la balise "attribut".
	 * Les informations sont stockées dans les balises "visibilite", "portee", "type", "nom", "estFinal" et "valeur".
	 * 
	 * @param balise
	 * @return
	 */
	private Attribut creerAttribut( Element balise )
	{
		// Données :
		// - - - - - -
		Visibilite  visibilite = Visibilite.depuisAnglais( this.recupEnfantFormatter( balise, "visibilite"  ) )   ;
		Portee      portee     = Portee    .depuisAnglais( this.recupEnfantFormatter( balise, "portee"      ) )   ;
		String      type       =                         this.recupEnfantFormatter( balise, "type"        )     ;
		String      nom        =                         this.recupEnfantFormatter( balise, "nom"         )     ;
		Contraintes contrainte = Contraintes.valueOf( this.recupEnfantFormatter( balise, "contrainte"  ) )   ;
		String      valeur     = this.recupEnfantFormatter( balise, "valeur"      ) ;
	
		// Multiplicité (peut être null)
		Multiplicite mult = null;
		Element elemMult = balise.getChild("multiplicite");
		if (elemMult != null)
		{
			try
			{
				int min = Integer.parseInt( this.recupEnfantFormatter(elemMult, "min") );
				int max = Integer.parseInt( this.recupEnfantFormatter(elemMult, "max") );
				mult = new Multiplicite(min, max);
			}
			catch (Exception e)
			{
				// En cas d'erreur, on garde mult = null
			}
		}
	
		// Création :
		// - - - - - -
		Attribut attribut;
		if (mult != null)
		{
			attribut = new Attribut( nom, type, visibilite, portee, contrainte, valeur, mult );
		}
		else
		{
			attribut = new Attribut( nom, type, visibilite, portee, contrainte, valeur );
		}
		
		return attribut;
	}

	/**
	 * Créer une méthode ou un constructeur.
	 * 
	 * La création de la méthode se fait à partir de la balise "methode" ou "constructeur".
	 * Les informations sont stockées dans les balises "visibilite", "portee", "type", "nom" et "parametres".
	 * 
	 * @param balise
	 * @param estConstructeur
	 * @return
	 */
	private Methode creerMethode( Element balise, Boolean estConstructeur )
	{
		// Données :
		// - - - - - -
		Visibilite      visibilite    = Visibilite.depuisAnglais  ( this.recupEnfantFormatter( balise, "visibilite"   ) );
		Portee          portee        = Portee    .depuisAnglais  ( this.recupEnfantFormatter( balise, "portee"       ) );
		String          type          =                             this.recupEnfantFormatter( balise, "type"           );
		String          nom           =                             this.recupEnfantFormatter( balise, "nom"            );
		boolean         estDefault    = Boolean.parseBoolean      ( this.recupEnfantFormatter( balise, "estDefault"   ) );
		
		List<Parametre> lstParametres = this.creerListeParametre( balise.getChild( "parametres" ) );
	
		// Création :
		// - - - - - 
		Methode methode = new Methode( estConstructeur, nom, type, visibilite, ModifierType.NORMAL, portee, lstParametres, estDefault );
		return methode;
	}

	/**
	 * Créer la liste des implements d'une classe.
	 * 
	 * La création de la liste d'implements se fait à partir de la balise "implements".
	 * Chaque implents est défini par une balise "implement".
	 * 
	 * @param balise
	 * @return List<String>, liste contenant les implements
	 */
	private List< String > creerListeImplements( Element balise )
	{
		// Données :
		// - - - - - -
		List< String > lstImplements = new ArrayList< String >();

		//Parcours :
		// - - - - - 		
		List<Element> lstParametreXML = balise.getChildren( "implement" );
		for ( Element element : lstParametreXML ) 
			lstImplements.add( this.recupEnfantFormatter( balise, "implement" ) );

		return lstImplements;
	}

	/**
	 * Créer la liste des paramètres d'une méthode ou d'un constructeur.
	 * 
	 * La création de la liste de paramètres se fait à partir de la balise "parametres".
	 * Chaque paramètre est défini par une balise "parametre" contenant les balises "type" et "nom".
	 * 
	 * @param balise
	 * @return List< Parametre >, liste contenant les parametres
	 */
	private List< Parametre > creerListeParametre( Element balise )
	{
		// Données :
		// - - - - - -
		List< Parametre > lstParametres = new ArrayList< Parametre >();

		//Parcours :
		// - - - - - 
		List<Element> lstParametreXML = balise.getChildren( "parametre" );
		Iterator      itLstParametre  = lstParametreXML.iterator();
		while ( itLstParametre.hasNext() )
		{
			Element baliseParametre = (Element) itLstParametre.next();

			String type = this.recupEnfantFormatter( baliseParametre, "type" );
			String nom  = this.recupEnfantFormatter( baliseParametre, "nom"  );

			Parametre parametre = new Parametre( nom, type );
			lstParametres.add( parametre );
		}

		return lstParametres;
	}

	
	/**
	 * Récupères la chaines et la formatte de la balise données avec sa clée.
	 * @param text le texte à échapper
	 * @return le texte avec les caractères XML 
	 */
	private String recupEnfantFormatter( Element balise, String clee )
	{
		Element mot = balise.getChild( clee );

		if ( mot == null ) return "";
		return this.formatter( mot.getText().trim() );
	}

	/**
	 * Échappe les caractères spéciaux XML dans une chaîne.
	 * @param text le texte à échapper
	 * @return le texte avec les caractères XML échappés
	 */
	private String formatter(String text) 
	{
		if (text == null) return "";

		return text.replace( "%EtCommercial%;"  , "&"  )
				   .replace( "%CrochetGauche%;" , "<"  )
				   .replace( "%CrochetDroit%;"  , ">"  )
				   .replace( "%DoubleCote%;"    , "\"" )
				   .replace( "%CoteSimple%;"    , "'"  );
	}
}
