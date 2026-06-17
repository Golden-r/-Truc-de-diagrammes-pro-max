package src.metier.classe;

import java.util.ArrayList;
import java.util.List;
import src.metier.enums.TypeAssociation;
import src.metier.enums.TypeClasse;

/**
* La classe Association représente une relation entre deux classes Java.
* Elle permet de détecter et de caractériser les associations (composition, agrégation, généralisation, etc.) entre classes.
* @author Equipe 1
* LECLERC  Jonathan
* DAMESTOY Ethan
* HERMILLY Joshua
* LAFOSSE  Lucas
* CHEVEAU  Matéo
*/
public class Association
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	// classe source
	private Classe          classeSource;
	private Multiplicite    multipliciteSource;
	private String          roleSource;

	// classe cible
	private Multiplicite    multipliciteCible;
	private Classe          classeCible;
	private String          roleCible;
	
	// type d'association
	private TypeAssociation typeAssociation;
	private boolean         bidirectionnelle;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	public Association ( Classe c1, Classe c2, Multiplicite multipliciteCible, Multiplicite multipliciteSource, 
	                     String roleSource, String roleCible, TypeAssociation typeAssociation, boolean bidirectionnelle )
	{
		this.classeSource       = c1;
		this.multipliciteSource = multipliciteSource;
		this.roleSource         = roleSource;
		this.multipliciteCible  = multipliciteCible;
		this.classeCible        = c2;
		this.roleCible          = roleCible;
		this.typeAssociation    = typeAssociation;
		this.bidirectionnelle   = bidirectionnelle;
	}
	
	/**
	* Constructeur privé, crée une association entre deux classes avec leurs multiplicités.
	* @param c1 la première classe
	* @param c2 la seconde classe
	* @param mult1vers2 multiplicité de c1 vers c2
	* @param mult2vers1 multiplicité de c2 vers c1
	*/
	private Association( Classe c1, Classe c2, Multiplicite mult1vers2, Multiplicite mult2vers1 )
	{
		// Unidirectionnelle C2 vers C1
		if ( mult1vers2 == null && mult2vers1 != null )
		{
			this.classeSource       = c2;
			this.classeCible        = c1;
			this.multipliciteCible  = mult2vers1;
			this.multipliciteSource = new Multiplicite(1, 1);
			this.bidirectionnelle   = false;
		}

		// Bidirectionnelle
		else if ( mult1vers2 != null && mult2vers1 != null )
		{
			this.classeSource       = c1;
			this.classeCible        = c2;
			this.multipliciteCible  = mult1vers2;
			this.multipliciteSource = mult2vers1;
			this.bidirectionnelle   = true;
		}

		// Unidirectionnelle C1 vers C2
		else
		{
			this.classeSource       = c1;
			this.classeCible        = c2;
			this.multipliciteCible  = mult1vers2 != null ? mult1vers2 : new Multiplicite(1, 1);
			this.multipliciteSource = new Multiplicite(1, 1);
			this.bidirectionnelle   = false;
		}

		this.typeAssociation = TypeAssociation.ASSOCIATION_SIMPLE;
		this.roleSource      = null;
		this.roleCible       = null;
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	/** @return la classe source de l'association */
	public Classe          getClasseSource      () { return this.classeSource;                     }

	/** @return la classe cible de l'association */
	public Classe          getClasseCible       () { return this.classeCible;                      }

	/** @return la multiplicité de la classe source */
	public Multiplicite    getMultipliciteSource() { return this.multipliciteSource;               }

	/** @return la multiplicité de la classe cible */
	public Multiplicite    getMultipliciteCible () { return this.multipliciteCible;                }

	/** @return le rôle de la classe cible */
	public String          getRoleCible         () { return this.roleCible;                        }

	/** @return le rôle de la classe source */
	public String          getRoleSource        () { return this.roleSource;                       }

	/** @return vrai si l'association est bidirectionnelle */
	public boolean         estBidirectionnelle  () { return this.bidirectionnelle;                 }

	/** @return le type d'association */
	public TypeAssociation getTypeAssociation   () { return this.typeAssociation;                  }

	/** @return vrai si l'association est réflexive */
	public boolean         estReflexive         () { return this.classeSource == this.classeCible; }

	/*-------------------------------*/
	/* Setters                       */
	/*-------------------------------*/
	public void setMultipliciteSource( Multiplicite    multipliciteSource ) { this.multipliciteSource = multipliciteSource; }
	public void setMultipliciteCible ( Multiplicite    multipliciteCible  ) { this.multipliciteCible  = multipliciteCible;  }
	public void setRoleCible         ( String          roleCible          ) { this.roleCible          = roleCible;          }
	public void setRoleSource        ( String          roleSource         ) { this.roleSource         = roleSource;         }
	public void setTypeAssociation   ( TypeAssociation type               ) { this.typeAssociation    = type;               }

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	/**
	* Détecte et crée les associations entre toutes les classes d'une liste.
	* Gère les associations multiples entre deux mêmes classes.
	*
	* @param classes la liste des classes à analyser
	* @return la liste des associations détectées
	*/
	public static List<Association> detecterAssociations(List<Classe> classes)
	{
		List<Association> lstAssociations = new ArrayList<>();

		for (int cptC1 = 0; cptC1 < classes.size(); cptC1++)
		{
			Classe c1 = classes.get(cptC1);

			for (int cptC2 = cptC1; cptC2 < classes.size(); cptC2++)
			{
				Classe c2 = classes.get(cptC2);

				// Cas spécial ENUM réflexive
				if (c1 == c2 && c1.getType() == TypeClasse.ENUM)
					continue;

				// Attributs de liaison
				List<Attribut> attrs1vers2 = trouverTousAttributsLiaison(c1, c2);
				List<Attribut> attrs2vers1 = trouverTousAttributsLiaison(c2, c1);

				// Détection héritage / implémentation
				TypeAssociation type1 = detecterTypeAssociation(c1, c2);
				TypeAssociation type2 = detecterTypeAssociation(c2, c1);

				// Cas : uniquement héritage / implémentation
				if (attrs1vers2.isEmpty() && attrs2vers1.isEmpty())
				{
					if (type1 != null || type2 != null)
					{
						Association.creerEtAjouterAssociation(
							lstAssociations,
							c1,
							c2,
							null,
							null,
							type1,
							type2
						);
					}
					continue;
				}

				// UML : UNE seule association entre c1 et c2
				Attribut attr1 = attrs1vers2.isEmpty() ? null : attrs1vers2.get(0);
				Attribut attr2 = attrs2vers1.isEmpty() ? null : attrs2vers1.get(0);

				Association.creerEtAjouterAssociation(
					lstAssociations,
					c1,
					c2,
					attr1,
					attr2,
					type1,
					type2
				);
			}
		}
		return lstAssociations;
	}

	/**
	 * Crée et ajoute une association à la liste si au moins une des multiplicités ou types est défini.
	 * 
	 * @param lst La liste des associations
	 * @param c1 La première classe
	 * @param c2 La seconde classe
	 * @param attr1 L'attribut de c1 vers c2
	 * @param attr2 L'attribut de c2 vers c1
	 * @param type1 Le type d'association détecté de c1 vers c2
	 * @param type2 Le type d'association détecté de c2 vers c1
	 */
	private static void creerEtAjouterAssociation(List<Association> lst, Classe c1, Classe c2, Attribut attr1, Attribut attr2, TypeAssociation type1, TypeAssociation type2)
	{
		Multiplicite mult1vers2 = (attr1 != null) ? calculerMultiplicite(attr1, c1, c2) : null;
		Multiplicite mult2vers1 = (attr2 != null) ? calculerMultiplicite(attr2, c2, c1) : null;

		if ( mult1vers2 != null || mult2vers1 != null || type1 != null || type2 != null )
		{
			Association asso = null;
			if ( type1 == null && type2 != null ) { asso = new Association( c2, c1, mult2vers1, mult1vers2 ); }
			else                                  { asso = new Association( c1, c2, mult1vers2, mult2vers1 ); }

			TypeAssociation finalType = TypeAssociation.ASSOCIATION_SIMPLE;
			if      (type1 == TypeAssociation.GENERALISATION           || type2 == TypeAssociation.GENERALISATION          ) { finalType = TypeAssociation.GENERALISATION;           }
			else if (type1 == TypeAssociation.IMPLEMENTATION_INTERFACE || type2 == TypeAssociation.IMPLEMENTATION_INTERFACE) { finalType = TypeAssociation.IMPLEMENTATION_INTERFACE; }

			asso.setTypeAssociation(finalType);
			lst.add(asso);
		}
	}

	/**
	* Détecte le type d'association entre deux classes.
	* @param c1 la première classe
	* @param c2 la seconde classe
	* @return le type d'association détecté
	*/
	private static TypeAssociation detecterTypeAssociation( Classe c1, Classe c2 )
	{
		String nomC2 = c2.getNom();

		// On ne vérifie l'héritage/implémentation que si ce sont deux classes différentes
		if ( c1 != c2 )
		{
			// généralisation (si c'est un extends)
			if (c1.getExtend() != null && c1.getExtend().equals(nomC2)) { return TypeAssociation.GENERALISATION; }

			// implémentation (si c'est un implements)
			for ( String impl : c1.getCopieLstImplementes() )
			{
				if ( impl.equals(nomC2) ) { return TypeAssociation.IMPLEMENTATION_INTERFACE; }
			}
		}

		for (Attribut attr : c1.getCopieLstAttributs())
		{
			// Association simple (si attribut du type de la classe cible)
			if ( attr.type().equals(nomC2) || attr.type().contains("<" + nomC2 + ">") || attr.type().contains(nomC2 + "[]") )
			{
				return TypeAssociation.ASSOCIATION_SIMPLE;
			}
		}
		return null;
	}

	/**
	* Trouve les attributs dans la classe source qui correspondent à une instance ou collection de la classe cible.
	*/
	private static List<Attribut> trouverTousAttributsLiaison(Classe src, Classe cible)
	{
		List<Attribut> attributsList = new ArrayList<>();
		String nomCible = cible.getNom();

		for ( Attribut attr : src.getCopieLstAttributs() )
		{
			String type = attr.type();

			// Collection
			if ( type.contains("<" + nomCible + ">") || type.contains(nomCible + "[]") )
			{
				attributsList.add(attr);
			}
			// Simple
			else if (type.equals(nomCible)) { attributsList.add(attr); }
		}
		return attributsList;
	}

	/**
	* Calcule la multiplicité à partir de l'attribut identifié.
	*
	* @param attr L'attribut faisant le lien
	* @param src La classe source
	* @param cible La classe cible
	* @return La multiplicité calculée
	*/
	private static Multiplicite calculerMultiplicite(Attribut attr, Classe src, Classe cible)
	{
		String nomSource = src.getNom();
		String nomCible  = cible.getNom();
		String type      = attr.type();

		// Cas Collection (List<Cible> ou Cible[])
		if ( type.contains("<" + nomCible + ">") || type.contains(nomCible + "[]") )
		{
			// Verif si cible a aussi un tab du type de la classe source (Relation N..N)
			boolean cibleACollection = false;
			for (Attribut attrCible : cible.getCopieLstAttributs())
			{
				if (attrCible.type().contains("<" + nomSource + ">") || attrCible.type().contains(nomSource + "[]"))
				{
					cibleACollection = true;
					break;
				}
			}

			if      ( cibleACollection      ) { return new Multiplicite(-1, -1); } // N vers N (*..*)
			else if ( attr.estFinal()       ) { return new Multiplicite( 1, -1); } // Collection final     = 1..*
			else                              { return new Multiplicite( 0, -1); } // Collection non final = 0..*
		}

		// Cas Simple (Cible)
		if (type.equals(nomCible))
		{
			if (attr.estFinal()) { return new Multiplicite(1, 1); } // Attribut final     = 1..1
			else                 { return new Multiplicite(0, 1); } // Attribut non final = 0..1
		}

		return null;
	}

	/*-------------------------------*/
	/* toString                      */
	/*-------------------------------*/

	/**
	* Retourne une représentation textuelle de l'association.
	* @return la chaîne représentant l'association
	*/
	public String toString()
	{
		return ( this.bidirectionnelle ? "bidirectionnelle" : "unidirectionnelle" ) + " de " +
		         this.classeSource.getNom() + " ( " + this.multipliciteSource.toString() + " ) vers " +
		         this.classeCible .getNom() + " ( " + this.multipliciteCible .toString() + " )"       ;
	}
}