package src.vue.GUI.dessin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import src.metier.classe.Association;
import src.metier.classe.ClasseExterne;
import src.metier.classe.Multiplicite;

/**
 * Classe de base representant une fleche graphique dans le diagramme UML.
 * Elle gere la geometrie (coordonnees de depart et fin), le style (couleur, traits),
 * et les fonctionnalites communes comme le dessin des multiplicites et des roles.
 * 
 */
public class Fleche
{
	/*-------------------------------*/
	/* Constantes           */
	/*-------------------------------*/

	/** Forme de tete en V (pour association simple). */
	protected static final Path2D        TETE_V                    = new Path2D.Double();
	/** Forme de tete carree (pour composition/agregation). */
	protected static final Path2D        TETE_CARRE                = new Path2D.Double();
	/** Forme de tete triangulaire (pour heritage/realisation). */
	protected static final Path2D        TETE_TRIANGLE             = new Path2D.Double();

	/** Style de ligne pleine standard. */
	protected static final BasicStroke   LIGNE_NORMAL              = new BasicStroke(3f);
	
	/** Style de ligne pointillee (pour dependance/implementation). */
	protected static final BasicStroke   lignePointille            = new BasicStroke(   3f,
																						BasicStroke.CAP_BUTT,
																						BasicStroke.JOIN_MITER,
																						10f, new float[]{10f, 5f},
																						0f);
  
	protected static final Color         COULEUR_PAR_DEFAUT        = new Color(160, 160, 160);
	protected static final boolean       BIDIRECTIONNEL_PAR_DEFAUT = false;
	protected static final double        TAILLE_TETE_FLECHE        = 10;
  
	protected static final int           ARC_ANGLE                 = 500;
	protected static final int           ARC_BEGIN_ANGLE           = 500;

	/** Objet pour les transformations geometriques (evite les instanciations repetees). */
	private static final AffineTransform TRANSFORM_CACHE           = new AffineTransform();

	// Initialisation statique des formes de tetes de fleches
	static
	{
		Fleche.TETE_V.moveTo(-Fleche.TAILLE_TETE_FLECHE, -Fleche.TAILLE_TETE_FLECHE);
		Fleche.TETE_V.lineTo(0, 0);
		Fleche.TETE_V.lineTo(-Fleche.TAILLE_TETE_FLECHE, Fleche.TAILLE_TETE_FLECHE);
		Fleche.TETE_V.lineTo(0, 0);
		Fleche.TETE_V.closePath();

		Fleche.TETE_TRIANGLE.moveTo(-Fleche.TAILLE_TETE_FLECHE, -Fleche.TAILLE_TETE_FLECHE);
		Fleche.TETE_TRIANGLE.lineTo(0, 0);
		Fleche.TETE_TRIANGLE.lineTo(-Fleche.TAILLE_TETE_FLECHE, Fleche.TAILLE_TETE_FLECHE);
		Fleche.TETE_TRIANGLE.lineTo(-Fleche.TAILLE_TETE_FLECHE, -Fleche.TAILLE_TETE_FLECHE);
		Fleche.TETE_TRIANGLE.closePath();

		Fleche.TETE_CARRE.moveTo(0, 0);
		Fleche.TETE_CARRE.lineTo(-Fleche.TAILLE_TETE_FLECHE, Fleche.TAILLE_TETE_FLECHE);
		Fleche.TETE_CARRE.lineTo(-(Fleche.TAILLE_TETE_FLECHE * 2), 0);
		Fleche.TETE_CARRE.lineTo(-Fleche.TAILLE_TETE_FLECHE, -Fleche.TAILLE_TETE_FLECHE);
		Fleche.TETE_CARRE.lineTo(0, 0);
		Fleche.TETE_CARRE.closePath();
	}

	/*-------------------------------*/
	/* Attributs           */
	/*-------------------------------*/

	// Coordonnees
	private int xDebut;
	private int yDebut;
	private int xFin;
	private int yFin;

	private boolean bidirectionnel;
	private Color   couleur;

	/** L'objet metier associe a cette representation graphique. */
	private Association association;

	/*-------------------------------*/
	/* Constructeurs         */
	/*-------------------------------*/

	/**
	 * Construit une fleche avec tous les parametres de configuration.
	 *
	 * @param xd Coordonnee X de depart.
	 * @param yd Coordonnee Y de depart.
	 * @param xf Coordonnee X de fin.
	 * @param yf Coordonnee Y de fin.
	 * @param bidirectionnel Indique si la fleche a un sens de lecture double.
	 * @param couleur La couleur du trait.
	 * @param association L'objet metier associe.
	 */
	public Fleche(int xd, int yd, int xf, int yf, boolean bidirectionnel, Color couleur, Association association)
	{
		this.xDebut         = xd;
		this.yDebut         = yd;
		this.xFin           = xf;
		this.yFin           = yf;
		this.couleur        = couleur;
		this.bidirectionnel = bidirectionnel;
		this.association    = association;
	}

	/**
	 * Construit une fleche avec les valeurs par defaut (non bidirectionnel, couleur grise).
	 */
	public Fleche(int xd, int yd, int xf, int yf, Association association)
	{
		this(xd, yd, xf,yf, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/**
	 * Construit une fleche avec une couleur specifique mais non bidirectionnelle.
	 */
	public Fleche(int xd, int yd, int xf, int yf, Color couleur, Association association)
	{
		this(xd, yd, xf,yf, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, couleur, association);
	}

	/**
	 * Construit une fleche a partir de deux objets Point.
	 */
	public Fleche( Point debut, Point fin, Association association )
	{
		this(debut.x, debut.y, fin.x, fin.y, Fleche.BIDIRECTIONNEL_PAR_DEFAUT, Fleche.COULEUR_PAR_DEFAUT, association);
	}

	/*-------------------------------*/
	/* Getters            */
	/*-------------------------------*/

	public int         getXDebut        () { return this.xDebut;         }
	public int         getYDebut        () { return this.yDebut;         }
	public int         getXFin          () { return this.xFin;           }
	public int         getYFin          () { return this.yFin;           }
	public Color       getCouleur       () { return this.couleur;        }
	public boolean     estBidirectionnel() { return this.bidirectionnel; }
	public Association getAssociation   () { return this.association;    }

	/*-------------------------------*/
	/* Setters            */
	/*-------------------------------*/

	public void setXDebut        ( int     x ) { this.xDebut = x;         }
	public void setYDebut        ( int     y ) { this.yDebut = y;         }
	public void setXFin          ( int     x ) { this.xFin = x;           }
	public void setYFin          ( int     y ) { this.yFin = y;           }
	public void setCouleur       ( Color   c ) { this.couleur = c;        }
	public void setBidirectionnel( boolean b ) { this.bidirectionnel = b; }

	/*-------------------------------*/
	/* Méthodes             */
	/*-------------------------------*/

	/**
	 * Methode de dessin principale (a surcharger par les sous-classes).
	 * Active l'antialiasing pour un rendu plus net.
	 *
	 * @param g Le contexte graphique.
	 */
	public void draw(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	/**
	 * Transforme une forme geometrique (rotation et translation) pour l'aligner 
	 * avec la direction de la fleche definie par (xd, yd) vers (xf, yf).
	 *
	 * @param shape La forme de base (ex: tete de fleche).
	 * @param xd X depart du segment de reference.
	 * @param yd Y depart du segment de reference.
	 * @param xf X fin du segment (la forme sera placee ici).
	 * @param yf Y fin du segment.
	 * @return La forme transformee prtse a etre dessinee.
	 */
	protected static Shape transform(Path2D shape, int xd, int yd, int xf, int yf)
	{
		double angle = Math.atan2(yf - yd, xf - xd);

		TRANSFORM_CACHE.setToIdentity();
		TRANSFORM_CACHE.translate(xf, yf);
		TRANSFORM_CACHE.rotate(angle);

		return TRANSFORM_CACHE.createTransformedShape(shape);
	}

	/**
	 * Dessine une ligne en pointilles entre deux points.
	 */
	protected static void drawLignePointille( Graphics2D g, int xd, int yd, int xf, int yf )
	{
		g.setStroke(Fleche.lignePointille);
		g.drawLine(xd, yd, xf, yf);
		g.setStroke(Fleche.LIGNE_NORMAL);
	}

	/**
	 * Dessine le chemin rectangulaire pour une association reflexive.
	 * Cree une boucle qui sort et revient vers la meme classe.
	 */
	private static void drawAutoAsso( Graphics2D g, int xd, int yd, int xf, int yf )
	{
		int hauteur = 40;
		int largeur = 60;

		g.drawLine(xd, yd, xd, yd - hauteur);
		g.drawLine(xd, yd - hauteur, xd + largeur, yd - hauteur);
		g.drawLine(xd + largeur, yd - hauteur, xf + largeur, yf);
	}

	/**
	 * Dessine une ligne pleine pour une auto-association.
	 */
	protected static void drawLigneAutoAsso(Graphics2D g, int xd, int yd, int xf, int yf)
	{
		g.setStroke(Fleche.LIGNE_NORMAL);
		Fleche.drawAutoAsso( g, xd, yd, xf, yf );
	}

	/**
	 * Dessine une ligne pointillee pour une auto-association.
	 */
	protected static void drawLignePointilleAutoAsso(Graphics2D g, int xd, int yd, int xf, int yf)
	{
		g.setStroke(Fleche.lignePointille);
		Fleche.drawAutoAsso( g, xd, yd, xf, yf );
		g.setStroke(Fleche.LIGNE_NORMAL);
	}

	/**
	 * Calcule et retourne la zone de collision (HitBox) de la fleche.
	 * Utilise un rectangle oriente autour de la ligne pour faciliter la selection a la souris.
	 *
	 * @return La forme geometrique representant la zone cliquable, ou null si longueur nulle.
	 */
	public Shape getHitBox()
	{
		double xd = xDebut;
		double yd = yDebut;
		double xf = xFin;
		double yf = yFin;

		// Vecteur directeur
		double dx = xf - xd;
		double dy = yf - yd;

		double longueur = Math.hypot(dx, dy);
		if (longueur == 0) return null;

		// Vecteur normal (perpendiculaire unitaire)
		double nx = -dy / longueur;
		double ny = dx / longueur;

		// Décalage
		double ox = nx * TAILLE_TETE_FLECHE;
		double oy = ny * TAILLE_TETE_FLECHE;

		Path2D.Double hitbox = new Path2D.Double();

		hitbox.moveTo(xd + ox, yd + oy);
		hitbox.lineTo(xf + ox, yf + oy);
		hitbox.lineTo(xf - ox, yf - oy);
		hitbox.lineTo(xd - ox, yd - oy);
		hitbox.closePath();

		return hitbox;
	}

	/**
	 * Affiche les multiplicites (ex: "0..1", "*") aux extremites de la fleche.
	 * Calcule la position pour eviter de chevaucher le trait ou les tetes de fleche.
	 * 
	 */
	protected void drawMultiplicite(Graphics2D g)
	{
		if ( this.getAssociation().getClasseSource() instanceof ClasseExterne ||
			 this.getAssociation().getClasseCible() instanceof ClasseExterne )   { return; }
		
		Multiplicite multCible  = this.getAssociation().getMultipliciteCible();
		Multiplicite multSource = this.getAssociation().getMultipliciteSource();
		g.setFont(g.getFont().deriveFont(12f));
		g.setColor(Color.BLACK);
		
		double angle = Math.atan2(this.getYFin() - this.getYDebut(), this.getXFin() - this.getXDebut());
		FontMetrics fm = g.getFontMetrics();
		
		// Vecteur perpendiculaire à la ligne (vers le bas)
		double perpX = Math.sin(angle);
		double perpY = -Math.cos(angle);
		
		// Multiplicité côté source
		if (multSource != null)
		{
			String text = multSource.toString();
			int texteLargeur = fm.stringWidth(text);
			int texteHauteur = fm.getHeight();
			
			// Position le long de la ligne
			double distanceDeLaLigne = Math.max(texteLargeur, 25);
			
			// Position perpendiculaire (vers le bas)
			int decalage = 12;
			
			int xm = (int)(this.getXDebut() + Math.cos(angle) * distanceDeLaLigne + perpX * decalage);
			int ym = (int)(this.getYDebut() + Math.sin(angle) * distanceDeLaLigne + perpY * decalage + texteHauteur/3);
			
			g.setColor(new Color(224, 224, 224, 128));
			//g.fillRoundRect(xm, ym - fm.getHeight(), fm.stringWidth(text) + 10, fm.getHeight() + 5, 10, 10); commenté pour éviter d'avoir un fond derrière les multiplicités
			g.setColor(Color.BLACK);
			g.drawString(text, xm + 5, ym);
		}
		
		// Multiplicité côté cible
		if (multCible != null)
		{
			String text = multCible.toString();
			int texteLargeur = fm.stringWidth(text);
			int texteHauteur = fm.getHeight();
			
			// Position le long de la ligne (recule pour ne pas chevaucher la flèche)
			double distanceDeLaLigne = Math.max(texteLargeur + 10, 35);
			
			// Position perpendiculaire (vers le bas)
			int decalage = 12;
			
			int xm = (int)(this.getXFin() - Math.cos(angle) * distanceDeLaLigne + perpX * decalage);
			int ym = (int)(this.getYFin() - Math.sin(angle) * distanceDeLaLigne + perpY * decalage + texteHauteur/3);
				
			g.setColor(new Color(224, 224, 224, 128));
			//g.fillRoundRect(xm, ym - fm.getHeight(), fm.stringWidth(text) + 10, fm.getHeight() + 5, 10, 10);
			g.setColor(Color.BLACK);
			g.drawString(text, xm + 5, ym);
		}
	}

	/**
	 * Affiche les noms des roles (ex: "+parent", "+enfant") aux extremites de la fleche.
	 * Positionne le texte du cote oppose aux multiplicites pour eviter les superpositions.
	 */
	public void drawRole(Graphics2D g)
	{
		g.setFont(g.getFont().deriveFont(12f));
		g.setColor(Color.BLACK);
		
		double angle = Math.atan2(this.getYFin() - this.yDebut, this.getXFin() - this.xDebut);
		FontMetrics fm = g.getFontMetrics();
		
		// Vecteur perpendiculaire à la ligne (pour décalage latéral)
		double perpX = -Math.sin(angle);
		double perpY = Math.cos(angle);
		
		// Détecter si la flèche va vers la droite ou vers la gauche
		boolean versLaDroite = (this.getXFin() - this.xDebut) >= 0;
		
		// Rôle côté source (près du début de la ligne)
		if (this.association.getRoleSource() != null && !this.association.getRoleSource().isEmpty())
		{
			String text = this.association.getRoleSource();
			int texteLargeur = fm.stringWidth(text);
			int texteHauteur = fm.getHeight();
			
			// Distance depuis le point de départ le long de la ligne
			double distanceAlongLine = 20;
			
			// Décalage perpendiculaire (vers le haut/bas de la ligne)
			int decalage = 15;
			
			// Position près du début de la ligne
			int xr = (int)(this.xDebut + Math.cos(angle) * distanceAlongLine + perpX * decalage);
			int yr = (int)(this.yDebut + Math.sin(angle) * distanceAlongLine + perpY * decalage);
			
			// Si la flèche va vers la gauche, décaler le texte source vers la gauche
			if (!versLaDroite) { xr -= texteLargeur; }
			
			// Fond semi-transparent
			//g.setColor(new Color(224, 224, 224, 200));
			//g.fillRoundRect(xr - 5, yr - texteHauteur + 3, texteLargeur + 10, texteHauteur + 4, 8, 8);
			
			// Texte du rôle
			g.setColor(Color.BLACK);
			g.drawString(text, xr, yr);
		}
		
		// Rôle côté destination (près de la fin de la ligne)
		if (this.association.getRoleCible() != null && !this.association.getRoleCible().isEmpty())
		{
			String text = this.association.getRoleCible();
			int texteLargeur = fm.stringWidth(text);
			int texteHauteur = fm.getHeight();
			
			// Distance depuis le point de fin le long de la ligne (vers l'arrière)
			double distanceAlongLine = 20;
			
			// Décalage perpendiculaire (vers le haut/bas de la ligne)
			int decalage = 15;
			
			// Position près de la fin de la ligne
			int xr = (int)(this.getXFin() - Math.cos(angle) * distanceAlongLine + perpX * decalage);
			int yr = (int)(this.getYFin() - Math.sin(angle) * distanceAlongLine + perpY * decalage);
			
			// Si la flèche va vers la droite, décaler le texte destination vers la gauche
			if (versLaDroite) { xr -= texteLargeur; }
			
			// Fond semi-transparent
			g.setColor(new Color(224, 224, 224, 200));
			g.fillRoundRect(xr - 5, yr - texteHauteur + 3, texteLargeur + 10, texteHauteur + 4, 8, 8);
			
			// Texte du rôle
			g.setColor(Color.BLACK);
			g.drawString(text, xr, yr);
		}
	}
}