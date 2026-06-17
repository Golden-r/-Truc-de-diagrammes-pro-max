package src.metier.export_import;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import src.metier.classe.*;

public class Exporter
{
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	private Exporter() {}


	/*-------------------------------*/
	/* Importation                   */
	/*-------------------------------*/
	/**
	 * Importe une liste de classes depuis un fichier XML.
	 * 
	 * Le fichier doit contenir des balises <CLASSE> et <CLASSE_FANTOME>
	 * respectant la structure définie dans la documentation du fichier.
	 * 
	 * @param fichier le chemin du fichier à importer
	 * @return la liste des classes importées
	 */
	public static void exporter(List<Classe> lstClass, List<Association> lstAssociation,  String fichier) 
	{
		try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream( fichier ), "UTF8"), true))
		{
			/*				CLASSE					*/
			/* ------------------------------------ */
			pw.println("<SAUVEGARDE>");

			pw.println( "\t<CLASSES>" );
			
			for (Classe c : lstClass)
			{
				if (c instanceof ClasseExterne) 
				{
					pw.println( "\t<CLASSE_FANTOME>"                                                                 );
					pw.println( "\t\t<nom> "  + Exporter.formatter(                 c.getNom ()   ) + " </nom>"      );
					pw.println( "\t\t<posX> " + Exporter.formatter( String.valueOf( c.getPosX() ) ) + " </posX>"     );
					pw.println( "\t\t<posY> " + Exporter.formatter( String.valueOf( c.getPosY() ) ) + " </posY>"     );
					pw.println( "\t</CLASSE_FANTOME>"                                                                );
				}
				else
				{
					List<Attribut > lstAttribut = c.getCopieLstAttributs  (); 
					List<Methode  > lstMethode  = c.getCopieLstMethodes   ();

					pw.println( "\t<CLASSE>"                                                                                         );
					pw.println( "\t\t<visibilite> "   + Exporter.formatter( c.getVisibilite  ().getAnglais()  ) + " </visibilite>"   );
					pw.println( "\t\t<modifierType> " + Exporter.formatter( c.getModifierType().getAnglais()  ) + " </modifierType>" );
					pw.println( "\t\t<type> "         + Exporter.formatter( c.getType        ().getType   ()  ) + " </type>"         );
					pw.println( "\t\t<nom> "          + Exporter.formatter( c.getNom         ()               ) + " </nom>"          );
					pw.println( "\t\t<posX> "         + Exporter.formatter( String.valueOf( c.getPosX() )     ) + " </posX>"         );
					pw.println( "\t\t<posY> "         + Exporter.formatter( String.valueOf( c.getPosY() )     ) + " </posY>"         );
					pw.println( "\t\t<extend> "       + Exporter.formatter( c.getExtend()                     ) + " </extend>"       );

					pw.println("\t\t<IMPLEMENTS> ");
					for (String implement : c.getCopieLstImplementes() ) 
						pw.println("\t\t\t<implement>" + Exporter.formatter( implement ) + " </implement>");
					pw.println( "\t\t</IMPLEMENTS>" );

					// ATTRIBUT
					pw.println( "\t<ATTRIBUTS>" );
					for (Attribut a : lstAttribut) 
						{
							String valeurFinal = (a.valeur() != null) ? a.valeur() : "";
							Multiplicite mult = a.multiplicite();
							
							pw.println( "\t\t\t<attribut>"                                                                                      );
							pw.println( "\t\t\t\t<visibilite> " + Exporter.formatter( a.visibilite().getAnglais()         ) + " </visibilite>"  );
							pw.println( "\t\t\t\t<portee> "     + Exporter.formatter( a.portee    ().getAnglais()         ) + " </portee>"      );
							pw.println( "\t\t\t\t<type> "       + Exporter.formatter( a.type      ()                      ) + " </type>"        );
							pw.println( "\t\t\t\t<nom> "        + Exporter.formatter( a.nom       ()                      ) + " </nom>"         );
							pw.println( "\t\t\t\t<contrainte> "  + Exporter.formatter( String.valueOf( a.getContrainte() ) ) + " </contrainte>" );
							pw.println( "\t\t\t\t<valeur> "     + Exporter.formatter( valeurFinal                         ) + " </valeur>"      );
							
							// Export de la multiplicité
							if (mult != null)
							{
								pw.println( "\t\t\t\t<multiplicite>"                                                  );
								pw.println( "\t\t\t\t\t<min>" + mult.getMinimum() + "</min>"                          );
								pw.println( "\t\t\t\t\t<max>" + mult.getMaximum() + "</max>"                          );
								pw.println( "\t\t\t\t</multiplicite>"                                                 );
							}
							
							pw.println( "\t\t\t</attribut>" );
						}
						
					pw.println( "\t\t</ATTRIBUTS>" );
					
					// METHODE et CONSTRUCTEUR
					pw.println( "\t\t<METHODES>" );
					for (Methode m : lstMethode) 
					{
						String typeMethode = (m.estConstructeur() ? "constructeur" : "methode");
						
						pw.println( "\t\t\t<" + typeMethode + ">"                                                                  );
						pw.println( "\t\t\t\t<visibilite> " + Exporter.formatter( m.visibilite().getAnglais() ) + " </visibilite>" );
						pw.println( "\t\t\t\t<portee> "     + Exporter.formatter( m.portee    ().getAnglais() ) + " </portee>"     );
						pw.println( "\t\t\t\t<type> "       + Exporter.formatter( m.type      ()              ) + " </type>"       );
						pw.println( "\t\t\t\t<nom> "        + Exporter.formatter( m.nom       ()              ) + " </nom>"        );
						pw.println( "\t\t\t\t<estDefault> " +                     m.estDefault()                + " </estDefault>" );
						pw.println( "\t\t\t\t<parametres>"                                                                         );
						
						for (Parametre param : m.lstParametres()) 
						{
							pw.println( "\t\t\t\t\t<parametre>"                                               );
							pw.println( "\t\t\t\t\t\t<type> " + Exporter.formatter(param.type()) + " </type>" );
							pw.println( "\t\t\t\t\t\t<nom> "  + Exporter.formatter(param.nom ()) + " </nom>"  );
							pw.println( "\t\t\t\t\t</parametre>"                                              );
						}
						
						pw.println( "\t\t\t\t</parametres>"                              );
						pw.println( "\t\t\t</" + Exporter.formatter( typeMethode ) + ">" );
					} 
					pw.println("\t\t</METHODES>");
					
					pw.println("\t</CLASSE>");
				}
			}
			pw.println("\t</CLASSES>");

			/*			Association					*/
			/* ------------------------------------ */
			pw.println("\t<ASSOCIATIONS>");
			
			// Classe c1, Classe c2, Multiplicite mult1vers2, Multiplicite mult2vers1
			for (Association a : lstAssociation ) 
			{
				pw.println("\t\t<ASSOCIATION>");
				String c1              = a.getClasseCible       ().getNom    ();
				String c2              = a.getClasseSource      ().getNom    ();

				String m1vers2Max      = a.getMultipliciteSource ().getMaximum() + "";
				String m1vers2Min      = a.getMultipliciteSource ().getMinimum() + "";
				String m2vers1Max      = a.getMultipliciteCible  ().getMaximum() + "";
				String m2vers1Min      = a.getMultipliciteCible  ().getMinimum() + "";

				String roleCible       = a.getRoleCible         ();
				String roleSource      = a.getRoleSource        ();
				
				String typeAssociation = a.getTypeAssociation   ().getLibelle();
				String bidirectionnel  = a.estBidirectionnelle  ()              + "";

				pw.println( "\t\t\t<classeCible>"          + Exporter.formatter( c1              ) + " </classeCible>"          );
				pw.println( "\t\t\t<classeSource>"         + Exporter.formatter( c2              ) + " </classeSource>"         );
				pw.println( "\t\t\t<rSource>"              + Exporter.formatter( roleSource      ) + " </rSource>"              );
				pw.println( "\t\t\t<rCible>"               + Exporter.formatter( roleCible       ) + " </rCible>"               );
				pw.println( "\t\t\t<typeAssociation>"      + Exporter.formatter( typeAssociation ) + " </typeAssociation>"      );
				pw.println( "\t\t\t<estBidirectionnel>"    + bidirectionnel                        + " </estBidirectionnel>"    );

				pw.println("\t\t\t<MULTIPLICITE_CIBLE>");
				pw.println("\t\t\t\t<mult_min>" + Exporter.formatter( m2vers1Min ) + "</mult_min>"  );
				pw.println("\t\t\t\t<mult_max>" + Exporter.formatter( m2vers1Max ) + "</mult_max>"  );
				pw.println("\t\t\t</MULTIPLICITE_CIBLE>");

				pw.println("\t\t\t<MULTIPLICITE_SOURCE>");
				pw.println("\t\t\t\t<mult_min>" + Exporter.formatter( m1vers2Min ) + "</mult_min>" );
				pw.println("\t\t\t\t<mult_max>" + Exporter.formatter( m1vers2Max ) + "</mult_max>" );
				pw.println("\t\t\t</MULTIPLICITE_SOURCE>");

				pw.println("\t\t</ASSOCIATION>");
			}

			pw.println("\t</ASSOCIATIONS>");
			pw.println("</SAUVEGARDE>");

			pw.flush();
		} catch (Exception e) { e.printStackTrace(); }			
	}
	
	/**
	 * Échappe les caractères spéciaux XML dans une chaîne.
	 * @param text le texte à échapper
	 * @return le texte avec les caractères XML échappés
	 */
	private static String formatter(String text) 
	{
		if (text == null) return "";

		return text.replace( "&" , "%EtCommercial%;" )
				   .replace( "<" , "%CrochetGauche%;" )
				   .replace( ">" , "%CrochetDroit%;"  )
				   .replace( "\"", "%DoubleCote%;"    )
				   .replace( "'" , "%CoteSimple%;"    );
	}
}