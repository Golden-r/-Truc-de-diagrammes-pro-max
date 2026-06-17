package src.utils;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/**
 * Classe utilitaire pour la gestion des dialogues de selection de fichiers (JFileChooser).
 * Elle permet de configurer l'interface en francais et offre des methodes simplifiees
 * pour ouvrir ou enregistrer des fichiers et des dossiers.
 */
public final class FileChooserUtils
{
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	
	/**
	 * Constructeur prive pour empecher l'instanciation de cette classe utilitaire.
	 */
	private FileChooserUtils() {}

	/*-------------------------------*/
	/* Traduction Langue FR          */
	/*-------------------------------*/

	/**
	 * Configure les proprietes de l'UIManager pour afficher les textes
	 * du JFileChooser en francais (boutons, labels, infobulles).
	 */
	private static void configurerLocaleFrancaise() 
	{
		UIManager.put("FileChooser.openDialogTitleText"         , "Ouvrir"                  );
		UIManager.put("FileChooser.openButtonText"              , "Ouvrir"                  );
		UIManager.put("FileChooser.cancelButtonText"            , "Annuler"                 );
		UIManager.put("FileChooser.saveButtonText"              , "Enregistrer"             );
		UIManager.put("FileChooser.lookInLabelText"             , "Chercher dans :"         );
		UIManager.put("FileChooser.fileNameLabelText"           , "Nom du fichier :"        );
		UIManager.put("FileChooser.filesOfTypeLabelText"        , "Type de fichier :"       );
		UIManager.put("FileChooser.newFolderToolTipText"        , "Créer un nouveau dossier");
		UIManager.put("FileChooser.listViewButtonToolTipText"   , "Liste"                   );
		UIManager.put("FileChooser.detailsViewButtonToolTipText", "Détails"                 );
		UIManager.put("FileChooser.saveInLabelText"             , "Enregistrer dans :"      );
		UIManager.put("FileChooser.directoryDescriptionText"    , "Répertoire"              );
		UIManager.put("FileChooser.fileDescriptionText"         , "Fichier"                 );
		UIManager.put("FileChooser.acceptAllFileFilterText"     , "Tous les fichiers"       );
		UIManager.put("FileChooser.newFolderButtonText"         , "Nouveau dossier"         );
		UIManager.put("OptionPane.yesButtonText"                , "Oui"                     );
		UIManager.put("OptionPane.noButtonText"                 , "Non"                     );
	}

	/*-------------------------------*/
	/* Méthode principale            */
	/*-------------------------------*/

	/**
	 * Ouvre une boite de dialogue de selection de fichiers ou dossiers.
	 * C'est la methode generique utilisee par les autres methodes de la classe.
	 *
	 * @param repertoireCourant Le chemin du dossier a ouvrir par defaut.
	 * @param modeSelection Le mode de selection (fichiers seulement, dossiers seulement, etc.).
	 * @param selectionMultiple Vrai pour autoriser la selection de plusieurs elements.
	 * @param filtre Le filtre de fichiers a appliquer (extensions).
	 * @return Un tableau des fichiers selectionnes, ou un tableau vide si annulation.
	 */
	public static File[] choisir(String repertoireCourant, int modeSelection, boolean selectionMultiple, FileFilter filtre)
	{
		FileChooserUtils.configurerLocaleFrancaise();
		
		JFileChooser chooser = new JFileChooser(repertoireCourant);
		chooser.setFileSelectionMode(modeSelection);
		chooser.setMultiSelectionEnabled(selectionMultiple);

		if (filtre != null && modeSelection != JFileChooser.DIRECTORIES_ONLY)
		{
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.addChoosableFileFilter(filtre);
		}

		int resultat = chooser.showOpenDialog(null);

		if ( resultat != JFileChooser.APPROVE_OPTION ) { return new File[0];                }                            
		if ( selectionMultiple                       ) { return chooser.getSelectedFiles(); }

		File selectionne = chooser.getSelectedFile();
		return selectionne != null ? new File[]{selectionne} : new File[0];
	}

	/*-------------------------------*/
	/* Méthodes de raccourci         */
	/*-------------------------------*/

	/**
	 * Raccourci pour selectionner un unique fichier.
	 *
	 * @param repertoireCourant Le chemin du dossier a ouvrir par defaut.
	 * @param filtre Le filtre de fichiers a appliquer.
	 * @return Le fichier selectionne, ou null si annulation.
	 */
	public static File choisirFichier(String repertoireCourant, FileFilter filtre)
	{
		File[] fichiers = choisir(repertoireCourant, JFileChooser.FILES_ONLY, false, filtre);
		return fichiers.length > 0 ? fichiers[0] : null;
	}

	/**
	 * Raccourci pour selectionner un unique dossier.
	 *
	 * @param repertoireCourant Le chemin du dossier a ouvrir par defaut.
	 * @return Le dossier selectionne, ou null si annulation.
	 */
	public static File choisirDossier(String repertoireCourant)
	{
		File[] dossiers = choisir(repertoireCourant, JFileChooser.DIRECTORIES_ONLY, false, null);
		return dossiers.length > 0 ? dossiers[0] : null;
	}

	/*-----------------------------------*/
	/* Méthode pour sauvegarder          */
	/*-----------------------------------*/

	/**
	 * Ouvre une boite de dialogue pour enregistrer un fichier.
	 * Gere automatiquement la confirmation d'ecrasement si le fichier existe deja.
	 *
	 * @param repertoireCourant Le chemin du dossier a ouvrir par defaut.
	 * @param nomFichierParDefaut Le nom a pre-remplir dans le champ de saisie.
	 * @param filtre Le filtre de fichiers a appliquer.
	 * @param titre Le titre de la fenetre de dialogue.
	 * @return Le fichier a sauvegarder, ou null si annulation ou refus d'ecrasement.
	 */
	public static File choisirPourSauvegarder(String repertoireCourant, String nomFichierParDefaut, FileFilter filtre, String titre)
	{
		FileChooserUtils.configurerLocaleFrancaise();
		
		JFileChooser chooser = new JFileChooser(repertoireCourant);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		
		if (nomFichierParDefaut != null && !nomFichierParDefaut.isEmpty())
		{
			chooser.setSelectedFile(new File(nomFichierParDefaut));
		}
		
		if (filtre != null)
		{
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.addChoosableFileFilter(filtre);
		}
		
		if (titre != null && !titre.isEmpty())
			chooser.setDialogTitle(titre);

		int resultat = chooser.showSaveDialog(null);

		if (resultat != JFileChooser.APPROVE_OPTION)  { return null;  }

		File fichierSelectionne = chooser.getSelectedFile();
		if (fichierSelectionne == null)  { return null;  }

		if (fichierSelectionne.exists())
		{
			int confirmation = JOptionPane.showConfirmDialog(
				null,
				"Le fichier existe déjà. Écraser ?",
				"Confirmation",
				JOptionPane.YES_NO_OPTION
			);

			if (confirmation != JOptionPane.YES_OPTION) {  return null;   }
		}

		return fichierSelectionne;
	}
}