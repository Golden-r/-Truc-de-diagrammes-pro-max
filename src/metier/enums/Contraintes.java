package src.metier.enums;

/**
 * Enumération des contraintes applicables aux attributs d'une classe UML.
 *
 * @author Equipe 1
 * @author CHEVEAU Matéo
 * @author DAMESTOY Ethan
 * @author HERMILLY Joshua
 * @author LAFOSSE Lucas
 * @author LECLERC Jonathan
 */
public enum Contraintes 
{
    /*-------------------------------*/
    /* Constantes                    */
    /*-------------------------------*/
    RIEN    ( ""          ),
    ADD_ONLY( "{addOnly}" ),
    REQUETE ( "{requête}" ),
    FROZEN  ( "{gelée}"   );

    /*-------------------------------*/
    /* Attributs                     */
    /*-------------------------------*/
    
    /** La representation textuelle de la contrainte. */
    private final String valeur;

    /*-------------------------------*/
    /* Constructeur                  */
    /*-------------------------------*/
    
    /**
     * Construit une contrainte avec sa valeur textuelle associee.
     *
     * @param valeur La chaine de caracteres representant la contrainte.
     */
    private Contraintes(String valeur)
    {
        this.valeur = valeur;
    }

    /*-------------------------------*/
    /* Accesseur                     */
    /*-------------------------------*/
    
    /**
     * Retourne la representation textuelle de la contrainte.
     *
     * @return La valeur chaine de la contrainte (ex: "{gelée}").
     */
    public String getValeur() 
    {
        return valeur;
    }
}