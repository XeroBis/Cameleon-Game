package model;

import java.util.ArrayList;

public class Model 
{
	//******************** Attributs et Constructeurs de la classe Model ********************//
	
	private Plateau plateau; // le plateau de jeu
	private QuadTree QuadTree; // l'arbre pour les régions.
	private int size;
	
	private ArrayList<Point> redPoints, bluePoints;
	private int redScore, blueScore;
	
	public Model() 
	{
		this.redPoints = new ArrayList<Point>();
		this.bluePoints = new ArrayList<Point>();
		
		this.redScore = 0;
		this.blueScore = 0;
	}
	
	public Model(int k)
	{
		this.setNewK(k);
		
		this.redPoints = new ArrayList<Point>();
		this.bluePoints = new ArrayList<Point>();
		
		this.redScore = 0;
		this.blueScore = 0;
	}
	
	public void setNewK(int k)
	{
		this.plateau = new Plateau(k);
		this.QuadTree = buildingQT(k, new Point(0,0));
		this.size = 3* (int) Math.pow(2, k);
	}
	
	private QuadTree buildingQT(int k, Point p)
	{
		if (k == 0)
		{
			return new QuadTree(QuadTree.blanc, p, null, null, null ,null);
		}
		else
		{
			int dim = (int) (3 * Math.pow(2, k));
			
			Point p1 = new Point(p.getx() + dim/2, p.gety());
			Point p2 = new Point(p.getx() + dim/2, p.gety() + dim/2);
			Point p3 = new Point(p.getx()        , p.gety() + dim/2);
			
			return new QuadTree(QuadTree.blanc, p,
					            buildingQT(k-1, p),
					            buildingQT(k-1, p1),
					            buildingQT(k-1, p2),
					            buildingQT(k-1, p3));
		}
	}
	
	
	
	
	//******************** Fonctions pour le mode de jeu "Brave" ********************//
	
	public boolean colorationBrave(int ligne, int col, int couleur)
	{
		if (plateau.couleurCase(ligne, col) != 0 || ligne < 0 || ligne >= size || col < 0 || col >= size)
		{
			System.out.println("Mouvement Interdit !");
			return false;
		}
		else
		{
			coloration(ligne, col, couleur);
			recolorationBrave(ligne, col, couleur);
			
			return true;
		}
	}
	
	public void recolorationBrave(int ligne, int col, int couleur)
	{
		for (int i = ligne - 1; i < ligne + 2; i++)
		{
			if (i >= 0 && i < this.size)
			{
				for (int j = col - 1; j < col + 2; j++)
				{
					if(j >= 0 && j < this.size && plateau.couleurCase(i, j) != 0)
					{
						coloration(i, j, couleur);
					}
				}
			}
		}
	}
	
	
	
	
	//******************** Fonctions pour le mode de jeu "Téméraire" ********************//
	
	public boolean colorationTemeraire(int ligne, int col, int couleur)
	{
		if (plateau.couleurCase(ligne, col) != 0)
		{
			System.out.println("Mouvement Interdit !");
			return false;
		}
		else
		{
			coloration(ligne, col, couleur);  //R1
			recolorationTemeraire(ligne, col, couleur);  //R2
			
			Point p = getSmallRegionTopLeft(ligne, col);
			
			if(isSmallRegionFull(p.gety(), p.getx()))
			{
				recoloringSmallRegion(p.gety(), p.getx(), couleur);
				acquiringRegion(p.gety(), p.getx(), couleur, this.QuadTree);
			}
		}
		return true;
	}
	
	public void recolorationTemeraire(int ligne, int col, int couleur)
	{
		for (int i = ligne - 1; i < ligne + 2; i++)
		{
			if (i >= 0 && i < this.size)
			{
				for (int j = col - 1; j < col + 2; j++)
				{
					if((j >= 0) && (j < this.size) && (plateau.couleurCase(i, j) != 0) && (isNotLock(i, j, this.QuadTree)))
					{
						coloration(i, j, couleur);
					}
				}
			}
		}
	}
	
	public boolean isNotLock(int ligne, int col, QuadTree qt)
	{
		if((qt.getQt(0) == null) && (qt.getQt(1) == null) && (qt.getQt(2) == null) && (qt.getQt(3) == null))
		{
			return qt.getValue() == QuadTree.blanc;
		}
		else if(qt.getValue() != QuadTree.blanc)
		{
			return false;
		}
		else
		{
			if(col >= qt.getQt(2).getPoint().getx())
			{
				if(ligne >= qt.getQt(2).getPoint().gety())
				{
					return isNotLock(ligne, col, qt.getQt(2));
				}
				else
				{
					return isNotLock(ligne, col, qt.getQt(1));
				}
			}
			else
			{
				if(ligne >= qt.getQt(2).getPoint().gety())
				{
					return isNotLock(ligne, col, qt.getQt(3));
				}
				else
				{
					return isNotLock(ligne, col, qt.getQt(0));
				}
			}
		}
	}
	
	public Point getSmallRegionTopLeft(int ligne, int col)
	{
		int x = col - (col % 3);
		int y = ligne - (ligne % 3);
		return new Point(x, y);
	}
	
	public void acquiringRegion(int ligne, int col, int couleur, QuadTree qt)
	{
		if((qt.getQt(0) == null) && (qt.getQt(1) == null) && (qt.getQt(2) == null) && (qt.getQt(3) == null))
		{
			qt.setValue(couleur);
			recoloringSmallRegion(ligne, col, couleur);
		}
		else
		{
			int rge = 0; int ble = 0;
			
			for(int i =0; i < 4; i++)
			{
				if (qt.getQt(i).getValue() == QuadTree.rouge ) {rge++;}
				if (qt.getQt(i).getValue() == QuadTree.bleu ) {ble++;}
			}
			
			if(rge + ble == 4)
			{
				for(int i =0; i < 4; i++)
				{
					if (qt.getQt(i).getValue() != couleur )
					{
						acquiringRegion(ligne, col, couleur, qt.getQt(i));
					}
				}
			}
			else
			{
				if(col >= qt.getQt(2).getPoint().getx())
				{
					if(ligne >= qt.getQt(2).getPoint().gety())
					{
						acquiringRegion(ligne, col, couleur, qt.getQt(2));
					}
					else
					{
						acquiringRegion(ligne, col, couleur, qt.getQt(1));
					}
				}
				else
				{
					if(ligne >= qt.getQt(2).getPoint().gety())
					{
						acquiringRegion(ligne, col, couleur, qt.getQt(3));
					}
					else
					{
						acquiringRegion(ligne, col, couleur, qt.getQt(0));
					}
				}
			}
		}
	}
	
	
	
	public boolean isSmallRegionFull(int ligne, int col)
	{
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(plateau.couleurCase(ligne +i, col + j) == Plateau.blanc)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public void recoloringSmallRegion(int ligne, int col, int couleur)
	{
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				plateau.changerValeur(ligne + i, col + j, couleur);
			}
		}
	}
	
	
	
	
	//******************** Fonctions générales ********************//
	
	public void coloration(int ligne, int col, int couleur)
	{
		if(couleur == 1 && plateau.couleurCase(ligne, col) == 2)
		{
			this.redScore--;
			this.blueScore++;
		}
		else if (couleur == 2 && plateau.couleurCase(ligne, col) == 1)
		{
			this.redScore++;
			this.blueScore--;
		}
		else if (couleur == 1 && plateau.couleurCase(ligne, col) == 0)
		{
			this.blueScore++;
		}
		else if (couleur == 2 && plateau.couleurCase(ligne, col) == 0)
		{
			this.redScore++;
		}
		if(plateau.couleurCase(ligne, col) == 0)
		{
			plateau.decrementerUncoloredNb();
		}
		
		plateau.changerValeur(ligne, col, couleur);
		plateau.decrementerUncoloredNb();
		
		switchingColors(ligne, col, couleur);
	}
	
	public void switchingColors(int ligne, int col, int couleur)
	{
		switch (couleur)
		{
		case 1 :
			this.bluePoints.add(new Point(col, ligne));
		break;
		
		case 2 :
			this.redPoints.add(new Point(col, ligne));
		break;
		
		default :
		break;
		}
	}
	
	public void afficher()
	{
		this.plateau.afficherPlateau();
	}
	
	public void afficherQT()
	{
		this.QuadTree.printValuePoint();
	}
	
	public boolean estTerminee()
	{
		return plateau.estEntierementColorie();
	}
	
	public Plateau getPlateau()
	{
		return this.plateau;
	}
	
	public void afficherScores()
	{
		System.out.println("rouge : " + redScore + "; bleu : " + blueScore);
	}
	
	public void actualizingArrayPoints() 
	{
		for (int i = 0; i < redPoints.size(); i++)
		{
			Point p = redPoints.get(i);
		}
		for (int i = 0; i < bluePoints.size(); i++)
		{
			Point p = bluePoints.get(i);
		}
	}
}