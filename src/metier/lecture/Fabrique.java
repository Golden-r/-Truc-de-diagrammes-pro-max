package src.metier.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.metier.classe.Attribut;
import src.metier.classe.Classe;
import src.metier.classe.Methode;
import src.metier.classe.Parametre;
import src.metier.enums.*;

/**
 * La classe Fabrique possède les méthodes pour la création des élémements.
 *  - classes
 *  - attributs
 *  - méthodes
 *  - ...
 * La création se fait via les chaînes de caractères issues de la lecture de fichiers.
 *
 * Elle gère la création de classes (classiques, interfaces, enums, records),l'ajout d'extends/implements.
 * La création d'attributs, de méthodes, de records et la récupération des paramètres associés à ces éléments.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public class Fabrique
{
	/*-------------------------------*/
	/* Normalisation types externes */
	/*-------------------------------*/

	/**
	 * Nettoie un nom de type externe pour l'affichage et la comparaison.
	 * - supprime les caractères de fin (ex: ';')
	 * - garde uniquement le dernier segment après '.' (ex: javax.swing.JFrame -> JFrame)
	 */
	private String nettoyerNomTypeExterne(String nom)
	{
		if (nom == null) return "";
		String n = nom.trim();
		if (n.endsWith(";")) n = n.substring(0, n.length() - 1).trim();

		// On garde le dernier segment (package. -> nom simple)
		int idx = n.lastIndexOf('.');
		if (idx != -1 && idx + 1 < n.length())
			n = n.substring(idx + 1);

		// Nettoyage résiduel (rare)
		if (n.endsWith(",")) n = n.substring(0, n.length() - 1);
		if (n.endsWith(">")) n = n.substring(0, n.length() - 1).trim();

		return n;
	}

	/*-------------------------------*/
	/* Creation                      */
	/*-------------------------------*/
	/**
	 * Crée un objet Classe à partir d'une ligne d'instruction envoyé en paramètre.
	 *
	 * Gère la création de classes, interfaces, enums et records, ainsi que l'ajout des extends et implémentations.
	 * Pour les records, crée également les attributs et méthodes prédéfinies.
	 *
	 * @param ligne la ligne de déclaration de la classe
	 * @return      l'objet Classe créé, ou null si la création échoue
	 */
	public Classe creerClasse( String ligne )
	{
		// public Classe( String nom, TypeClasse type, Visibilite visibilite )
		//  -> autres attibuts gérer averc les méthodes de classe

		// données de parcours :
		Scanner sc = new Scanner( ligne );

		// Attributs de classe
		// - - - - - - - - - - 
		Classe       classe         = null;
		String       nom            = "";
		TypeClasse   typeClass      = TypeClasse  .CLASS;
		Visibilite   visibilite     = Visibilite  .PACKAGE;
		ModifierType modifierType   = ModifierType.NORMAL;
		String       tmp            = "";

		// Récup :
		// - - - - - - -
		// visibilité
		visibilite = this.recupVisi(sc, ligne);

		// modifierType
		modifierType = this.recupModifierType(sc, ligne);

		// typeClass
		typeClass = this.recupTypeClass(sc, ligne);

		// nom
		if ( nom.equals("") && sc.hasNext() ) { nom = sc.next(); }

		// Erreur récup nom
		if (nom.equals("")) { sc.close(); return null; }


		// Creation :
		// - - - - - - -
		classe = new Classe( nom, typeClass, visibilite, modifierType );

		/**
		 * Gérer les autres paramètres de la ligne :
		 *
		 *  - EXTENDS
		 *  - IMPLEMETES
		 *  - RECORD ( ATTRIBUT )
		 */
		// RECORD
		if ( typeClass == TypeClasse.RECORD )
		{
			this.ajouterRecord( this.recupLstParams( ligne ), classe );
		}
		
		// EXTEND + IMPLEMENTS
		while ( sc.hasNext() )
		{
			tmp = sc.next();

			// EXETNDS
			if ( tmp.equals("extends") && sc.hasNext() )
			{
				String nomExtend = this.nettoyerNomTypeExterne( sc.next() );
				classe.ajouterExtend( nomExtend );
			}



			// IMPLEMENTS ( plusieurs possbiles )
			else if ( tmp.equals("implements") && sc.hasNext() )
			{
					while ( sc.hasNext() )
					{
							String nomImplementes = this.nettoyerNomTypeExterne(sc.next());


					// Gérer si implements Type _ , _ Type2
					//  car ',' est prise dans next
					if ( nomImplementes.charAt( nomImplementes.length() - 1) == ',' )
						nomImplementes = nomImplementes.substring( 0, nomImplementes.length()-1 );

					// ajout si pas vide ( eviter car ','-> '' )
					if ( ! nomImplementes.trim().isEmpty() )
						classe.ajouterImplementes( nomImplementes );
				}
			}
		}

		sc.close();
		return classe;
	}

	/**
	 * Ajoute les attributs à une classe de type record à partir d'une liste de paramètres.
	 *
	 * Les attributs sont créés à partir des paramètres et ajoutés à la classe.
	 * Ensuite les méthodes spécifiques aux records sont générées.
	 *
	 * ( Les attributs et méthodes seront prédéfinis, on ne gère pas les redéfinitions )
	 *
	 * @param lstParamRecord la liste des paramètres du record
	 * @param classe         la classe {@link Classe} à compléter
	 */
	private void ajouterRecord( List<Parametre> lstParamRecord, Classe classe )
	{
		for (Parametre para : lstParamRecord)
		{
			classe.ajouterAttribut( ( new Attribut( para.nom ()       ,
													para.type()       ,
													Visibilite.PUBLIC ,
													Portee.INSTANCE   ,
													Contraintes.FROZEN,
													"" ) ) );
		}
		classe.creerClasseRecord();
	}

	/**
	 * Crée un objet {@link Attribut} à partir d'une ligne de déclaration Java.
	 *
	 * Récupère la visibilité, la portée, le type, le nom et le statut final de
	 * l'attribut à partir de la ligne fournie.
	 *
	 * @param ligne la ligne de déclaration de l'attribut
	 * @return l'objet {@link Attribut} créé
	 */
	public Attribut creeAttribut(String ligne)
	{
		// On le stocke dans un record -> Attribut( String nom, String type, Visibilite visibilite, Portee portee )

		// Données de parcours
		Scanner scLigne = new Scanner(ligne);

		// Attributs de Attribut
		// - - - - - - - - - - -
		String     nom;
		String     type;
		Visibilite visibilite;
		Portee     portee; 

		Contraintes estFinal = Contraintes.RIEN;
		String      finalValeur = "";

		// visibilité
		visibilite = this.recupVisi(scLigne, ligne);

		// portee
		portee = this.recupPortee(scLigne, ligne);

		// Constante
		if (ligne.contains("final")) { estFinal = Contraintes.FROZEN; scLigne.next(); }

		// type
		if ( ! scLigne.hasNext() ) { scLigne.close(); return null; }
		type = scLigne.next();

		// nom
		if ( ! scLigne.hasNext() ) { scLigne.close(); return null; }
		nom = scLigne.next().replace(";", "");

		// SI FINAL 
		if ( ligne.contains("=") && scLigne.hasNext() && ligne.contains( "final" ) )
		{
			scLigne.next(); // pour éviter de prendre le "="
			String temp; 

			// Si la ligne contient "", c'est donc un string
			if ( ligne.contains( "\"" ) )
			{
				int posDeb = ligne.indexOf( "\""           );		
				int posFin = ligne.indexOf( "\"", posDeb+1 );
				
				
				temp = ligne.substring( posDeb-1, posFin+1 );

				// Si c'est un string, on a formaté la chaine de caractère avant pour éviter des problèmes de formatterLigne()
				temp = temp.replace("ACCO_OUVRANTE" , "{")
						   .replace("ACCO_FERMANTE" , "}")
						   .replace("SLASH_14100"   , "/");
			}
			else
			{
				// On récupère le type non immuable 
				temp = scLigne.next().trim();
				if      ( temp.isEmpty()      || ligne.contains("new") || temp.trim().equals(";") ) { temp  = "..."; }
				else if ( ligne.contains("(") && ligne.contains(")")                        ) { temp += "()";  }

			}
			finalValeur = temp;
		}

		// Ajout
		Attribut attribut = new Attribut( nom, type, visibilite, portee, estFinal, finalValeur );

		scLigne.close();
		return attribut;
	}

	/**
	 * Crée un objet Methode à partir d'une ligne d'instruction et du nom de la classe.
	 *
	 * Gère:
	 *  - la détection des constructeurs,
	 *  - la récupération du type de retour,
	 *  - du nom,
	 *  - des paramètres,
	 *  - de la visibilité,
	 *  - du type de modification
	 *  - de la portée.
	 *
	 * @param ligne     la ligne de déclaration de la méthode
	 * @param nomClasse le nom de la classe à laquelle la méthode appartient
	 * @return l'objet {@link Methode} créé
	 */
	public Methode creerMethode( String ligne, String nomClasse )
	{
		// Methode( boolean      estConstructeur, String nom   , String type, Visibilite visibilite,
		//          ModifierType modifierType   , Portee portee, List<Parametre> lstParametres        )

		// Données de parcours
		Scanner scLigne = new Scanner( ligne );

		boolean         estDefault      = false;
		boolean         estConstructeur = false;
		String          type            = "";

		String          nom;
		Visibilite      visibilite;
		ModifierType    modifierType;
		Portee          portee;
		List<Parametre> lstParametres;

		visibilite   = this.recupVisi        ( scLigne, ligne );
		portee       = this.recupPortee      ( scLigne, ligne );
		modifierType = this.recupModifierType( scLigne, ligne );

		// Détection constructeur ou type
		String mot1 = ( scLigne.hasNext() ? scLigne.next() : "") ;
		String mot2 = ( scLigne.hasNext() ? scLigne.next() : "") ;
		
		// Si le mot suivant est "(", c'est que mot1 est le nom -> Constructeur
		if ( mot2.equals("(") )
		{
			estConstructeur = true;
			nom             = mot1;
		}
		// Sinon, mot1 est le Type et mot2 est le Nom -> Méthode
		else
		{
			if ( mot1.equals("default") )
			{
				estDefault = true;
				type = mot2;
				nom  = scLigne.next().trim();
			}
			else
			{
				type = mot1;
				nom  = mot2;
			}
		}

		//enlever les ()
		int inderParenthese = nom.indexOf( "(" );
		if ( inderParenthese != -1 ) nom = nom.substring( 0, inderParenthese );

		// Paramètres
		lstParametres = this.recupLstParams( ligne);

		scLigne.close();
		return new Methode(estConstructeur, nom, type, visibilite, modifierType, portee, lstParametres, estDefault );
	}

	/* ------------------------------------ */
	/* Méthodes de récupération d'attributs */
	/* ------------------------------------ */
	/**
	 * Récupère la visibilité à partir d'une ligne envoyé et passe le mot si trouvé.
	 *
	 * @param sc    le scanner sur la ligne (pour skip le mot si trouvé)
	 * @param ligne la ligne de déclaration
	 * @return l'objet {@link Visibilite}, ou PACKAGE par défaut
	 */
	private Visibilite recupVisi( Scanner sc, String ligne )
	{
		for ( Visibilite visi : Visibilite.values() )
		{
			if ( ligne.contains(visi.getAnglais() ) )
			{
				sc.next();
				return visi;
			}
		}
		return Visibilite.PACKAGE;
	}

	/**
	 * Récupère la portée (instance ou classe) à partir d'une ligne envoyé et passe le mot si trouvé.
	 *
	 * @param sc    le scanner sur la ligne (pour skip si trouvé)
	 * @param ligne la ligne de déclaration
	 * @return l'objet {@link Portee}, ou INSTANCE par défaut
	 */
	private Portee recupPortee(Scanner sc, String ligne)
	{
		if ( ligne.contains("static") )
		{
			sc.next();
			return Portee.CLASSE;
		}
		return Portee.INSTANCE;
	}

	/**
	 * Récupère le type de modification (normal, final, abstract, etc.) à partir d'une ligne envoyé et passe le mot si trouvé.
	 *
	 * @param sc    le scanner sur la ligne (pour skip le mot si trouvé)
	 * @param ligne la ligne de déclaration
	 * @return le type de modification détecté, ou NORMAL si trouvé
	 */
	private ModifierType recupModifierType( Scanner sc, String ligne )
	{
		for ( ModifierType mType : ModifierType.values() )
		{
			if ( ligne.contains(mType.getAnglais()) )
			{
				sc.next();
				return mType;
			}
		}
		return ModifierType.NORMAL;
	}

	/**
	 * Récupère le type de classe (class, interface, enum, record) à partir d'une ligne envoyé et passe le mot si trouvé.
	 *
	 * @param sc    le scanner sur la ligne (pour skip si trouvé)
	 * @param ligne la ligne de déclaration
	 * @return l'objet {@link TypeClasse}, ou CLASS par défaut
	 */
	public TypeClasse recupTypeClass( Scanner sc, String ligne )
	{
		for ( TypeClasse cType : TypeClasse.values() )
		{
			if ( ligne.contains(cType.getType()) )
			{
				sc.next();
				return cType;
			}
		}
		return TypeClasse.CLASS;
	}

	/**
	 * Récupère la liste des paramètres à partir d'une ligne de encoyé.
	 *
	 * @param sc    le scanner sur la ligne ( pour skip )
	 * @param ligne la ligne de déclaration
	 * @return la liste  d'objet {@link Parametre},
	 */
	private List<Parametre> recupLstParams( String ligne )
	{

		List<Parametre> lstParms = new ArrayList<Parametre>();

		// trouver l'emplacement des parmas
		int posDeb = ligne.indexOf("(");
		int posFin = ligne.indexOf(")");

		// recuper les params
		String parametres;
		if (posDeb != -1 && posFin != -1) { parametres = ligne.substring(posDeb + 1, posFin).trim(); } 
		else                              { parametres = "";                                         }

		// ajouter les paramètres
		if ( !parametres.isEmpty() )
		{
			Scanner scParam = new Scanner(parametres);
			scParam.useDelimiter(",");

			while ( scParam.hasNext() )
			{
				Scanner scPart = new Scanner(scParam.next().trim());
				if ( scPart.hasNext() )
				{
					String typeParam = scPart.next();
					String nomParam  = scPart.next();
					lstParms.add( new Parametre(nomParam, typeParam) );
				}
				scPart.close();
			}
			scParam.close();
		}

		return lstParms;
	}

	/**
	* Crée un objet {@link Attribut} pour une constante d'énumération.
	*
	* @param sc   le scanner sur la ligne de déclaration
	* @param type le nom de l'énumération (qui est le type de la constante)
	* @return l'objet {@link Attribut} créé
	*/
	public Attribut creerEnumAttribut( Scanner sc, String type )
	{
		if ( !sc.hasNext() ) { return null; }
		
		String ligne = sc.nextLine().trim();
		String nom;

		// Nettoyage fin de ligne
		if ( ligne.endsWith(",") || ligne.endsWith(";") ) { ligne = ligne.substring(0, ligne.length()-1).trim(); }

		// Séparation Nom / Args
		int posParen = ligne.indexOf("(");
		if ( posParen != -1 ) { nom = ligne.substring(0, posParen).trim(); }
		else                  { nom = ligne;                               }
		
		return new Attribut( nom, type, Visibilite.PUBLIC, Portee.CLASSE, Contraintes.FROZEN, null );
	}
}